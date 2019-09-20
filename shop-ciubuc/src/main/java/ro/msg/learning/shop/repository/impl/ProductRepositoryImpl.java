package ro.msg.learning.shop.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Example;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.exception.CouldNotFindProductException;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.mapper.ProductRowMapper;
import ro.msg.learning.shop.util.RepositoryUtils;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductRepositoryImpl implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final EntityManager entityManager;
    private final RepositoryUtils repositoryUtils;

    @Override
    public List<Product> findAll() {
        String query = "SELECT DISTINCT * FROM product, product_category, supplier " +
                "WHERE product.category_id = product_category.id " +
                "AND product.supplier_id = supplier.id";
        return jdbcTemplate.query(query, new ProductRowMapper());
    }

    @Override
    public List<Product> findAll(Sort sort) {
        StringBuilder query = new StringBuilder("SELECT DISTINCT * FROM product, product_category, supplier " +
                "WHERE product.category_id = product_category.id " +
                "AND product.supplier_id = supplier.id ");

        repositoryUtils.appendSortElements(query, sort);

        return jdbcTemplate.query(query.toString(), new ProductRowMapper());
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        StringBuilder query = new StringBuilder("SELECT DISTINCT * FROM product, product_category, supplier " +
                "WHERE product.category_id = product_category.id " +
                "AND product.supplier_id = supplier.id ");

        repositoryUtils.appendSortElements(query, pageable.getSort());

        query.append("LIMIT ").append(pageable.getPageNumber()).append(",").append(pageable.getPageSize());

        return new PageImpl<>(jdbcTemplate.query(query.toString(), new ProductRowMapper()));
    }

    @Override
    public List<Product> findAllById(Iterable<Integer> iterable) {
        StringBuilder query = new StringBuilder("SELECT * FROM product, product_category, supplier WHERE product.id IN (");

        iterable.forEach(integer -> query.append(integer).append(","));

        query.deleteCharAt(query.length() - 1);
        query.append(") ");
        query.append("AND product.category_id = product_category.id " +
                "AND product.supplier_id = supplier.id");

        return jdbcTemplate.query(query.toString(), new ProductRowMapper());
    }

    @Override
    public long count() {
        String query = "SELECT COUNT(*) FROM product";
        Long count = jdbcTemplate.queryForObject(query, Long.class);
        if (count == null)
            return 0;
        else return count;
    }

    @Override
    public void deleteById(Integer integer) {
        String query = "DELETE FROM product WHERE ID=?";
        jdbcTemplate.update(query, integer);
    }

    @Override
    public void delete(Product product) {
        deleteById(product.getId());
    }

    @Override
    public void deleteAll(Iterable<? extends Product> iterable) {
        iterable.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        String query = "DELETE FROM product";
        jdbcTemplate.update(query);
    }

    @Override
    public <S extends Product> S save(S s) {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("product").usingGeneratedKeyColumns("ID");
        Map<String, Object> queryParams = new HashMap<>();

        queryParams.put("ID", null);
        queryParams.put("NAME", s.getName());
        queryParams.put("DESCRIPTION", s.getDescription());
        queryParams.put("PRICE", s.getPrice());
        queryParams.put("WEIGHT", s.getWeight());
        queryParams.put("CATEGORY_ID", s.getProductCategory().getId());
        queryParams.put("SUPPLIER_ID", s.getSupplier().getId());
        queryParams.put("IMAGE_URL", s.getImageUrl());

        Integer key = (Integer) simpleJdbcInsert.executeAndReturnKey(queryParams);

        s.setId(key);
        return s;
    }

    @Override
    public <S extends Product> List<S> saveAll(Iterable<S> iterable) {
        List<S> products = new ArrayList<>();
        iterable.forEach(s -> products.add(save(s)));
        return products;
    }

    @Override
    public Optional<Product> findById(Integer integer) {
        String query = "SELECT * FROM product, product_category, supplier WHERE product.id=:id " +
                "AND product.category_id = product_category.id " +
                "AND product.supplier_id = supplier.id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", integer);

        try {
            Product product = namedParameterJdbcTemplate.queryForObject(query, sqlParameterSource, new ProductRowMapper());
            if (product != null)
                return Optional.of(product);
            else return Optional.empty();
        } catch (DataAccessException exception) {
            log.warn(Arrays.toString(exception.getStackTrace()));
            return Optional.empty();
        }
    }

    @Override
    public boolean existsById(Integer integer) {
        String query = "SELECT * FROM product, product_category, supplier WHERE product.id=:id " +
                "AND product.category_id = product_category.id " +
                "AND product.supplier_id = supplier.id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", integer);

        Product product = namedParameterJdbcTemplate.queryForObject(query, sqlParameterSource, new ProductRowMapper());

        return product != null;
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    @Override
    public <S extends Product> S saveAndFlush(S s) {
        S product = this.save(s);
        this.flush();
        return product;
    }

    @Override
    public void deleteInBatch(Iterable<Product> iterable) {
        StringBuilder query = new StringBuilder("DELETE FROM product WHERE ID IN (");
        iterable.forEach(product -> query.append(product.getId()).append(","));

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
    public Product getOne(Integer integer) {
        String query = "SELECT * FROM product, product_category, supplier WHERE product.id=:id " +
                "AND product.category_id = product_category.id " +
                "AND product.supplier_id = supplier.id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", integer);

        try {
            return namedParameterJdbcTemplate.queryForObject(query, sqlParameterSource, new ProductRowMapper());
        } catch (DataAccessException exception) {
            log.warn(Arrays.toString(exception.getStackTrace()));
            throw new CouldNotFindProductException();
        }
    }

    @Override
    public <S extends Product> Optional<S> findOne(Example<S> example) {
        StringBuilder query = new StringBuilder("SELECT * FROM product, product_category, supplier WHERE ");
        appendExampleInfo(query, example);

        try {
            Product product = jdbcTemplate.queryForObject(query.toString(), new ProductRowMapper());
            if (product != null)
                return Optional.of((S) product);
            else return Optional.empty();
        } catch (DataAccessException exception) {
            log.warn(Arrays.toString(exception.getStackTrace()));
            return Optional.empty();
        }
    }

    @Override
    public <S extends Product> List<S> findAll(Example<S> example) {
        StringBuilder query = new StringBuilder("SELECT * FROM product, product_category, supplier WHERE ");
        appendExampleInfo(query, example);

        try {
            return (List<S>) jdbcTemplate.queryForObject(query.toString(), new ProductRowMapper());
        } catch (DataAccessException ex) {
            log.warn(Arrays.toString(ex.getStackTrace()));
            return Collections.emptyList();
        }
    }

    @Override
    public <S extends Product> List<S> findAll(Example<S> example, Sort sort) {
        StringBuilder query = new StringBuilder("SELECT * FROM product, product_category, supplier WHERE ");
        appendExampleInfo(query, example);
        repositoryUtils.appendSortElements(query, sort);

        try {
            return (List<S>) jdbcTemplate.queryForObject(query.toString(), new ProductRowMapper());
        } catch (DataAccessException ex) {
            log.warn(Arrays.toString(ex.getStackTrace()));
            return Collections.emptyList();
        }
    }

    @Override
    public <S extends Product> Page<S> findAll(Example<S> example, Pageable pageable) {
        StringBuilder query = new StringBuilder("SELECT * FROM product, product_category, supplier WHERE ");
        appendExampleInfo(query, example);
        repositoryUtils.appendSortElements(query, pageable.getSort());
        query.append(" LIMIT ").append(pageable.getPageNumber()).append(",").append(pageable.getPageSize());
        try {
            return new PageImpl<>((List<S>) jdbcTemplate.queryForObject(query.toString(), new ProductRowMapper()));
        } catch (DataAccessException ex) {
            log.warn(Arrays.toString(ex.getStackTrace()));
            return Page.empty();
        }
    }

    @Override
    public <S extends Product> long count(Example<S> example) {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM product, product_category, supplier WHERE ");
        appendExampleInfo(query, example);

        Long count = jdbcTemplate.queryForObject(query.toString(), new SingleColumnRowMapper<>());
        if (count == null) return 0;
        else return count;
    }

    @Override
    public <S extends Product> boolean exists(Example<S> example) {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM product, product_category, supplier WHERE ");
        appendExampleInfo(query, example);

        Long count = jdbcTemplate.queryForObject(query.toString(), new SingleColumnRowMapper<>());
        return count != null;
    }

    private <S extends Product> void appendExampleInfo(StringBuilder query, Example<S> example) {
        Product probe = example.getProbe();
        String matchOption;

        if (example.getMatcher().isAllMatching()) {
            matchOption = "AND";
        } else {
            matchOption = "OR";
        }

        String checkMatchers = " product.id=" + probe.getId() +
                matchOption + " product.name='" + probe.getName() + "'" +
                matchOption + " product.description='" + probe.getDescription() + "'" +
                matchOption + " product.price=" + probe.getPrice() +
                matchOption + " product.weight=" + probe.getWeight() +
                matchOption + " product.category_id=" + probe.getProductCategory().getId() +
                matchOption + " product.supplier_id=" + probe.getSupplier().getId() +
                matchOption + " product.image_url=" + probe.getImageUrl() + " ";

        query.append(checkMatchers);
    }
}
