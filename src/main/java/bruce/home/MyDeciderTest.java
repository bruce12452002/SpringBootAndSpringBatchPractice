package bruce.home;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

//@Configuration
public class MyDeciderTest {
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Resource
    private MyDecider myDecider;

//    @Bean
    public Job mdt() {
        /**
         * 一開始執行 step1, 然後執行執行 Decider 去做判斷
         * 因為 count 加 1 後為奇數，會再執行 step1
         * 然後 step1 執行後，不管結果如何會再回到 Decider
         * count 再加 1 後為偶數，這時執行 step2 後結束
         */
        JobBuilder jobBuilder = jobBuilderFactory.get("MyDeciderTest");
        return jobBuilder.start(step1())
                .next(myDecider)
                .from(myDecider).on("odd").to(step1())
                .from(myDecider).on("even").to(step2())
                .from(step1()).on("*").to(myDecider)
                .end().build();
    }

    private Step step1() {
        return stepBuilderFactory.get("s1").tasklet((stepContribution, chunkContext) -> {
            System.out.println("MyDeciderTest 第一步 one");
            return RepeatStatus.FINISHED;
        }).build();
    }

    private Step step2() {
        return stepBuilderFactory.get("s2").tasklet((stepContribution, chunkContext) -> {
            System.out.println("MyDeciderTest 第二步 two");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Component
    class MyDecider implements JobExecutionDecider {
        private int count;

        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            count++;
            if ((count & 1) == 0) {
                return new FlowExecutionStatus("even");
            } else {
                return new FlowExecutionStatus("odd");
            }
        }
    }
}

