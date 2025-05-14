package com.remote_vitals.backend.db_handler;

import com.remote_vitals.backend.user.entities.User;
import org.springframework.context.ApplicationContext;

public class StaticDataClass {
    public static Integer currentUserId;
    public static User registeringUserDetails;
    public static String lastOtpSent;
    public static ApplicationContext context1;
}
