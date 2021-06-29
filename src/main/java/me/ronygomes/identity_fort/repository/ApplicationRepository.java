package me.ronygomes.identity_fort.repository;

import me.ronygomes.identity_fort.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    Optional<Application> findByClientId(String clientId);
}
