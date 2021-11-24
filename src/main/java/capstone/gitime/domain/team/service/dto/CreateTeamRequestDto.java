package capstone.gitime.domain.team.service.dto;

import capstone.gitime.domain.team.entity.DevelopType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateTeamRequestDto {

    private String teamName;
    private String teamDescription;
    private String gitRepoUrl;
    private DevelopType developType;

}
