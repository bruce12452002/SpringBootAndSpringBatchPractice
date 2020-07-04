package bruce.home;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

// 一個工作(Job)有很多個步驟(Step)

/**
 * 一個 Job 只能執行一次，BATCH_JOB_EXECUTION.EXIT_CODE，
 * 如果是 COMPLETED 為完成
 * NOOP 為已執行過或沒有 Step 設定
 * UNKNOWN 為執行中
 */
@Configuration
public class Hello {
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job helloSpringBatch() {
        JobBuilder jobBuilder = jobBuilderFactory.get("helloBatch");
        SimpleJobBuilder simpleJobBuilder = jobBuilder.start(step1()).next(step2());
        return simpleJobBuilder.build();
    }

    private Step step1() {
        StepBuilder stepBuilder = stepBuilderFactory.get("s1");
        // StepBuilder 可以用 tasklet 和 chunk
        TaskletStepBuilder taskletStepBuilder = stepBuilder.tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("第一步 one");
                return RepeatStatus.FINISHED;
            }
        });
        return taskletStepBuilder.build();
    }

    private Step step2() {
        StepBuilder stepBuilder = stepBuilderFactory.get("s2");
        TaskletStepBuilder taskletStepBuilder = stepBuilder.tasklet((stepContribution, chunkContext) -> {
            System.out.println("第二步 two");
            return RepeatStatus.FINISHED;
        });
        return taskletStepBuilder.build();
    }
}
