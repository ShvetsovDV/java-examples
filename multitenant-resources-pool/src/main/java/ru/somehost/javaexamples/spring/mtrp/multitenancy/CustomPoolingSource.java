package ru.somehost.javaexamples.spring.mtrp.multitenancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

@Component(value = CustomPoolingSource.COMPONENT_NAME)
public class CustomPoolingSource {
    public static final String COMPONENT_NAME = "CustomPoolingSource";

    private static Logger logger = LoggerFactory.getLogger(CustomPoolingSource.class.getName());
    private Map<String, TargetSource> source = new HashMap<>();

    @Autowired
    private ObjectFactory<CustomPool2TargetSource> customPool2TargetSourceObjectFactory;

    public CustomPoolingSource() {
    }

    private CustomPool2TargetSource getTargetObjectPoolingTargetSource(){
        return this.customPool2TargetSourceObjectFactory.getObject();
    }

    private synchronized void createTargetObjectPooling(String token) {
        if (!source.containsKey(token)) {
            logger.debug(String.format("createTargetObjectPooling for token = %s", token));
            TargetSource targetSource = getTargetObjectPoolingTargetSource();
            ProxyFactoryBean proxyBean = new ProxyFactoryBean();
            proxyBean.setTargetSource(targetSource);
            source.put(token, targetSource);
            printState(token);
        }
    }

    private TargetSource getTargetSource(String token) {
        createTargetObjectPooling(token);
        TargetSource targetSource = source.get(token);
        return targetSource;
    }

    public Object getTarget(String token) throws Exception {
        TargetSource targetSource = getTargetSource(token);
        Object target = targetSource.getTarget();
        printState(token);
        return target;
    }

    public void invalidateTarget(String token, Object object) throws Exception {
        TargetSource targetSource = getTargetSource(token);
        CustomPool2TargetSource customPool2TargetSource = (CustomPool2TargetSource) targetSource;
        customPool2TargetSource.invalidateObject(object);
        printState(token);
    }

    public void releaseTarget(String token, Object object) throws Exception {
        TargetSource targetSource = getTargetSource(token);
        targetSource.releaseTarget(object);
        printState(token);
    }

    private void printState(String token){
        if (!logger.isDebugEnabled()){
            return;
        }
        CustomPool2TargetSource customPool2TargetSource = (CustomPool2TargetSource)source.get(token);
        String state = String.format("State pool: MaxSize=%s; ActiveCount=%s; IdleCount=%s",
                customPool2TargetSource.getMaxSize(),
                customPool2TargetSource.getActiveCount(),
                customPool2TargetSource.getIdleCount());

        logger.debug(String.format("for token = %s; %s", token, state));
    }

    @PreDestroy
    public void preDestroy() {
        source.keySet().forEach(p->printState(p));
    }

}