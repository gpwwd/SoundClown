package com.soundclown.common;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * Получение текущего авторизованного пользователя, например, его ID.
 * Работает в паре с CustomUserDetails, где есть метод getId().
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal(expression = "id")
public @interface CurrentUser {
}
