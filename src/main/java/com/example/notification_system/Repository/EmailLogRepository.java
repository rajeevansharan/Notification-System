package com.example.notification_system.Repository;

import com.example.notification_system.Entity.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmailLogRepository extends JpaRepository<EmailLog,Long> {
}
