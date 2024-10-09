package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.model.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long>{
	
	@Query("select rating from Rating rating where rating.product.Id = :productId")
	public List<Rating> getAllRatings(@Param("productId") Long productId);
	
}
