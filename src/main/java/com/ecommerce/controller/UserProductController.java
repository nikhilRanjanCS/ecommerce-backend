package com.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.exception.ProductException;
import com.ecommerce.model.Product;
import com.ecommerce.service.ProductService;

@RestController
@RequestMapping("/api")
public class UserProductController {
	
	@Autowired
	private ProductService productService;
	
//	@GetMapping("/products")
//	public ResponseEntity<Page<Product>> findProductByCategoryHandler(@RequestParam String
//			category, @RequestParam List<String> colors, @RequestParam List<String> sizes,
//			@RequestParam Integer minPrice, @RequestParam Integer maxPrice, 
//			@RequestParam Integer minDiscount, @RequestParam String sort, 
//			@RequestParam String stock, @RequestParam Integer pageNumber, 
//			@RequestParam Integer pageSize){
//		
//		Page<Product> res = productService.getFilteredProducts(category, colors, sizes, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize);
//		
//		
//		return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
//	}
	
	@GetMapping("/products")
	public ResponseEntity<Page<Product>> findProductByCategoryHandler(@RequestParam String
			category, @RequestParam List<String> colors, @RequestParam List<String> sizes,
			 @RequestParam String sort, @RequestParam Integer pageNumber, 
			@RequestParam Integer pageSize){
		
		Page<Product> res = productService.getFilteredProducts(category, colors, sizes, sort, pageNumber, pageSize);
		
		
		return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
	}
	
	
	@GetMapping("/products/id/{productId}")
	public ResponseEntity<Product> getProductByIdHandler(@PathVariable Long productId)
	throws ProductException{
		
		Product product = productService.findProductById(productId);
		
		return new ResponseEntity<>(product,HttpStatus.ACCEPTED);
		
	}
	
	
//	@GetMapping("/products/search")
//	public ResponseEntity<List<Product>> searchProductHandler(@RequestParam String q){
//		
//	}
	
	
}
