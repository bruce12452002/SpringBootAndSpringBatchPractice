package bruce.home.read;

import bruce.home.entity.MyTable1;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Resource;
import java.util.List;

//@Configuration
public class ReadMultiTextFile {
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Value("classpath*:/twelveAnimal*.txt")
    private org.springframework.core.io.Resource[] fileResources;

//    @Bean
    public Job rtf() {
        return jobBuilderFactory.get("job ReadMultiTextFile")
                .start(step())
                .build();
    }

    private Step step() {
        return stepBuilderFactory.get("step ReadMultiTextFile")
                .<MyTable1, MyTable1>chunk(5)
                .reader(readMultiFile())
                .writer(writer())
                .build();
    }

    private ItemReader<? extends MyTable1> readMultiFile() {
        MultiResourceItemReader<MyTable1> multiReader = new MultiResourceItemReader<>();
        multiReader.setDelegate(reader());
        multiReader.setResources(fileResources);
        return multiReader;
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

    private FlatFileItemReader<? extends MyTable1> reader() {
        FlatFileItemReader<MyTable1> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1); // 每個檔的第一行都會跳過

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
