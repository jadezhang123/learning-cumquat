package springzk;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Zhang Junwei on 2017/4/28 0028.
 */
public class SpringZkTester {
    @Test
    public void testZKConfig() throws Exception {

        ExecutorService service = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            service.execute(new Runnable() {
                @Override
                public void run() {
                    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
                    TestDomain bean = ctx.getBean(TestDomain.class);
                    Assert.assertEquals("the name of testDomain should be 123", "123", bean.getName());
                }
            });
        }
        service.shutdown();

    }
}
