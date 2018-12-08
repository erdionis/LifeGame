package ru.erdi.game.facade.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;

import ru.erdi.game.facade.processors.FaultResponseBuilder;
import ru.erdi.game.facade.processors.LogIdGenerator;
import ru.erdi.game.facade.processors.ResponseSetter;


public class LifeRest extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		 errorHandler(defaultErrorHandler());

		 onException(Exception.class)
	        .handled(true)
	        .useOriginalMessage()
	        
	        .process(new FaultResponseBuilder())
	        
	        .wireTap("vm:logging")
	        	.newExchangeBody(exchangeProperty("toLog"))
	        .end()
		    .setHeader("Content-Type", simple("${exchangeProperty.ContentType}"))
		    .setBody(exchangeProperty("WebServiceFault"))
	        .setFaultBody(exchangeProperty("WebServiceFault"))
	        .choice()
		        .when(simple("${header.Content-Type} != null"))
		        	.setProperty("ContentType", simple("${header.Content-Type}"))
		        .otherwise()
		        	.setProperty("ContentType", constant("application/json; charset=UTF-8"))
	        .end()
	        
	        .removeHeaders("*")
	        .setHeader("Content-Type", simple("${exchangeProperty.ContentType}"))
	    .end();


		from("cxfrs:bean:lifeRestServer?bindingStyle=SimpleConsumer&continuationTimeout=70000")
	        .routeId("LifeRest")

	        .process(new LogIdGenerator())
	        .wireTap("vm:logging").end()

	        .choice()
	            .when(simple("${header."+CxfConstants.OPERATION_NAME+"} in 'seventhday'"))
	            	.to("vm:seventhday")
	            .when(simple("${header."+CxfConstants.OPERATION_NAME+"} in 'nextday'"))
	            	.to("vm:nextday")	
	        .end()

	        .process(new ResponseSetter())
	        .wireTap("vm:logging").end()
	        .choice()
		        .when(simple("${header.Content-Type} != null"))
		        	.setProperty("ContentType", simple("${header.Content-Type}"))
		        .otherwise()
		        	.setProperty("ContentType", constant("application/json; charset=UTF-8"))
	        .end()
	        
	        .removeHeaders("*")
	        .setHeader("Content-Type", simple("${exchangeProperty.ContentType}"))
	    .end();

	}

}