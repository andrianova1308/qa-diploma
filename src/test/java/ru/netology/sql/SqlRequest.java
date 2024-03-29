package ru.netology.sql;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqlRequest {

    private static final String DB_URL = System.getProperty("url");
    private static final String USER = System.getProperty("db.username");
    private static final String PASS = System.getProperty("db.password");

    private SqlRequest() {
    }

    @SneakyThrows
    public static Connection getConnection() {
         return DriverManager.getConnection(DB_URL, USER, PASS);
        //   String url = "jdbc:mysql://localhost/app";
        //   String username = "app";
        //   String password = "pass";
        //    return DriverManager.getConnection(url, username, password);
    }
    @SneakyThrows
    public static void clearDB() {
        QueryRunner runner = new QueryRunner();
        var connection = getConnection();
            runner.execute(connection, "DELETE FROM credit_request_entity");
            runner.execute(connection, "DELETE FROM order_entity");
            runner.execute(connection, "DELETE FROM payment_entity");

    }

    @SneakyThrows
    public static String getDebitPaymentStatus() {
        QueryRunner runner = new QueryRunner();
        String sqlStatus = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        var connection = getConnection();
            String result = runner.query(connection, sqlStatus, new ScalarHandler<>());
            return result;

    }

    @SneakyThrows
    public static String getCreditPaymentStatus() {
        QueryRunner runner = new QueryRunner();
        String sqlStatus = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        var connection = getConnection();
            String result = runner.query(connection, sqlStatus, new ScalarHandler<>());
            return result;

    }
}
