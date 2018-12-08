package ru.erdi.game;

public interface Constant {
	public static final int ENTITY_SIZE = 10;
	public static final int WORLD_ARENA = 500;
	public static int[] DIFF_POINTS = {-1,-1,0,-1,1,-1,-1,0,0,0,1,0,-1,1,0,1,1,1};
	public static final int UNIVERSE = WORLD_ARENA+100;
	public static final int LIFE_ARENA = WORLD_ARENA-ENTITY_SIZE;
	public static final int SLEEP=100;
}
