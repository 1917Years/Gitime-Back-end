package capstone.gitime.domain.common.service.dto;

import capstone.gitime.domain.common.entity.GitRepo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GitRepoResponseDto {

    private String gitRepoUrl;
    private String gitRepoName;

    public GitRepoResponseDto(String gitRepoUrl, String gitRepoName) {
        this.gitRepoUrl = gitRepoUrl;
        this.gitRepoName = gitRepoName;
    }

    public static GitRepoResponseDto of(GitRepo gitRepo) {
        return new GitRepoResponseDto(gitRepo.getUrl(), null);
    }
}
