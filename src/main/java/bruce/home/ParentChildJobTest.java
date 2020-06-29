package bruce.home;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;

@Configuration
public class ParentChildJobTest {
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

    //    @Resource(name = "cj1")
    @Qualifier("cj1")
    @Autowired
    private Job stepChild1;

    //    @Resource(name = "cj2")
    @Qualifier("cj2")
    @Autowired
    private Job stepChild2;

    @Resource
    private JobLauncher jobLauncher;

    @Bean
    public Job pct(JobRepository repository, PlatformTransactionManager transactionManager) {
        return jobBuilderFactory.get("ParentChildJobTest")
                .start(useChild1(repository, transactionManager))
                .next(useChild2(repository, transactionManager))
                .build();
    }

    @Bean
    public Step useChild1(JobRepository repository, PlatformTransactionManager transactionManager) {
        return new JobStepBuilder(new StepBuilder("c1"))
                .job(stepChild1)
                .launcher(jobLauncher)
                .repository(repository)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public Step useChild2(JobRepository repository, PlatformTransactionManager transactionManager) {
        return new JobStepBuilder(new StepBuilder("c2"))
                .job(stepChild2)
                .launcher(jobLauncher)
                .repository(repository)
                .transactionManager(transactionManager)
                .build();
    }
}

