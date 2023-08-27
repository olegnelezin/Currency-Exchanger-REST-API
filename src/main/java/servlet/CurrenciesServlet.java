package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import database.CurrenciesDatabase;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Currency;
import utils.Validator;

import java.io.IOException;

@WebServlet(name = "currencies", value = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    CurrenciesDatabase currenciesDatabase;
    Validator validator;

    @Override
    public void init(ServletConfig config) {
        currenciesDatabase = (CurrenciesDatabase) config.getServletContext().getAttribute("currenciesDatabase");
        validator = new Validator(currenciesDatabase);
    }

    // GET: Получение всех валют из базы Currencies
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        new ObjectMapper().writeValue(resp.getWriter(), currenciesDatabase.getAllCurrencies());
    }

    // POST: Добавление новой записи в базу Currencies
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");

        if (validator.isCurrencyNotExists(code, resp)) {
            Currency currency = new Currency(code, name, sign);
            currenciesDatabase.insertRecord(currency);

            doGet(req, resp);
        }


    }
}
