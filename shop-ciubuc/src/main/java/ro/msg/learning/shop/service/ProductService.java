package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dto.ProductDTO;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.repository.ProductCategoryRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.SupplierRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final SupplierRepository supplierRepository;

    public ProductDTO createProduct(ProductDTO productDTO) {

        Product product = generateProduct(productDTO);
        return ProductDTO.toDTO(productRepository.save(product));
    }

    public ProductDTO getProductById(Integer productId) {
        return ProductDTO.toDTO(productRepository.getOne(productId));
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

            return ProductDTO.toDTO(productRepository.save(product));
        } else {
            return null;
        }
    }

    public List<ProductDTO> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(ProductDTO::toDTO)
                .collect(Collectors.toList());
    }

    private Product generateProduct(ProductDTO productDTO) {

        return Product.builder()
                .name(productDTO.getProductName())
                .description(productDTO.getProductDescription())
                .price(productDTO.getPrice())
                .weight(productDTO.getWeight())
                .imageUrl(productDTO.getImageUrl())
                .productCategory(productCategoryRepository.getOne(productDTO.getCategoryId()))
                .supplier(supplierRepository.getOne(productDTO.getSupplierId()))
                .build();
    }
}
