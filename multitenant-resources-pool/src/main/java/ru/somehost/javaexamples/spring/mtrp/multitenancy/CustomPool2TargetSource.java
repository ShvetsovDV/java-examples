package ru.somehost.javaexamples.spring.mtrp.multitenancy;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectState;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.aop.target.CommonsPool2TargetSource;

public class CustomPool2TargetSource extends CommonsPool2TargetSource {

    private ObjectPool pool;
    private boolean testOnCreate = false;
    //проверять валидность обьекта после возвращения в пул
    private boolean testOnBorrow = false;;
    //проверять валидность обьекта перед получением из пула
    private boolean testOnReturn = false;;

    @Override
    protected ObjectPool createObjectPool() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(this.getMaxSize());
        config.setMaxIdle(this.getMaxIdle());
        config.setMinIdle(this.getMinIdle());
        config.setMaxWaitMillis(this.getMaxWait());
        config.setTimeBetweenEvictionRunsMillis(this.getTimeBetweenEvictionRunsMillis());
        config.setMinEvictableIdleTimeMillis(this.getMinEvictableIdleTimeMillis());
        config.setBlockWhenExhausted(this.isBlockWhenExhausted());
        config.setTestOnCreate(this.testOnCreate);
        config.setTestOnReturn(this.testOnReturn);
        config.setTestOnBorrow(this.testOnBorrow);
        this.pool = new GenericObjectPool(this, config);
        return this.pool;
    }

    @Override
    public boolean validateObject(PooledObject<Object> p) {
        return p.getState() != PooledObjectState.INVALID;
    }

    public void invalidateObject(Object obj) throws Exception {
        this.pool.invalidateObject(obj);
    }

    public boolean isTestOnCreate() {
        return testOnCreate;
    }

    public void setTestOnCreate(boolean testOnCreate) {
        this.testOnCreate = testOnCreate;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }
}
