package com.repository;

import com.domain.DocumentStore;
import com.domain.DriverProfile;
import com.domain.OnboardingVerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface OnBoardingVerificationStatusRepo extends JpaRepository<OnboardingVerificationStatus,Integer>
{
    @Query( value = "select o.* from onboarding_verification_status o join driver_profile d on o.user_id = d.user_id where o.user_id = :userId and o.type = :type", nativeQuery =
            true )
    Optional<OnboardingVerificationStatus> findOneByUserIdAndType(@Param( "userId" ) String userId, @Param( "type" ) String type);

}
