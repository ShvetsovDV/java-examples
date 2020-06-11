package ${package}.exceptions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.Optional;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceFault", propOrder = {
        "code",
        "description"
})
public class ServiceFault {

    protected String code;
    protected String description;

    public ServiceFault() {
    }

    public ServiceFault(String code, String description, String ErrorDetail) {
        this.code = code;
        String errorDetail = Optional.ofNullable(ErrorDetail).orElse("");
        errorDetail = errorDetail.isEmpty() ? "" : " Error: " + errorDetail;

        this.description = description + errorDetail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return  Optional.ofNullable(description).orElse("");
    }

    public void setDescription(String description) {
        this.description = description;
    }
}