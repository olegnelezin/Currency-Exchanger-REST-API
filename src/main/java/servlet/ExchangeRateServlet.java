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

@WebServlet(name = "exchangeRate", value = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    ExchangeRatesDatabase exchangeRatesDatabase;
    CurrenciesDatabase currenciesDatabase;
    Validator validator;

    @Override
    public void init(ServletConfig config) {
        exchangeRatesDatabase = (ExchangeRatesDatabase) config.getServletContext().getAttribute("exchangeRateDatabase");
        currenciesDatabase = (CurrenciesDatabase) config.getServletContext().getAttribute("currenciesDatabase");

        validator = new Validator(exchangeRatesDatabase);
    }


    // GET: Получение обменного курса конкретной пары валют из базы ExchangeRate
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!validator.isCorrectRequest(req, resp)) {
            return;
        }

        String line = req.getPathInfo().replace("/", "").toUpperCase();

        if (validator.isCorrectPathLength(line, 6, resp)) {
            return;
        }

        String baseCurrency = line.substring(0, 3);
        String targetCurrency = line.substring(3, 6);

        if (validator.isCurrenciesValid(baseCurrency, targetCurrency, resp)) {
            ExchangeRate exchangeRate = exchangeRatesDatabase.findByCodes(baseCurrency, targetCurrency);
            exchangeRate.setBaseCurrency(currenciesDatabase.findByCode(baseCurrency));
            exchangeRate.setTargetCurrency(currenciesDatabase.findByCode(targetCurrency));
            new ObjectMapper().writeValue(resp.getWriter(), exchangeRate);
        }
    }
}
