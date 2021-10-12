package capstone.gitime.api.service;

import capstone.gitime.api.common.utils.RestTemplateUtils;
import capstone.gitime.api.service.dto.GithubTokenRequestDto;
import capstone.gitime.domain.common.entity.GitRepo;
import capstone.gitime.domain.common.repository.GitRepoRepository;
import capstone.gitime.domain.member.entity.Authority;
import capstone.gitime.domain.member.entity.Member;
import capstone.gitime.domain.member.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SyncService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final GitRepoRepository gitRepoRepository;
    private final RestTemplateUtils restTemplateUtils;

    @Value("${sync.github.client-id}")
    private String CLIENT_ID;

    @Value("${sync.github.client-secret}")
    private String CLIENT_SECRET;

    @Transactional
    public void getTokenByGithub(String code, Long memberId) throws JsonProcessingException {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException());
        if (findMember.getAuthority().equals(Authority.ROLE_SYNC_USER)) {
            System.out.println("findMember = " + findMember);
            return;
        }

        ResponseEntity<String> responseEntity = restTemplate.exchange("https://github.com/login/oauth/access_token?" +
                        "client_id=" + CLIENT_ID +
                        "&client_secret=" + CLIENT_SECRET +
                        "&code=" + code,
                HttpMethod.POST, restTemplateUtils.createHttpEntity(null,MediaType.APPLICATION_JSON), String.class);

        getInfoByGithubToken(findMember,objectMapper.readValue(responseEntity.getBody(), GithubTokenRequestDto.class),memberId);

    }

    public void getInfoByGithubToken(Member findMember, GithubTokenRequestDto requestDto, Long memberId) throws JsonProcessingException {
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://api.github.com/user",
                HttpMethod.GET, restTemplateUtils.createHttpEntity(requestDto.getAccess_token(),MediaType.APPLICATION_JSON), String.class);

        Map<String, Object> a = (Map<String, Object>) objectMapper.readValue(responseEntity.getBody(), Map.class);
        String gitName = (String) a.get("login");

        List<GitRepo> gitRepos = new ArrayList<>();

        ResponseEntity<String> responseEntity2 = restTemplate.exchange("https://api.github.com/users/" +
                gitName + "/repos",
                HttpMethod.GET, restTemplateUtils.createHttpEntity(null,MediaType.APPLICATION_JSON), String.class);


        List<Map<String, String>> b = objectMapper.readValue(responseEntity2.getBody(), List.class);
        b.forEach(r -> {
            String url = r.get("html_url");
            gitRepos.add(GitRepo.createGitRepoEntity()
                    .url(url)
                    .member(findMember)
                    .build());
        });
        gitRepoRepository.saveAll(gitRepos);
        findMember.updateSync();

    }

}
