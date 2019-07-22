package ro.msg.learning.shop.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dto.OrderCreationDTO;
import ro.msg.learning.shop.dto.ProductQuantityDTO;
import ro.msg.learning.shop.dto.StrategyResultsDTO;
import ro.msg.learning.shop.exception.CouldNotFindLocationException;
import ro.msg.learning.shop.model.*;
import ro.msg.learning.shop.model.ids.OrderDetailId;
import ro.msg.learning.shop.model.ids.StockId;
import ro.msg.learning.shop.repository.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class MostAbundantStrategy implements CreateOrderStrategy {

    private final LocationRepository locationRepository;
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    @Override
    public List<Order> createOrder(OrderCreationDTO orderCreationDTO) {

        List<Location> locations = locationRepository.findAll();

        List<ProductQuantityDTO> products = orderCreationDTO.getProducts();

        List<Location> maxLocations = products.stream()
                .map(product -> getMaxLocation(locations, product.getId(), product.getQuantity()))
                .collect(Collectors.toList());

        if (!maxLocations.stream().allMatch(Objects::nonNull))
            throw new CouldNotFindLocationException("Could not find a suitable location corresponding to user needs!");

        List<StrategyResultsDTO> results = IntStream
                .range(0, products.size())
                .boxed()
                .map(i -> StrategyResultsDTO.builder()
                        .product(productRepository.getOne(products.get(i).getId()))
                        .location(maxLocations.get(i))
                        .quantity(products.get(i).getQuantity())
                        .build()
                )
                .collect(Collectors.toList());

        return generateOrder(results, orderCreationDTO);
    }

    private Location getMaxLocation(List<Location> locations, Integer productId, Integer neededQuantity) {

        Optional<Location> optional = locations.stream()
                .filter(location -> {
                    Integer quantityOfLocation = getQuantityOfLocation(location, productId);
                    return quantityOfLocation > 0 && quantityOfLocation >= neededQuantity;
                })
                .max(Comparator.comparing(a -> getQuantityOfLocation(a, productId)));

        return optional.orElse(null);
    }

    private Integer getQuantityOfLocation(Location location, Integer productId) {

        List<Stock> stocks = stockRepository.getByLocation(location);

        Optional<Integer> optional = stocks.stream()
                .filter(stock -> stock.getProduct().getId().equals(productId))
                .map(Stock::getQuantity)
                .findFirst();

        return optional.orElse(0);
    }

    private List<Order> generateOrder(List<StrategyResultsDTO> results, OrderCreationDTO orderCreationDTO) {

        Customer customer = customerRepository.getOne(1);
        Address address = addressRepository.getOne(orderCreationDTO.getAddressId());

        Map<Location, Map<Product, Integer>> groupedByLocation = results
                .stream()
                .collect(Collectors.groupingBy(
                        StrategyResultsDTO::getLocation,
                        Collectors.toMap(StrategyResultsDTO::getProduct, StrategyResultsDTO::getQuantity)
                ));

        List<Order> orders = new ArrayList<>();

        groupedByLocation.forEach((key, value) -> {

            Order order = Order.builder()
                    .id(null)
                    .location(key)
                    .customer(customer)
                    .createdAt(orderCreationDTO.getCreatedAt())
                    .address(address)
                    .build();

            order = orderRepository.save(order);
            final int orderId = order.getId();
            final Location location = order.getLocation();

            orders.add(order);

            value.forEach((product, quantity) -> {

                orderDetailRepository.save(OrderDetail.builder()
                        .id(new OrderDetailId(orderId, product.getId()))
                        .quantity(quantity)
                        .build()
                );

                StockId stockId = new StockId(product.getId(), location.getId());
                Stock stock = stockRepository.getOne(stockId);
                stock.setQuantity(stock.getQuantity() - quantity);
                stockRepository.save(stock);
            });
        });

        return orders;
    }
}
