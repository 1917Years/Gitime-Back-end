package capstone.gitime.domain.team.service.dto;

import capstone.gitime.domain.memberteam.entity.MemberTeam;
import capstone.gitime.domain.memberteam.entity.TeamAuthority;
import capstone.gitime.domain.team.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamMemberListInfoRequestDto {

    private String memberEmail;
    private String memberName;
//    private String memberProfileImgUrl;
    private TeamAuthority teamAuthority;
    private String developField;

    public static TeamMemberListInfoRequestDto of(MemberTeam memberTeam) {
        return new TeamMemberListInfoRequestDto(memberTeam.getMember().getEmail(),
                memberTeam.getMember().getUserName(),
                memberTeam.getTeamAuthority(),
                memberTeam.getDevelopField() == null ? null:memberTeam.getDevelopField().getField());
    }

}
