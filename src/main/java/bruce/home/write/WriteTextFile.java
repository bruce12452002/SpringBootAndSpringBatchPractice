package bruce.home.write;

import bruce.home.entity.MyTable1;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import javax.annotation.Resource;
import java.util.Objects;

//@Configuration
public class WriteTextFile {
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

//    @Bean
    public Job rtf() {
        return jobBuilderFactory.get("job WriteTextFile")
                .start(step())
                .build();
    }

    private Step step() {
        return stepBuilderFactory.get("step WriteTextFile")
                .<MyTable1, MyTable1>chunk(5)
                .reader(reader())
                .writer(writer())
                .build();
    }

    private ItemWriter<MyTable1> writer() {
        FlatFileItemWriter<MyTable1> write = new FlatFileItemWriter<>();
        write.setResource(new FileSystemResource("D:/xxx.txt"));
        write.setLineAggregator((MyTable1 item) -> {
                    ObjectMapper mapper = new ObjectMapper(); // 輸出格式為 JSON
                    String rtn = null;
                    try {
                        rtn = mapper.writeValueAsString(item);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return Objects.requireNonNull(rtn);
                }
        );

        try {
            write.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("finish~~~~");
        return write;
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
