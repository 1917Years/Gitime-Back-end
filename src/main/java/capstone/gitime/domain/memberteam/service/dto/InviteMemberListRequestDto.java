package capstone.gitime.domain.memberteam.service.dto;

import capstone.gitime.domain.memberteam.entity.MemberTeam;
import capstone.gitime.domain.memberteam.entity.TeamMemberStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InviteMemberListRequestDto {

    private String memberName;
    private String memberEmail;
    private TeamMemberStatus inviteStatus;

    public static InviteMemberListRequestDto of(MemberTeam memberTeam) {
        return new InviteMemberListRequestDto(memberTeam.getMember().getUserName(),
                memberTeam.getMember().getEmail(),
                memberTeam.getTeamMemberStatus());
    }

}
