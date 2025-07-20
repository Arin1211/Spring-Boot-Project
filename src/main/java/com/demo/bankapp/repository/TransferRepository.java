package com.demo.bankapp.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.demo.bankapp.model.Transfer;

@RepositoryRestResource(exported = false)
public interface TransferRepository extends JpaRepository<Transfer, Long> {

	@Query("SELECT t FROM Transfer t WHERE t.fromUserId = :userId AND t.transferTime >= :cutoff")
	List<Transfer> findAllTransfersFrom24Hours(@Param("userId") Long userId, @Param("cutoff") Date cutoff);

}
