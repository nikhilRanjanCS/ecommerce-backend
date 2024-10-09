package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
	
	@Query("select cart from Cart cart where cart.user.Id = :userId")
	public Cart findCartByUserId(@Param("userId") Long userId);
	
}
