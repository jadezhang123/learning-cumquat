package own.jadezhang.learning.cumquat.test.springzk;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Zhang Junwei on 2017/4/28 0028.
 */
public class SpringZkTester {

    public static void testZKConfig() throws Exception {

        ExecutorService service = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            service.execute(new Runnable() {
                @Override
                public void run() {
                    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
                    TestDomain bean = ctx.getBean(TestDomain.class);
                    System.out.println(" the name of testDomain is  " + bean.getName());
                    System.out.println(" the quartzDB of testDomain is  " + bean.getQuartzDB());
                }
            });
        }
        service.shutdown();

    }

    public static void test() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        TestDomain bean = ctx.getBean(TestDomain.class);
        System.out.println(" the name of testDomain is  " + bean.getName());
        System.out.println(" the quartzDB of testDomain is  " + bean.getQuartzDB());
    }

    public static void main(String[] args) throws Exception {
        //SpringZkTester.testZKConfig();
        SpringZkTester.test();
    }
}
