package com.marekhudyma.style;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.h2.tools.Server;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;
import org.testcontainers.containers.PostgreSQLContainer;

public class H2PerformanceComparisonTest {

  @Test
  void startTestContainers() throws SQLException {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:13.1")
        .withUsername("marek")
        .withPassword("password")
        .withDatabaseName("application-style");

    postgreSQLContainer.start();

    Connection conn = DriverManager.getConnection(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT 1 AS A");

    rs.next();
    int value = rs.getInt("A");
    rs.close();
    stmt.close();
    conn.close();

    stopWatch.stop();
    System.out.println(stopWatch.getTotalTimeMillis()); // 3719 ms
    System.out.println(value);
  }

  @Test
  void startH2Database() throws SQLException, ClassNotFoundException {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    Server server = Server.createTcpServer().start();

    Class.forName ("org.h2.Driver");
    Connection conn = DriverManager.getConnection("jdbc:h2:~/test", "sa","");
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT 1 AS A");

    rs.next();
    int value = rs.getInt("A");
    rs.close();
    stmt.close();
    conn.close();

    stopWatch.stop();
    System.out.println(stopWatch.getTotalTimeMillis()); // 407 ms
    System.out.println(value);
  }
}
