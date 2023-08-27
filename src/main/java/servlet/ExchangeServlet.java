package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import database.ExchangeRatesDatabase;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import DTO.Exchange;
import model.ExchangeRate;
import utils.Validator;

import java.io.IOException;
import java.math.BigDecimal;


@WebServlet(name = "ExchangeServlet", value = "/exchange")
public class ExchangeServlet extends HttpServlet {
    ExchangeRatesDatabase exchangeRatesDatabase;
    Validator validator;

    @Override
    public void init(ServletConfig config) {
        exchangeRatesDatabase = (ExchangeRatesDatabase) config.getServletContext().getAttribute("exchangeRateDatabase");
        validator = new Validator(exchangeRatesDatabase);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String from = req.getParameter("from").toUpperCase();
        String to = req.getParameter("to").toUpperCase();
        String amount = req.getParameter("amount");

        if (validator.isCurrenciesValid(from, to, resp)){
            ExchangeRate exchangeRate = exchangeRatesDatabase.findByCodes(from, to);

            Exchange exchange = convert(exchangeRate, amount);
            new ObjectMapper().writeValue(resp.getWriter(), exchange);
        }


    }

    // Инициализируем DTO Exchange, чтобы потом провести сериализацию
    private Exchange convert(ExchangeRate exchangeRate, String amount) {
        Exchange exchange = new Exchange();

        exchange.setBaseCurrency(exchangeRate.getBaseCurrency());
        exchange.setTargetCurrency(exchangeRate.getTargetCurrency());
        exchange.setRate(exchangeRate.getRate());
        exchange.setAmount(new BigDecimal(amount));

        BigDecimal convertedAmount = new BigDecimal(amount).multiply(exchangeRate.getRate());
        exchange.setConvertedAmount(convertedAmount);
        return exchange;
    }
}
