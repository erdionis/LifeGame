package ru.erdi.game;

import java.util.HashSet;

import ru.erdi.game.common.Constant;
import ru.erdi.game.facade.types.Entity;

public class RunLife {
		//Запускаем жизнь
		public HashSet<Entity> generation(HashSet<Entity> world) {
			HashSet<Entity> nextWorld = new HashSet<Entity>();
			world.forEach(entity->{
				int x = entity.getX();
				int y = entity.getY();
		 
				// оцениваем сами себя
				int count=neighbors(x,y,world);
				if (count == 2 || count ==3) {
					nextWorld.add(entity);
				}
				// оцениваем мир вокруг нас
				for(int i=0;i<Constant.DIFF_POINTS.length; i+=2) {
					int dx=x+Constant.DIFF_POINTS[i]*Constant.ENTITY_SIZE;
					int dy=y+Constant.DIFF_POINTS[i+1]*Constant.ENTITY_SIZE;

					int dxx=dx>Constant.LIFE_ARENA ? dx-Constant.WORLD_ARENA : (dx<0?dx+Constant.WORLD_ARENA:dx);
					int dyy=dy>Constant.LIFE_ARENA ? dy-Constant.WORLD_ARENA : (dy<0?dy+Constant.WORLD_ARENA:dy);
					
					count=neighbors(dxx,dyy,world);
					if (count == 3) {
						Entity child= new Entity(dxx,dyy);
						if (!(world.contains(child))) {
							nextWorld.add(child);
						} else {
							child=null;
						}
					}
				}
			});

			return nextWorld;
		}
	 
		
		// Есть кто живой в округе? 
		private static int neighbors(int x, int y, HashSet<Entity> world) {
			int counter = 0;
			for(int i=0;i<Constant.DIFF_POINTS.length; i+=2) {
				int dx=x+Constant.DIFF_POINTS[i]*Constant.ENTITY_SIZE;
				int dy=y+Constant.DIFF_POINTS[i+1]*Constant.ENTITY_SIZE;
				
				int dxx=dx>Constant.LIFE_ARENA ? dx-Constant.WORLD_ARENA: (dx<0?dx+Constant.WORLD_ARENA:dx);
				int dyy=dy>Constant.LIFE_ARENA ? dy-Constant.WORLD_ARENA: (dy<0?dy+Constant.WORLD_ARENA:dy);
				
				Entity tmpEntity = new Entity(dxx, dyy);
				if (world.contains(tmpEntity) && !(dx == x && dy == y)) {
					 counter++; 
					 tmpEntity=null;
				}
			}
			return counter;
		}
}
