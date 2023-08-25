package listener;

import database.CurrenciesDatabase;
import database.ExchangeRatesDatabase;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import service.ConnectDatabaseService;

import java.sql.Connection;


@WebListener
public class ContextListener implements ServletContextListener {

    // Связываем базы и сервлет
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        Connection connection = new ConnectDatabaseService().getConnection();

        CurrenciesDatabase currenciesDatabase = new CurrenciesDatabase(connection);
        ExchangeRatesDatabase exchangeRatesDatabase = new ExchangeRatesDatabase(connection);

        context.setAttribute("currenciesDatabase", currenciesDatabase);
        context.setAttribute("exchangeRateDatabase", exchangeRatesDatabase);
    }
}
