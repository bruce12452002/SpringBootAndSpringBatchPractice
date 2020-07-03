package bruce.home.write;

import bruce.home.entity.MyTable1;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Resource;
import javax.sql.DataSource;

//@Configuration
public class WriteDatabase {
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Resource
    private DataSource dataSource;

//    @Bean
    public Job wdb() {
        return jobBuilderFactory.get("WriteDatabase job")
                .start(step())
                .build();
    }

    private Step step() {
        return stepBuilderFactory.get("WriteDatabase step")
                .<MyTable1, MyTable1>chunk(2)
                .reader(reader())
                .writer(writer())
                .build();
    }


    private ItemWriter<MyTable1> writer() {
        JdbcBatchItemWriter<MyTable1> write = new JdbcBatchItemWriter<>();
        write.setDataSource(dataSource);
        write.setSql("insert into my_table1(id, name, price) values(:id, :name, :price)");
        write.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>()); // 將上行的 :名稱 對應 MyTable1
        write.afterPropertiesSet();
        return write;
    }

    private ItemReader<MyTable1> reader() {
        FlatFileItemReader<MyTable1> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("twelveAnimal2.txt"));
        reader.setLinesToSkip(1);

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("id", "name", "price");

        DefaultLineMapper<MyTable1> mapper = new DefaultLineMapper<>();
        mapper.setLineTokenizer(tokenizer);
        mapper.setFieldSetMapper(fieldSet -> {
            MyTable1 t = new MyTable1();
            t.setId(fieldSet.readInt("id"));
            t.setName(fieldSet.readString("name"));
            t.setPrice(fieldSet.readInt("price"));
            return t;
        });

        mapper.afterPropertiesSet();
        reader.setLineMapper(mapper);
        return reader;
    }
}
