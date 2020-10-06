package com.codegym;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductTests {
    @Autowired
    private ProductRepository repository;

    @Test
    @Rollback(value = false)
    @Order(1)
    public void testCreateProduct(){
        Product product = new Product("iPhone 10", 789);
        Product savedProduct = repository.save(product);

        assertNotNull(savedProduct);
    }

    @Test
    @Order(2)
    public void testFindProductByNameNotExist(){
        String name = "iPhone 11";
        Product product = repository.findProductByName(name);

        assertNull(product);
    }

    @Test
    @Order(3)
    public void testFindProductByNameExist(){
        String name = "iPhone 10";
        Product product = repository.findProductByName(name);

        assertThat(product.getName().equals(name));
    }

    @Test
    @Rollback(value = false)
    @Order(4)
    public void testUpdateProduct(){
        String productName = "Kindle Reader";
        Product product = new Product(productName, 199);
        product.setId(2);
        repository.save(product);
        Product updatedProduct = repository.findProductByName(productName);
        assertThat(updatedProduct.getName().equals(productName));
    }

    @Test
    @Order(5)
    public void testListProducts(){
        List<Product> productList = (List<Product>) repository.findAll();

        for(Product product : productList){
            System.out.println(product);
        }

        assertThat(productList).size().isGreaterThan(0);
    }

    @Test
    @Rollback(value = false)
    @Order(6)
    public void testDeleteProduct(){
        Integer id = 2;
        boolean isExistBeforeDelete = repository.findById(id).isPresent();
        repository.deleteById(id);
        boolean notExistAfterDelete = repository.findById(id).isPresent();
        assertTrue(isExistBeforeDelete);
        assertFalse(notExistAfterDelete);
    }
}
