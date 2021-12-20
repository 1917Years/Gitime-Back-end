package capstone.gitime.api.service.dto;

import capstone.gitime.domain.memberteam.entity.MemberTeam;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class TeamInfoResponseDto {
    private String teamName;
    private String teamDescription;
    private LocalDate teamCreatedAt;

    public TeamInfoResponseDto(MemberTeam memberTeam) {
        this.teamName = memberTeam.getTeam().getTeamName();
        this.teamDescription = memberTeam.getTeam().getTeamDescription();
//        this.teamMemberCount = memberTeam.;
//        this.working = memberTeam.get
        LocalDateTime time = memberTeam.getTeam().getCreatedAt();
        this.teamCreatedAt = LocalDate.of(time.getYear(), time.getMonth(), time.getDayOfMonth());
    }
}
