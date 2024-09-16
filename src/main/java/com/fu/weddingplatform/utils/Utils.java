package com.fu.weddingplatform.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public static LocalDateTime convertStringToLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDate = LocalDateTime.parse(date, formatter);
        return localDate;
    }

    public static List<String> parseStringToListImages(String imageString) {
        List<String> result = new ArrayList<String>();
        if (imageString != null && imageString != "") {
            String[] imageArray = imageString.split("\n,");
            for (String image : imageArray) {
                result.add(image.trim());
            }
        }
        return result;
    }

    public static String formatAmountToVND(long amount) {
        // Define the decimal format pattern
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMAN);
        decimalFormat.applyPattern("#,###");

        // Format the amount and append the currency
        return decimalFormat.format(amount) + " VND";
    }

    public static String formatServiceDetail(String name, String price, String note, String dateComplete) {
        return String.format("- " + "%-" + 35 + "s%s\n \t\tNgày hoàn thành: %s \n\t\tGhi chú: %s\n", name, price,
                dateComplete,
                note);
    }

    public static String formatLocalDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(dateTimeFormatter);
    }

}
