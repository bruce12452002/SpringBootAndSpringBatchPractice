package bruce.home.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class LauncherTest {
    @Resource
    private JobLauncher launcher;

    @Resource
    private Job job;

    @GetMapping("/xxx/{param}")
    public String begin(@PathVariable String param) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        System.out.println("pppppppppppppppp=" + param);
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("qoo", param)
                .toJobParameters();
        launcher.run(job, jobParameters);
        return "SUCCESS";
    }
}
