package com.project.happyshop.security.config;

import com.project.happyshop.security.authentication.handler.CommonAccessDeniedHandler;
import com.project.happyshop.security.authentication.provider.TokenProvider;
import com.project.happyshop.security.authentication.service.CustomOAuth2UserService;
import com.project.happyshop.security.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class ApiSecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    @Order(1)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/api/", "/api/register", "/api/successRegister", "/api/items/detail/**", "/api/items", "api/token").permitAll()
                .antMatchers("/api/login").anonymous()
                .antMatchers("/api/logout").authenticated()
                .anyRequest().authenticated();

        http
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/api/login"))
            .and()
                .formLogin()
                .loginProcessingUrl("/api/login")
                .failureUrl("/api/login?error=true")
                .usernameParameter("email")
                .passwordParameter("password")
            .and()
                .logout()
                .logoutUrl("/api/logout")
                .logoutSuccessUrl("/api/")
                .deleteCookies("SESSION", "JSESSIONID", "remember-me")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
        ;

        http
                .oauth2Login(oauth2 ->
                        oauth2.userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig.userService(customOAuth2UserService)));

        http.csrf().ignoringAntMatchers("/api/**"); // REST API 사용 시 csrf 비활성화 처리

        http.addFilterBefore(jwtFilter(), OAuth2AuthorizationRequestRedirectFilter.class);




        return http.build();
    }


    /**
     * jwtFilter
     */
    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(tokenProvider);
//        JwtFilter filter = new JwtFilter();
//        filter.setAuthenticationManager(apiAuthenticationManager());
    }

//    public AuthenticationManager apiAuthenticationManager() {
//        List<AuthenticationProvider> authProviderList = new ArrayList<>();
//        authProviderList.add(apiAuthenticationProvider);
//        ProviderManager providerManager = new ProviderManager(authProviderList);
//        return providerManager;
//    }

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

//    /**
//     * AccessDeniedHandler
//     */
//    @Bean
//    public AccessDeniedHandler accessDeniedHandler() {
//        CommonAccessDeniedHandler commonAccessDeniedHandler = new CommonAccessDeniedHandler();
//        commonAccessDeniedHandler.setErrorPage("/api/denied");
//        return commonAccessDeniedHandler;
//    }
}