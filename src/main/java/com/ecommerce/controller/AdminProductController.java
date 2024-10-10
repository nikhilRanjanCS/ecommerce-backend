package com.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.exception.ProductException;
import com.ecommerce.model.Product;
import com.ecommerce.request.CreateProductRequest;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.service.ProductService;

@RestController
@RequestMapping("api/admin/products")
public class AdminProductController {
	
	@Autowired
	private ProductService productService;
	
	
	@PostMapping("/createproduct")
	public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest req){
		
		Product createdProduct = productService.createProduct(req);
		
		return new ResponseEntity<Product>(createdProduct,HttpStatus.CREATED);
		
	}
	
	
	@DeleteMapping("/{productId}/delete")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) throws
	ProductException{
		
		productService.deleteProduct(productId);
		
		ApiResponse res = new ApiResponse();
		res.setMessage("product deleted successfully");
		res.setStatus(true);
		
		return new ResponseEntity<ApiResponse>(res,HttpStatus.OK);
		
	}
	
	
	@GetMapping("/")
	public List<Product> getAllProductsHandler(){
		
		List<Product> products = productService.getAllProducts();
		
		return products;
		
	}
	
}
