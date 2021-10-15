package capstone.gitime.api.common.oauth2;

import capstone.gitime.api.common.token.TokenProvider;
import capstone.gitime.domain.member.entity.Authority;
import capstone.gitime.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService userService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = userService.loadUser(userRequest);

        String clientId = userRequest.getClientRegistration().getClientId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(Authority.ROLE_NOT_SYNC_USER.toString())),
                oAuth2User.getAttributes(), userNameAttributeName);
    }
}
