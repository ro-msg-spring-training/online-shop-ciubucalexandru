package ro.msg.learning.shop.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ro.msg.learning.shop.model.mongodb.ProductMongo;

public interface ProductMongoRepository extends MongoRepository<ProductMongo, Integer> {
}
