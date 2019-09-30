package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dto.StockCsvDTO;
import ro.msg.learning.shop.service.StockExportService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StockController {

    private final StockExportService stockExportService;

    @GetMapping(value = "/{locationId}/stocks-csv")
    public List<StockCsvDTO> getStocksCsv(@PathVariable("locationId") Integer locationId) {
        return stockExportService.exportStocks(locationId);
    }
}
