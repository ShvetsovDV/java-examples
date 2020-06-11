package ru.somehost.javaexamples.spring.mtrp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.somehost.javaexamples.spring.mtrp.components.TargetObject;
import ru.somehost.javaexamples.spring.mtrp.multitenancy.CustomPoolingSource;

@Service
public class ServiceExecutor {
    private static Logger logger = LoggerFactory.getLogger(ServiceExecutor.class.getName());

    private final CustomPoolingSource customPoolingSource;

    public ServiceExecutor(CustomPoolingSource customPoolingSource) {
        this.customPoolingSource = customPoolingSource;
    }

    public boolean execute(String token){

        TargetObject targetObject;
        try {
            targetObject =  (TargetObject)customPoolingSource.getTarget(token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            targetObject.doPayload(token);
            return releaseTarget(token,targetObject);
        } catch (Exception e) {
            return invalidateTarget(token,targetObject);
        }
    }

    private boolean releaseTarget(String token, Object targetObject){
        try {
            customPoolingSource.releaseTarget(token, targetObject);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean invalidateTarget(String token, Object targetObject){
        try {
            customPoolingSource.invalidateTarget(token, targetObject);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
