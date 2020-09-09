package com.ievavizgirdaite.java.Entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter @Setter
@Table(name = "currency")
@Entity
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fromCurrency", length = 15)
    private String fromCurrency;

    @Column(name = "toCurrency", length = 15)
    private String toCurrency;

    @Column(name = "tradeAmount")
    private Double tradeAmount;

    @Column(name = "exchangeRate")
    private Double exchangeRate;

    @JacksonXmlProperty(localName = "Dt")
    @Column(name = "date")
    private Date date;
}
