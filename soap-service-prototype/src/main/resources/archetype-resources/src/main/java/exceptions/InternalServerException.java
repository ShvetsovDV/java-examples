package ${package}.exceptions;

public class InternalServerException extends ServiceFaultException  {
    public InternalServerException(Throwable e) {
        super(ApiExceptionCodes.INTERNAL_SERVER_ERROR, e);
    }
}