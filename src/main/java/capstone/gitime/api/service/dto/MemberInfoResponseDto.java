package capstone.gitime.api.service.dto;

import capstone.gitime.domain.member.entity.Authority;
import capstone.gitime.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoResponseDto {
    private boolean isSync;
    private String email;
    private String userName;
    private String nickName;

    private String phoneNumber;

    private LocalDate birth;
    private LocalDateTime lastGitSyncAt;

    private String profileImg;

    public MemberInfoResponseDto(Member member) {
        isSync(member);
        this.email = member.getEmail();
        this.userName = member.getUserName();
        this.nickName = member.getNickName();
        this.phoneNumber = member.getPhoneNumber();
        this.birth = member.getBirth();
        this.lastGitSyncAt = member.getLastGitSyncAt();
        this.profileImg = member.getProfileImgName();
    }

    private void isSync(Member member) {
        if (member.getAuthority().equals(Authority.ROLE_SYNC_USER)) {
            this.isSync = true;
        } else {
            this.isSync = false;
        }
    }
}
