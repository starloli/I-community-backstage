package com.example.demo2.repository;

public interface CategorySummaryProjection {
	Integer getYear();
    Integer getMonth();
    Object getType();
    String getCategory();
    Long getTotalAmount();
}
