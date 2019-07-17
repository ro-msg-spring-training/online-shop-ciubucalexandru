package ro.msg.learning.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.repository.AddressRepository;
import ro.msg.learning.shop.repository.StockRepository;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    StockRepository stockRepository;

    @RequestMapping("/addresses")
    public String getAddresses() {

        return addressRepository.getOne(2).getLocation().getName();
    }

    @RequestMapping("/stocks")
    public String getStocks() {
        List<Stock> stockList = stockRepository.findAll();
        String result = "";

        result += stockList.get(0).getId();
        result += stockList.get(0).getQuantity();
        result += "<br><br>";

        return result;
    }
}
