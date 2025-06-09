package com.dungcode.demo.posgresql.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "notifications")
public class NotificationEvent extends AbstractEntity implements Serializable {
    private String notificationId;
    private String type; // EMAIL, SMS, PUSH
    private String recipient;
    private String subject;
    private String content;
    private Date timestamp;
    private NotificationStatus status;

    public enum NotificationStatus {
        PENDING,
        SENT,
        FAILED
    }
} 