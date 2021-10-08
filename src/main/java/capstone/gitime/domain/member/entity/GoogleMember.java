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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = "GOOGLE")
public class GoogleMember extends Member {

    @Builder(builderMethodName = "createdGoogleMemberEntity")
    public GoogleMember(String email, String password, String userName, String nickName, String phoneNumber, LocalDate birth, Authority authority) {
        super(email, password, userName, nickName, phoneNumber, birth, authority);
    }
}
