package com.ecommerce.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResouceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<Category> getAllCategories() {
		List<Category> categories = categoryRepository.findAll();
		if (categories.isEmpty()) {
			throw new APIException("No category created till now!");
		}
		return categoryRepository.findAll();
	}

	@Override
	public void CreateCategory(Category category) {
		Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
		if (savedCategory != null) {
			throw new APIException("Category With The Name: " + category.getCategoryName() + " Already Exists !!!");
		}
		categoryRepository.save(category);
	}

	@Override
	public String deleteCategory(Long categoryId) {

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResouceNotFoundException("Category", "CategorID", categoryId));

		categoryRepository.delete(category);
		return "Deleted Category With ID: " + categoryId;
	}

	@Override
	public Category updateCategory(Category category, Long categoryId) {

		Category savedCategory = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResouceNotFoundException("Category", "CategorID", categoryId));

		category.setCategoryId(categoryId);
		savedCategory = categoryRepository.save(category);
		return savedCategory;

	}

}
