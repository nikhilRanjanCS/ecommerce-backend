package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	
	@Query("select review from Review review where review.product.Id = :productId")
	public List<Review> getAllReviews(@Param("productId") Long productId);
	
}
