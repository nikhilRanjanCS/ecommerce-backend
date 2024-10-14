package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
	@Query("select order from Order order where order.user.Id = :userId and ("
			+ "order.orderStatus = 'PLACED' or order.orderStatus = 'CONFIRMED' "
			+ "or order.orderStatus = 'SHIPPED' or order.orderStatus = 'DELIVERED')")
	public List<Order> getUserOrders(@Param("userId") Long userId);
	
	@Query("select order from Order order where order.user.Id = :userId and "
			+ "order.orderStatus = :orderStatus")
	public List<Order> getUserOrdersByOrderStatus(@Param("userId") Long userId,
			@Param("orderStatus") String orderStatus);
	
}
