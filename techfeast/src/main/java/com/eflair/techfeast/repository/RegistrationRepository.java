package com.eflair.techfeast.repository;

import com.eflair.techfeast.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    List<Registration> findByEventName(String eventName);

    List<Registration> findByPaymentMode(String paymentMode);

    List<Registration> findByEventNameAndPaymentMode(String eventName, String paymentMode);

}
