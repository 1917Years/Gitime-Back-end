package capstone.gitime.api.service.dto;

import capstone.gitime.domain.memberTeam.entity.MemberTeam;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class TeamInfoResponseDto {
    private String teamName;
    private String teamDescription;
    private int teamMemberCount;
    private List<String> teamMemberProfileImgUrl = new ArrayList<>();
    private String gitRepoName;
    private String gitRepoUrl;

    public TeamInfoResponseDto(MemberTeam memberTeam) {
        this.teamName = memberTeam.getTeam().getTeamName();
        this.teamDescription = memberTeam.getTeam().getTeamDescription();
//        this.teamMemberCount = memberTeam.;
//        this.teamMemberProfileImgUrl = teamMemberProfileImgUrl;
        this.gitRepoName = memberTeam.getTeam().getGitRepo().getUrl();
        this.gitRepoUrl = memberTeam.getTeam().getGitRepo().getUrl();
    }
}
