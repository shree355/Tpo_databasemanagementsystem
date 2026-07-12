package net.javaguides.sms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguides.sms.entity.RegisterEntity;

public interface RegisterRepository extends JpaRepository<RegisterEntity, Long> {
    Optional<RegisterEntity> findByUserName(String userName);
    Optional<RegisterEntity> findByEmail(String email);
}
