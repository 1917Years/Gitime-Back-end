package capstone.gitime.domain.memberteam.service.dto;

import capstone.gitime.domain.memberteam.entity.MemberTeam;
import capstone.gitime.domain.memberteam.entity.TeamMemberStatus;
import capstone.gitime.domain.team.service.dto.DevelopFieldResponseDto;
import capstone.gitime.domain.team.service.dto.InviteTeamRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class InvitationListRequestDto {

    private String teamName;
    private List<DevelopFieldResponseDto> teamDevelopTypes;
    private TeamMemberStatus teamMemberStatus;

    public static InvitationListRequestDto of(MemberTeam memberTeam) {
        return new InvitationListRequestDto(memberTeam.getTeam().getTeamName(),
                memberTeam.getTeam().getDevelopFields().stream()
                        .map(DevelopFieldResponseDto::of)
                        .collect(Collectors.toList()),
                memberTeam.getTeamMemberStatus());
    }
}
