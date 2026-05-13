package com.bar.bms.repository;

import com.bar.bms.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification>
    findByBossIdOrderByCreatedAtDesc(Long bossId);

    long countByBossIdAndSeenFalse(Long bossId);
}