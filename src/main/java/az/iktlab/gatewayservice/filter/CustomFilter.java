package az.iktlab.gatewayservice.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class CustomFilter implements GlobalFilter {

    @Autowired
    private WebClient.Builder builder;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (exchange.getRequest().getPath().toString().contains("auth")) {
            return chain.filter(exchange);
        } else {
            String jwt;
            try {
                jwt = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            } catch (NullPointerException e) {
                throw new NullPointerException("Token not found");
            }
            return builder.build().get()
                    .uri("http://:8080/auth/token")
                    .header("Authorization", jwt)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .map(s -> exchange)
                    .flatMap(chain::filter);
        }
    }
}
