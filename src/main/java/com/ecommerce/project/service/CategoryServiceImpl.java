package com.ecommerce.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ecommerce.project.model.Category;

@Service
public class CategoryServiceImpl implements CategoryService {

	private List<Category> categories = new ArrayList<Category>();

	private Long nextId = 1L;

	@Override
	public List<Category> getAllCategories() {
		return categories;
	}

	@Override
	public void CreateCategory(Category category) {
		category.setCategoryId(nextId++);
		categories.add(category);
	}

	@Override
	public String deleteCategory(Long categoryId) {
		Category category = categories.stream().filter(c -> c.getCategoryId().equals(categoryId)).findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));
		categories.remove(category);
		return "Deleted Category With ID: " + categoryId;
	}

}
