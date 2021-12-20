package capstone.gitime.domain.endpoint.entity;

import capstone.gitime.domain.endpoint.service.dto.ShellCommandResponseDto;
import capstone.gitime.domain.team.entity.Team;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.catalina.Server;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class EndPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "end_point_id")
    private Long id;

    private LocalDateTime lastCodeUpdateAt;

    private LocalDateTime lastCodeBuildAt;

    @Enumerated(value = EnumType.STRING)
    private ServerStatus serverStatus;

    @Enumerated(value = EnumType.STRING)
    private BuildStatus buildStatus;

    private String serverUrl;

    private String serverPort;

    private String codeFilePath;

    // teamName
    private String dockerImageName;

    private Boolean serverCreated;

    private String dockerContainerName;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", foreignKey = @ForeignKey(name = "team_end_point_fk"))
    private Team team;


    @Builder(builderMethodName = "createEndPoint")
    public EndPoint(LocalDateTime lastCodeUpdateAt, LocalDateTime lastCodeBuildAt, ServerStatus serverStatus,
                    BuildStatus buildStatus, String serverUrl, String serverPort,
                    String codeFilePath, String dockerImageName,
                    Team team) {

        this.lastCodeUpdateAt = lastCodeUpdateAt;
        this.lastCodeBuildAt = lastCodeBuildAt;
        this.serverStatus = serverStatus;
        this.buildStatus = buildStatus;
        this.serverUrl = serverUrl;
        this.serverPort = serverPort;
        this.codeFilePath = codeFilePath;
        this.dockerImageName = dockerImageName;
        this.team = team;
        this.serverCreated = false;
        this.dockerContainerName = UUID.randomUUID().toString();
        updateEndPoint(team);
    }

    public void updateServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void updateServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public void updateServerStatus(ServerStatus serverStatus) {
        this.serverStatus = serverStatus;
    }

    public void updateBuildStatus(BuildStatus buildStatus) {
        this.buildStatus = buildStatus;
    }

    public void updateLastCodeBuildAt(LocalDateTime lastCodeBuildAt) {
        this.lastCodeBuildAt = lastCodeBuildAt;
    }

    public void updateEndPoint(Team team) {
        team.updateEndPoint(this);
    }

    public void updateServerCreated() {
        this.serverCreated = true;
    }

    public void updateDockerContainer(String dockerContainerName) {
        this.dockerContainerName = dockerContainerName;
    }


}
