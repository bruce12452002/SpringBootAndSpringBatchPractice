package bruce.home.read;

import bruce.home.entity.MyTable1;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Resource;
import java.util.List;

//@Configuration
public class ReadTextFile {
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

    //    @Bean
    public Job rf() {
        return jobBuilderFactory.get("job ReadFile")
                .start(step())
                .build();
    }

    private Step step() {
        return stepBuilderFactory.get("step ReadFile")
                .<MyTable1, MyTable1>chunk(5)
                .reader(reader())
                .writer(writer())

                /**
                 *  以 下為容錯的重試功能，chunk 才有
                 *  必需 faultTolerant，才有 retry 可用，
                 *  遇到什麼錯，重試幾次(reader writer processor)
                 */
                // .faultTolerant().retry(Exception.class).retryLimit(5)

                /**
                 *  以 下為容錯的跳過功能，chunk 才有
                 *  必需 faultTolerant，才有 skip 可用，
                 *  遇到什麼錯，略過幾次(reader writer processor)
                 *  listener 要實現 SkipListener，如果想對跳過的資料做記錄可以用
                 */
                // .faultTolerant().skip(Exception.class).skipLimit(5).listener()
                .build();
    }

    private ItemWriter<MyTable1> writer() {
        return (List<? extends MyTable1> items) -> {
            items.forEach(t -> {
                System.out.println("id=" + t.getId());
                System.out.println("name=" + t.getName());
                System.out.println("price=" + t.getPrice());
                System.out.println("------------------------");
            });
        };
    }

    private ItemReader<MyTable1> reader() {
        FlatFileItemReader<MyTable1> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("twelveAnimal1.txt"));
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
