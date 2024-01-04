package com.jpastudy.studyolle.main;

import com.jpastudy.studyolle.account.AccountRepository;
import com.jpastudy.studyolle.account.AccountService;
import com.jpastudy.studyolle.account.SignUpForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // junit5 사용시, @RunWith or @ExtendWith 필요 X. 해당 annotation에 이미 포함되어 있음.
@AutoConfigureMockMvc
public class MainControllerTest {

    // @Autowired로 주입 받는 이유 - junit5는 dependency injection을 지원하는데, 타입이 정해져 있음.
    // junit5는 생성자 주입 지원 X,
    // Spring dependency injection 전, junit이 먼저 해당 생성자에 다른 인스턴스를 주입하려고 시도하기 때문.
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void beforeEach() {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("test");
        signUpForm.setEmail("test@github.com");
        signUpForm.setPassword("test-password");
        accountService.processNewAccount(signUpForm);
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @DisplayName("이메일로 로그인 처리")
    @Test
    void login_with_email() throws Exception {
        mockMvc.perform(post("/login")
                        // username, password는 Spring security에 정해져 있는 parameter.
                        // Security Config에서 username, password parameter custom 가능.
                        .param("username", "test@github.com")
                        .param("password", "test-password")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("test"));
    }

    @DisplayName("닉네임으로 로그인 처리")
    @Test
    void login_with_nickname() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "test")
                        .param("password", "test-password")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("test"));
    }

    @DisplayName("로그인 실패")
    @Test
    void login_fail() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "noUser")
                        .param("password", "no-password")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @WithMockUser
    @DisplayName("로그아웃")
    @Test
    void logout() throws Exception {
        mockMvc.perform(post("/logout")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());
    }
}
