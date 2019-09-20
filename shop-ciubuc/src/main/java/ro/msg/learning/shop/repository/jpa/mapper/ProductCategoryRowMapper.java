package ro.msg.learning.shop.repository.jpa.mapper;

import org.springframework.jdbc.core.RowMapper;
import ro.msg.learning.shop.model.ProductCategory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductCategoryRowMapper implements RowMapper<ProductCategory> {
    @Override
    public ProductCategory mapRow(ResultSet resultSet, int i) throws SQLException {
        return ProductCategory.builder()
                .id(resultSet.getInt(1))
                .name(resultSet.getString(2))
                .description(resultSet.getString(3))
                .build();
    }
}
