package bruce.home;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.*;

@SpringBootTest
class SpringBootAndSpringBatchPracticeApplicationTests {
    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Test
    void testDb4free() throws ClassNotFoundException, SQLException {
        // https://www.db4free.net/phpMyAdmin/
        Class.forName(driver);

        try (
                Connection conn = DriverManager.getConnection(url, username, password);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM my_table1");
        ) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                System.out.println("id=" + id);
                System.out.println("name=" + name);
                System.out.println("price=" + price);
                System.out.println("===========================");
            }
        }
    }
}
