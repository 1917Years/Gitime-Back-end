package capstone.gitime.domain.member.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaverMessageDto {
    private String to;
    private String content;
}
