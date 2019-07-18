package ro.msg.learning.shop.controller;

import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dto.ProductDTO;
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

    @PostMapping("/")
    public ProductDTO createProduct(@RequestBody ProductDTO product) {
        return productService.createProduct(product);
    }

    @DeleteMapping("/")
    public void deleteProduct(@RequestBody ProductDTO product) {
        productService.deleteProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable("id") Integer id) {
        productService.deleteProductById(id);
    }

    @PutMapping("/")
    public ProductDTO updateProduct(@RequestBody ProductDTO product) {
        return productService.updateProduct(product);
    }
}
