package com.github.katerynabratiuk.repository.implementation;

import com.github.katerynabratiuk.entity.Category;
import com.github.katerynabratiuk.entity.Product;
import com.github.katerynabratiuk.repository.DbConnection;
import com.github.katerynabratiuk.repository.ProductRepository;
import com.github.katerynabratiuk.repository.criteria.ProductSearchCriteria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {

    @Override
    public Product create(Product product) {

        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement pst = connection.prepareStatement("INSERT INTO product(name, quantity, price, id_category) VALUES (?,?,?, ?)");
            pst.setString(1, product.getName());
            pst.setInt(2, product.getQuantity());
            pst.setBigDecimal(3, product.getPrice());
            pst.setInt(4, product.getCategory().getId());

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void delete(Integer productId) {

        try(Connection connection = DbConnection.getConnection()) {

            PreparedStatement pst = connection.prepareStatement("DELETE FROM product WHERE id_product=?");
            pst.setInt(1, productId);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Product product) {

        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement pst = connection.prepareStatement("UPDATE product SET name = ?, quantity = ?, price = ?, id_category = ? WHERE id_product=?");
            pst.setString(1, product.getName());
            pst.setInt(2, product.getQuantity());
            pst.setBigDecimal(3, product.getPrice());
            pst.setInt(4, product.getCategory().getId());
            pst.setInt(5, product.getId());

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Product get(Integer productId) {
        try(Connection connection = DbConnection.getConnection()) {

            PreparedStatement pst = connection.prepareStatement("SELECT * FROM product WHERE id_product=?");
            pst.setInt(1, productId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return extractProductFromResultSet(rs);
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> getAll() {

        try (Connection connection = DbConnection.getConnection()) {
            Statement st = connection.createStatement();
            ResultSet resultSet = st.executeQuery("SELECT * FROM product");

            List<Product> res = new ArrayList<>();

            while(resultSet.next())
            {
                Product current = extractProductFromResultSet(resultSet);
                res.add(current);
            }
            return res;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("id_product"),
                rs.getString("name"),
                rs.getInt("quantity"),
                rs.getBigDecimal("price"),
                new Category(rs.getInt("id_category")));
    }


    @Override
    public List<Product> findByCriteria(ProductSearchCriteria criteria) {
        StringBuilder query = new StringBuilder("SELECT * FROM product WHERE 1=1");

        List<Object> params = new ArrayList<>();

        if (criteria.getName() != null) {
            query.append(" AND name ILIKE ?");
            params.add("%" + criteria.getName() + "%");
        }

        if (criteria.getCategory() != null) {
            query.append(" AND id_category = ?");
            params.add(criteria.getCategory().getId());
        }

        if (criteria.getLowerLimit() != null) {
            query.append(" AND price >= ?");
            params.add(criteria.getLowerLimit());
        }

        if (criteria.getUpperLimit() != null) {
            query.append(" AND price <= ?");
            params.add(criteria.getUpperLimit());
        }

        if (criteria.getPage() != null) {
            query.append(" LIMIT ?");
            params.add(criteria.getPage());
        }

        if (criteria.getPageSize() != null) {
            query.append(" OFFSET ?");
            params.add(criteria.getPageSize());
        }

        System.out.println(query);

        try(Connection connection = DbConnection.getConnection()) {


            List<Product> res = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement(query.toString());


            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            System.out.println(stmt);

            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                res.add(extractProductFromResultSet(rs));
            }
            return res;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
