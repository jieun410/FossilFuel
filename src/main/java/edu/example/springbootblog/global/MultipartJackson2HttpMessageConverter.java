package edu.example.springbootblog.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MultipartJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    public MultipartJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper);

        this.setSupportedMediaTypes(List.of(
                MediaType.APPLICATION_OCTET_STREAM,
                MediaType.MULTIPART_FORM_DATA
        ));
    }
}

