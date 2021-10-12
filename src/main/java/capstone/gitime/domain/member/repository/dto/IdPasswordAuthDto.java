package capstone.gitime.domain.member.repository.dto;

import capstone.gitime.domain.member.entity.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdPasswordAuthDto {

    private Long id;
    private String password;
    private Authority authority;
}
