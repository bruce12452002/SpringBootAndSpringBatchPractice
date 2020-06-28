package bruce.home;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.FlowJobBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

//@Configuration
public class FlowTest {
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

//    @Bean
    public Job jc3() {
        JobBuilder jobBuilder = jobBuilderFactory.get("FlowTest");
        FlowJobBuilder flowJobBuilder = jobBuilder.start(getFlow()).next(step3()).end();
        return flowJobBuilder.build();
    }

    private Step step1() {
        return stepBuilderFactory.get("s1").tasklet((stepContribution, chunkContext) -> {
            System.out.println("FlowTest 第一步 one");
            return RepeatStatus.FINISHED;
        }).build();
    }

    private Step step2() {
        return stepBuilderFactory.get("s2").tasklet((stepContribution, chunkContext) -> {
            System.out.println("FlowTest 第二步 two");
            return RepeatStatus.FINISHED;
        }).build();
    }

    private Step step3() {
        return stepBuilderFactory.get("s3").tasklet((stepContribution, chunkContext) -> {
            System.out.println("FlowTest 第三步 three");
            return RepeatStatus.FINISHED;
        }).build();
    }

    // Flow 為 Step 的集合
    private Flow getFlow() {
        FlowBuilder<Flow> flowFlowBuilder = new FlowBuilder<>("myFlow");
        FlowBuilder<Flow> builder = flowFlowBuilder.start(step1()).next(step3());
        return builder.build();
    }
}
