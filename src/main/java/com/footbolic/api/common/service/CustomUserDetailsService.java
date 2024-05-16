package com.footbolic.api.common.service;

import com.footbolic.api.member.entity.MemberEntity;
import com.footbolic.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(() ->
                new UsernameNotFoundException("사용자가 존재하지 않습니다."));
        return new User(memberId, "",  List.of(new SimpleGrantedAuthority(member.getRoleId())));
    }
}
