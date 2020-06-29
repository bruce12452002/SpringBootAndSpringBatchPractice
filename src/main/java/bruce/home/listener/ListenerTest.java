package bruce.home.listener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;

//@Configuration
public class ListenerTest {
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Resource
    private MyJobListener myJobListener;

    @Resource
    private MyStepListener myStepListener;

    @Resource
    private MyListener myListener;

//    @Bean
    public Job listenert() {
        return jobBuilderFactory.get("listenerTest")
                .start(step())
                .listener(myJobListener)
                .build();
    }

    private Step step() {
        // 將英文字轉成 ASCII，會使用到 chunk 的 ItemReader、ItemProcessor、ItemWriter
        /**
         * chunk listener 會在 chunk 前後執行；step 也一樣，有多個 listener，都會執行，誰寫在前面誰先執行
         */
        return stepBuilderFactory.get("stepListenTest")
                .<String, Integer>chunk(2)// 每讀完 2 筆資料再處理，讀什麼由 reader 決定
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .faultTolerant()
                .listener(myListener)
                .listener(myStepListener)
                .build();
    }

    private ItemProcessor<? super String, ? extends Integer> processor() {
        return new ItemProcessor<String, Integer>() {
            @Override
            public Integer process(String item) throws Exception {
                return item.codePointAt(0);
            }
        };
    }

    private ItemWriter<Integer> writer() {
        return new ItemWriter<Integer>() {
            @Override
            public void write(List<? extends Integer> items) throws Exception {
                items.forEach(System.out::println);
            }
        };
    }

    private ItemReader<String> reader() {
        return new ListItemReader<>(List.of("A", "B", "C"));
    }

}
