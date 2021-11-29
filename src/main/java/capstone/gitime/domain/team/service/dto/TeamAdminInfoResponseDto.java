package capstone.gitime.domain.team.service.dto;

import capstone.gitime.domain.team.entity.DevelopType;
import capstone.gitime.domain.team.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamAdminInfoResponseDto {

    private String teamName;
    private String teamDescription;
    private DevelopType developType;
    private String gitRepoUrl;

    public static TeamAdminInfoResponseDto of(Team team) {
        return new TeamAdminInfoResponseDto(team.getTeamName(),
                team.getTeamDescription(), team.getDevelopType(),
                team.getGitRepo().getUrl());
    }

}
