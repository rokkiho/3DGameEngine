package com.base.engine.components;

import org.lwjgl.input.Keyboard;

import com.base.engine.Camera;
import com.base.engine.Game;
import com.base.engine.Input;
import com.base.engine.Window;
import com.base.engine.math.Vector2f;
import com.base.engine.math.Vector3f;

public class Player
{
	private static final float MOUSE_SENSITIVITY = 0.5f;
	private static final float MOVE_SPEED = 5f;
	
	private static final float PLAYER_SIZE = 0.2f;
	
	private Camera camera;
	
	private boolean mouseLocked = false;
	private Vector2f centerPos = new Vector2f(Window.getWidth() / 2,
			Window.getHeight() / 2);
	private Vector3f movementVector;
	
	public Player(Vector3f position)
	{
		camera = new Camera(position, new Vector3f(0, 0, 1), new Vector3f(0, 1, 0));
	}
	
	public void input()
	{	
		if (Input.getKey(Keyboard.KEY_ESCAPE))
		{
			Input.setCursor(true);
			mouseLocked = false;
		}
		if (Input.getMouseDown(0))
		{
			Input.setMousePosition(centerPos);
			Input.setCursor(false);
			mouseLocked = true;
		}
		
		movementVector = Vector3f.Zero;

		if (Input.getKey(Keyboard.KEY_W))
			movementVector = movementVector.add(camera.getForward());
		if (Input.getKey(Keyboard.KEY_S))
			movementVector = movementVector.subtract(camera.getForward());
		if (Input.getKey(Keyboard.KEY_A))
			movementVector = movementVector.add(camera.getLeft());
		if (Input.getKey(Keyboard.KEY_D))
			movementVector = movementVector.add(camera.getRight());

		
		if (mouseLocked)
		{			
			Vector2f deltaPos = Input.getMousePosition().subtract(centerPos);
			
			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;
			
			if (rotY)
				camera.rotateY(deltaPos.getX() * MOUSE_SENSITIVITY);
			if (rotX)
				camera.rotateX(-deltaPos.getY() * MOUSE_SENSITIVITY);

			if (rotY || rotX)
				Input.setMousePosition(centerPos);
		}
	}
	
	public void update()
	{
		float moveAmount = (float)(MOVE_SPEED * Time.getDelta());
		
		movementVector.setY(0);
		if(movementVector.length() > 0)
			movementVector = movementVector.normalized();
		
		Vector3f oldPos = camera.getPos();
		Vector3f newPos = oldPos.add(movementVector.multiply(moveAmount));
		
		Vector3f collisionVector = Game.getLevel().checkCollision(oldPos, newPos, PLAYER_SIZE, PLAYER_SIZE);
		movementVector = movementVector.multiply(collisionVector);
		
		camera.move(movementVector, moveAmount);
	}
	
	public void render()
	{
		
	}

	public Camera getCamera()
	{
		return camera;
	}
}
