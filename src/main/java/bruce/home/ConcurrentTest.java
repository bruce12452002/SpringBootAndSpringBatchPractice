package bruce.home;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.annotation.Resource;

@Configuration
public class ConcurrentTest {
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jc3() {
        JobBuilder jobBuilder = jobBuilderFactory.get("ConcurrentTest");
        return jobBuilder.start(getFlow1())
                .split(new SimpleAsyncTaskExecutor())// split 可以將 getFlow1() 和 getFlow2() 分別併發執行
                .add(getFlow2())
                .end().build();
    }

    private Step step1() {
        return stepBuilderFactory.get("s1").tasklet((stepContribution, chunkContext) -> {
            System.out.println("ConcurrentTest 第一步 one");
            return RepeatStatus.FINISHED;
        }).build();
    }

    private Step step2() {
        return stepBuilderFactory.get("s2").tasklet((stepContribution, chunkContext) -> {
            System.out.println("ConcurrentTest 第二步 two");
            return RepeatStatus.FINISHED;
        }).build();
    }

    private Step step3() {
        return stepBuilderFactory.get("s3").tasklet((stepContribution, chunkContext) -> {
            System.out.println("ConcurrentTest 第三步 three");
            return RepeatStatus.FINISHED;
        }).build();
    }

    private Step step4() {
        return stepBuilderFactory.get("s4").tasklet((stepContribution, chunkContext) -> {
            System.out.println("ConcurrentTest 第四步 four");
            return RepeatStatus.FINISHED;
        }).build();
    }

    private Flow getFlow1() {
        FlowBuilder<Flow> flowFlowBuilder = new FlowBuilder<>("myFlow1");
        return flowFlowBuilder.start(step1()).next(step2()).build();
    }

    private Flow getFlow2() {
        FlowBuilder<Flow> flowFlowBuilder = new FlowBuilder<>("myFlow2");
        return flowFlowBuilder.start(step3()).next(step4()).build();
    }
}
