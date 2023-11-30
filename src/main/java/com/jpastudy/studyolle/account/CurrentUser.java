package com.jpastudy.studyolle.account;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
/*
 * @AuthenticationPrincipal
 * 스프링 시큐리티의 스프링 웹 MVC 지원 annotation
 * 핸들러 매개변수로 현재 인증된 Principal을 참조 할 수 있음.
 * (AuthenticationToken의 첫 번째 인자가 Principal)
 *
 * @AuthenticationPrincipal를 사용하면, 로그인하지 않은 사용자는 "anonymousUser"라는 문자열.
 * 정규식을 통해 anonymousUser일 경우 account 객체는 null 반환됨.
 */
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")
public @interface CurrentUser {
}
