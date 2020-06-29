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
public class ChildJob2 {
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

//    @Bean
    public Job cj2() {
        return jobBuilderFactory.get("ChildJob2")
                .start(stepChild2()).build();
    }

    private Step stepChild2() {
        return stepBuilderFactory.get("s_two").tasklet((stepContribution, chunkContext) -> {
            System.out.println("ChildJob2");
            return RepeatStatus.FINISHED;
        }).build();
    }
}
