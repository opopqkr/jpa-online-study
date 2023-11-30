package com.jpastudy.studyolle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    /**
     * password 양방향 암호화, 복호화를 할 필요 없음(단방향 암호화만 필요)
     * spring security 에서는 해싱 algorithm 을 default 로 bcrypt 사용 </br>
     * <p>
     * salt 하는 이유
     * - 이미 해싱 정보를 해커가 알고 있을 수 있기 때문에 특정 값(salt)을 추가 하여 해싱 (salt-소금, 소금을 첨가)
     * - salt value 를 매번 랜덤하게 하여 해싱한 결과가 매번 바뀜.
     * - 입력한 password 평문 + 저장된 해시 value 를 다시 해싱하면 저장된 해시 value 가 나옴.
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
