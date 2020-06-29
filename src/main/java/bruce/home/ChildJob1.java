package bruce.home;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

//@Configuration
public class ChildJob1 {
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

//    @Bean
    public Job cj1() {
        return jobBuilderFactory.get("ChildJob1")
                .start(stepChild1()).build();
    }

    private Step stepChild1() {
        return stepBuilderFactory.get("s_one").tasklet((stepContribution, chunkContext) -> {
            System.out.println("ChildJob1");
            return RepeatStatus.FINISHED;
        }).build();
    }
}
