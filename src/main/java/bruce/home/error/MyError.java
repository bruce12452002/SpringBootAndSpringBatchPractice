package bruce.home.error;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Map;

//@Configuration
public class MyError {
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

//    @Bean
    public Job me() {
        // 總共要三次 Job 才會完成
        return jobBuilderFactory.get("MyError job")
                .start(step1())
                .next(step2())
                .build();
    }

    private Step step1() {
        return stepBuilderFactory.get("MyError step1").tasklet(getTasklet()).build();
    }

    private Step step2() {
        return stepBuilderFactory.get("MyError step2").tasklet(getTasklet()).build();
    }

    private Tasklet getTasklet() {
        return (StepContribution contribution, ChunkContext chunkContext) -> {
            StepContext stepContext = chunkContext.getStepContext();
            Map<String, Object> ctx = stepContext.getStepExecutionContext();

            if (ctx.containsKey("xxx")) {
                System.out.println("second time...");
                return RepeatStatus.FINISHED;
            } else {
                System.out.println("first time...");
                // stepContext.getStepExecutionContext().put("xxx", true); 這行取得的是不可變的 Map
                stepContext.getStepExecution().getExecutionContext().put("xxx", true);
                throw new RuntimeException("I'm wrong...");
            }
        };
    }
}
