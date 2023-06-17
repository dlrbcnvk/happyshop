package com.project.happyshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.happyshop.config.jwt.JwtFactory;
import com.project.happyshop.domain.Address;
import com.project.happyshop.domain.SocialProvider;
import com.project.happyshop.domain.dto.CreateAccessTokenRequest;
import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.domain.entity.RefreshToken;
import com.project.happyshop.repository.JpaMemberRepository;
import com.project.happyshop.repository.JpaRefreshTokenRepository;
import com.project.happyshop.repository.MemberRepository;
import com.project.happyshop.security.authentication.provider.TokenProvider;
import com.project.happyshop.security.util.JwtProperties;
import com.project.happyshop.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class TokenApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    private MemberService memberService;
    @Autowired
    private JpaRefreshTokenRepository jpaRefreshTokenRepository;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private JpaMemberRepository jpaMemberRepository;

    @AfterEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        Member findMember = jpaMemberRepository.findByEmailAndProvider("deapsea@gmail.com", SocialProvider.LOCAL);
        jpaMemberRepository.delete(findMember);
    }

    @DisplayName("createNewAccessToken: 새로운 액세스 토큰을 발급한다.")
    @Test
    public void createNewAccessToken() throws Exception {
        // given
        final String url = "/api/token";

        String email = "deapsea@gmail.com";
        Member member = Member.createMember(
                email,
                SocialProvider.LOCAL,
                "deapsea",
                "1234",
                "010-1234-1234",
                new Address("juso", "detail", "zipcode")
        );
        memberService.join(member);

        String token = JwtFactory.builder()
                .claims(Map.of("id", member.getId()))
                .build()
                .createToken(tokenProvider);

        RefreshToken refreshToken = new RefreshToken(member.getId(), token);
        jpaRefreshTokenRepository.save(refreshToken);

        CreateAccessTokenRequest request = new CreateAccessTokenRequest();
        request.setRefreshToken(token);
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }
}
