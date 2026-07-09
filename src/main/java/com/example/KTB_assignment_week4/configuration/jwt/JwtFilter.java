package com.example.KTB_assignment_week4.configuration.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        try {
            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Claims claims = jwtTokenProvider.validateAccessToken(token);
                String authority = jwtTokenProvider.getAuthority(claims);

                if (!StringUtils.hasText(authority)) {
                    throw new JwtException(
                            "JWT에 권한 정보가 없습니다."
                    );
                }

                CustomUserPrincipal customUserPrincipal = jwtTokenProvider.getCustomUserPrincipal(token);   //customUserPrincipal을 제작하고 이를 Authentication 객체에 넣어
                                                                                                            //추후 컨트롤러에서 @AuthenticationPrincipal을 이용하여 사용자 정보를 사용 가능하다.

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(customUserPrincipal, null,
                                List.of(
                                        new SimpleGrantedAuthority(authority)
                                )
                        );

                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);
            }
        } catch(JwtException | IllegalArgumentException exception){
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request,response);
    }

    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");    //요청 헤더에서 Authorization을 꺼냄
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
