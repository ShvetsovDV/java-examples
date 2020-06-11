package ru.somehost.javaexamples.effectivecache;

import java.util.concurrent.atomic.AtomicLong;

public class TestMemoizer<A, V> extends Memoizer<A, V> {

    private final AtomicLong counter;

    public TestMemoizer(Computable<A, V> c, AtomicLong counter) {
        super(c);
        this.counter = counter;
    }

    public V compute(final A arg) throws InterruptedException {
        long start = System.nanoTime();
        try {
            return super.compute(arg);
        } finally {
            long end = System.nanoTime();
            System.out.println(String.format("order %s for arg %s compute time = %s",counter.incrementAndGet(), arg, end - start));
        }
    }
}
