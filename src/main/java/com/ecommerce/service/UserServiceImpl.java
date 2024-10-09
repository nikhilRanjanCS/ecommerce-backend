package com.ecommerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.config.JwtProvider;
import com.ecommerce.exception.UserException;
import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Override
	public User findUserById(Long userId) throws UserException {
		
		Optional<User> user = userRepository.findById(userId);
		if(user!=null) {
			return user.get();
		}
		
		throw new UserException("user id : "+ userId+ " not found");
	}

	@Override
	public User findUserProfileByJwt(String jwt) throws UserException {
		
		String email = jwtProvider.getEmailFromToken(jwt);
		User user = userRepository.findByEmail(email);
		
		if(user==null) {
			throw new UserException("no user found with email-id : "+email);
		}
		return user;
	}

}
