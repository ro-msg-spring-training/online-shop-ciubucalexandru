package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ro.msg.learning.shop.dto.IntegerListDTO;
import ro.msg.learning.shop.dto.PageOptionsDTO;
import ro.msg.learning.shop.dto.ProductCategoryListDTO;
import ro.msg.learning.shop.dto.SortOptionsListDTO;
import ro.msg.learning.shop.model.ProductCategory;
import ro.msg.learning.shop.model.mongodb.ProductCategoryMongo;
import ro.msg.learning.shop.service.ProductCategoryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product-category/")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @GetMapping("/all")
    public List<ProductCategory> getAllCategories() {
        return productCategoryService.getAllProducts();
    }

    @PostMapping("/all/sorted")
    public List<ProductCategory> getAllCategoriesSorted(@RequestBody SortOptionsListDTO sortOptionsListDTO) {
        return productCategoryService.getAllProductsSorted(sortOptionsListDTO.getSortOptions());
    }

    @PostMapping("/all/paged")
    public List<ProductCategory> getAllCategoriesPaged(@RequestBody PageOptionsDTO pageOptionsDTO) {
        return productCategoryService.getAllProductsPaged(pageOptionsDTO.getStart(), pageOptionsDTO.getSize(), pageOptionsDTO.getSortOptions());
    }

    @PostMapping("/all")
    public List<ProductCategory> getAllCategoriesIds(@RequestBody IntegerListDTO integerListDTO) {
        return productCategoryService.getAllByIds(integerListDTO.getIntegers());
    }

    @GetMapping("/count")
    public Long countProductCategories() {
        return productCategoryService.count();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable("id") Integer id) {
        productCategoryService.deleteById(id);
    }

    @DeleteMapping("/delete")
    public void deleteCategory(@RequestBody ProductCategory productCategory) {
        productCategoryService.delete(productCategory);
    }

    @DeleteMapping("/delete/all")
    public void deleteAllCategories(@RequestBody IntegerListDTO integerListDTO) {
        productCategoryService.deleteAll(integerListDTO.getIntegers()
                .stream()
                .map(integer -> {
                    ProductCategory productCategory = new ProductCategory();
                    productCategory.setId(integer);
                    return productCategory;
                })
                .collect(Collectors.toList()));
    }

    @PostMapping("/create")
    public ProductCategory createProductCategory(@RequestBody ProductCategory productCategory) {
        return productCategoryService.createCategory(productCategory);
    }

    @PostMapping("/create/all")
    public List<ProductCategory> createProductCategories(@RequestBody ProductCategoryListDTO productCategoryListDTO) {
        return productCategoryService.createCategories(productCategoryListDTO.getProductCategories());
    }

    @GetMapping("/{id}")
    public ProductCategory getCategoryById(@PathVariable("id") Integer id) {
        return productCategoryService.findCategoryById(id);
    }

    @GetMapping("/check-if-exists/{id}")
    public boolean checkIfCategoryExists(@PathVariable("id") Integer id) {
        return productCategoryService.checkIfCategoryExistsById(id);
    }

    @PostMapping("/all/by-example/{matchingOption}")
    public List findAllByExample(@PathVariable("matchingOption") String matchingOption, @RequestBody ProductCategory productCategory) {
        return productCategoryService.findAllByExample(productCategory, matchingOption);
    }

    @PostMapping("/mongo/create")
    public ProductCategoryMongo createProductCategoryMongo(@RequestBody ProductCategoryMongo productCategoryMongo) {
        return productCategoryService.createProductCategoryMongo(productCategoryMongo);
    }

    @GetMapping("/mongo/all")
    public List<ProductCategoryMongo> getAllMongoCategories() {
        return productCategoryService.getAllMongoCategories();
    }
}
