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
        DatabaseService databaseService = new DatabaseService();

        if (args[0].equals("insertdata")) {
            databaseService.createTable();
            databaseService.insertData();
        }
        if (args[0].equals("endpoints")) {
            Javalin app = Javalin.create().start(8080);
            app.get("/", databaseService::getAllCurrencies);
            app.get("/{currency}", databaseService::getSpecificCurrency);
            logger.info("endpoints have been created");
        }
    }
}