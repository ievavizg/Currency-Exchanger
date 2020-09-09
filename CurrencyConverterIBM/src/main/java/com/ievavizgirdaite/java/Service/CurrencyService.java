package com.ievavizgirdaite.java.Service;

import com.ievavizgirdaite.java.Entity.Currency;
import com.ievavizgirdaite.java.Entity.Repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyService {

    @Autowired
    CurrencyRepository currencyRepository;

    private final RestTemplate restTemplate;

    public CurrencyService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getCurrenciesInXml() {
        String url = "http://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrentFxRates?tp=EU";
        return this.restTemplate.getForObject(url, String.class);
    }

    @Transactional
    public Currency add(Currency currency) {
/*        if currencyRepository.findCurrencyByFromCurrencyAndToCurrency(currency.getFromCurrency(), currency.getToCurrency())
        {

        }*/
        return currencyRepository.save(currency);
    }

    @Transactional
    public void update(Currency currency) {
        currencyRepository.save(currency);
    }

    @Transactional
    public Currency findCurrencyExchangeRate(String fromCurrency, String toCurrency){
        return currencyRepository.findCurrencyByFromCurrencyAndToCurrency(fromCurrency, toCurrency);
    }

    @Transactional
    public Boolean checkIfDataExist(String fromCurrency, String toCurrency){

        if (currencyRepository.existsByFromCurrencyAndToCurrency(fromCurrency, toCurrency))
        {
            return true;
        }
        return false;
    }
}
