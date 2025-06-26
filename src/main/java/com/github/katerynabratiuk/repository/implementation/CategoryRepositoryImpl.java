package com.github.katerynabratiuk.repository.implementation;

import com.github.katerynabratiuk.entity.Category;
import com.github.katerynabratiuk.repository.CategoryRepository;
import com.github.katerynabratiuk.repository.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {
    @Override
    public Category create(Category category) {
        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement pst = connection.prepareStatement("INSERT INTO product_category(name) VALUES (?)");
            pst.setString(1, category.getName());
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void delete(Integer id) {
        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement pst = connection.prepareStatement("DELETE FROM product_category WHERE id_category = ?");
            pst.setInt(1, id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Category category) {
        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement pst = connection.prepareStatement("UPDATE product_category SET name = ? WHERE id_category=?");
            pst.setString(1, category.getName());
            pst.setInt(2, category.getId());

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Category get(Integer id) {
        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM product_category WHERE id_category=?");

            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();

            return new Category(rs.getInt("id_category"), rs.getString("name"));


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Category> getAll() {
        try (Connection connection = DbConnection.getConnection()) {
            Statement st = connection.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM product_category");

            List<Category> res = new ArrayList<>();

            while(rs.next())
            {
                res.add(new Category(rs.getInt("id_category"), rs.getString("name")));
            }
            return res;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
