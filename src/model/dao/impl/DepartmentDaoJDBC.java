package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
    private Connection connection;
    
    public DepartmentDaoJDBC (Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Department department) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(
              "INSERT INTO department (name) "
                + "VALUES (?)",
                Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, department.getName());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    department.setId(id);
                }
                System.out.println("\tInsert success");
            } else {
                throw new DbException("Insert failed");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatemente(ps);
        }
    }

    @Override
    public void update(Department department) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(
              "UPDATE department "
              + "SET name = ? "
              + "WHERE id = ?"  
            );

            ps.setString(1, department.getName());
            ps.setInt(2, department.getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("\tUpdate success");
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
    public void deleteById(Integer id) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(
              "DELETE FROM department "
              + "WHERE id = ?"  
            );

            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("\tDelete success");
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
    public Department findById(Integer id) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(
              "SELECT * "
              + "FROM department "
              + "WHERE id = ?"  
            );

            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                return instanciateDepartment(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatemente(ps);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Department> list = new ArrayList<>();
        try {
            ps = connection.prepareStatement(
              "SELECT * FROM department ORDER BY id" 
            );

            rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(instanciateDepartment(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatemente(ps);
        }
    }
    
    private Department instanciateDepartment(ResultSet rs) throws SQLException {
        Department department = new Department();
        department.setId(rs.getInt("id"));
        department.setName(rs.getString("name"));
        return department;
    }
}
