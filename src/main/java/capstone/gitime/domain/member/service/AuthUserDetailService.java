package capstone.gitime.domain.member.service;

import capstone.gitime.api.exception.exception.member.NotFoundEmailException;
import capstone.gitime.domain.member.entity.Member;
import capstone.gitime.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundEmailException("이메일을 찾을 수 없습니다."));

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(findMember.getAuthority().toString());
        return new User(String.valueOf(findMember.getId()), findMember.getPassword(), Collections.singleton(grantedAuthority));
    }
}
