package capstone.gitime.domain.member.service;

import capstone.gitime.api.common.token.TokenDto;
import capstone.gitime.api.common.token.TokenProvider;
import capstone.gitime.domain.member.entity.Authority;
import capstone.gitime.domain.member.entity.GoogleMember;
import capstone.gitime.domain.member.entity.KakaoMember;
import capstone.gitime.domain.member.repository.MemberRepository;
import capstone.gitime.domain.member.service.dto.GoogleOauthTokenDto;
import capstone.gitime.domain.member.service.dto.KakaoOauthTokenDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service("google")
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoogleOauthService implements OauthService<GoogleOauthTokenDto>{

    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    @Value("${oauth2.password}")
    private String password;

    @Value("${oauth2.google.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${oauth2.google.client-id}")
    private String CLIENT_ID;

    @Value("${oauth2.google.client-secret}")
    private String CLIENT_SECRET;

    @Override
    @Transactional
    public TokenDto getToken(String code) throws JsonProcessingException {
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://oauth2.googleapis.com/token?grant_type=authorization_code" +
                        "&client_id=" + CLIENT_ID +
                        "&client_secret=" + CLIENT_SECRET +
                        "&redirect_uri=" + REDIRECT_URI +
                        "&code=" + code,
                HttpMethod.POST, createHttpEntity(null, MediaType.APPLICATION_FORM_URLENCODED, null), String.class);
        GoogleOauthTokenDto dto = parseToDto(responseEntity.getBody());
        return postTokenToGetInfo(dto.getAccess_token());
    }

    @Override
    public TokenDto postTokenToGetInfo(String token) throws JsonProcessingException {
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST, createHttpEntity(token, MediaType.APPLICATION_JSON,null), String.class);

        Map<String, String> a = (Map<String, String>) objectMapper.readValue(responseEntity.getBody(), Map.class);
        String email = "G" + String.valueOf(a.get("id")) + String.valueOf("@google.com");

        if (!memberRepository.existsByEmail(email)) {
            // 존재하지 않는 회원
            GoogleMember newMember = GoogleMember.createdGoogleMemberEntity()
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
    public GoogleOauthTokenDto parseToDto(String body) throws JsonProcessingException {
        return objectMapper.readValue(body, GoogleOauthTokenDto.class);
    }
}
