package com.ecommerce.service;

import com.ecommerce.exception.CartItemException;
import com.ecommerce.exception.ProductException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.User;
import com.ecommerce.request.AddItemRequest;

public interface CartService {
	
	public Cart createCart(User user);
	
	public String addCartItem(Long userId, AddItemRequest request)throws ProductException,
	CartItemException;
	
	public Cart getUserCart(Long userId);
	
	
	
}
