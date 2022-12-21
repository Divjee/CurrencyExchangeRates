package org.example;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAndSql {
    Connection connection = null;
    String url = "jdbc:mariadb://exchange_rates:3306/exchange_rates";
    String user = "root";
    String pwd = "root";
    final static Logger logger = LoggerFactory.getLogger(DatabaseAndSql.class);
    private final List<CurrencyRate> currencies = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    public DatabaseAndSql() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, pwd);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        logger.info("Successfully connected to database");
    }
    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS `exchange_rates` (" +
                "`date` DATE NOT NULL," +
                "`currency` VARCHAR(50) NOT NULL," +
                "`rate` DECIMAL(19, 6) NOT NULL" +
                ");";

        stmt.executeUpdate(sql);
        logger.info("Created table in given database...");
    }
    public void insertData() throws SQLException, FeedException, IOException {
        Statement stmt = connection.createStatement();
        addCurrenciesToList();
        for (CurrencyRate rates : this.currencies) {
            stmt.executeUpdate("INSERT INTO exchange_rates(date,currency,rate) VALUES ('" + rates.getDate() + "','" + rates.getCurrency() + "','" + rates.getRate() + "')");
        }
    }

    public void getAllCurrencies(Context context) throws SQLException {
        PreparedStatement pr = connection.prepareStatement("SELECT * FROM exchange_rates");
        ResultSet rs = pr.executeQuery();
        List<CurrencyRate> currencyRateList = new ArrayList<>();
        while (rs.next()) {
            currencyRateList.add(new CurrencyRate(rs.getDate("date").toLocalDate(), rs.getString("currency"), rs.getBigDecimal("rate")));
        }
        context.result(String.valueOf(currencyRateList));
    }
    public void getSpecificCurrency(Context context) throws SQLException {
        String param = context.pathParam("currency");
        PreparedStatement pr = connection.prepareStatement("SELECT * FROM exchange_rates WHERE CURRENCY LIKE '" + param + "' ");
        ResultSet rs = pr.executeQuery();
        List<CurrencyRate> currencyRateList = new ArrayList<>();
        while (rs.next()) {
            currencyRateList.add(new CurrencyRate(rs.getDate("date").toLocalDate(), rs.getString("currency"), rs.getBigDecimal("rate")));
        }
        context.result(String.valueOf(currencyRateList));
    }
    public void addCurrenciesToList() throws IOException, FeedException {
        URL feedSource = new URL("https://www.bank.lv/vk/ecb_rss.xml");
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feeds = input.build(new XmlReader(feedSource));

        for(Object o : feeds.getEntries()) {
            SyndEntry entry = (SyndEntry) o;
            ArrayList<String> description = new ArrayList<>(List.of(entry.getDescription().getValue().split(" ")));

            for (int j = 0; j < description.size(); j += 2) {
                String parsed1 = String.valueOf(entry.getPublishedDate()).substring(8, 10);
                String parsed2 = String.valueOf(entry.getPublishedDate()).substring(4, 7);
                String parsed3 = parsed1 + "-" + parsed2 + "-" + String.valueOf(entry.getPublishedDate()).substring(24, 28);
                this.currencies.add(new CurrencyRate(LocalDate.parse(parsed3, formatter), description.get(j), new BigDecimal(description.get(j + 1))));
            }
        }
    }
}
