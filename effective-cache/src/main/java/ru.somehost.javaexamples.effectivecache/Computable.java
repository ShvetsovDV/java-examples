package ru.somehost.javaexamples.effectivecache;

public interface Computable<A, V> {
    V compute(A arg) throws InterruptedException;
}