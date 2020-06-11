package ${package}.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceFaultException extends RuntimeException {

    private ServiceFault serviceFault;
    private Logger logger = LoggerFactory.getLogger(ServiceFaultException.class);

    public ServiceFaultException(ApiExceptionCodes diadocApiExceptionCode, Throwable e) {
        super(e.getMessage());
        serviceFault = ServiceFaultFactory.getServiceFault(diadocApiExceptionCode, e);
        logger.error(serviceFault.getCode(), e);
    }

    public ServiceFault getServiceFault() {
        return serviceFault;
    }
}