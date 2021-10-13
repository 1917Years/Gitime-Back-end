package capstone.gitime.domain.member.service;

import capstone.gitime.api.common.token.TokenDto;
import capstone.gitime.api.common.token.TokenProvider;
import capstone.gitime.domain.member.entity.Authority;
import capstone.gitime.domain.member.entity.KakaoMember;
import capstone.gitime.domain.member.repository.MemberRepository;
import capstone.gitime.domain.member.service.dto.KakaoOauthTokenDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Primary
@Service("kakao")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class KakaoOauthService implements OauthService<KakaoOauthTokenDto> {

    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    @Value("${oauth2.password}")
    private String password;

    @Value("${oauth2.kakao.client-id}")
    private String CLIENT_ID;

//    @Value("${oauth2.kakao.redirect-uri}")
    private String REDIRECT_URI;


    @Override
    @Transactional
    public TokenDto getToken(String code) throws JsonProcessingException {


        ResponseEntity<String> responseEntity = restTemplate.exchange("https://kauth.kakao.com/oauth/token?grant_type=authorization_code&" +
                "client_id="+ CLIENT_ID +
                "&redirect_uri="+ REDIRECT_URI +
                "&code=" + code,
                HttpMethod.POST, createHttpEntity(null, MediaType.APPLICATION_FORM_URLENCODED,null), String.class);
        KakaoOauthTokenDto dto = parseToDto(responseEntity.getBody());
        return postTokenToGetInfo(dto.getAccess_token());
    }

    @Override
    public TokenDto postTokenToGetInfo(String token) throws JsonProcessingException {
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST, createHttpEntity(token, MediaType.APPLICATION_JSON,null), String.class);

        Map<String, String> a = (Map<String, String>) objectMapper.readValue(responseEntity.getBody(), Map.class);
        String email = "K" + String.valueOf(a.get("id")) + String.valueOf("@kakao.com");

        if (!memberRepository.existsByEmail(email)) {
            // 존재하지 않는 회원
            KakaoMember newMember = KakaoMember.createdKakaoMemberEntity()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .nickName(null)
                    .userName(null)
                    .birth(null)
                    .phoneNumber(null)
                    .authority(Authority.ROLE_NOT_SYNC_USER)
                    .build();
            memberRepository.save(newMember);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        return tokenProvider.generateTokenDto(authenticate);
    }

    @Override
    public KakaoOauthTokenDto parseToDto(String body) throws JsonProcessingException {
        return objectMapper.readValue(body, KakaoOauthTokenDto.class);
    }
}
