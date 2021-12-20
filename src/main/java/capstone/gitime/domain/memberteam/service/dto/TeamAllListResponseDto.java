package capstone.gitime.domain.memberteam.service.dto;

import capstone.gitime.api.service.dto.TeamInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamAllListResponseDto {

    private Page<TeamInfoResponseDto> pages;

    private Map<String, TeamAllListAdditionalResponseDto> maps;



}
