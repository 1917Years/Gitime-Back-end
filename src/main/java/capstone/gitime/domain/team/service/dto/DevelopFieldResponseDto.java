package capstone.gitime.domain.team.service.dto;

import capstone.gitime.domain.developfield.entity.DevelopField;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DevelopFieldResponseDto {

    private String field;

    public static DevelopFieldResponseDto of(DevelopField developField) {
        return new DevelopFieldResponseDto(developField.getField());
    }
}
