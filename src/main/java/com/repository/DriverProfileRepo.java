package com.repository;

import com.domain.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@EnableJpaRepositories
@Repository
public interface DriverProfileRepo extends JpaRepository<DriverProfile,Integer>
{
    Optional<DriverProfile> findOneByEmailAndPassword(String email, String password);
    Optional<DriverProfile> findOneByUserIdAndIsActiveTrue(String userId);
    DriverProfile findByEmail(String email);

    Optional<DriverProfile> findOneByEmail(String email);
}
