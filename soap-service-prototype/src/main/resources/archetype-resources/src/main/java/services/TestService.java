package ${package}.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TestService {
    public String getContent(String name){
        return String.format("result for %s", name.toUpperCase());
    }

}
