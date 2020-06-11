package ${package}.exceptions;

import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import javax.xml.namespace.QName;

public class DetailSoapFaultDefinitionExceptionResolver extends SoapFaultMappingExceptionResolver {

    private static final QName CODE = new QName("code");
    private static final QName DESCRIPTION = new QName("description");

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        ex.printStackTrace();
        ServiceFault serviceFault = null;
        if (ex instanceof ServiceFaultException) {
            serviceFault = ((ServiceFaultException) ex).getServiceFault();
        }
        if (serviceFault == null) {
            serviceFault = ServiceFaultFactory.createInternalServiceFault(ex);
        }
        SoapFaultDetail detail = fault.addFaultDetail();
        detail.addFaultDetailElement(CODE).addText(serviceFault.getCode());
        detail.addFaultDetailElement(DESCRIPTION).addText(serviceFault.getDescription());
    }

}