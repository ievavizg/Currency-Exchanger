package com.ievavizgirdaite.java.Reader;

import com.ievavizgirdaite.java.Entity.Currency;
import com.ievavizgirdaite.java.Service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class XmlReader {

    @Autowired
    CurrencyService currencyService;


    public String readXml(String xmlDoc) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        String forreturn = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(xmlDoc)));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("FxRate");

            List<Currency> currencies = new ArrayList<Currency>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Currency currency = getCurrency(nodeList.item(i));

               /// currencyManager.create(currency);

                //>TEMPORARY
                forreturn = forreturn + currency.getFromCurrency() + ' ' + currency.getTradeAmount() + ' ' + currency.getToCurrency() + ' ' + currency.getExchangeRate() + '\n';
                //<TEMPORARY

                currencies.add(currency);
            }

            return forreturn;
        } catch (SAXException | ParserConfigurationException | IOException | ParseException e1) {
            e1.printStackTrace();
        }

        return "false";
    }

    private Currency getCurrencyFrom(Node node, Currency currency){
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            currency.setFromCurrency(getTagValue("Ccy", element));
            currency.setTradeAmount(Double.parseDouble(getTagValue("Amt", element)));
        }
        return currency;
    }
    private Currency getCurrencyTo(Node node, Currency currency){
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            currency.setToCurrency(getTagValue("Ccy", element));
            currency.setExchangeRate(Double.parseDouble(getTagValue("Amt", element)));
        }
        return currency;
    }

    private Currency getCurrency(Node node) throws ParseException {
        Currency currency = new Currency();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            currency.setDate(format.parse(getTagValue("Dt", element)));
            NodeList nodeListExchangeRates = element.getElementsByTagName("CcyAmt");
            for (int x = 0; x < nodeListExchangeRates.getLength(); x++) {
                if(x == 0)
                {
                    getCurrencyFrom(nodeListExchangeRates.item(x), currency);
                }
                else
                {
                    getCurrencyTo(nodeListExchangeRates.item(x), currency);
                }
            }
        }
        if (currencyService.checkIfDataExist(currency.getFromCurrency(), currency.getToCurrency()))
        {
            Currency curr = currencyService.findCurrencyExchangeRate(currency.getFromCurrency(), currency.getToCurrency());
            curr.setTradeAmount(currency.getTradeAmount());
            curr.setExchangeRate(currency.getExchangeRate());
            curr.setDate(currency.getDate());
            currencyService.update(curr);
        }else
            currencyService.add(currency);
        return currency;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }
}
