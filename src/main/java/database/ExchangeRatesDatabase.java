package database;

import model.ExchangeRate;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExchangeRatesDatabase implements CrudInterface<ExchangeRate> {
    private Connection connection;
    CurrenciesDatabase currenciesDatabase;

    public ExchangeRatesDatabase(Connection connection) {
        this.connection = connection;
    }

    private ExchangeRate getExchangeRate(ResultSet resultSet) { //
        ExchangeRate exchangeRate = new ExchangeRate();
        try {
            exchangeRate.setId(resultSet.getInt("Id"));
            exchangeRate.setBaseCurrencyId(resultSet.getInt("BaseCurrencyId"));
            exchangeRate.setTargetCurrencyId(resultSet.getInt("TargetCurrencyId"));
            exchangeRate.setRate(resultSet.getBigDecimal("Rate"));

            currenciesDatabase = new CurrenciesDatabase(connection);
            exchangeRate.setBaseCurrency(currenciesDatabase.findById(exchangeRate.getBaseCurrencyId()));
            exchangeRate.setTargetCurrency(currenciesDatabase.findById(exchangeRate.getTargetCurrencyId()));
            return exchangeRate;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ExchangeRate> getAllExchangeRates() {
        String query = "SELECT Id, BaseCurrencyId, TargetCurrencyId, Rate FROM ExchangeRates";
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                exchangeRates.add(getExchangeRate(resultSet));
            }
            return exchangeRates;
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public ExchangeRate findById(int id) {
        ExchangeRate exchangeRate = null;
        final String query = "SELECT Id, BaseCurrencyId, TargetCurrencyId, Rate FROM ExchangeRates WHERE id = " + id;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.getInt(1) != 0) {
                exchangeRate = getExchangeRate(resultSet);
            }
            return exchangeRate;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ExchangeRate findByCodes(String baseCurrencyCode, String targetCurrencyCode) {
        currenciesDatabase = new CurrenciesDatabase(connection);
        ExchangeRate exchangeRate;
        int baseCurrencyId = currenciesDatabase.findByCode(baseCurrencyCode).getId();
        int targetCurrencyId = currenciesDatabase.findByCode(targetCurrencyCode).getId();
        final String query = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId = " + baseCurrencyId + " AND TargetCurrencyId = " + targetCurrencyId;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            exchangeRate = getExchangeRate(resultSet);
            return exchangeRate;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void insertRecord(ExchangeRate entity) {
        final String query = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, entity.getBaseCurrencyId());
            statement.setInt(2, entity.getTargetCurrencyId());
            statement.setBigDecimal(3, entity.getRate());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateRecord(ExchangeRate entity) {
        final String query = "UPDATE ExchangeRates SET BaseCurrencyid=?, TargetCurrencyid=?, Rate=? WHERE Id=?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setLong(1, entity.getBaseCurrencyId());
            statement.setLong(2, entity.getTargetCurrencyId());
            statement.setBigDecimal(3, entity.getRate());
            statement.setInt(4, entity.getId());

            currenciesDatabase = new CurrenciesDatabase(connection);
            entity.setBaseCurrency(currenciesDatabase.findById(entity.getBaseCurrencyId()));
            entity.setTargetCurrency(currenciesDatabase.findById(entity.getTargetCurrencyId()));
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteRecord(int id) {
        final String query = "DELETE FROM ExchangeRates WHERE Id = " + id;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
