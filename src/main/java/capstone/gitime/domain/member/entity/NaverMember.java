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
@DiscriminatorValue(value = "NAVER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NaverMember extends Member {
    //TODO 필드 추가
    private String test;

    @Builder(builderMethodName = "createdNaverMemberEntity")

    public NaverMember(String email, String password, String userName, String nickName, String phoneNumber, LocalDate birth, Authority authority, String test) {
        super(email, password, userName, nickName, phoneNumber, birth, authority);
        this.test = test;
    }
}
