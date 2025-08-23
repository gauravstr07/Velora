package com.ecommerce.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

	// private List<Category> categories = new ArrayList<Category>();

	//private Long nextId = 1L;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public void CreateCategory(Category category) {
		categoryRepository.save(category);
	}

	@Override
	public String deleteCategory(Long categoryId) {

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));

		categoryRepository.delete(category);
		return "Deleted Category With ID: " + categoryId;
	}

	@Override
	public Category updateCategory(Category category, Long categoryId) {

		Category savedCategory = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));

		category.setCategoryId(categoryId);
		savedCategory = categoryRepository.save(category);
		return savedCategory;

	}

}
