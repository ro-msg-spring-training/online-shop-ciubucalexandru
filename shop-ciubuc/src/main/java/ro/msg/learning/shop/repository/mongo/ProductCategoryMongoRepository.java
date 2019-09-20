package ro.msg.learning.shop.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ro.msg.learning.shop.model.mongodb.ProductCategoryMongo;

public interface ProductCategoryMongoRepository extends MongoRepository<ProductCategoryMongo, Integer> {
}
