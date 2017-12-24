package ComputeEngine.ComputeEngine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:routing.properties")
public class RoutingConfig {
    private String crud;

    public String getCrud() {
        return crud;
    }

    @Autowired
    public void setCrud(@Value("${crud}") final String crud) {
        this.crud = crud;
    }
}
