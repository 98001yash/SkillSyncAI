package com.company.SkillSyncAI.service;


import com.company.SkillSyncAI.dtos.CategoryDto;
import com.company.SkillSyncAI.entities.Category;
import com.company.SkillSyncAI.exceptions.ResourceNotFoundException;
import com.company.SkillSyncAI.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public List<CategoryDto> getAllCategories(){
        return categoryRepository.findAll()
                .stream()
                .map(category->modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    public CategoryDto createCategory(CategoryDto categoryDto){
        if(categoryRepository.existsByName(categoryDto.getName())){
            throw new RuntimeException("Category already exists");
        }
        Category category = modelMapper.map(categoryDto, Category.class);
        category = categoryRepository.save(category);
        return modelMapper.map(category, CategoryDto.class);
    }

    public void deleteCategory(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("category not found"));
        categoryRepository.delete(category);
    }
}
