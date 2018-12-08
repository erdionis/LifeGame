package ru.erdi.game.facade.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import ru.erdi.game.common.Constant;
import java.util.UUID;


public class LogIdGenerator implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String id = exchange.getIn().getHeader(Constant.LOG_ID, String.class);
        String correlationId = exchange.getProperty(Constant.LOG_CORRELATION_ID, String.class);
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
            exchange.setProperty(Constant.LOG_ID, id);
            exchange.setProperty(Constant.LOG_CORRELATION_ID, id);
            exchange.getIn().setHeader(Constant.LOG_ID, id);
        } else if (correlationId == null || correlationId.isEmpty()){
        	exchange.setProperty(Constant.LOG_ID, id);
        	correlationId = UUID.randomUUID().toString();
        	exchange.setProperty(Constant.LOG_CORRELATION_ID, correlationId);
        	exchange.getIn().setHeader(Constant.LOG_CORRELATION_ID, correlationId);
        }
    }
}
