package com.helpdeskchatbot.helpdeskchat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailLog {

    public enum Status {
        PENDING, SENT, FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipient;

    private String subject;

    @Column(length = 4000)
    private String body;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String errorMessage;

    private LocalDateTime createdAt;

    private LocalDateTime sentAt;

    private Long ticketId;

}

