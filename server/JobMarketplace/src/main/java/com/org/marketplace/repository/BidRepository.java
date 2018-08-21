package com.org.marketplace.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.org.marketplace.model.Bid;

/**
 * @author gauravkahadane
 *
 */
public interface BidRepository extends JpaRepository<Bid, Long> {
	@Query("SELECT b.project.id FROM Bid b WHERE b.user.id = :userId")
	Page<Long> findBiddedProjectIdsByUserId(@Param("userId") Long userId, Pageable pageable);
}
