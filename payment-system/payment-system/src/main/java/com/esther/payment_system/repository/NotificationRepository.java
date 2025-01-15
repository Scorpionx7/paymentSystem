package com.esther.payment_system.repository;

import com.esther.payment_system.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository <Notification, Long>{

}
