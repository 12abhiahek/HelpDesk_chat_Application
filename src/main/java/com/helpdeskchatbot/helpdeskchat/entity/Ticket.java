package com.helpdeskchatbot.helpdeskchat.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long ticketId;

    @NotNull(message = "Title cannot be blank")
    private String title;

    @NotNull(message = "Description cannot be blank")
    @Column(length = 2000)
    private String description;

    @NotNull(message = "Status cannot be null")
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull(message = "Priority cannot be null")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @NotNull(message = "AssignedTo cannot be blank")
    private String assignedTo;

    @NotNull(message = "Username cannot be blank")
    @Column(unique = true)
    private String username;

    @Column(nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate createdDate;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedDateTime;

    private LocalTime endTime;

    @Column(unique = true)
    private String email;
    /**
     * Sets timestamps before persisting the entity.
     */
    @PrePersist
    void prePersist() {
        if (this.createdDate == null) {
            this.createdDate = LocalDate.now();
        }
        if (this.updatedDateTime == null) {
            this.updatedDateTime = LocalDateTime.now();
        }
    }

    /**
     * Updates the timestamp before each update.
     */
    @PreUpdate
    void preUpdate() {
        this.updatedDateTime = LocalDateTime.now();
    }

}
