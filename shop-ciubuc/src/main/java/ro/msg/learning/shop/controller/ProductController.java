package ro.msg.learning.shop.controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dto.PageOptionsDTO;
import ro.msg.learning.shop.dto.ProductDTO;
import ro.msg.learning.shop.dto.SortOptionsListDTO;
import ro.msg.learning.shop.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/all")
    public List<ProductDTO> allProducts() {
        return productService.getAllProducts();
    }

    @GetMapping(value = "/{id}")
    public ProductDTO getProductById(@PathVariable("id") Integer id) {
        return productService.getProductById(id);
    }

    @Transactional
    @PostMapping("/")
    public ProductDTO createProduct(@RequestBody ProductDTO product) {
        return productService.createProduct(product);
    }

    @Transactional
    @DeleteMapping("/")
    public void deleteProduct(@RequestBody ProductDTO product) {
        productService.deleteProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable("id") Integer id) {
        productService.deleteProductById(id);
    }

    @Transactional
    @PutMapping("/")
    public ProductDTO updateProduct(@RequestBody ProductDTO product) {
        return productService.updateProduct(product);
    }

    @PostMapping("/all/paged")
    public List<ProductDTO> getProductsPaged(@RequestBody PageOptionsDTO pageOptionsDTO) {
        return productService.getAllProductsPaged(pageOptionsDTO.getStart(), pageOptionsDTO.getSize(),
                pageOptionsDTO.getSortOptions());
    }

    @PostMapping("/all/sorted")
    public List<ProductDTO> getProductsSorted(@RequestBody SortOptionsListDTO sortOptionsListDTO) {
        return productService.getAllProductsSorted(sortOptionsListDTO.getSortOptions());
    }
}
