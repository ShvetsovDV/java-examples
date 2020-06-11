package ru.somehost.javaexamples.spring.mtrp.components;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.somehost.javaexamples.spring.mtrp.multitenancy.CustomPool2TargetSource;


@Component(TargetObjectPoolingTargetSource.COMPONENT_NAME)
@Scope("prototype")
public class TargetObjectPoolingTargetSource extends CustomPool2TargetSource {
    public static final String COMPONENT_NAME = "TargetObjectPoolingTargetSource";

    public TargetObjectPoolingTargetSource() {
        super();
        this.setTargetBeanName(TargetObject.COMPONENT_NAME);
        this.setMaxSize(10);
        this.setTestOnBorrow(true);
        this.setTestOnReturn(true);
    }
}
