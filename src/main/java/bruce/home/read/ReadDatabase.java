package bruce.home.read;

import bruce.home.entity.MyTable1;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Configuration
@Import({MySqlPagingQueryProvider.class})
public class ReadDatabase {
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Resource
    private MyTable1 myTable1;

    @Resource
    private DataSource dataSource;

    @Resource
    private MySqlPagingQueryProvider provider;

    //    @Bean
    public Job datat() {
        return jobBuilderFactory.get("Datasource job Test")
                .start(step())
                .build();
    }

    private Step step() {
        return stepBuilderFactory.get("Datasource step Test")
                .<MyTable1, MyTable1>chunk(2)
                .reader(reader())
                .writer(writer())
                .build();
    }


    private ItemWriter<MyTable1> writer() {
        return (List<? extends MyTable1> items) -> items.forEach(m -> System.out.println(m.getName()));
    }

    // @JobScope
    @StepScope
    @Bean
    public JdbcPagingItemReader<MyTable1> reader() {
        /**
         *  1.JdbcPagingItemReader 不可用 @Import 去抓，dataSource 會抓不到
         *  2.必需使用 @StepScope 或 @JobScope，這樣才能設定檔才能給這個範圍共享，然後還要配合 @Bean
         *  3.用 @Bean 就不能是 private
         */
        JdbcPagingItemReader<MyTable1> jdbcPagingItemReader = new JdbcPagingItemReader<>();
        jdbcPagingItemReader.setDataSource(dataSource);
        jdbcPagingItemReader.setFetchSize(2);
        jdbcPagingItemReader.setRowMapper((ResultSet rs, int rowNum) -> {
                    MyTable1 mt = new MyTable1();
                    mt.setId(rs.getInt(1));
                    mt.setName(rs.getString(2));
                    mt.setPrice(rs.getInt(3));
                    return mt;
                }
        );

        Map<String, Order> order = new HashMap<>(1);
        order.put("name", Order.DESCENDING);

        provider.setSortKeys(order);
        provider.setSelectClause("id, name, price");
        provider.setFromClause("from my_table1");

        jdbcPagingItemReader.setQueryProvider(provider);
        return jdbcPagingItemReader;
    }
}
