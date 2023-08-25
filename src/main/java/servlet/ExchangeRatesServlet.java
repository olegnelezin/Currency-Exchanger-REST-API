package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import database.CurrenciesDatabase;
import database.ExchangeRatesDatabase;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ExchangeRate;
import utils.Validator;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "exchangeRates", value = "/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    ExchangeRatesDatabase exchangeRatesDatabase;
    CurrenciesDatabase currenciesDatabase;
    Validator validator;
    @Override
    public void init(ServletConfig config) {
        exchangeRatesDatabase = (ExchangeRatesDatabase) config.getServletContext().getAttribute("exchangeRateDatabase");
        currenciesDatabase = (CurrenciesDatabase) config.getServletContext().getAttribute("currenciesDatabase");
        validator = new Validator(currenciesDatabase, exchangeRatesDatabase);
    }


    // GET: Получение всех обменных курсов валют из базы ExchangeRate
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        new ObjectMapper().writeValue(resp.getWriter(), exchangeRatesDatabase.getAllExchangeRates());
        validator = new Validator(exchangeRatesDatabase);
    }


    // POST: Добавление новой записи в базу ExchangeRate
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int baseCurrencyId = Integer.parseInt(req.getParameter("baseCurrencyId"));
        int targetCurrencyId = Integer.parseInt(req.getParameter("targetCurrencyId"));
        BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(req.getParameter("rate")));

        if (validator.isExchangeRateNotExists(baseCurrencyId, targetCurrencyId, resp)) {
            ExchangeRate exchangeRate = new ExchangeRate(baseCurrencyId, targetCurrencyId, rate);
            exchangeRatesDatabase.insertRecord(exchangeRate);

            doGet(req, resp);
        }

    }
}
