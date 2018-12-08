package ru.erdi.game.facade.processors;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import ru.erdi.game.common.Constant;
import ru.erdi.game.common.ErrorMessage;

public class ResponseSetter implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
    	Object response=exchange.getProperty(Constant.RESPONSE);
    	Status status=Status.SEE_OTHER;
    	if (response==null){
    		throw new ErrorMessage("Inner Exception - No Response");
    	} else {
    		if (exchange.getProperty(Constant.HTTP_RESPONSE_STATUS)==null || exchange.getProperty(Constant.HTTP_RESPONSE_STATUS, int.class)==0 || exchange.getProperty(Constant.HTTP_RESPONSE_STATUS, int.class)==200) {
    			status=Status.OK;
    		}

    		Response resp=Response.
    		        status(status).type(MediaType.APPLICATION_JSON).
    		          entity(response).
    		            build();
    		exchange.getOut().setBody(resp);
    	}

    }
}
