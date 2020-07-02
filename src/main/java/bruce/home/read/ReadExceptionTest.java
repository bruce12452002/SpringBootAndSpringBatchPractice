package bruce.home.read;

import bruce.home.entity.MyTable1;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;

//@Configuration
public class ReadExceptionTest {
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Resource
    private ItemReader<MyTable1> reader;

//    @Bean
    public Job re() {
        return jobBuilderFactory.get("job ReadException7")
                .start(step())
                .build();
    }

    private Step step() {
        return stepBuilderFactory.get("step ReadException")
                .<MyTable1, MyTable1>chunk(2)
                .reader(reader)
                .writer(writer())
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
}
