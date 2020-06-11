package ru.somehost.javaexamples.spring.mtrp.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Random;

@Component(TargetObject.COMPONENT_NAME)
@Scope("prototype")
public class TargetObject {
    public static final String COMPONENT_NAME = "TargetObject";

    private static final int MIN_RUNDOM_VALUE = 5;
    private static final int MAX_RUNDOM_VALUE = 10;
    private static Logger logger = LoggerFactory.getLogger(TargetObject.class.getName());
    private long deadTime;
    private int numberOfCalls;


    public TargetObject(){
        int liveTime = new Random().ints(MIN_RUNDOM_VALUE, MAX_RUNDOM_VALUE + 1).iterator().nextInt();
        this.deadTime = System.currentTimeMillis() + liveTime * 1000;
        this.numberOfCalls = 0;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidate(){
        return (deadTime >  System.currentTimeMillis());
    }

    public void doPayload(String token) throws Exception {
        if(isValidate()){
            Thread.sleep(200);
            logger.debug(String.format("doPayload for %s(%s); number of calls = %s", token, this.hashCode(), ++this.numberOfCalls));
        }else{
            logger.debug(String.format("Something bad happened for %s(%s); number of calls = %s", token, this.hashCode(), this.numberOfCalls));
            throw new Exception("Something bad happened!");
        }
    }


    @PreDestroy
    public void preDestroy() {
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
