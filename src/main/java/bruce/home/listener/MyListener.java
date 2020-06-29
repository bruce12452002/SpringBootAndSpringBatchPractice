package bruce.home.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

//@Component
public class MyListener {
    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("anno step before=" + stepExecution.getStepName());
    }

    @AfterStep
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("anno step before=" + stepExecution.getStepName());
        return null;
    }

    @BeforeChunk
    public void beforeChunk(ChunkContext ctx) {
        System.out.println("anno chunk before=" + ctx.getStepContext().getStepName());
    }

    @AfterChunk
    public void afterChunk(ChunkContext ctx) {
        System.out.println("anno chunk after=" + ctx.getStepContext().getStepName());
    }
}
