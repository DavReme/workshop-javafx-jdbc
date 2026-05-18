package model.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.dao.ProductDao;
import model.entities.Department;
import model.entities.Product;

public class ProductDaoJDBC implements ProductDao {
    private Connection connection;

    public ProductDaoJDBC (Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Product product) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(
                "INSERT INTO product "
                + "(name, price, quantity, category_id) "
                + "VALUES "
                + "(?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getQuantity());
            ps.setInt(4, product.getDepartment().getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    product.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("No rows affected");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatemente(ps);
        }
    }

    @Override
    public void update(Product product) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(
                "UPDATE product "
                + "SET name = ?, price = ?, quantity = ?, category_id = ? "
                + "WHERE id = ?"
            );

            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getQuantity());
            ps.setInt(4, product.getDepartment().getId());
            ps.setInt(5, product.getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("\t" + rowsAffected + " rows affected");
            } else {
                throw new DbException("\tNo rows affected");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatemente(ps);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(
                "DELETE FROM product "
                + "WHERE id = ?"
            );
            ps.setInt(1, id);

            Product p = findById(id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Confirming deletion of product: " + p.getId() + " named \"" + p.getName() + "\".");
            } else {
                throw new DbException("No rows affected");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatemente(ps);
        }
    }

    @Override
    public Product findById(Integer id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(
                "SELECT product.*, department.name as department "
                + "FROM product INNER JOIN department "
                + "ON product.category_id = department.id "
                + "WHERE product.id = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                Department department = instanciateDepartment(rs);
                Product product = instaciateProduct(rs, department);
                return product;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatemente(ps);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Product> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Product> list = new ArrayList<>();

        try {
            ps = connection.prepareStatement(
                "SELECT product.*, department.name as department FROM product "
                + "INNER JOIN department "
                + "ON product.category_id = department.id "
                + "ORDER BY id"
            );
            rs = ps.executeQuery();

            Map<Integer, Department> map = new HashMap<>();
            
            while (rs.next()) {
                Department d = map.get(rs.getInt("category_id"));
                if (d == null) {
                    d = instanciateDepartment(rs);
                    map.put(rs.getInt("category_id"), d);
                }
                list.add(instaciateProduct(rs, instanciateDepartment(rs)));
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatemente(ps);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Product> findByDepartment(Department department) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Product> list = new ArrayList<>();

        try {
            ps = connection.prepareStatement(
                "SELECT product.*, department.name as department "
                + "FROM product INNER JOIN department "
                + "ON product.category_id = department.id "
                + "WHERE department.id = ? "
                + "ORDER BY id");
            ps.setInt(1, department.getId());
            rs = ps.executeQuery();

            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {
                Department d = map.get(rs.getInt("category_id"));
                if (d == null) {
                    d = instanciateDepartment(rs);
                    map.put(rs.getInt("category_id"), d);
                }
                list.add(instaciateProduct(rs, d));
            }
        
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatemente(ps);
            DB.closeResultSet(rs);
        }
    }

    private Product instaciateProduct(ResultSet rs, Department department) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getDouble("price"));
        product.setQuantity(rs.getInt("quantity"));
        product.setDepartment(department);
        return product;
    }

    private Department instanciateDepartment(ResultSet rs) throws SQLException {
        Department department = new Department();
        department.setId(rs.getInt("category_id"));
        department.setName(rs.getString("department"));
        return department;
    }
}
