package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import database.CurrenciesDatabase;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.Validator;

import java.io.IOException;

@WebServlet(name = "currency", value = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    CurrenciesDatabase currenciesDatabase;
    Validator validator;

    @Override
    public void init(ServletConfig config) {
        currenciesDatabase = (CurrenciesDatabase) config.getServletContext().getAttribute("currenciesDatabase");
        validator = new Validator(currenciesDatabase);
    }

    // GET: Получение информации о конкретной валюте из базы Currencies
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        if (!validator.isCorrectRequest(req, resp)) {
            return;
        }

        String currencyCode = req.getPathInfo().replace("/", "").toUpperCase();

        if (validator.isCorrectPathLength(currencyCode, 3, resp)) {
            return;
        }

        if (validator.isCurrencyValid(currencyCode, resp)) {
            resp.setContentType("application/json; charset=UTF-8");
            new ObjectMapper().writeValue(resp.getWriter(), currenciesDatabase.findByCode(currencyCode));
        }
    }

}
