package com.fu.weddingplatform.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import com.fu.weddingplatform.entity.Account;
import com.fu.weddingplatform.jwt.JwtConfig;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class Utils {
    public static String buildJWT(Authentication authenticate, Account accountAuthenticated, SecretKey secretKey,
            JwtConfig jwtConfig) {
        String token = Jwts.builder().setSubject(authenticate.getName())
                .claim("authorities", authenticate.getAuthorities())
                .claim("email", accountAuthenticated.getEmail())
                .claim("accountId", accountAuthenticated.getId())
                .setIssuedAt((new Date()))
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                .signWith(secretKey).compact();
        return token;
    };

    public static String formatVNDatetimeNow() {
        ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now(vietnamZoneId);
        return localDateTime.format(dateTimeFormatter);
    }

    public static LocalDate getCurrentDate() {
        ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime vietnamZonedDateTime = ZonedDateTime.now(vietnamZoneId);
        LocalDate vietnamLocalDate = vietnamZonedDateTime.toLocalDate();
        return vietnamLocalDate;
    }

    public static LocalDate convertStringToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }

}
