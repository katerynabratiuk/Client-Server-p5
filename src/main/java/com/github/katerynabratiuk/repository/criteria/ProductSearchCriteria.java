package com.github.katerynabratiuk.repository.criteria;

import com.github.katerynabratiuk.entity.Category;

import java.math.BigDecimal;

public class ProductSearchCriteria {

    private String name;
    private Category category;
    private BigDecimal lowerLimit;
    private BigDecimal upperLimit;
    private Integer page;
    private Integer pageSize;

    public ProductSearchCriteria() {
    }

    public ProductSearchCriteria(String name, Category category, BigDecimal lowerLimit, BigDecimal upperLimit, Integer size, Integer offset) {
        this.name = name;
        this.category = category;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.page = size;
        this.pageSize = offset;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(BigDecimal lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public BigDecimal getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(BigDecimal upperLimit) {
        this.upperLimit = upperLimit;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean empty() {
        return name == null && category == null
                && lowerLimit == null && upperLimit == null;
    }
}
