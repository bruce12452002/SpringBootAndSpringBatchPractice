package bruce.home;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowJobBuilder;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

//@Configuration
public class JobAnotherStart {
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

//    @Bean
    public Job jas() {
        FlowJobBuilder flowJobBuilder = jobBuilderFactory.get("JobAnotherStart")
                .start(step1()).on(FlowExecutionStatus.COMPLETED.toString()).to(step2())
                .from(step2()).on(FlowExecutionStatus.COMPLETED.toString()).to(step3()) // to 改 fail() 不會執行下一個 step；stopAndRestart(Step or Flow) 重啟動
                .from(step3()).end();
        return flowJobBuilder.build();
    }

    private Step step1() {
        return stepBuilderFactory.get("s1").tasklet((stepContribution, chunkContext) -> {
            System.out.println("JobAnotherStart 第一步 one");
            return RepeatStatus.FINISHED;
        }).build();
    }

    private Step step2() {
        return stepBuilderFactory.get("s2").tasklet((stepContribution, chunkContext) -> {
            System.out.println("JobAnotherStart 第二步 two");
            return RepeatStatus.FINISHED;
        }).build();
    }

    private Step step3() {
        return stepBuilderFactory.get("s3").tasklet((stepContribution, chunkContext) -> {
            System.out.println("JobAnotherStart 第三步 three");
            return RepeatStatus.FINISHED;
        }).build();
    }
}
