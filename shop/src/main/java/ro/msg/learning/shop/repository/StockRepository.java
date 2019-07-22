package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.model.ids.StockId;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, StockId> {
    List<Stock> getByLocation(Location location);
}
