package com.project.happyshop.security.config;

import com.project.happyshop.security.authentication.handler.CommonAccessDeniedHandler;
import com.project.happyshop.security.authentication.provider.TokenProvider;
import com.project.happyshop.security.authentication.service.CustomOAuth2UserService;
import com.project.happyshop.security.filter.EmailPasswordApiAuthenticationFilter;
import com.project.happyshop.security.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class ApiSecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    private final EmailPasswordApiAuthenticationFilter emailPasswordApiAuthenticationFilter;

    // static 파일들은 spring security 에서 보안처리를 하지 않도록 해야 함
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .antMatchers("/static/bootstrap-5.3.0-alpha3-dist/js/**", "/static/images/**", "/static/bootstrap-5.3.0-alpha3-dist/css/**", "/static/scss/**", "/static/js/**");
    }

    @Order(1)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf().ignoringAntMatchers("/api/**"); // REST API 사용 시 csrf 비활성화 처리

        http
                .httpBasic().disable()
                .logout().disable();

        http.formLogin()
                .loginProcessingUrl("/api/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/api/")
                .failureUrl("/api/login?error=true")
                .loginPage("/api/login");

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilter(), OAuth2AuthorizationRequestRedirectFilter.class);
        http.addFilterBefore(emailPasswordApiAuthenticationFilter, JwtFilter.class);

        http
                .authorizeRequests()
                .antMatchers("/api/", "/api/items/detail/**", "/api/items", "/api/token").permitAll()
                .antMatchers("/api/login", "/api/register", "/api/successRegister").anonymous()
                .antMatchers("/api/logout").authenticated()
                .anyRequest().authenticated();

        http
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new AntPathRequestMatcher("/api/**"));

        http
                .oauth2Login(oauth2 ->
                        oauth2.userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig.userService(customOAuth2UserService)));

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