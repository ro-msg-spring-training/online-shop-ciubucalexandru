package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dto.StockCsvDTO;
import ro.msg.learning.shop.exception.CouldNotFindLocationException;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.StockRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockExportService {

    private final StockRepository stockRepository;
    private final LocationRepository locationRepository;

    public List<StockCsvDTO> exportStocks(Integer locationId) {
        Optional<Location> locationOptional = locationRepository.findById(locationId);

        if (locationOptional.isPresent())
            return stockRepository.getByLocation(locationOptional.get())
                    .stream()
                    .map(StockCsvDTO::fromStock)
                    .collect(Collectors.toList());
        else throw new CouldNotFindLocationException("The location was not found!");
    }
}
