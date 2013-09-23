package com.base.engine;

import com.base.engine.components.Level;
import com.base.engine.components.Player;
import com.base.engine.components.Transform;
import com.base.engine.math.Vector3f;

public class Game
{
	private static Level level;
	private Player player;
	
	
	public Game()
	{	
		level = new Level("level1.png", "wolf_collection.png");
		player = new Player(new Vector3f(7, 0.4375f, 5));
		
		
		Transform.setProjection(70, Window.getWidth(), Window.getHeight(), 0.01f, 1000f);
		Transform.setCamera(player.getCamera());
	}

	public void input()
	{
		level.input();
		player.input();
	}

	public void update()
	{
		level.update();
		player.update();
	}

	public void render()
	{
		level.render();
		player.render();
	}
	
	public static Level getLevel()
	{
		return level;
	}
}
