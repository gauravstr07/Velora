package com.ecommerce.project.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/echo")
	public ResponseEntity<String> echoMessage(@RequestParam(name = "message", defaultValue = "Hello Velora") String message) {
		return new ResponseEntity<String>("Echoed Message: " + message, HttpStatus.OK);
	}

	@GetMapping("/public/categories")
	public ResponseEntity<CategoryResponse> getAllCategories() {
		CategoryResponse categoryResponse = categoryService.getAllCategories();
		return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
	}

	@PostMapping("/public/categories")
	public ResponseEntity<CategoryDTO> createCategor(@Valid @RequestBody CategoryDTO categoryDTO) {
		CategoryDTO savedCategoryDTO = categoryService.CreateCategory(categoryDTO);
		return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
	}

	@DeleteMapping("/admin/categories/{categoryId}")
	public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
		CategoryDTO deletedCategory = categoryService.deleteCategory(categoryId);
		return new ResponseEntity<>(deletedCategory, HttpStatus.OK);
	}

	@PutMapping("/public/categories/{categoryId}")
	public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO,
			@PathVariable Long categoryId) {
		CategoryDTO savedCategoryDTO = categoryService.updateCategory(categoryDTO, categoryId);
		return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);

	}
}
