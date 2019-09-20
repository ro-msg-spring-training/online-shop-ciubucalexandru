package ro.msg.learning.shop.repository.jpa.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.exception.CouldNotFindProductCategoryException;
import ro.msg.learning.shop.model.ProductCategory;
import ro.msg.learning.shop.repository.jpa.mapper.ProductCategoryRowMapper;
import ro.msg.learning.shop.util.RepositoryUtils;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductCategoryJpaRepositoryImpl implements JpaRepository<ProductCategory, Integer> {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final EntityManager entityManager;
    private final RepositoryUtils repositoryUtils;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Override
    public List<ProductCategory> findAll() {
        String query = "SELECT DISTINCT * FROM product_category";
        return jdbcTemplate.query(query, new ProductCategoryRowMapper());
    }

    @Override
    public List<ProductCategory> findAll(Sort sort) {
        StringBuilder query = new StringBuilder("SELECT DISTINCT * FROM product_category ");
        repositoryUtils.appendSortElements(query, sort);
        return jdbcTemplate.query(query.toString(), new ProductCategoryRowMapper());
    }

    @Override
    public Page<ProductCategory> findAll(Pageable pageable) {
        StringBuilder query = new StringBuilder("SELECT DISTINCT * FROM product_category ");
        repositoryUtils.appendSortElements(query, pageable.getSort());
        query.append(" LIMIT ").append(pageable.getPageNumber()).append(",").append(pageable.getPageSize());
        return new PageImpl<>(jdbcTemplate.query(query.toString(), new ProductCategoryRowMapper()));
    }

    @Override
    public List<ProductCategory> findAllById(Iterable<Integer> iterable) {
        StringBuilder query = new StringBuilder("SELECT * FROM product_category WHERE id IN (");

        iterable.forEach(integer -> query.append(integer).append(","));

        query.deleteCharAt(query.length() - 1);
        query.append(") ");

        return jdbcTemplate.query(query.toString(), new ProductCategoryRowMapper());
    }

    @Override
    public long count() {
        String query = "SELECT COUNT(*) FROM product_category";
        Long count = jdbcTemplate.queryForObject(query, new SingleColumnRowMapper<>(Long.class));
        if (count == null) return 0;
        else return count;
    }

    @Override
    public void deleteById(Integer integer) {
        String query = "DELETE FROM product_category WHERE id=" + integer;
        jdbcTemplate.update(query);
    }

    @Override
    public void delete(ProductCategory productCategory) {
        this.deleteById(productCategory.getId());
    }

    @Override
    public void deleteAll(Iterable<? extends ProductCategory> iterable) {
        iterable.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        String query = "DELETE FROM product_category";
        jdbcTemplate.update(query);
    }

    @Override
    public <S extends ProductCategory> S save(S s) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("product_category").usingGeneratedKeyColumns("ID");
        Map<String, Object> queryParams = new HashMap<>();

        queryParams.put("ID", null);
        queryParams.put("NAME", s.getName());
        queryParams.put("DESCRIPTION", s.getDescription());

        Integer key = (Integer) simpleJdbcInsert.executeAndReturnKey(queryParams);

        s.setId(key);
        return s;
    }

    @Override
    public <S extends ProductCategory> List<S> saveAll(Iterable<S> iterable) {
        List<S> productCategories = new ArrayList<>();
        iterable.forEach(s -> productCategories.add(save(s)));
        return productCategories;
    }

    @Override
    public Optional<ProductCategory> findById(Integer integer) {
        String query = "SELECT * FROM product_category WHERE id=:id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", integer);

        try {
            ProductCategory category = namedParameterJdbcTemplate.queryForObject(query, sqlParameterSource, new ProductCategoryRowMapper());
            if (category != null)
                return Optional.of(category);
            else return Optional.empty();
        } catch (DataAccessException exception) {
            log.warn(Arrays.toString(exception.getStackTrace()));
            return Optional.empty();
        }
    }

    @Override
    public boolean existsById(Integer integer) {
        String query = "SELECT * FROM product_category WHERE id=:id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", integer);

        try {
            ProductCategory productCategory = namedParameterJdbcTemplate.queryForObject(query, sqlParameterSource, new ProductCategoryRowMapper());
            return productCategory != null;
        } catch (DataAccessException ex) {
            return false;
        }
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    @Override
    public <S extends ProductCategory> S saveAndFlush(S s) {
        S productCategory = this.save(s);
        this.flush();
        return productCategory;
    }

    @Override
    public void deleteInBatch(Iterable<ProductCategory> iterable) {
        StringBuilder query = new StringBuilder("DELETE FROM product_category WHERE ID IN (");
        iterable.forEach(productCategory -> query.append(productCategory.getId()).append(","));

        query.deleteCharAt(query.length() - 1);
        query.append(")");

        jdbcTemplate.update(query.toString());
    }

    @Override
    public void deleteAllInBatch() {
        String query = "DELETE FROM product";
        jdbcTemplate.update(query);
    }

    @Override
    public ProductCategory getOne(Integer integer) {
        String query = "SELECT * FROM product_category WHERE id=:id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", integer);

        try {
            return namedParameterJdbcTemplate.queryForObject(query, sqlParameterSource, new ProductCategoryRowMapper());
        } catch (DataAccessException exception) {
            log.warn(Arrays.toString(exception.getStackTrace()));
            throw new CouldNotFindProductCategoryException();
        }
    }

    @Override
    public <S extends ProductCategory> Optional<S> findOne(Example<S> example) {
        StringBuilder query = new StringBuilder("SELECT * FROM product_category WHERE ");
        appendExampleInfo(query, example);

        try {
            ProductCategory category = jdbcTemplate.queryForObject(query.toString(), new ProductCategoryRowMapper());
            if (category != null)
                return Optional.of((S) category);
            else return Optional.empty();
        } catch (DataAccessException exception) {
            log.warn(Arrays.toString(exception.getStackTrace()));
            return Optional.empty();
        }
    }

    @Override
    public <S extends ProductCategory> List<S> findAll(Example<S> example) {
        StringBuilder query = new StringBuilder("SELECT * FROM product_category WHERE ");
        appendExampleInfo(query, example);

        try {
            return (List<S>) jdbcTemplate.query(query.toString(), new ProductCategoryRowMapper());
        } catch (DataAccessException ex) {
            return Collections.emptyList();
        }
    }

    @Override
    public <S extends ProductCategory> List<S> findAll(Example<S> example, Sort sort) {
        StringBuilder query = new StringBuilder("SELECT * FROM product_category WHERE ");
        appendExampleInfo(query, example);
        repositoryUtils.appendSortElements(query, sort);

        try {
            return (List<S>) jdbcTemplate.query(query.toString(), new ProductCategoryRowMapper());
        } catch (DataAccessException ex) {
            return Collections.emptyList();
        }
    }

    @Override
    public <S extends ProductCategory> Page<S> findAll(Example<S> example, Pageable pageable) {
        StringBuilder query = new StringBuilder("SELECT * FROM product_category WHERE ");
        appendExampleInfo(query, example);
        repositoryUtils.appendSortElements(query, pageable.getSort());
        query.append(" LIMIT ").append(pageable.getPageNumber()).append(",").append(pageable.getPageSize());

        try {
            return new PageImpl<>((List<S>) jdbcTemplate.queryForObject(query.toString(), new ProductCategoryRowMapper()));
        } catch (DataAccessException ex) {
            return Page.empty();
        }
    }

    @Override
    public <S extends ProductCategory> long count(Example<S> example) {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM product_category WHERE ");
        appendExampleInfo(query, example);

        Long count = jdbcTemplate.queryForObject(query.toString(), new SingleColumnRowMapper<>());
        if (count == null) return 0;
        else return count;
    }

    @Override
    public <S extends ProductCategory> boolean exists(Example<S> example) {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM product_category WHERE ");
        appendExampleInfo(query, example);

        Long count = jdbcTemplate.queryForObject(query.toString(), new SingleColumnRowMapper<>());
        return count != null;
    }

    private <S extends ProductCategory> void appendExampleInfo(StringBuilder query, Example<S> example) {
        ProductCategory probe = example.getProbe();
        String matchOption;

        if (example.getMatcher().isAllMatching()) {
            matchOption = "AND";
        } else {
            matchOption = "OR";
        }

        String checkMatchers = " product_category.id=" + probe.getId() +
                matchOption + " product_category.name='" + probe.getName() + "'" +
                matchOption + " product_category.description='" + probe.getDescription() + "'";

        query.append(checkMatchers);
    }
}
