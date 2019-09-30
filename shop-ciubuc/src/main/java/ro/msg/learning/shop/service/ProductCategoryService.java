package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dto.SortOption;
import ro.msg.learning.shop.exception.CouldNotFindProductCategoryException;
import ro.msg.learning.shop.model.ProductCategory;
import ro.msg.learning.shop.model.mongodb.ProductCategoryMongo;
import ro.msg.learning.shop.repository.jpa.impl.ProductCategoryJpaRepositoryImpl;
import ro.msg.learning.shop.repository.mongo.ProductCategoryMongoRepository;
import ro.msg.learning.shop.util.RepositoryUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryJpaRepositoryImpl productCategoryRepository;
    private final RepositoryUtils repositoryUtils;
    private final ProductCategoryMongoRepository productCategoryMongoRepository;

    public List<ProductCategory> getAllProducts() {
        return productCategoryRepository.findAll();
    }

    public List<ProductCategory> getAllProductsSorted(List<SortOption> sortOptions) {
        return productCategoryRepository.findAll(repositoryUtils.generateSort(sortOptions));
    }

    public List<ProductCategory> getAllProductsPaged(int start, int size, List<SortOption> sortOptions) {
        return productCategoryRepository.findAll(PageRequest.of(start, size, repositoryUtils.generateSort(sortOptions)))
                .get().collect(Collectors.toList());
    }

    public List<ProductCategory> getAllByIds(List<Integer> ids) {
        return productCategoryRepository.findAllById(ids);
    }

    public Long count() {
        return productCategoryRepository.count();
    }

    public void deleteById(Integer id) {
        productCategoryRepository.deleteById(id);
    }

    public void delete(ProductCategory productCategory) {
        productCategoryRepository.delete(productCategory);
    }

    public void deleteAll(List<ProductCategory> productCategories) {
        productCategoryRepository.deleteAll(productCategories);
    }

    public void deleteAll() {
        productCategoryRepository.deleteAll();
    }

    public ProductCategory createCategory(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }

    public List<ProductCategory> createCategories(List<ProductCategory> productCategories) {
        return productCategoryRepository.saveAll(productCategories);
    }

    public ProductCategory findCategoryById(Integer id) {
        Optional<ProductCategory> productCategoryOptional = productCategoryRepository.findById(id);
        if (productCategoryOptional.isPresent())
            return productCategoryOptional.get();
        else throw new CouldNotFindProductCategoryException();
    }

    public boolean checkIfCategoryExistsById(Integer id) {
        return productCategoryRepository.existsById(id);
    }

    public List findAllByExample(ProductCategory productCategory, String matchingOption) {
        Example example;

        if ("ALL".equals(matchingOption) || "all".equals(matchingOption)) {
            example = Example.of(productCategory, ExampleMatcher.matchingAll());
        } else {
            example = Example.of(productCategory, ExampleMatcher.matchingAny());
        }

        return productCategoryRepository.findAll(example);
    }

    public List<ProductCategoryMongo> getAllMongoCategories() {
        return productCategoryMongoRepository.findAll();
    }

    public ProductCategoryMongo createProductCategoryMongo(ProductCategoryMongo productCategoryMongo) {
        return productCategoryMongoRepository.save(productCategoryMongo);
    }
}
