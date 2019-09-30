package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dto.ProductDTO;
import ro.msg.learning.shop.dto.SortOption;
import ro.msg.learning.shop.exception.CouldNotFindProductException;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.model.mongodb.ProductMongo;
import ro.msg.learning.shop.repository.jpa.ProductCategoryJpaRepository;
import ro.msg.learning.shop.repository.jpa.SupplierJpaRepository;
import ro.msg.learning.shop.repository.jpa.impl.ProductJpaRepositoryImpl;
import ro.msg.learning.shop.repository.mongo.ProductMongoRepository;
import ro.msg.learning.shop.util.RepositoryUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductJpaRepositoryImpl productRepository;
    private final ProductCategoryJpaRepository productCategoryJpaRepository;
    private final SupplierJpaRepository supplierJpaRepository;
    private final RepositoryUtils repositoryUtils;
    private final ProductMongoRepository productMongoRepository;

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = generateProduct(productDTO);
        return ProductDTO.toDTO(productRepository.save(product));
    }

    public ProductDTO getProductById(Integer productId) {
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isPresent())
            return ProductDTO.toDTO(productOptional.get());
        else throw new CouldNotFindProductException();
    }

    public void deleteProduct(ProductDTO productDTO) {
        Optional<Product> productOptional = productRepository.findById(productDTO.getProductId());

        if (productOptional.isPresent())
            productRepository.delete(productOptional.get());
        else throw new CouldNotFindProductException();
    }

    public void deleteProductById(Integer productId) {
        productRepository.deleteById(productId);
    }

    public ProductDTO updateProduct(ProductDTO productDTO) {

        Optional<Product> productOptional = productRepository.findById(productDTO.getProductId());

        if (productOptional.isPresent()) {
            Product product = generateProduct(productDTO);
            product.setId(productDTO.getProductId());

            return ProductDTO.toDTO(productRepository.save(product));
        } else {
            throw new CouldNotFindProductException();
        }
    }

    public List<ProductDTO> getAllProducts() {

        return productRepository.findAll(PageRequest.of(1, 4, Sort.by(Sort.Order.asc("price"))))
                .stream()
                .map(ProductDTO::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getAllProductsSorted(List<SortOption> sortOptions) {
        return productRepository.findAll(repositoryUtils.generateSort(sortOptions))
                .stream()
                .map(ProductDTO::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getAllProductsPaged(int start, int size, List<SortOption> sortOptions) {
        return productRepository.findAll(PageRequest.of(start, size, repositoryUtils.generateSort(sortOptions)))
                .stream()
                .map(ProductDTO::toDTO)
                .collect(Collectors.toList());
    }

    public ProductMongo createProductMongo(ProductMongo productMongo) {
        return productMongoRepository.save(productMongo);
    }

    public List<ProductMongo> getAllMongoProducts() {
        return productMongoRepository.findAll();
    }

    private Product generateProduct(ProductDTO productDTO) {

        return Product.builder()
                .name(productDTO.getProductName())
                .description(productDTO.getProductDescription())
                .price(productDTO.getPrice())
                .weight(productDTO.getWeight())
                .imageUrl(productDTO.getImageUrl())
                .productCategory(productCategoryJpaRepository.getOne(productDTO.getCategoryId()))
                .supplier(supplierJpaRepository.getOne(productDTO.getSupplierId()))
                .build();
    }
}
