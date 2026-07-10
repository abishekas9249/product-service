package com.learn.product_service.repository;

import com.learn.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository
        extends JpaRepository<Product, Long> {

    //Spring Data JPA Methods with different querying based on conditions
    List<Product> findByCategory(String category);

    List<Product> findByPriceLessThan(Double price);

    @Query("SELECT p FROM Product p " +
            "WHERE p.price BETWEEN :min AND :max " +
            "ORDER BY p.price ASC")
    List<Product> findByPriceBetween(@Param("min") Double min,@Param("max") Double max);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByCategoryOrderByPriceAsc(String category);

    long countByCategory(String category);

    boolean existsByNameIgnoreCase(String name);

    List<Product> findTop3ByOrderByPriceDesc();

    //Adding JPQL Queries in methods of Repository
    @Query("SELECT p FROM Product p " +
            "WHERE LOWER(p.name) LIKE " +
            "LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByName(
            @Param("keyword") String keyword);

    @Query("SELECT p.category, COUNT(p), AVG(p.price) " +
            "FROM Product p GROUP BY p.category")
    List<Object[]> getCategoryStats();

    @Query("SELECT p FROM Product p " +
            "WHERE p.category = :category " +
            "AND p.price = (" +
            "  SELECT MAX(p2.price) FROM Product p2 " +
            "  WHERE p2.category = :category" +
            ")")
    Optional<Product> findMostExpensiveInCategory(
            @Param("category") String category);

    //Added Native SQL Queries
    @Query(value =
            "SELECT * FROM products " +
                    "WHERE price > :price " +
                    "ORDER BY created_at DESC " +
                    "LIMIT :limit",
            nativeQuery = true)
    List<Product> findExpensiveProducts(
            @Param("price") Double price,
            @Param("limit") int limit);


    //@Modifying queries for updates and deletes
    @Modifying
    @Transactional
    @Query("UPDATE Product p " +
            "SET p.price = p.price * :factor " +
            "WHERE p.category = :category")
    int applyPriceIncrease(
            @Param("category") String category,
            @Param("factor") Double factor);

    @Modifying
    @Transactional
    @Query("DELETE FROM Product p " +
            "WHERE p.category = :category")
    int deleteByCategory(
            @Param("category") String category);
}
