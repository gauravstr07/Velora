package com.ecommerce.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.project.exceptions.ResouceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public ProductDTO addProduct(Long categoryId, Product product) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResouceNotFoundException("Category", "categoryId", categoryId));
		product.setImage("default.png");
		product.setCategory(category);
		double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
		product.setSpecialPrice(specialPrice);
		Product savedProduct = productRepository.save(product);
		return modelMapper.map(savedProduct, ProductDTO.class);
	}

	@Override
	public ProductResponse getAllProducts() {
		List<Product> products = productRepository.findAll();
		List<ProductDTO> productDTOs = products.stream()
				.map(product -> modelMapper.map(product, ProductDTO.class))
				.collect(Collectors.toList());
		
		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(productDTOs);
		
		return productResponse;
	}

	@Override
	public ProductResponse searchByCategory(Long categoryId) {
		
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResouceNotFoundException("Category", "categoryId", categoryId));
		
		List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
		List<ProductDTO> productDTOs = products.stream()
				.map(product -> modelMapper.map(product, ProductDTO.class))
				.collect(Collectors.toList());
		
		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(productDTOs);
		
		return productResponse;
	}

}





