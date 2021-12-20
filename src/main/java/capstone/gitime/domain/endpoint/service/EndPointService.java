package capstone.gitime.domain.endpoint.service;

import capstone.gitime.api.exception.exception.global.NotFoundException;
import capstone.gitime.api.exception.exception.team.NotFoundTeamException;
import capstone.gitime.domain.endpoint.entity.BuildStatus;
import capstone.gitime.domain.endpoint.entity.EndPoint;
import capstone.gitime.domain.endpoint.entity.ServerStatus;
import capstone.gitime.domain.endpoint.repository.EndPointRepository;
import capstone.gitime.domain.endpoint.service.dto.EndPointResponseDto;
import capstone.gitime.domain.team.entity.Team;
import capstone.gitime.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EndPointService {

    private final EndPointRepository endPointRepository;
    private final TeamRepository teamRepository;
    private final EndPointInit endPointInit;


    @Value("${endpoint.code.dir}")
    private String codeDir;

    private Integer shellCommand(String command) {

        Integer resultCode = null;
        String s;
        Process p;
        try {
            String[] cmd = {"/bin/bash", "-c", command};
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null) {
                System.out.println("s = " + s);
            }
            p.waitFor();
            resultCode = p.exitValue();
            System.out.println("exit: " + p.exitValue());
            p.destroy();

        } catch (Exception e) {
        }

        return resultCode;
    }

    public EndPointResponseDto getEndPointInfo(String teamName) {
        EndPoint findEndPoint = endPointRepository.findByTeamName(teamName)
                .orElseThrow(() -> new NotFoundException());

        return EndPointResponseDto.of(findEndPoint);
    }

    @Transactional
    public void updateCodeFile(String teamName) {

    }

    @Transactional
    public void codeFileDownloadAndSave(Team team) {

        String cmd = "cd /Users/wk30815/endpoint && git clone " + team.getGitRepo().getUrl() + ".git";

        if (shellCommand(cmd) != 0) {
            throw new RuntimeException();
        }

        String[] splits = team.getGitRepo().getUrl().split("/");

        String codeFilePath = codeDir + "/" + splits[4];

        EndPoint newEndPoint = EndPoint.createEndPoint()
                .serverStatus(ServerStatus.OFF)
                .buildStatus(BuildStatus.FAILED)
                .lastCodeBuildAt(null)
                .lastCodeUpdateAt(LocalDateTime.now())
                .serverUrl(null)
                .serverPort(null)
                .team(team)
                .dockerImageName(UUID.randomUUID().toString())
                .codeFilePath(codeFilePath)
                .build();

        endPointRepository.save(newEndPoint);
    }

    @Transactional
    public void createDocker(MultipartFile multipartFile, String serverPort, String teamName) throws IOException, InterruptedException {

        Team findTeam = teamRepository.findTeamByName(teamName)
                .orElseThrow(() -> new NotFoundTeamException());

        EndPoint endPoint = findTeam.getEndPoint();

        endPoint.updateServerPort(determineServerPort());

        // 최상위 루트
        String codeFilePath = endPoint.getCodeFilePath();

        multipartFile.transferTo(new File(codeFilePath + "/Dockerfile"));


        if (shellCommand("cd " + codeFilePath + " && chmod +x gradlew && ./gradlew build -x test && " +
                "docker build --build-arg DEPENDENCY=build/dependency --platform linux/amd64 --tag " + endPoint.getDockerImageName() + " . && " +
                "docker run -d --name "+ endPoint.getDockerContainerName() + " -p " + endPoint.getServerPort() + ":8080 " + endPoint.getDockerImageName()
                ) != 0) {
            throw new RuntimeException();
        }

        usingServerPort(endPoint.getServerPort());

        endPoint.updateServerUrl("http://hoduback.space:" + endPoint.getServerPort());
        endPoint.updateBuildStatus(BuildStatus.SUCCESS);
        endPoint.updateServerStatus(ServerStatus.ON);
        endPoint.updateLastCodeBuildAt(LocalDateTime.now());
        endPoint.updateServerCreated();


    }

    @Transactional
    public void stopDocker(String teamName) {
        Team findTeam = teamRepository.findTeamByName(teamName)
                .orElseThrow(() -> new NotFoundTeamException());

        EndPoint endPoint = findTeam.getEndPoint();


        if (shellCommand("docker stop " + endPoint.getDockerContainerName() + " && docker rm " + endPoint.getDockerContainerName()) != 0) {
            throw new RuntimeException();
        }
        disableServerPort(endPoint.getServerPort());

        endPoint.updateServerPort(null);

        endPoint.updateServerStatus(ServerStatus.OFF);
    }

    @Transactional
    public void runDocker(String teamName) {
        Team findTeam = teamRepository.findTeamByName(teamName)
                .orElseThrow(() -> new NotFoundTeamException());

        EndPoint endPoint = findTeam.getEndPoint();

        endPoint.updateServerPort(determineServerPort());

        if (shellCommand("docker run -d --name "+ endPoint.getDockerContainerName() + " -p " + endPoint.getServerPort() + ":8080 " + endPoint.getDockerImageName()) != 0) {
            throw new RuntimeException();
        }

        usingServerPort(endPoint.getServerPort());

        endPoint.updateServerUrl("http://hoduback.space:" + endPoint.getServerPort());
        endPoint.updateServerStatus(ServerStatus.ON);
    }

    public String determineServerPort() {
        Map<String, Boolean> tree = endPointInit.callPortTree();

        String port = tree.keySet().stream().filter((key) -> tree.get(key).equals(false))
                .findFirst().orElseThrow(() -> new RuntimeException("더 이상 남은 포트가 없습니다."));

        return port;

    }

    public void usingServerPort(String port) {
        endPointInit.callPortTree().put(port, true);
    }

    public void disableServerPort(String port) {
        endPointInit.callPortTree().put(port, false);
    }

}
