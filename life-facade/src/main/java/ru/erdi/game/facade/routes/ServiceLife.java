package ru.erdi.game.facade.routes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import org.apache.camel.builder.RouteBuilder;

import ru.erdi.game.RunLife;
import ru.erdi.game.common.Constant;
import ru.erdi.game.common.ErrorMessage;
import ru.erdi.game.facade.types.Entity;
import ru.erdi.game.facade.types.Form;
import ru.erdi.game.facade.types.InfoEntity;

public class ServiceLife extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        errorHandler(noErrorHandler());

        // -------------------------------------------------        
        // сервис создания мира
        // -------------------------------------------------
        from("vm:seventhday")
            .routeId("ServiceSeventhDay")
            
            .process(exchange -> {
				Form form=exchange.getIn().getBody(Form.class);
				
				HashSet<Entity> world = new HashSet<Entity>();
				int[] deffPoints=null;
				switch (form) {
				case GLIDE:
					deffPoints=Form.GLIDE.getPoints();
					break;
				case APIARY:
					deffPoints=Form.APIARY.getPoints();
					break;
				case EIGHT:
					deffPoints=Form.EIGHT.getPoints();
					break;
				case SHIP:
					deffPoints=Form.SHIP.getPoints();
					break;
				case RANDOM:
				default:
					deffPoints=Form.RANDOM.getPoints();
				}
				
				for(int i=0;i<deffPoints.length; i+=2) {
					world.add(new Entity(deffPoints[i], deffPoints[i+1]));
				}
				
//				RunLife runLife = new RunLife();
//				HashSet<Entity> nextWorld = runLife.generation(world);
//				
//				if (world.equals(nextWorld) || nextWorld.size()==0) {
//					throw new ErrorMessage("Устойчивая конфигурация");
//				}
				InfoEntity infoEntity=new InfoEntity();
				infoEntity.setAge(1L);
				infoEntity.setKey(UUID.randomUUID().toString());
				infoEntity.setEntitys(new ArrayList<Entity>(world));
				
				exchange.getIn().setBody(infoEntity);
				exchange.setProperty(Constant.RESPONSE, infoEntity);
			})
            
            .to("bean:LifeDbService?method=setEntity(${body})")
        .end();
        
        // -------------------------------------------------        
        // сервис следующего поколения
        // -------------------------------------------------
        from("vm:nextday")
	        .routeId("ServiceNextDay")
	       
	        .to("bean:LifeDbService?method=getEntity(${body.getKey}, ${body.getAge})")
	        .process(exchange -> {
	        	if (exchange.getIn().getBody()!=null){
	        		InfoEntity preInfoEntity=exchange.getIn().getBody(InfoEntity.class);
	        		HashSet<Entity> world = new HashSet<Entity>(preInfoEntity.getEntitys());
	        		
	        		RunLife runLife = new RunLife();
					HashSet<Entity> nextWorld = runLife.generation(world);
					
					if (world.equals(nextWorld) || nextWorld.size()==0) {
						throw new ErrorMessage("Устойчивая конфигурация");
					}
					InfoEntity infoEntity=new InfoEntity();
					infoEntity.setAge(preInfoEntity.getAge()+1);
					infoEntity.setKey(preInfoEntity.getKey());
					infoEntity.setEntitys(new ArrayList<Entity>(nextWorld));
					
					exchange.getIn().setBody(infoEntity);
					exchange.setProperty(Constant.RESPONSE, infoEntity);	        		
	        	} else {
	        		throw new ErrorMessage("Отсутствует исходное состояние");
	        	}
			})
	        
	        .to("bean:LifeDbService?method=setEntity(${body})")
	    .end();
    }
}
