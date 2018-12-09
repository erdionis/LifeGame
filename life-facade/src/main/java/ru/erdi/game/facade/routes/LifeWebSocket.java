package ru.erdi.game.facade.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;


public class LifeWebSocket extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
        errorHandler(defaultErrorHandler());
        onException(Exception.class)
            .log(LoggingLevel.WARN, "Websocket Route Exception: ${exception.message}")
        .end();

      
        from("timer://wsTimer?fixedRate=true&period=10000")
        	.setBody(constant("echo"))
        	.log("Sending nextWorld: ${body}")
        .end();
		
	}

}
