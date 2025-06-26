package com.github.katerynabratiuk.service;

import com.github.katerynabratiuk.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAll();
    Category get(Integer id);
    Category create(Category category);
    void update(Category category);
    void delete(Integer id);

}
