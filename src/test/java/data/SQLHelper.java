package data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private SQLHelper() {}

    private static Connection getConn() throws SQLException {
        return DriverManager.getConnection(
            System.getProperty("db.url"),
            "app",
            "pass");
    }

    @SneakyThrows
    public static String getStatusPayment() {
        var codeSQL = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1;";
        var connection = getConn();
        return QUERY_RUNNER.query(connection, codeSQL, new ScalarHandler<>());
    }

    @SneakyThrows
    public static String getStatusCreditPayment() {
        var codeSQL = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1;";
        var connection = getConn();
        return QUERY_RUNNER.query(connection, codeSQL, new ScalarHandler<>());
    }

    @SneakyThrows
    public static String getPaymentTransaction() {
        var codeSQL = "SELECT transaction_id FROM payment_entity ORDER BY created DESC LIMIT 1;";
        var connection = getConn();
        return QUERY_RUNNER.query(connection, codeSQL, new ScalarHandler<>());
    }

    @SneakyThrows
    public static String getBankId() {
        var codeSQL = "SELECT bank_id FROM credit_request_entity ORDER BY created DESC LIMIT 1;";
        var connection = getConn();
        return QUERY_RUNNER.query(connection, codeSQL, new ScalarHandler<>());
    }

    @SneakyThrows
    public static String getPaymentId() {
        var codeSQL = "SELECT payment_id FROM order_entity ORDER BY created DESC LIMIT 1;";
        var connection = getConn();
        return QUERY_RUNNER.query(connection, codeSQL, new ScalarHandler<>());
    }

    @SneakyThrows
    public static String getCreditId() {
        var codeSQL = "SELECT credit_id FROM order_entity ORDER BY created DESC LIMIT 1;";
        var connection = getConn();
        return QUERY_RUNNER.query(connection, codeSQL, new ScalarHandler<>());
    }
}
