package ${package}.exceptions;

import javax.net.ssl.SSLException;
import java.io.InterruptedIOException;
import java.net.*;
import java.nio.channels.ClosedChannelException;

public class ServiceFaultFactory{

    public static ServiceFault getServiceFault(ApiExceptionCodes diadocApiExceptionCode, Throwable e){
        if(isNetworkException(e)){
            return new ServiceFault(ApiExceptionCodes.SERVICE_NETWORK_EXCEPTION.name(), ApiExceptionCodes.SERVICE_NETWORK_EXCEPTION.getDescription(), e.getMessage());
        }
        return new ServiceFault(diadocApiExceptionCode.name(), diadocApiExceptionCode.getDescription(), e.getMessage());
    }

    private static boolean isNetworkException(Throwable e){
        return  e instanceof BindException
                || e instanceof ClosedChannelException
                || e instanceof ConnectException
                || e instanceof InterruptedIOException
                || e instanceof NoRouteToHostException
                || e instanceof PortUnreachableException
                || e instanceof ProtocolException
                || e instanceof SocketException
                || e instanceof SocketTimeoutException
                || e instanceof SSLException
                || e instanceof UnknownHostException
                || e instanceof UnknownServiceException;
    }

    public static ServiceFault createInternalServiceFault(Throwable e){
        return new ServiceFault(ApiExceptionCodes.INTERNAL_SERVER_ERROR.name(),  ApiExceptionCodes.INTERNAL_SERVER_ERROR.getDescription(), e.getMessage());
    }
}