package ${package}.exceptions;

public class ServerValidateException extends ServiceFaultException {
    public ServerValidateException(Throwable e) {
        super(ApiExceptionCodes.SERVER_VALIDATE_EXCEPTION, e);
    }
}