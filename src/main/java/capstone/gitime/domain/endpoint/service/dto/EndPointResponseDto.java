package capstone.gitime.domain.endpoint.service.dto;

import capstone.gitime.domain.endpoint.entity.BuildStatus;
import capstone.gitime.domain.endpoint.entity.EndPoint;
import capstone.gitime.domain.endpoint.entity.ServerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class EndPointResponseDto {
    private LocalDateTime codeUpdateAt;
    private LocalDateTime buildUpdateAt;
    private BuildStatus buildStatus;
    private ServerStatus serverStatus;
    private String serverUrl;
    private Boolean serverCreated;

    public static EndPointResponseDto of(EndPoint endPoint) {
        return new EndPointResponseDto(endPoint.getLastCodeUpdateAt(),
                endPoint.getLastCodeBuildAt(),
                endPoint.getBuildStatus(),
                endPoint.getServerStatus(),
                endPoint.getServerUrl(),
                endPoint.getServerCreated());

    }

}
