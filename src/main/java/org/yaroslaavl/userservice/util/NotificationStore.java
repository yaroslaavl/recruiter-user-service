package org.yaroslaavl.userservice.util;

import lombok.experimental.UtilityClass;
import org.yaroslaavl.userservice.broker.dto.NotificationDto;
import org.yaroslaavl.userservice.database.entity.User;

import java.util.Map;

@UtilityClass
public class NotificationStore {

    public static NotificationDto userBlockNotification(User user, String blockTime) {
        return NotificationDto.builder()
                .targetUserId(user.getKeycloakId())
                .entityType("SYSTEM")
                .notificationType("EMAIL")
                .content("user_temporary_block")
                .contentVariables(Map.of(
                        "email", user.getEmail(),
                        "unblock_date", blockTime
                ))
                .build();
    }

    public static NotificationDto recruiterRegistrationApprove(User user) {
        return NotificationDto.builder()
                .targetUserId(user.getKeycloakId())
                .entityType("SYSTEM")
                .notificationType("EMAIL")
                .content("recruiter_registration_approve")
                .contentVariables(Map.of(
                        "email", user.getEmail()
                ))
                .build();
    }

    public static NotificationDto userRegistered(User user) {
        return NotificationDto.builder()
                .targetUserId(user.getKeycloakId())
                .entityType("SYSTEM_USER_REGISTRATION")
                .notificationType("DASHBOARD_APP")
                .contentVariables(Map.of(
                        "firstName", user.getFirstName()
                ))
                .build();
    }
}
