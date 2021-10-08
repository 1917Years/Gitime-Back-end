package capstone.gitime.domain.member.entity;

import capstone.gitime.domain.common.entity.BaseTimeEntity;
import capstone.gitime.domain.common.entity.GitRepo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "memberType")
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

    @OneToMany(mappedBy = "member")
    private List<GitRepo> gitRepos = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private Authority authority;

    private boolean gitSync;

    public Member(String email, String password, String userName, String nickName, String phoneNumber, LocalDate birth, Authority authority) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
        this.authority = authority;
        this.gitSync = false;
    }

    public void updateSync() {
        this.gitSync = true;
    }
}
