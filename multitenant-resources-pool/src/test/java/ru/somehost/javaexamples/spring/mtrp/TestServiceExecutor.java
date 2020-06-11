package ru.somehost.javaexamples.spring.mtrp;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.somehost.javaexamples.spring.mtrp.services.ServiceExecutor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestServiceExecutor {

    @Autowired
    private ServiceExecutor serviceExecutor;

    @Test
    public void testOne() throws InterruptedException {
        Assert.assertTrue(serviceExecutor.execute("token0"));
    }

    private final int nThreads = 1000;
    private final CountDownLatch endGate = new CountDownLatch(nThreads);

    private final AtomicLong checkCountRunned = new AtomicLong();


    private Callable createCallable(String tokenName){
        return () -> {
            System.out.println(String.format("Start for %s", tokenName));
            try {
                return serviceExecutor.execute(tokenName);
            } finally {
                checkCountRunned.incrementAndGet();
                endGate.countDown();
            }
        };
    }

    @Test
    public void testPool() throws InterruptedException {
        Callable[] callables = new Callable[]{
                createCallable("token1"),
                createCallable("token2"),
                createCallable("token3"),
                createCallable("token4"),
                createCallable("token5"),
                createCallable("token6"),
                createCallable("token7"),
                createCallable("token8")
        };

        List<FutureTask> taskList = new LinkedList<>();
        ExecutorService executorService = (ExecutorService) Executors.newCachedThreadPool();

        for (int item : new int[nThreads]) {
            for (Callable callable : Arrays.asList(callables)) {
                FutureTask futureTask = new FutureTask(callable);
                taskList.add(futureTask);
                executorService.submit(futureTask);
            }
        }

        endGate.await();

        taskList.stream().forEach(p -> {
            try {
                Assert.assertTrue( ((Boolean)p.get()) );
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        System.out.println(String.format("all started %s", checkCountRunned));
        executorService.shutdown();
    }

}
