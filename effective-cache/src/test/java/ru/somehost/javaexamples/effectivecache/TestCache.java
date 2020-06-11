package ru.somehost.javaexamples.effectivecache;


import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicLong;

public class TestCache {

    private final Computable<String, String> c = TestCacheHelper.createComputable(3000);

    private static final CyclicBarrier BARRIER = TestCacheHelper.createCyclicBarrier(100);

    private final AtomicLong counter = new AtomicLong();

    private final Computable<String, String> cache = new TestMemoizer<String, String>(c, counter);

    final int nThreads = 100;

    @Test
    public void test1() {
        try {
            String arg = "23";
            TestCacheHelper.reverseString(arg).equals(cache.compute(arg));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Callable createCallable(String arg) {
        return () -> {
            BARRIER.await();
            return TestCacheHelper.reverseString(arg).equals(cache.compute(arg));
        };
    }

    @Test
    public void testPool() throws InterruptedException {
        Callable[] callables = new Callable[]{
                createCallable("123"),
                createCallable("234"),
                createCallable("345"),
                createCallable("456"),
                createCallable("567")
        };

        List<FutureTask> threadList = new LinkedList<>();

        for (int item : new int[nThreads]) {
            for (Callable callable : Arrays.asList(callables)) {
                FutureTask futureTask = new FutureTask(callable);
                threadList.add(futureTask);
                (new Thread(futureTask)).start();
              }

            threadList.stream().forEach(p -> {
                try {
                    Assert.assertTrue(((Boolean) p.get()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
