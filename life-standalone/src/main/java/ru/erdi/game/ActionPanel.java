package ru.erdi.game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import javax.swing.JPanel;

public class ActionPanel extends JPanel {
	private HashSet<Entity> world;
	private static final long serialVersionUID = 4203453035237215212L;

	public ActionPanel(HashSet<Entity> world) {
		super();
		this.world = world;
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		world.forEach(entity->{
			int x = entity.getX();
			int y = entity.getY();
			g.setColor(Color.BLUE);
			g.fillRect((this.getWidth() - Constant.WORLD_ARENA)/2 + x,(this.getHeight() - Constant.WORLD_ARENA)/2 + y,Constant.ENTITY_SIZE,Constant.ENTITY_SIZE);
			g.setColor(Color.WHITE);
			g.drawRect((this.getWidth() - Constant.WORLD_ARENA)/2 + x,(this.getHeight() - Constant.WORLD_ARENA)/2 + y,Constant.ENTITY_SIZE,Constant.ENTITY_SIZE);
		});
	}
	
	
}
