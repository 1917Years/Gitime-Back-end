package capstone.gitime.domain.member.service;

import capstone.gitime.api.common.token.TokenDto;
import capstone.gitime.api.common.token.TokenProvider;
import capstone.gitime.api.controller.dto.OwnMemberJoinDto;
import capstone.gitime.api.controller.dto.OwnMemberLoginDto;
import capstone.gitime.domain.member.repository.MemberRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Transactional
    public void ownJoin(OwnMemberJoinDto dto) {
        DuplicateEmailExists(dto);
        memberRepository.save(dto.toEntity(passwordEncoder));
    }

    public TokenDto ownLogin(OwnMemberLoginDto dto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        return tokenProvider.generateTokenDto(authenticate);
    }

    private void DuplicateEmailExists(OwnMemberJoinDto member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new RuntimeException();
        }
    }
}
