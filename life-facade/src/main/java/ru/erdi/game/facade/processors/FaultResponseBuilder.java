package ru.erdi.game.facade.processors;

import ru.erdi.game.common.ErrorMessage;
import ru.erdi.game.facade.types.Error;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class FaultResponseBuilder implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
    	  int status = Status.INTERNAL_SERVER_ERROR.getStatusCode();
          Error error;
          Exception exception = exceptionExtractor(exchange);

          if (exception != null) {
        	  if (exception instanceof ErrorMessage) {
        		  error = ((ErrorMessage) exception).getFaultInfo();
        		  if (error==null) {
        			  error = new Error();
        			  error.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
        			  error.setMessage(exception.getMessage());
        		  }
                  status = error.getCode();
              } else {
            	  error = new Error(1, "Inner error", "Details is log");
              }
          } else {
        	  error = new Error(-1, "Unknown error", "");
          }
          Response resp = Response.
                  status(status).
                  entity(error).
                  build();

          exchange.getOut().setBody(resp);
          exchange.setProperty("WebServiceFault", resp);
          exchange.setProperty("toLog", error);
    }
    
    private static Exception exceptionExtractor(Exchange exchange) {
		Exception e = null;
		if (exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class) != null) {
			e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
		} else if (exchange.getIn().getBody(Exception.class) != null) {
			e = exchange.getIn().getBody(Exception.class);
		} else if (exchange.getException() != null) {
			e = exchange.getException();
		} else {
			String c = exchange.getIn().getHeader(Exchange.EXCEPTION_CAUGHT, String.class);
			if (c != null) {
				e = new Exception(c);
			}
		}
		return e;
	}
}