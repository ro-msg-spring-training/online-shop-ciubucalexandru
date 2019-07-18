package ro.msg.learning.shop.service;

import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dto.ProductDTO;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.model.ProductCategory;
import ro.msg.learning.shop.repository.ProductCategoryRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.SupplierRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private ProductCategoryRepository productCategoryRepository;
    private SupplierRepository supplierRepository;

    public ProductService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository, SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.supplierRepository = supplierRepository;
    }

    public ProductDTO createProduct(ProductDTO productDTO) {

        Product product = generateProduct(productDTO);
        return generateProductDTO(productRepository.save(product));
    }

    public ProductDTO getProductById(Integer productId) {
        return generateProductDTO(productRepository.getOne(productId));
    }

    public void deleteProduct(ProductDTO productDTO) {
        Product product = productRepository.getOne(productDTO.getProductId());
        productRepository.delete(product);
    }

    public void deleteProductById(Integer productId) {
        productRepository.deleteById(productId);
    }

    public ProductDTO updateProduct(ProductDTO productDTO) {
        if (productRepository.getOne(productDTO.getProductId()) != null) {
            Product product = generateProduct(productDTO);
            product.setId(productDTO.getProductId());

            return generateProductDTO(productRepository.save(product));
        } else {
            return null;
        }
    }

    public List<ProductDTO> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(product -> generateProductDTO(product))
                .collect(Collectors.toList());
    }

    private ProductDTO generateProductDTO(Product product) {

        ProductDTO productDTO = new ProductDTO();
        ProductCategory productCategory = product.getProductCategory();

        productDTO.setProductId(product.getId());
        productDTO.setProductName(product.getName());
        productDTO.setProductDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setWeight(product.getWeight());
        productDTO.setImageUrl(product.getImageUrl());
        productDTO.setCategoryId(productCategory.getId());
        productDTO.setCategoryName(productCategory.getName());
        productDTO.setCategoryDescription(productCategory.getDescription());
        productDTO.setSupplierId(product.getSupplier().getId());
        productDTO.setSupplierName(product.getSupplier().getName());

        return productDTO;
    }

    private Product generateProduct(ProductDTO productDTO) {

        Product product = new Product();

        product.setName(productDTO.getProductName());
        product.setDescription(productDTO.getProductDescription());
        product.setPrice(productDTO.getPrice());
        product.setWeight(productDTO.getWeight());
        product.setImageUrl(productDTO.getImageUrl());

        product.setProductCategory(productCategoryRepository.getOne(productDTO.getCategoryId()));
        product.setSupplier(supplierRepository.getOne(productDTO.getSupplierId()));

        return product;
    }

}
