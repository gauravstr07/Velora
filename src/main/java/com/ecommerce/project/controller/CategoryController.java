package com.ecommerce.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/public/categories")
	public ResponseEntity<List<Category>> getAllCategories() {
		List<Category> categories = categoryService.getAllCategories();
		return new ResponseEntity<>(categories, HttpStatus.OK);
	}

	@PostMapping("/public/categories")
	public ResponseEntity<String> createCategor(@Valid @RequestBody Category category) {
		categoryService.CreateCategory(category);
		return new ResponseEntity<>("Category Added Successfully", HttpStatus.CREATED);
	}

	@DeleteMapping("/admin/categories/{categoryId}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
		String status = categoryService.deleteCategory(categoryId);
		return new ResponseEntity<>(status, HttpStatus.OK);
	}

	@PutMapping("/public/categories/{categoryId}")
	public ResponseEntity<String> updateCategory(@RequestBody Category category, @PathVariable Long categoryId) {
			Category savedCategory = categoryService.updateCategory(category, categoryId);
			return new ResponseEntity<>("Category With Category ID: " + categoryId, HttpStatus.OK);
		
	}
}
