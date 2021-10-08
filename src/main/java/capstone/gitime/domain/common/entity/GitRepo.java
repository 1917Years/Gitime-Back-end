package capstone.gitime.domain.common.entity;

import capstone.gitime.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GitRepo {

    @Id
    @Column(name = "git_repo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "git_repo_member_fk"))
    private Member member;

    @Builder(builderMethodName = "createGitRepoEntity")
    public GitRepo(String url, Member member) {
        this.url = url;
        updateMember(member);
    }

    private void updateMember(Member member) {
        this.member = member;
        member.getGitRepos().add(this);
    }
}
