package com.oasis.hms.utils;

import com.oasis.hms.model.User;
import com.oasis.hms.service.UserService;
import org.springframework.util.Base64Utils;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Toyin on 2/3/19.
 */
public class GenericUtil {
    public static LocalDateTime truncateTime(LocalDateTime dateTime) {
        return dateTime.truncatedTo(ChronoUnit.DAYS);
    }

    public static LocalDateTime ceilTime(LocalDateTime dateTime) {
        return dateTime.truncatedTo(ChronoUnit.DAYS).plusHours(24).minusNanos(1);
    }

    public static User getActiveUser(Principal principal, UserService userService) {
        return userService.findOne(principal.getName());
    }

    public static String bytesToBase64(byte[] bytes) {
        return Base64Utils.encodeToString(bytes);
    }

    public static LocalDateTime getFirstDayOfTheWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        return LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS);
    }

    public static LocalDateTime getFirstDayOfTheMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE, 1);
        return LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS);
    }
}
