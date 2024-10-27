package com.ecommerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecommerce.exception.ProductException;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.request.CreateProductRequest;


@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private UserService userService;
	
	
	@Override
	public Product createProduct(CreateProductRequest request) {
		
		Category topLevelCategory = categoryRepository.findByName(request.getTopLevelCategory());
		
		if(topLevelCategory==null) {
			topLevelCategory = new Category();
			topLevelCategory.setName(request.getTopLevelCategory());
			topLevelCategory.setLevel(1);
			topLevelCategory = categoryRepository.save(topLevelCategory);
		}
		
		Category secondLevelCategory = categoryRepository.findByNameAndParent(request.getSecondLevelCategory(),
				topLevelCategory.getName());
		
		if(secondLevelCategory==null) {
			secondLevelCategory = new Category();
			secondLevelCategory.setName(request.getSecondLevelCategory());
			secondLevelCategory.setLevel(2);
			secondLevelCategory.setParentCategory(topLevelCategory);
			secondLevelCategory = categoryRepository.save(secondLevelCategory);
		}
		
		Category thirdLevelCategory = categoryRepository.findByNameAndParent(request.getThirdLevelCategory(),
				secondLevelCategory.getName());
		
		if(thirdLevelCategory==null) {
			thirdLevelCategory = new Category();
			thirdLevelCategory.setName(request.getThirdLevelCategory());
			thirdLevelCategory.setLevel(3);
			thirdLevelCategory.setParentCategory(secondLevelCategory);
			thirdLevelCategory = categoryRepository.save(thirdLevelCategory);
		}
		
		Product product = new Product();
		
		product.setTitle(request.getTitle());
		product.setBrand(request.getBrand());
		product.setColor(request.getColor());
		product.setDescription(request.getDescription());
		product.setPrice(request.getPrice());
		product.setDiscountRate(request.getDiscountRate());
		product.setDiscountedPrice(request.getDiscountedPrice());
		product.setCategory(thirdLevelCategory);
		product.setImageUrl(request.getImageUrl());
		product.setQuantity(request.getQuantity());
		product.setSizes(request.getSizes());
		product.setCreatedAt(LocalDateTime.now());
		
		product = productRepository.save(product);
		
		return product;
	}

	@Override
	public String deleteProduct(Long productId) throws ProductException {
		
		Product product = findProductById(productId);
		product.getSizes().clear();
		productRepository.delete(product);
		
		return "Product deleted successfully";
	}

	@Override
	public Product updateProduct(Long productId, Product request) throws ProductException {
		
		Product product = findProductById(productId);
		
		if(request.getQuantity()!=0) {
			product.setQuantity(request.getQuantity());
		}
		
		return productRepository.save(product);
	}

	@Override
	public Product findProductById(Long productId) throws ProductException {
		
		Optional<Product> optional = productRepository.findById(productId);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		
		throw new ProductException("Product not found");
	}

	@Override
	public List<Product> findProductsByCategory(String category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Product> getFilteredProducts(String category, List<String> colors, List<String> sizes,
			String sort, Integer pageNumber, Integer pageSize) {
		
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		
		List<Product> products = productRepository.filterProducts(category, sort);
		
		if(!colors.isEmpty()) {
			products = products.stream().filter(p->colors.stream().anyMatch(
					c->c.equalsIgnoreCase(p.getColor())))
					.collect(Collectors.toList());
		}
		
//		if(stock!=null) {
//			if("in_stock".equals(stock)) {
//				products = products.stream().filter(p->p.getQuantity()>0).collect(
//						Collectors.toList());
//			}
//			else if("out_of_stock".equals(stock)) {
//				products = products.stream().filter(p->p.getQuantity()<1).collect(
//						Collectors.toList());
//			}
//		}
		
		int startIndex = (int)pageable.getOffset();	
		int endIndex = Math.min(startIndex+pageable.getPageSize(), products.size());
		
		List<Product> pageContent = products.subList(startIndex, endIndex);
		
		Page<Product> filteredProducts = new PageImpl<>(pageContent,pageable,products.size());
		
		return filteredProducts;
	}

	@Override
	public List<Product> getAllProducts() {
		
		List<Product> allProducts = productRepository.findAll();
		
		return allProducts;
	}
	
	

}
