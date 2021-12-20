package capstone.gitime.domain.team.service.dto;

import capstone.gitime.domain.memberteam.entity.MemberTeam;
import capstone.gitime.domain.memberteam.entity.TeamAuthority;
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
    private TeamAuthority teamAuthority;
    private Integer totalMembers;

    public static TeamAdminInfoResponseDto of(MemberTeam memberTeam,Integer totalMembers) {
        return new TeamAdminInfoResponseDto(memberTeam.getTeam().getTeamName(),
                memberTeam.getTeam().getTeamDescription(), memberTeam.getTeam().getDevelopType(),
                memberTeam.getTeam().getGitRepo().getUrl(),
                memberTeam.getTeamAuthority(), totalMembers);
    }

}
