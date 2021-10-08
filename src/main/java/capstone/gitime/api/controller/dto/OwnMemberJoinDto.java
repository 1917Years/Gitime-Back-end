package capstone.gitime.api.controller.dto;

import capstone.gitime.domain.member.entity.Authority;
import capstone.gitime.domain.member.entity.Member;
import capstone.gitime.domain.member.entity.OwnMember;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class OwnMemberJoinDto {

    private String email;
    private String password;
    private String username;
    private String nickName;

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return OwnMember.createOwnMemberEntity()
                .email(this.getEmail())
                .password(passwordEncoder.encode(this.getPassword()))
                .userName(this.getUsername())
                .nickName(this.getNickName())
                .authority(Authority.ROLE_USER)
                .build();
    }
}
