package com.helpdeskchatbot.helpdeskchat.repository;

import com.helpdeskchatbot.helpdeskchat.entity.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {

}


