package org.r2learning.gateway.predicate;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import lombok.Data;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class TimeBetweenRoutePredicateFactory
    extends AbstractRoutePredicateFactory<TimeBetweenRoutePredicateFactory.Config> {

    public TimeBetweenRoutePredicateFactory() {
        super(Config.class);
    }

    @Data
    public static class Config {
        private LocalTime start;
        private LocalTime end;
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return exchange -> {
            LocalTime now = LocalTime.now();
            return now.isAfter(config.getStart()) && now.isBefore(config.getEnd());
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("start", "end");
    }
}