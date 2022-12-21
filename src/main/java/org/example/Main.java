package org.example;

import com.sun.syndication.io.FeedException;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class Main {
    final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SQLException, ParseException, IOException, FeedException {
        DatabaseAndSql databaseConnection = new DatabaseAndSql();

        if (args[0].equals("insertdata")) {
            databaseConnection.createTable();
            databaseConnection.insertData();
        }
        if (args[0].equals("endpoints")) {
            Javalin app = Javalin.create().start(8080);
            app.get("/", databaseConnection::getAllCurrencies);
            app.get("/{currency}", databaseConnection::getSpecificCurrency);
            logger.info("endpoints have been created");
        }
    }
}