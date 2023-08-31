package utils;

import database.CurrenciesDatabase;
import database.ExchangeRatesDatabase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import model.Currency;

import java.io.IOException;
@AllArgsConstructor
public class Validator {
    CurrenciesDatabase currenciesDatabase;
    ExchangeRatesDatabase exchangeRatesDatabase;


    public Validator(CurrenciesDatabase currenciesDatabase) {
        this.currenciesDatabase = currenciesDatabase;
    }

    public Validator(ExchangeRatesDatabase exchangeRatesDatabase) {
        this.exchangeRatesDatabase = exchangeRatesDatabase;
    }

    // Проверка на ошибку 404
    public boolean isCorrectRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        boolean isCorrect = true;
        if (req.getPathInfo() == null || req.getPathInfo().equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат ввода.");
            isCorrect = false;
        }
        return isCorrect;
    }

    // Проверка длины пути
    public boolean isCorrectPathLength(String line, int correctLength, HttpServletResponse resp) throws IOException {
        boolean isCorrect = true;
        if (line.length() != correctLength) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат ввода.");
            isCorrect = false;
        }
        return !isCorrect;
    }

    // Проверка на корректность валюты в базе Currencies
    public boolean isCurrencyValid(String currencyCode, HttpServletResponse resp) throws IOException {
        boolean isFind = true;
        if (currenciesDatabase.findByCode(currencyCode) == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Неизвестные данные");
            isFind = false;
        }
        return isFind;
    }

    // Проверка на существование валюты в базе ExchangeRate
    public boolean isExchangeRateNotExists(int baseCurrencyId, int targetCurrencyId, HttpServletResponse resp) throws IOException {
        boolean isNotExist = false;
        Currency baseCurrency = currenciesDatabase.findById(baseCurrencyId);
        Currency targetCurrency = currenciesDatabase.findById(targetCurrencyId);
        if (exchangeRatesDatabase.findByCodes(baseCurrency.getCode(), targetCurrency.getCode()) != null) {
            isNotExist = true;
        } else {
            resp.sendError(HttpServletResponse.SC_CONFLICT, "Введённые данные уже существуют");
        }
        return isNotExist;
    }

    // Проверка на корректность валют в базе ExchangeRates
    public boolean isCurrenciesValid(String baseCurrency, String targetCurrency, HttpServletResponse resp) throws IOException {
        boolean isFind = true;
        if (exchangeRatesDatabase.findByCodes(baseCurrency, targetCurrency) == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Неизвестные данные");
            isFind = false;
        }
        return isFind;
    }

}

