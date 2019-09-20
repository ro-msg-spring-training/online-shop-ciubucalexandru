package ro.msg.learning.shop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.model.ids.StockId;

import java.util.List;
import java.util.Optional;

public interface StockJpaRepository extends JpaRepository<Stock, StockId> {
    List<Stock> getByLocation(Location location);

    Optional<Stock> getByLocationAndProductId(Location location, Integer productId);
}
