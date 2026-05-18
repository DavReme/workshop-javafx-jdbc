package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Product;

public interface ProductDao {
    public void insert(Product product);
    public void update(Product product);
    public void deleteById(Integer id);
    public Product findById(Integer id);
    public List<Product> findAll();
    public List<Product> findByDepartment(Department department);
}