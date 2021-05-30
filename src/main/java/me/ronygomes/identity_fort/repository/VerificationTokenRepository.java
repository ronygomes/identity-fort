package me.ronygomes.identity_fort.repository;

import me.ronygomes.identity_fort.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {
}
