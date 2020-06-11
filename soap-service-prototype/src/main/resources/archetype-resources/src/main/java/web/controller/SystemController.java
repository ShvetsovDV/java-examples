package ${package}.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import ${package}.exceptions.ApiExceptionCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ${package}.web.model.ExceptionDto;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SystemController {

    @Value("${service.description}")
    private String serviceDescription;
    @Value("${wsdl.LocationUri}")
    private String wsdlLocationUri;

    @Autowired
    private BuildProperties buildProperties;

    @RequestMapping(value = {"/", "/index", "/serviceInfo"}, method = RequestMethod.GET)
    public String getInfo(Model model) throws JsonProcessingException {
        List<ExceptionDto> exceptionsList = new ArrayList();
        for(ApiExceptionCodes val : ApiExceptionCodes.values()){
            ExceptionDto item = new ExceptionDto();
            item.setCode(val.name());
            item.setDescription(val.getDescription());
            exceptionsList.add(item);
        }
        model.addAttribute("exceptions", exceptionsList);
        model.addAttribute("buildInfo", buildProperties);
        model.addAttribute("maintitle", buildProperties.getName());
        model.addAttribute("description", getSescription());
        model.addAttribute("wsdl", String.format("%s?%s", wsdlLocationUri, "wsdl"));
        model.addAttribute("address", wsdlLocationUri);
        return "serviceInfo";
    }

    private String getSescription(){
        try {
            return serviceDescription == null ||  serviceDescription.isEmpty() ? "Описание сервиса можно указать в настройках в application.properties в параметре service.description": new String(serviceDescription.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "Ошибка преобразования кодировки описания сервиса. Проверьте значение параметра service.description";
        }
    }
}
