package bruce.home.read;

import bruce.home.entity.MyTable1;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

//@Configuration
public class ReadXMLFile {
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

//    @Bean
    public Job rf() {
        return jobBuilderFactory.get("job ReadXMLFile")
                .start(step())
                .build();
    }

    private Step step() {
        return stepBuilderFactory.get("step ReadXMLFile")
                .<MyTable1, MyTable1>chunk(5)
                .reader(reader())
                .writer(writer())
                .build();
    }

    private ItemWriter<MyTable1> writer() {
        return (List<? extends MyTable1> items) -> {
            items.forEach(t -> {
                System.out.println("id=" + t.getId());
                System.out.println("name=" + t.getName());
                System.out.println("price=" + t.getPrice());
                System.out.println("------------------------");
            });
        };
    }

    private ItemReader<MyTable1> reader() {
        StaxEventItemReader<MyTable1> reader = new StaxEventItemReader<>();
        reader.setResource(new ClassPathResource("twelveAnimal.xml"));
        reader.setFragmentRootElementName("animal"); // root 的下層

        HashMap<String, Class<MyTable1>> map = new HashMap<>();
        map.put("animal", MyTable1.class); // root 的下層

        XStreamMarshaller stream = new XStreamMarshaller();
        stream.setAliases(map);
        reader.setUnmarshaller(stream);
        try {
            reader.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reader;
    }
}
