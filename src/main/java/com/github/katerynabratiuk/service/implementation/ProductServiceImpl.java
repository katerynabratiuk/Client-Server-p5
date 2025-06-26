package com.github.katerynabratiuk.service.implementation;

import com.github.katerynabratiuk.entity.Category;
import com.github.katerynabratiuk.entity.Product;
import com.github.katerynabratiuk.repository.CategoryRepository;
import com.github.katerynabratiuk.repository.criteria.ProductSearchCriteria;
import com.github.katerynabratiuk.repository.implementation.CategoryRepositoryImpl;
import com.github.katerynabratiuk.repository.implementation.ProductRepositoryImpl;
import com.github.katerynabratiuk.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

public class ProductServiceImpl implements ProductService {

    private final ProductRepositoryImpl productRepository;

    public ProductServiceImpl(ProductRepositoryImpl productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAll() {
        return productRepository.getAll();
    }

    @Override
    public Product get(Integer id) {
        return productRepository.get(id);
    }

    @Override
    public Product create(Product product) {
        if(isValid(product))
           return productRepository.create(product);
        return null;
    }

    @Override
    public void update(Product product) {
        if (isValid(product))
            productRepository.update(product);
    }

    @Override
    public void delete(Integer id) {
        productRepository.delete(id);
    }

    @Override
    public List<Product> filter(ProductSearchCriteria criteria) {
        return productRepository.findByCriteria(criteria);
    }

    private boolean isValid(Product product)
    {

        if (product.getPrice() == null)
            throw new RuntimeException("Price cannot be empty");
        if (product.getPrice().compareTo(new BigDecimal("0.1")) < 0)
            throw new RuntimeException("Price should be more or equal to 0.1");


        if (product.getQuantity() == null)
            throw new RuntimeException("Quantity cannot be empty");
        if (product.getQuantity() < 0)
            throw new RuntimeException("Quantity should be more or equal to 0");

        if (product.getName() == null || product.getName().isBlank() || product.getName().isEmpty())
            throw new RuntimeException("Name cannot be blank");

        if (product.getCategory() == null)
            throw new RuntimeException("Product should belong to category");

        return true;
    }

}
