package ru.fokin.java_pro_limitservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fokin.java_pro_limitservice.entity.UserLimit;

public interface UserLimitRepository extends JpaRepository<UserLimit, Long> {
}