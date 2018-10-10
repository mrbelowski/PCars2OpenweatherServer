package org.belowski.weather;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan("org.belowski.weather")
@Configuration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public Marshaller jaxbMarshaller() {
        Map<String, Object> props = new HashMap<>();
        props.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, false);
        props.put("jaxb.formatted.output", false);

        Jaxb2Marshaller m = new Jaxb2Marshaller();
        m.setMarshallerProperties(props);
        m.setPackagesToScan("org.belowski.weather.model");
        return m;
    }
}
