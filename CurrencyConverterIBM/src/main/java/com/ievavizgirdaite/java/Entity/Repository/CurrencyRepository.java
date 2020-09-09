package com.ievavizgirdaite.java.Entity.Repository;

import com.ievavizgirdaite.java.Entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findCurrencyById(Long id);
    Currency findCurrencyByFromCurrencyAndToCurrency(String FromCurrency, String ToCurrency);
}
