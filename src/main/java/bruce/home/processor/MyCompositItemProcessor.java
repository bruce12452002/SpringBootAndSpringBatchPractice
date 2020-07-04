package bruce.home.processor;

import bruce.home.entity.MyTable1;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Resource;
import java.util.List;

//@Configuration
public class MyCompositItemProcessor {
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

//    @Bean
    public Job rf() {
        return jobBuilderFactory.get("job processor")
                .start(step())
                .build();
    }

    private Step step() {
        // 多個 processor() 只有最後一個會執行
        return stepBuilderFactory.get("step processor")
                .<MyTable1, MyTable1>chunk(5)
                .reader(reader())
                .processor(processor())
//                .processor(proA())
//                .processor(proB())
                .writer(writer())
                .build();
    }

    private ItemProcessor<MyTable1, MyTable1> processor() {
        CompositeItemProcessor<MyTable1, MyTable1> process = new CompositeItemProcessor<>();
        process.setDelegates(List.of(proA(), proB()));
        return process;
    }

    private ItemProcessor<MyTable1, MyTable1> proA() {
        return (MyTable1 item) -> {
            item.setName(item.getName().toUpperCase());
            return item;
        };
    }

    private ItemProcessor<MyTable1, MyTable1> proB() {
        // 只抓偶數的
        return (MyTable1 item) -> ((item.getId() & 1) == 0) ? item : null;
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
