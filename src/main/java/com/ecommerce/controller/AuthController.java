package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.config.JwtProvider;
import com.ecommerce.exception.UserException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.request.LoginRequest;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.response.AuthResponse;
import com.ecommerce.service.CartService;
import com.ecommerce.service.UserDetailsServiceImpl;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	private CartService cartService;
	
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException{
		
		if(userRepository.findByEmail(user.getEmail())!=null) {
			throw new UserException("Email already exists!");
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("ROLE_USER");
		User createdUser = userRepository.save(user);
		
		Cart cart = cartService.createCart(createdUser);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(createdUser.getEmail(), createdUser.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = jwtProvider.generateToken(authentication);
		
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(jwt);
		authResponse.setMessage("user created successfully");
		
		return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest){
		
		String username = loginRequest.getEmail();
		String password = loginRequest.getPassword();
		
//		Authentication authentication = authenticate(username,password);
//		
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//		
//		String jwt = jwtProvider.generateToken(authentication);
//		
//		AuthResponse authResponse = new AuthResponse();
//		authResponse.setJwt(jwt);
//		authResponse.setMessage("user login successfull");
//		
//		return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.OK);
		AuthResponse authResponse = new AuthResponse();
		try {
			Authentication authentication = authenticate(username,password);
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			String jwt = jwtProvider.generateToken(authentication);
			
			
			authResponse.setJwt(jwt);
			authResponse.setMessage("user login successfull");
			
			return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.OK);
		} catch(Exception e) {
			authResponse.setMessage(e.getMessage());
			return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.UNAUTHORIZED);
		}
	}
	
	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		
		if(userDetails==null) {
			throw new BadCredentialsException("Username not found!");
		}
		if(!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Incorrect username and password!");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		
	}
}
