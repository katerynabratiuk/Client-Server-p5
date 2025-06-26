package com.github.katerynabratiuk.service;

import com.github.katerynabratiuk.entity.Category;
import com.github.katerynabratiuk.entity.Product;
import com.github.katerynabratiuk.repository.criteria.ProductSearchCriteria;

import java.util.List;

public interface ProductService {

    List<Product> getAll();
    Product get(Integer id);
    Product create(Product category);
    void update(Product category);
    void delete(Integer id);
    List<Product> filter(ProductSearchCriteria criteria);
}
