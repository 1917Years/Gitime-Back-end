package capstone.gitime.domain.endpoint.service.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShellCommandResponseDto {

    private List<String> commands = new ArrayList<String>();
}
