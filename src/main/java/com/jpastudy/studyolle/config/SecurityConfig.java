package com.jpastudy.studyolle.config;

import com.jpastudy.studyolle.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountService accountService;

    private final DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // mvcMatcher 사용 시, 인증 없이 사용할 수 있지만 안전 하지 않은 것까지 허용 하지 않음
                .mvcMatchers("/", "/login", "/sign-up", "/check-email-token"
                        , "/email-login", "/check-email-login", "/login-link").permitAll()
                .mvcMatchers(HttpMethod.GET, "/profile/*").permitAll()
                .anyRequest()
                .authenticated();

        http.formLogin()
                .loginPage("/login") // custom login page
                .permitAll();

        http.logout()
                .logoutSuccessUrl("/");

        // Spring Security RememberMe Cookie
        // 1. 해싱 기반의 쿠키 방식, 탈취 당할 시 보안 취약
        //    -> Username, password, 만료 기간, key를 해싱하여 쿠키 발급.
        //     - 개선 방안 : Username과 랜덤한 토큰을 이용해 로그인 쿠키를 만들고, 인증 시점(로그인) 마다 쿠키를 변경.
        //                  단, 해싱 기반의 쿠키 방식과 마찬 가지로 토큰을 탈취 당할 시 보안 취약.
        // 2. 시리즈 기반의 쿠키 방식
        //    -> Username, 랜덤 토큰, 고정된 시리즈(난수로 생성)를 이용하여 쿠키 발급.
        //       Username, 랜덤 토큰, 시리즈를 DB에 저장하고 인증 시점(로그인) 마다 토큰을 새로 발급하여 업데이트.
        //       해커가 토큰을 탈취해 인증 하더라도, username과 시리즈가 일치하지 않기 때문에 보안 안전.
        //       - 토큰이 일치하지 않은 경우 모든 세션정보를 삭제 시킴.
        http.rememberMe()
                // .key("Cookie key") // 해싱만 이용한 방법, 보안 취약.
                .tokenRepository(tokenRepository());
    }

    // series 기반 rememberMe Cookie 설정 시 JdbcTokenRepository bean 등록 필요!
    // series, username, token을 저장할 persistent_logins 테이블이 필요하며, JPA를 사용하기 때문에 Entity 생성.
    // -> com.jpastudy.studyolle.domain.PersistentLogins.java
    // * JdbcTokenRepositoryImpl에 ddl 정의되어 있음.
    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // static 정적 자원은 필터링 x
        web.ignoring()
                .mvcMatchers("/node_modules/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
