package capstone.gitime.domain.team.service.dto;

import capstone.gitime.domain.team.entity.DevelopType;
import lombok.Data;


@Data
public class UpdateTeamInfoRequestDto {

    private String teamName;
    private String teamDescription;
    private DevelopType developType;
    private String fromMemberEmail;
    private String toMemberEmail;

}