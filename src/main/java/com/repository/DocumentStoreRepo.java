package com.repository;

import com.domain.DocumentStore;
import com.domain.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface DocumentStoreRepo extends JpaRepository<DocumentStore,Integer>
{
}
