package com.ecommerce.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.exception.OrderException;
import com.ecommerce.model.Address;
import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.User;
import com.ecommerce.repository.AddressRepository;
import com.ecommerce.repository.OrderItemRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.UserRepository;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Autowired
	private OrderItemRepository orderItemReposiory;
	
	
	@Override
	public Order createOrder(User user, Address shippingAddress) {
		
		shippingAddress.setUser(user);
		
//		The shipping address provided by the user is saved in to the address repository
//		and then the address is address is added to the list of addresses of the user
//		so that the the newCreatedAddress can be used next time
		
		Address newCreatedAddress = addressRepository.save(shippingAddress);
		user.getAddresses().add(newCreatedAddress);
		userRepository.save(user);
		
		Cart cart = cartService.getUserCart(user.getId());
		List<OrderItem> orderItems = new ArrayList<>();
		
		for(CartItem cartItem : cart.getCartItems()) {
			
			OrderItem orderItem = new OrderItem();
			orderItem.setPrice(cartItem.getPrice());
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setSize(cartItem.getSize());
			orderItem.setUserId(cartItem.getUserId());
			orderItem.setDiscountedPrice(cartItem.getDiscountedPrice());
			
			orderItems.add(orderItemReposiory.save(orderItem));
		}
		
		Order createdOrder = new Order();
		createdOrder.setUser(user);
		createdOrder.setOrderItems(orderItems);
		createdOrder.setTotalPrice(cart.getTotalPrice());
		createdOrder.setTotaldiscountedPrice(cart.getTotalDiscountedPrice());
		createdOrder.setTotalItem(cart.getTotalItem());
		createdOrder.setShippingAddress(newCreatedAddress);
		createdOrder.setOrderDate(LocalDateTime.now());
		createdOrder.setOrderStatus("PENDING");
		createdOrder.getPaymentDetails().setPaymentStatus("PENDING");
		createdOrder.setCreatedAt(LocalDateTime.now());
		
		Order savedOrder = orderRepository.save(createdOrder);
		
		for(OrderItem item:orderItems) {
			
			item.setOrder(savedOrder);
			orderItemReposiory.save(item);
		}
		
		return savedOrder;
	}

	@Override
	public Order findOrderById(Long orderId) throws OrderException {
		
		Optional<Order> optional = orderRepository.findById(orderId);
		if(optional.isPresent()) {
			return optional.get();
		}
		throw new OrderException("Order id not found");
	}

	@Override
	public List<Order> usersOrderHistory(Long userId) {
		
		List<Order> orders = orderRepository.getUserOrders(userId);
		return orders;
		
	}

	@Override
	public Order placeOrder(Long orderId) throws OrderException {
		
		Order order = findOrderById(orderId);
		order.setOrderStatus("PLACED");
		order.getPaymentDetails().setPaymentStatus("COMPLETED");
		
		return orderRepository.save(order);
	}

	@Override
	public Order confirmOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus("CONFIRMED");
		
		return orderRepository.save(order);
	}

	@Override
	public Order shipOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus("SHIPPED");
		
		return orderRepository.save(order);
	}

	@Override
	public Order deliverOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus("DELIVERED");
		
		return orderRepository.save(order);
	}

	@Override
	public Order cancelOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus("CANCELLED");
		
		return orderRepository.save(order);
	}

	@Override
	public List<Order> getAllOrders() {
		
		return orderRepository.findAll();
	}

	@Override
	public void deleteOrder(Long orderId) throws OrderException {
		
		orderRepository.deleteById(orderId);
		
	}

}
