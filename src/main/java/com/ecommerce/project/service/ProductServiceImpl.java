package com.ecommerce.project.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
	
	@Autowired
	FileService fileService;
	
	@Value("${project.image}")
	private String path;

	@Override
	public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResouceNotFoundException("Category", "categoryId", categoryId));

		Product product = modelMapper.map(productDTO, Product.class);

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
		List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
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
		List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
				.collect(Collectors.toList());

		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(productDTOs);

		return productResponse;
	}

	@Override
	public ProductResponse searchProductByKeyword(String keyword) {

		List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');
		List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
				.collect(Collectors.toList());

		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(productDTOs);

		return productResponse;
	}

	@Override
	public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {

		Product productFromDB = productRepository.findById(productId)
				.orElseThrow(() -> new ResouceNotFoundException("Product", "productId", productId));

		Product product = modelMapper.map(productDTO, Product.class);

		productFromDB.setProductName(product.getProductName());
		productFromDB.setDescription(product.getDescription());
		productFromDB.setQuantity(product.getQuantity());
		productFromDB.setPrice(product.getPrice());
		productFromDB.setDiscount(product.getDiscount());
		productFromDB.setSpecialPrice(product.getSpecialPrice());

		Product savedProduct = productRepository.save(productFromDB);

		return modelMapper.map(savedProduct, ProductDTO.class);
	}

	@Override
	public ProductDTO deleteProduct(Long productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResouceNotFoundException("Product", "productId", productId));

		productRepository.delete(product);
		return modelMapper.map(product, ProductDTO.class);
	}

	@Override
	public ProductDTO updateProductImage(Long productId, MultipartFile file) throws IOException {

		Product productFromDB = productRepository.findById(productId)
				.orElseThrow(() -> new ResouceNotFoundException("Product", "productId", productId));

		

		String fileName = fileService.uploadImage(path, file);

		productFromDB.setImage(fileName);

		Product updatedProduct = productRepository.save(productFromDB);
		return modelMapper.map(updatedProduct, ProductDTO.class);
	}

	

}
