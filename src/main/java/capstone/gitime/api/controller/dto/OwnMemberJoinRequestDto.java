package capstone.gitime.api.controller.dto;

import capstone.gitime.domain.member.entity.Authority;
import capstone.gitime.domain.member.entity.Member;
import capstone.gitime.domain.member.entity.OwnMember;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Data
public class OwnMemberJoinRequestDto {

    private String email;
    private String password;
    private String userName;
    private String nickName;
    private String phoneNumber;
    private String birth;

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return OwnMember.createOwnMemberEntity()
                .email(this.getEmail())
                .password(passwordEncoder.encode(this.getPassword()))
                .userName(this.getUserName())
                .nickName(this.getNickName())
                .phoneNumber(this.getPhoneNumber())
                .birth(LocalDate.of(2020,1,23))
                .authority(Authority.ROLE_NOT_SYNC_USER)
                .build();
    }
}
