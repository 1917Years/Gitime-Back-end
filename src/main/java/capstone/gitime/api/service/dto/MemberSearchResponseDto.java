package capstone.gitime.api.service.dto;

import capstone.gitime.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MemberSearchResponseDto {

    private String memberEmail;
    private String memberName;
    private String memberNickName;
    private LocalDate memberBirth;
    private String memberProfileImageUrl;

    public static MemberSearchResponseDto of(Member member) {
        return new MemberSearchResponseDto(member.getEmail(),
                member.getUserName(),
                member.getNickName(),
                member.getBirth(),
                member.getProfileImgName());
    }
}
