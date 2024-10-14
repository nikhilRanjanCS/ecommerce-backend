package com.ecommerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.exception.CartItemException;
import com.ecommerce.exception.UserException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.CartItemRepository;


@Service
public class CartItemServiceImpl implements CartItemService {
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private UserService userService;
	
	
	
	
	@Override
	public CartItem createCartItem(CartItem cartItem) {
		cartItem.setQuantity(1);
		cartItem.setPrice(cartItem.getProduct().getPrice()*cartItem.getQuantity());
		cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice()*
				cartItem.getQuantity());
		
		
		return cartItemRepository.save(cartItem);
	}

	@Override
	public CartItem updateCartItem(Long userId, Long itemId, CartItem cartItem)
			throws CartItemException, UserException {
		
		CartItem item = findCartItemById(itemId);
		User user = userService.findUserById(userId);
		
		item.setQuantity(cartItem.getQuantity());
		item.setPrice(cartItem.getProduct().getPrice()*cartItem.getQuantity());
		item.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice()*
				cartItem.getQuantity());
		
		return cartItemRepository.save(item);
	}

	@Override
	public CartItem ifItemExists(Cart cart, Product product, String size, Long userId) {
		
		return cartItemRepository.ifItemExists(cart, product, size, userId);
		
	}

	@Override
	public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
		
		CartItem cartItem = findCartItemById(cartItemId);
		User user = userService.findUserById(cartItem.getUserId());
		User reqUser = userService.findUserById(userId);
		
		if(reqUser==user) {
			cartItemRepository.deleteById(cartItemId);
		}
		
		else {
			throw new UserException("can't remove other user's item");
		}
		
	}

	@Override
	public CartItem findCartItemById(Long cartItemId) throws CartItemException {
		
		Optional<CartItem> optional = cartItemRepository.findById(cartItemId);
		if(optional.isPresent()) {
			return optional.get();
		}
		else {
			throw new CartItemException("item with id : "+cartItemId+" not found");
		}
		
	}
	
	@Override
	public CartItem findCartItemByProductId(Long productId, Long userId) throws CartItemException{
		
		CartItem item = cartItemRepository.findCartItemByproductId(productId,
				userId);
		if(item!=null) {
			return item;
		}
		else {
			throw new CartItemException("user has no cart item with given productId : "
		+productId);
		}
		
	}
	
}
