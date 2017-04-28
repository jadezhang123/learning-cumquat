package springzk;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Zhang Junwei on 2017/4/28 0028.
 */
public class SpringZkTester {
    @Test
    public void testZKConfig() throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        TestDomain bean = ctx.getBean(TestDomain.class);
        Assert.assertEquals("the name of testDomain should be 123", "123", bean.getName());

    }
}
