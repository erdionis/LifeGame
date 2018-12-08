package ru.erdi.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Form {
	GLIDE,	// планер
	SHIP,	// корабль
	EIGHT,	// восьмерка
	APIARY, // пасека
	RANDOM;	// случайное распределение
	
	private static final Map<String, Form> nameIndex = new HashMap<>(Form.values().length);
    static {
        for (Form form : Form.values()) {
            nameIndex.put(form.name(), form);
        }
    }
    public static Form lookupByName(String name) {
        return nameIndex.get(name);
    }
	
	public int[] getPoints() {
	
		switch (this) {
		case GLIDE:
			return new int[] {Constant.WORLD_ARENA/2,Constant.WORLD_ARENA/2,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE,Constant.WORLD_ARENA/2,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*2,Constant.WORLD_ARENA/2,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*2, Constant.WORLD_ARENA/2-Constant.ENTITY_SIZE,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE,Constant.WORLD_ARENA/2-Constant.ENTITY_SIZE*2
			};
			
		case APIARY:
			return new int[] {Constant.WORLD_ARENA/2,Constant.WORLD_ARENA/2,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE,Constant.WORLD_ARENA/2,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*2,Constant.WORLD_ARENA/2,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*3,Constant.WORLD_ARENA/2,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*4,Constant.WORLD_ARENA/2,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*5,Constant.WORLD_ARENA/2,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*6,Constant.WORLD_ARENA/2
			};
		case EIGHT:
			return new int[] {Constant.WORLD_ARENA/2,Constant.WORLD_ARENA/2,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE,Constant.WORLD_ARENA/2,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*2,Constant.WORLD_ARENA/2,
					Constant.WORLD_ARENA/2,Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE,Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*2,Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE,
					Constant.WORLD_ARENA/2,Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*2,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE,Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*2,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*2,Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*2,
					
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*3,Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*3,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*4,Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*3,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*5,Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*3,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*3,Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*4,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*4,Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*4,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*5,Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*4,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*3,Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*5,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*4,Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*5,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*5,Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*5
			};
		case SHIP:
			return new int[] {Constant.WORLD_ARENA/2,Constant.WORLD_ARENA/2,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE,Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*2,Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*3,Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*4,Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*4,Constant.WORLD_ARENA/2,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*4,Constant.WORLD_ARENA/2-Constant.ENTITY_SIZE,
					Constant.WORLD_ARENA/2+Constant.ENTITY_SIZE*3,Constant.WORLD_ARENA/2-Constant.ENTITY_SIZE*2
					
			};
		case RANDOM:
		default:
			List<Integer> pointsL=new ArrayList<Integer>();
			for (int x = 0; x < Constant.WORLD_ARENA; x += Constant.ENTITY_SIZE) {
				for (int y = 0; y < Constant.WORLD_ARENA; y += Constant.ENTITY_SIZE) {
					int temp = (int) (Math.random() * 4);
					if (temp == 0) {
						pointsL.add(x);
						pointsL.add(y);
					}
				}
			};

			int[] points = new int[pointsL.size()];
			for(int i=0; i<pointsL.size(); i++) {
				points[i] = pointsL.get(i);
		      }
			return points;
		}
	}
	
}
