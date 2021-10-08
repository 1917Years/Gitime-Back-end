package capstone.gitime.api.service;

import capstone.gitime.api.service.dto.GithubTokenRequestDto;
import capstone.gitime.domain.common.entity.GitRepo;
import capstone.gitime.domain.common.repository.GitRepoRepository;
import capstone.gitime.domain.member.entity.Member;
import capstone.gitime.domain.member.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SyncService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final GitRepoRepository gitRepoRepository;

    private final String CLIENT_ID = "a76250c81934f034f0d9";
    private final String CLIENT_SECRET = "46f8be76a51a6511ce20a4a83c6e48ce576e775f";
    private final String REDIRECT_URL = "";

    public void getTokenByGithub(String code, Long memberId) throws JsonProcessingException {
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://github.com/login/oauth/access_token?" +
                        "client_id=" + CLIENT_ID +
                        "&client_secret=" + CLIENT_SECRET +
                        "&code=" + code,
                HttpMethod.POST, createHttpEntity(null), String.class);

        getInfoByGithubToken(objectMapper.readValue(responseEntity.getBody(), GithubTokenRequestDto.class),memberId);

    }

    public void getInfoByGithubToken(GithubTokenRequestDto requestDto, Long memberId) throws JsonProcessingException {
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://api.github.com/user",
                HttpMethod.GET, createHttpEntity(requestDto.getAccess_token()), String.class);

        Map<String, Object> a = (Map<String, Object>) objectMapper.readValue(responseEntity.getBody(), Map.class);
        String gitName = (String) a.get("login");

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException());

        List<GitRepo> gitRepos = new ArrayList<>();

        ResponseEntity<String> responseEntity2 = restTemplate.exchange("https://api.github.com/users/" +
                gitName + "/repos",
                HttpMethod.GET, createHttpEntity(null), String.class);

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

    private HttpEntity createHttpEntity(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(new MediaType(MediaType.APPLICATION_JSON_VALUE)));
        if (StringUtils.hasText(token))
            httpHeaders.add("Authorization", token);

        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        return httpEntity;
    }

}
