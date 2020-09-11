package com.ievavizgirdaite.java.Controller;

import com.ievavizgirdaite.java.Entity.Currency;
import com.ievavizgirdaite.java.Entity.Repository.CurrencyRepository;
import com.ievavizgirdaite.java.Reader.XmlReader;
import com.ievavizgirdaite.java.Service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
public class CurrencyController {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private XmlReader xmlReader;

    @GetMapping("/list")
    public @ResponseBody List<Currency> getCurrencies() {
        return currencyRepository.findAll();
    }

    @GetMapping("/find/{id}")
    public @ResponseBody Currency findCurrencyExchangeById(@PathVariable Long id) {
        return currencyRepository.findCurrencyById(id);
    }

    @PostMapping("/add")
    public String addCurrencyExchangeRate(@RequestParam String fromCurrency, @RequestParam String toCurrency,
                                          @RequestParam Double tradeAmount, @RequestParam Double exchangeRate) {
        Currency currency = new Currency();
        currency.setFromCurrency(fromCurrency);
        currency.setToCurrency(toCurrency);
        currency.setTradeAmount(tradeAmount);
        currency.setExchangeRate(exchangeRate);
        currencyRepository.save(currency);
        return "Added new currency to repo!";
    }

    @GetMapping("/getExchanges")
    public ResponseEntity<?> getCurrenciesExchange()
    {
       xmlReader.readXml(currencyService.getCurrenciesInXml());
       return ResponseEntity.ok("Loaded exchanges");
    }

    @GetMapping("/calculate")
    public ResponseEntity<?> calculateExchange(@RequestParam String fromCurrency, @RequestParam String toCurrency, @RequestParam Double quantity)
    {
        Currency currency = new Currency();
        Double price = 0.0;

        if (currencyService.checkIfDataExist(fromCurrency, toCurrency))
        {
            currency = currencyService.findCurrencyExchangeRate(fromCurrency, toCurrency);
            price = currency.getExchangeRate() * quantity;
        } else if (currencyService.checkIfDataExist(toCurrency, fromCurrency))
        {
            currency = currencyService.findCurrencyExchangeRate(toCurrency, fromCurrency);
            price = quantity * (currency.getTradeAmount() / currency.getExchangeRate());
        }
        return ResponseEntity.ok(price);
    }

    @GetMapping("/check")
    public ResponseEntity<?> check(@RequestParam String fromCurrency, @RequestParam String toCurrency, @RequestParam Double quantity)
    {
        return ResponseEntity.ok(currencyService.checkIfDataExist(fromCurrency, toCurrency));
    }

}
