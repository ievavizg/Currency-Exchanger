package com.ievavizgirdaite.java.Controller;

import com.ievavizgirdaite.java.Entity.Currency;
import com.ievavizgirdaite.java.Entity.Repository.CurrencyRepository;
import com.ievavizgirdaite.java.Reader.XmlReader;
import com.ievavizgirdaite.java.Service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;

@RestController
public class CurrencyController {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private XmlReader xmlReader;

    @GetMapping("/list")
    public Iterable<Currency> getCurrencies() {
        return currencyRepository.findAll();
    }

    @GetMapping("/find/{id}")
    public Currency findCurrencyExchangeById(@PathVariable Long id) {
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
    public String getCurrenciesExchange()
    {
       return xmlReader.readXml(currencyService.getCurrenciesInXml());
    }

    @GetMapping("/calculate")
    public Double calculateExchange(@RequestParam String fromCurrency, @RequestParam String toCurrency, @RequestParam Double quantity)
    {
        Currency currency = currencyService.findCurrencyExchangeRate(fromCurrency, toCurrency);
        return currency.getExchangeRate() * quantity;
    }

    @GetMapping("/check")
    public Boolean check(@RequestParam String fromCurrency, @RequestParam String toCurrency, @RequestParam Double quantity)
    {
        return currencyService.checkIfDataExist(fromCurrency, toCurrency);
    }

}
