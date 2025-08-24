package com.ecommerce.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResouceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize) {

		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		Page<Category> categoryPage = categoryRepository.findAll(pageable);

		List<Category> categories = categoryPage.getContent();
		if (categories.isEmpty()) {
			throw new APIException("No category created till now!");
		}

		List<CategoryDTO> categoryDTOs = categories.stream()
				.map(category -> modelMapper.map(category, CategoryDTO.class)).collect(Collectors.toList());

		CategoryResponse categoryResponse = new CategoryResponse();
		categoryResponse.setContent(categoryDTOs);
		categoryResponse.setPageNumber(categoryPage.getNumber());
		categoryResponse.setPageSize(categoryPage.getSize());
		categoryResponse.setTotalElements(categoryPage.getTotalElements());
		categoryResponse.setTotalPages(categoryPage.getTotalPages());
		categoryResponse.setLastPage(categoryPage.isLast());

		return categoryResponse;
	}

	@Override
	public CategoryDTO CreateCategory(CategoryDTO categoryDTO) {
		Category category = modelMapper.map(categoryDTO, Category.class);
		Category categoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName());
		if (categoryFromDB != null) {
			throw new APIException("Category With The Name: " + category.getCategoryName() + " Already Exists !!!");
		}
		Category savedCategory = categoryRepository.save(category);
		return modelMapper.map(savedCategory, CategoryDTO.class);

	}

	@Override
	public CategoryDTO deleteCategory(Long categoryId) {

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResouceNotFoundException("Category", "CategorID", categoryId));

		categoryRepository.delete(category);
		return modelMapper.map(category, CategoryDTO.class);
	}

	@Override
	public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {

		Category savedCategory = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResouceNotFoundException("Category", "CategorID", categoryId));

		Category category = modelMapper.map(categoryDTO, Category.class);

		category.setCategoryId(categoryId);
		savedCategory = categoryRepository.save(category);
		return modelMapper.map(savedCategory, CategoryDTO.class);

	}

}
