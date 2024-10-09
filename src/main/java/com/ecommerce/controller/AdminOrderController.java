package com.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.exception.OrderException;
import com.ecommerce.model.Order;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.service.OrderService;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {
	
	@Autowired
	OrderService orderService;
	
	@GetMapping("/")
	public ResponseEntity<List<Order>> getAllOrdersHandler(){
		
		List<Order> allOrders = orderService.getAllOrders();
		
		return new ResponseEntity<List<Order>>(allOrders,HttpStatus.ACCEPTED);
		
	}
	
	
	@PutMapping("/{orderId}/confirmed")
	public ResponseEntity<Order> getConfirmedOrderHandler(@PathVariable Long orderId,
			@RequestHeader("Authorization") String jwt) throws OrderException{
		
		Order confirmedOrder = orderService.confirmedOrder(orderId);
		
		return new ResponseEntity<>(confirmedOrder,HttpStatus.OK);
		
	}
	
	
	@PutMapping("/{orderId}/shipped")
	public ResponseEntity<Order> getShippedOrderHandler(@PathVariable Long orderId,
			@RequestHeader("Authorization") String jwt) throws OrderException{
		
		Order shippedOrder = orderService.shippedOrder(orderId);
		
		return new ResponseEntity<>(shippedOrder,HttpStatus.OK);
		
	}
	
	@PutMapping("/{orderId}/delivered")
	public ResponseEntity<Order> getDeliveredOrderHandler(@PathVariable Long orderId,
			@RequestHeader("Authorization") String jwt) throws OrderException{
		
		Order deliveredOrder = orderService.deliveredOrder(orderId);
		
		return new ResponseEntity<>(deliveredOrder,HttpStatus.OK);
		
	}
	
	
	@PutMapping("/{orderId}/cancelled")
	public ResponseEntity<Order> getCancelledOrderHandler(@PathVariable Long orderId,
			@RequestHeader("Authorization") String jwt) throws OrderException{
		
		Order cancelledOrder = orderService.cancelledOrder(orderId);
		
		return new ResponseEntity<>(cancelledOrder,HttpStatus.OK);
		
	}
	
	@DeleteMapping("/{orderId}/delete")
	public ResponseEntity<ApiResponse> deleteOrderHandler(@PathVariable Long orderId,
			@RequestHeader("Authorization") String jwt) throws OrderException{
		
		orderService.deleteOrder(orderId);
		
		ApiResponse res = new ApiResponse("order deleted successfully", true);
		
		return new ResponseEntity<ApiResponse>(res, HttpStatus.OK);
		
	}
}
