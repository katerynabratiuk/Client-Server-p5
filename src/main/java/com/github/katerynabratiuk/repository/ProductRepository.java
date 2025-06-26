package com.github.katerynabratiuk.repository;

import com.github.katerynabratiuk.entity.Product;
import com.github.katerynabratiuk.repository.criteria.ProductSearchCriteria;

import java.util.List;

public interface ProductRepository extends GenericRepository<Product, Integer> {

    List<Product> findByCriteria(ProductSearchCriteria criteria);

}
