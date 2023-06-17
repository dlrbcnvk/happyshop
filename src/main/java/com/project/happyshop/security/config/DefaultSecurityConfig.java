package com.project.happyshop.security.config;

import com.project.happyshop.security.authentication.handler.CommonAccessDeniedHandler;
import com.project.happyshop.security.authentication.handler.FormAuthenticationFailureHandler;
import com.project.happyshop.security.authentication.handler.FormAuthenticationSuccessHandler;
import com.project.happyshop.security.authentication.service.CustomOAuth2UserService;
import com.project.happyshop.security.authentication.service.CustomUserDetailsService;
import com.project.happyshop.security.filter.JwtFilter;
import com.project.happyshop.security.repository.InMemoryJwtTokenRepository;
import com.project.happyshop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class DefaultSecurityConfig {

    private final FormAuthenticationSuccessHandler formAuthenticationSuccessHandler;
    private final FormAuthenticationFailureHandler formAuthenticationFailureHandler;
    private final CustomUserDetailsService userDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final MemberService memberService;

    private final PasswordEncoder passwordEncoder;

    // static 파일들은 spring security 에서 보안처리를 하지 않도록 해야 함
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers("/static/bootstrap-5.3.0-alpha3-dist/js/**", "/static/images/**", "/static/bootstrap-5.3.0-alpha3-dist/css/**", "static/scss/**");
    }

    @Order(0)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/register", "/successRegister", "/items/detail/**").permitAll()
                .antMatchers("/login").anonymous()
                .antMatchers("/logout").authenticated();

        http
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
            .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login.html?error=true")
                .usernameParameter("email")
                .passwordParameter("password")
                .loginProcessingUrl("/login")
                .successHandler(formAuthenticationSuccessHandler)
                .failureHandler(formAuthenticationFailureHandler)
            .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("SESSION", "JSESSIONID", "remember-me")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
            ;

        http
                .oauth2Login(oauth2 ->
                        oauth2.userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig.userService(customOAuth2UserService)));

        http.csrf().ignoringAntMatchers("/api/**"); // REST API 사용 시 csrf 비활성화 처리

        http.cors().configurationSource(corsConfigurationSource()); // CorsConfigurer 설정 초기화



       http.sessionManagement()
                .maximumSessions(1)// 동시 세션 제어
                .maxSessionsPreventsLogin(true)
                .expiredUrl("/");



       http.sessionManagement()
               .sessionFixation().changeSessionId() // 세션 고정 보호
               .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED); // 세션 정책

        return http.build();
    }

    /**
     * Cors
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        passwordEncoder.encode("dd");

        return source;
    }

    /**
     * HttpSessionEventPublisher : 세션이 생성되거나 폐기될 때 호출되는 클래스
     * 동시 세션 제어 설정한 경우, 로그아웃 하고 다시 로그인할 때 세션의 최대 개수를 조절하여 성공적으로 재로그인 할 수 있도록 함.
     */
    @Bean
    public ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }



    /**
     * AccessDeniedHandler
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        CommonAccessDeniedHandler commonAccessDeniedHandler = new CommonAccessDeniedHandler();
        commonAccessDeniedHandler.setErrorPage("/denied");
        return commonAccessDeniedHandler;
    }



}
