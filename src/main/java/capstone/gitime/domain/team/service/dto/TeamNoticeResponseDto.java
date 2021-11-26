package capstone.gitime.domain.team.service.dto;

import capstone.gitime.domain.team.entity.TeamNotice;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TeamNoticeResponseDto {

    private String notice;
    private LocalDate postAt;

    public static TeamNoticeResponseDto of(TeamNotice teamNotice) {
        LocalDateTime dateTime = teamNotice.getCreatedAt();
        return new TeamNoticeResponseDto(teamNotice.getNotice(), LocalDate.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth()));
    }

}
