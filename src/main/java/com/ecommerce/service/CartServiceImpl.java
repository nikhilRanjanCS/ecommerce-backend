package com.ecommerce.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.exception.CartItemException;
import com.ecommerce.exception.ProductException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.request.AddItemRequest;

@Service
public class CartServiceImpl implements CartService {
	
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	
	private CartItem cartItem;
	

	@Override
	public Cart createCart(User user) {
		
		Cart cart = new Cart();
		cart.setUser(user);
		
		return cartRepository.save(cart);
		
	}

	@Override
	public String addCartItem(Long userId, AddItemRequest request) throws ProductException,
	CartItemException{
		
		
		
		Cart cart = getUserCart(userId);
		Product product = productService.findProductById(request.getProductId());
		if(cartItemService.ifItemExists(cart, product, request.getSize(), userId)==null) {
			cartItem = new CartItem();
			cartItem.setCart(cart);
			cartItem.setUserId(userId);
			cartItem.setProduct(product);
			cartItem.setSize(request.getSize());
			cartItem.setQuantity(request.getQuantity());
			cartItem.setPrice(product.getPrice()*request.getQuantity());
			cartItem.setDiscountedPrice(product.getDiscountedPrice()*request.getQuantity());
			
			
		}
		
		else {
			cartItem = cartItemService.ifItemExists(cart, product, request.getSize(), userId);
			cartItem.setCart(cart);
			cartItem.setUserId(userId);
			cartItem.setProduct(product);
			cartItem.setSize(request.getSize());
			cartItem.setQuantity(request.getQuantity()+
					cartItemService.findCartItemByProductId(product.getId(),
							userId).getQuantity());
			cartItem.setPrice(product.getPrice()*request.getQuantity()
					+cartItemService.findCartItemByProductId(product.getId(),
							userId).getPrice());
			cartItem.setDiscountedPrice(product.getDiscountedPrice()*request.getQuantity()
					+cartItemService.findCartItemByProductId(product.getId(),
							userId).getDiscountedPrice());
			
		}
		
		cart.getCartItems().add(cartItem);
		cartRepository.save(cart);
		
		return "item added to cart successfully";
	}

	@Override
	public Cart getUserCart(Long userId) {
		
		Cart cart = cartRepository.findCartByUserId(userId);
		
		double totalPrice = 0;
		int totalDiscountedPrice = 0;
		int totalItem = 0;
		
		for(CartItem cartItem:cart.getCartItems()) {
			totalPrice = totalPrice+cartItem.getPrice();
			totalDiscountedPrice = totalDiscountedPrice+cartItem.getDiscountedPrice();
			totalItem = totalItem+cartItem.getQuantity();
		}
		
		cart.setTotalDiscountedPrice(totalDiscountedPrice);
		cart.setTotalPrice(totalPrice);
		cart.setTotalItem(totalItem);
		cart.setDiscount((int)totalPrice-totalDiscountedPrice);
		
		return cartRepository.save(cart);
	}

}
