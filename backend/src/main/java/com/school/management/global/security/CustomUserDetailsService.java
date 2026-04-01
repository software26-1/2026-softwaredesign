package com.school.management.global.security;

import com.school.management.domain.auth.entity.User;
import com.school.management.domain.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service //서비스 레이어
@RequiredArgsConstructor //생성자 안써도 됨 , final 붙은 필드를 파라미터로 받는 생성자를 Lombok이 자동생성
public class CustomUserDetailsService implements UserDetailsService { //UserDetailService 인터페이스 구현,spring security가 자동으로 이 클래스 인식

    private final UserRepository userRepository; //DB에서 유저 조회할 때 필요하므로

    // UserDetails를 반환하는 메서드,spring Security가 이 메서드를 호출해서 반환된 UserDetails를 가지고 인증 처리
    // JwtAuthenticationFilter에서
    //  Long userId = jwtProvider.getUserId(token);  // Long
    //  userDetailsService.loadUserByUsername(String.valueOf(userId));  // String으로 변환해서 넘김
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException { //UserDetailService 인터페이스의 메서드 구현,JWT에서 거낸 Userid를 받음
        User user = userRepository.findById(Long.parseLong(userId))
                .filter(u -> !u.isDeleted()) //sofedelete 된 유저 로그인 못 하게 차단
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다.")); //유저 없으면 예외

        return new org.springframework.security.core.userdetails.User( //spring security 내장 userdetails 구현체로 감싸서 반환
                String.valueOf(user.getId()),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}


