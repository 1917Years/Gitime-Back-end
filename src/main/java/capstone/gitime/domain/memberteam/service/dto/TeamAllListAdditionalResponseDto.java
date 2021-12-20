package capstone.gitime.domain.memberteam.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class TeamAllListAdditionalResponseDto {

    private int teamMemberCount;
    private List<Integer> working = new ArrayList<>();
    private Double progress;

}
