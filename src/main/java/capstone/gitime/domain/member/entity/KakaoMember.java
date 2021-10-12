package capstone.gitime.domain.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Getter
@DiscriminatorValue(value = "KAKAO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoMember extends Member {

    private String profileImgUrl;

    @Builder(builderMethodName = "createdKakaoMemberEntity")
    public KakaoMember(String email, String password, String userName, String nickName, String phoneNumber, LocalDate birth, Authority authority, String profileImgUrl) {
        super(email, password, userName, nickName, phoneNumber, birth, authority);
        this.profileImgUrl = profileImgUrl;
    }
}
