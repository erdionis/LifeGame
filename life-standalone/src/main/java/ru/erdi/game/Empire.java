package ru.erdi.game;

import java.util.HashSet;
import javax.swing.JFrame;

public class Empire {
	private HashSet<Entity> world;
	private HashSet<Entity> nextWorld;
	private JFrame window;
	private ActionPanel actionPanel;
	private boolean isLife;
	
 
	//Создаем множество, состоящее из живых клеток
	public void seventhDay(Form form) {
		this.window = new JFrame();
		this.world = new HashSet<Entity>();
		this.actionPanel = new ActionPanel(world);
		this.nextWorld = new HashSet<Entity>();
		this.isLife=true;
		
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
		
		setUpGui();
		while(true) {
			if (isLife) {
				runLife();
			}
		}
	}
 
	//Запускаем жизнь
	private void runLife() {
		world.forEach(entity->{
			ageOfEntity(entity, world);
		});
		
		if (world.equals(nextWorld) || nextWorld.size()==0) {
			isLife=!isLife;
		} else {
			world.clear();
			world.addAll(nextWorld);
			nextWorld.clear();
			actionPanel.repaint();
			try {
				Thread.sleep(Constant.SLEEP);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}	
	}
 
	// Следующее поколение
	private void ageOfEntity(Entity entity, HashSet<Entity> world) {
		int x = entity.getX();
		int y = entity.getY();
 
		// оцениваем сами себя
		int count=neighbors(x,y);
		if (count == 2 || count ==3) {
			nextWorld.add(entity);
		}
		// оцениваем мир вокруг нас
		for(int i=0;i<Constant.DIFF_POINTS.length; i+=2) {
			int dx=x+Constant.DIFF_POINTS[i]*Constant.ENTITY_SIZE;
			int dy=y+Constant.DIFF_POINTS[i+1]*Constant.ENTITY_SIZE;

			int dxx=dx>Constant.LIFE_ARENA ? dx-Constant.WORLD_ARENA : (dx<0?dx+Constant.WORLD_ARENA:dx);
			int dyy=dy>Constant.LIFE_ARENA ? dy-Constant.WORLD_ARENA : (dy<0?dy+Constant.WORLD_ARENA:dy);
			
			count=neighbors(dxx,dyy);
			if (count == 3) {
				Entity child= new Entity(dxx,dyy);
				if (!(world.contains(child))) {
					nextWorld.add(child);
				} else {
					child=null;
				}
			}
		}
	}
 
	
	// Есть кто живой в округе? 
	private int neighbors(int x, int y) {
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
	
	// gui
	private void setUpGui() {
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(Constant.UNIVERSE,Constant.UNIVERSE);
		window.getContentPane().add(actionPanel);
		window.setTitle("Цивилизация");
		window.setVisible(true);
		
		actionPanel.repaint();
	}
 
}

