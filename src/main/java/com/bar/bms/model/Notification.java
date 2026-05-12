package com.bar.bms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bossId;
    private String message;
    private LocalDateTime createdAt;
    private boolean seen = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBossId() { return bossId; }
    public void setBossId(Long bossId) { this.bossId = bossId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isSeen() { return seen; }
    public void setSeen(boolean seen) { this.seen = seen; }
}