package capstone.gitime.domain.endpoint.service;

import capstone.gitime.api.exception.exception.team.NotFoundTeamException;
import capstone.gitime.domain.endpoint.entity.BuildStatus;
import capstone.gitime.domain.endpoint.entity.EndPoint;
import capstone.gitime.domain.endpoint.entity.ServerStatus;
import capstone.gitime.domain.endpoint.repository.EndPointRepository;
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
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EndPointService {

    private final EndPointRepository endPointRepository;
    private final TeamRepository teamRepository;

    // --------------- 로컬 서버 ----------------

    @Value("${endpoint.code.dir}")
    private String codeDir;

    // 코드 파일 다운로드

    private Integer shellCommand(String command) {
        Integer resultCode = null;
        String s;
        Process p;
        try {
            String[] cmd = {"/bin/bash", "-c", command};
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println(s);
            p.waitFor();
            resultCode = p.exitValue();
            System.out.println("exit: " + p.exitValue());
            p.destroy();

        } catch (Exception e) {
        }

        return resultCode;
    }

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

    public void runDocker(MultipartFile multipartFile, String serverPort, String teamName) throws IOException, InterruptedException {

        Team findTeam = teamRepository.findTeamByName(teamName)
                .orElseThrow(() -> new NotFoundTeamException());

        EndPoint endPoint = findTeam.getEndPoint();

        endPoint.updateServerPort(serverPort);

        // 최상위 루트
        String codeFilePath = endPoint.getCodeFilePath();

        multipartFile.transferTo(new File(codeFilePath + "/Dockerfile"));

        if(shellCommand("cd " + codeFilePath + " && ./gradlew build -x test && " +
                "docker build --build-arg DEPENDENCY=build/dependency --platform linux/amd64 --tag " + endPoint.getDockerImageName() + " . && " +
                "docker run -d -p 8081:" + endPoint.getServerPort() + " " + endPoint.getDockerImageName()) != 0){
            throw new RuntimeException();
        };

        endPoint.updateServerUrl("http://localhost:" + endPoint.getServerPort());
        endPoint.updateBuildStatus(BuildStatus.SUCCESS);
        endPoint.updateServerStatus(ServerStatus.ON);

    }

    // docker build --build-arg DEPENDENCY=build/dependency --platform linux/amd64 --tag dockerImageName. 실행

    // docker run -d -p 8081:serverPort dockerImageName

    // status 반환

}
