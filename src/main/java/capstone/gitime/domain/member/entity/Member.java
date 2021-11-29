package capstone.gitime.domain.member.entity;

import capstone.gitime.api.controller.dto.MemberModifyRequestDto;
import capstone.gitime.api.controller.dto.OauthMemberJoinRequestDto;
import capstone.gitime.domain.common.entity.BaseTimeEntity;
import capstone.gitime.domain.common.entity.GitRepo;
import capstone.gitime.domain.memberteam.entity.MemberTeam;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "memberType")
@ToString(of = {"email","userName","nickName"})
public abstract class Member extends BaseTimeEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String userName;
    private String nickName;

    private String phoneNumber;

    private LocalDate birth;

    private LocalDateTime lastGitSyncAt;

    @OneToMany(mappedBy = "member")
    private List<GitRepo> gitRepos = new ArrayList<>();

    private String profileImgName;

    @Enumerated(value = EnumType.STRING)
    private Authority authority;

    // 연관관계 필드
    @OneToMany(mappedBy = "member")
    private List<MemberTeam> memberTeams = new ArrayList<>();

    public Member(String email, String password, String userName, String nickName, String phoneNumber, LocalDate birth, Authority authority) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
        this.authority = authority;
    }

    // Update To Authority -> ROLE_SYNC_USER
    public void updateSync() {
        this.authority = Authority.ROLE_SYNC_USER;
        this.lastGitSyncAt = LocalDateTime.now();
    }

    public void updateInfo(MemberModifyRequestDto requestDto, PasswordEncoder passwordEncoder) {
        if (hasText(requestDto.getNickName())) {
            this.nickName = requestDto.getNickName();
        } else if (hasText(requestDto.getPhoneNumber())) {
            this.phoneNumber = requestDto.getPhoneNumber();
        } else if (hasText(requestDto.getPassword())) {
            this.password = passwordEncoder.encode(requestDto.getPassword());
        }
    }

    public void updateOauthInfo(OauthMemberJoinRequestDto requestDto) {
        this.nickName = requestDto.getNickName();
        this.phoneNumber = requestDto.getPhoneNumber();
        this.birth = LocalDate.now();
        this.userName = requestDto.getUserName();
    }

    public void updateProfileImg(String storeFileName) {
        this.profileImgName = storeFileName;
    }
}
