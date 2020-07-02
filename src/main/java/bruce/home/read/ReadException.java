package bruce.home.read;

import bruce.home.entity.MyTable1;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

// 如果上次沒有執行完，會從上次的進度繼續
//@Component
public class ReadException implements ItemStreamReader<MyTable1> {
    private boolean restart = false;
    private int lineCount = 0;
    private FlatFileItemReader<MyTable1> reader = new FlatFileItemReader<>();
    private ExecutionContext executionContext;

    public ReadException() {
        reader.setResource(new ClassPathResource("twelveAnimal1.txt"));
        reader.setLinesToSkip(1);

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("id", "name", "price");

        DefaultLineMapper<MyTable1> mapper = new DefaultLineMapper<>();
        mapper.setLineTokenizer(tokenizer);
        mapper.setFieldSetMapper(fieldSet -> {
            MyTable1 t = new MyTable1();
            t.setId(fieldSet.readInt("id"));
            t.setName(fieldSet.readString("name"));
            t.setPrice(fieldSet.readInt("price"));
            return t;
        });

        mapper.afterPropertiesSet();
        reader.setLineMapper(mapper);
    }

    @Override
    public MyTable1 read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        this.lineCount++;

        if (restart) {
            reader.setLinesToSkip(lineCount - 1);
            restart = false;
            System.out.println("curr line=" + this.lineCount);
        }

        reader.open(this.executionContext);
        MyTable1 t = reader.read();

        if (t != null) {
            if (t.getName().equals("dragon")) {
                throw new RuntimeException("name is limited, name=" + t.getName() + ",id=" + t.getId());
            }
        } else {
            this.lineCount--;
        }
        return t;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        System.out.println("open...");
        this.executionContext = executionContext;

        if (executionContext.containsKey("xxx")) {
            this.lineCount = executionContext.getInt("xxx");
            this.restart = true;
        } else {
            this.lineCount = 0;
            executionContext.put("xxx", this.lineCount);
            System.out.println("open lineCount=" + (this.lineCount + 1));
        }
    }

    // 會根據 chunk() 的數字，整批處理到資料庫。 如果發生例外不會執行
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        System.out.println("update...");
        executionContext.put("xxx", this.lineCount);
    }

    // 每個 step 完，不管有沒有發生例外都會調用
    @Override
    public void close() throws ItemStreamException {
        System.out.println("close...");
    }
}
