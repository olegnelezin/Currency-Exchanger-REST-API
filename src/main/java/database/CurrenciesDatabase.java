package database;

import model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CurrenciesDatabase implements CrudInterface<Currency> {
    private Connection connection;

    public CurrenciesDatabase(Connection connection) {
        this.connection = connection;
    }

    public List<Currency> getAllCurrencies() {
        String query = "SELECT Id, Code, Name, Sign FROM Currencies";
        List<Currency> currencies = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                currencies.add(getCurrency(resultSet));
            }
            return currencies;
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    private Currency getCurrency(ResultSet resultSet) {
        Currency currency = new Currency();
        try {
            currency.setId(resultSet.getInt("id"));
            currency.setCode(resultSet.getString("code"));
            currency.setName(resultSet.getString("name"));
            currency.setSign(resultSet.getString("sign"));
            return currency;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Currency findById(int id) {
        Currency currency = null;
        final String query = "SELECT Id, Code, Name, Sign FROM Currencies WHERE Id = " + id;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.getInt(1) != 0) {
                currency = getCurrency(resultSet);
            }
            return currency;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Currency findByCode(String code) {
        Currency currency = null;
        final String query = "SELECT Id, Code, Name, Sign FROM Currencies WHERE Code ='" + code + "'";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.getInt(1) != 0) {
                currency = getCurrency(resultSet);
            }
            return currency;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertRecord(Currency entity) {
        final String query = "INSERT INTO Currencies (Code, Name, Sign) VALUES (?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, entity.getCode());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getSign());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateRecord(Currency entity) {
        final String query = "UPDATE Currencies SET Code=?, Name=?, Sign=? WHERE Id=?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, entity.getCode());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getSign());
            statement.setInt(4, entity.getId());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteRecord(int id) {
        final String query = "DELETE FROM Currencies WHERE Id = " + id;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
