package ro.msg.learning.shop.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.model.ProductCategory;
import ro.msg.learning.shop.model.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {

    @Override
    public Product mapRow(ResultSet resultSet, int i) throws SQLException {

        ProductCategory productCategory = ProductCategory.builder()
                .id(resultSet.getInt(9))
                .name(resultSet.getString(10))
                .description(resultSet.getString(11))
                .build();

        Supplier supplier = Supplier.builder()
                .id(resultSet.getInt(12))
                .name(resultSet.getString(13))
                .build();

        return Product.builder()
                .id(resultSet.getInt(1))
                .name(resultSet.getString(2))
                .description(resultSet.getString(3))
                .price(resultSet.getBigDecimal(4))
                .weight(resultSet.getDouble(5))
                .productCategory(productCategory)
                .supplier(supplier)
                .imageUrl(resultSet.getString(8))
                .build();
    }
}
