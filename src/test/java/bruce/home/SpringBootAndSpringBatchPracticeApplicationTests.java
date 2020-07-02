package bruce.home;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;

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

    @javax.annotation.Resource
    private DataSource dataSource;

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

    @Test
    void testResources() throws IOException {
        ResourcePatternResolver loader = new PathMatchingResourcePatternResolver();
        Resource[] resources = loader.getResources("classpath*:/twelveAnimal*.txt");

        Arrays.stream(resources).forEach(r -> {
            try {
                System.out.println("file=" + r.getURL().getFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    void testResources2() {
        Resource[] re = new Resource[]{
                new ClassPathResource("twelveAnimal1.txt"),
                new ClassPathResource("twelveAnimal2.txt")
        };

        Arrays.stream(re).forEach(r -> {
            try {
                System.out.println("file=" + r.getURL().getFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    void testAssert() {
        Assert.notNull(null, "我錯了");
    }

    @Test
    void testDataSource() {
        System.out.println("xxxxxxxxxxxxxxxxxxx=" + dataSource);
    }
}
