package com.example.KTB_assignment_week4.configuration;

import com.example.KTB_assignment_week4.configuration.jwt.JwtFilter;
import com.example.KTB_assignment_week4.configuration.jwt.JwtProperties;
import com.example.KTB_assignment_week4.configuration.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        JwtFilter jwtFilter = new JwtFilter(jwtTokenProvider);

        RequestMatcher csrfRequestMatcher = request -> {
            if (!HttpMethod.POST.matches(request.getMethod())){ //POST요청이 아니라면 CSRF 검사X
                return false;
            }

            String path = request.getServletPath(); //요청 URL

            return path.equals("/auth/refresh") || path.equals("/auth/logout") || path.equals("/auth/refresh"); //Access Token리프레쉬, 회원가입 또는 로그아웃 시에만 CSRF검사
        };



        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .csrf(csrf -> csrf.
                        csrfTokenRepository(
                                CookieCsrfTokenRepository.withHttpOnlyFalse()   //JS에서 CSRF 토큰을 헤더에 담아 전송하므로 HTTPOnly 설정 끔
                        )
                        .requireCsrfProtectionMatcher(csrfRequestMatcher)   //설정해둔 URL에서만 수행
                )

                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())        //마찬가지로 JWT사용하므로 form로그인과 Basic 인증 비활설와

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)      //세션 비활성화
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/error").permitAll()  //인증 관련 경로는 인증 불필요
                        .requestMatchers("/admin/**").hasRole("ADMIN")        //추후 생길 ADMIN관련 경로는 ADMIN(ROLE_ADMIN)권한 필요
                        .anyRequest().authenticated()                           //나머지는 인증이 필ㅓ
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() { //프론트엔드 CORS 설정.
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://127.0.0.1:5500"));                //허용할 출처, JS 라이브 서버 주소
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));    //허용할 요청
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-XSRF-TOKEN"));//허용할 헤더
        configuration.setAllowCredentials(true);                                                //자격증명 요청 허용
        configuration.setMaxAge(3600L);                                                         //Preflight 결과 캐시 시간.

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);                         //모든 경로에 대해 정의한 CORS 정책 적용

        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
