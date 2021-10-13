package capstone.gitime.domain.member.service;

import capstone.gitime.api.common.token.TokenDto;
import capstone.gitime.api.common.token.TokenProvider;
import capstone.gitime.api.controller.dto.OwnMemberJoinRequestDto;
import capstone.gitime.api.controller.dto.OwnMemberLoginRequestDto;
import capstone.gitime.api.exception.exception.member.DuplicateEmailException;
import capstone.gitime.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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
    public void ownJoin(OwnMemberJoinRequestDto dto) {
        DuplicateEmailExists(dto);
        memberRepository.save(dto.toEntity(passwordEncoder));
    }

    public TokenDto ownLogin(OwnMemberLoginRequestDto dto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        return tokenProvider.generateTokenDto(authenticate);
    }

    private void DuplicateEmailExists(OwnMemberJoinRequestDto member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new DuplicateEmailException("해당 이메일은 이미 가입이 완료된 회원입니다.");
        }
    }

    /**
     * TODO
     * 이메일 및 비밀번호 찾기
     */

}
