package ru.erdi.game.facade.routes;

import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class LoggingRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        errorHandler(noErrorHandler());

        from("vm:logging?concurrentConsumers=30&waitForTaskToComplete=Never")
        	.routeId("logging").setExchangePattern(ExchangePattern.InOnly)
            
        	.log(LoggingLevel.INFO,"${body}")
        .end();
        
    }
}

