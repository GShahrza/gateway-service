package az.iktlab.gatewayservice.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyTokenExp extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> map = new HashMap<>();

        String message = "Server is unavailable";
        Exception exception = (Exception) getError(request);

        if (exception.getClass().getName().equals("java.lang.NullPointerException")) {
            message = exception.getMessage();
        } else if (exception.getMessage().startsWith("4")) {
            message = "Token is invalid";
        }
        map.put("time", LocalDateTime.now().toString());
        map.put("message", message);
        return map;
    }
}
