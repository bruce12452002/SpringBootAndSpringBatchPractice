package bruce.home;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Map;

//@Configuration
public class ParameterTest implements StepExecutionListener {
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

    private Map<String, JobParameter> map;

//    @Bean
    public Job paramTest() {
        return jobBuilderFactory.get("parameterTest")
                .start(step())
                .build();
    }

    private Step step() {
        return stepBuilderFactory.get("step param test")
                .listener(this)
                .tasklet((contribution, chunkContext) -> {
                    /**
                     *  xxx 可在 Run configuration 的參數列表設定，如 xxx=abc，會存到 BATCH_JOB_EXECUTION_PARAMS 表裡
                     *  ooo 是寫在程式裡的，不會儲存到資料庫
                     */
                    System.out.println("xxx=" + map.get("xxx"));
                    System.out.println("ooo=" + map.get("ooo"));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }


    @Override
    public void beforeStep(StepExecution stepExecution) {
        map = stepExecution.getJobParameters().getParameters();
        map.put("ooo", new JobParameter(123L));
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
