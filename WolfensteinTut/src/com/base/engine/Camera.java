package com.base.engine;

import org.lwjgl.input.Keyboard;

import com.base.engine.components.Time;
import com.base.engine.math.Vector2f;
import com.base.engine.math.Vector3f;

public class Camera
{
	private boolean mouseLocked = false;
	private Vector2f centerPos = new Vector2f(Window.getWidth() / 2,
			Window.getHeight() / 2);
	
	public static final Vector3f yAxis = new Vector3f(0, 1, 0);

	private Vector3f pos;
	private Vector3f forward;
	private Vector3f up;

	public Camera()
	{
		this(new Vector3f(0, 0, 0), new Vector3f(0, 0, 1),
				new Vector3f(0, 1, 0));
	}

	public Camera(Vector3f pos, Vector3f forward, Vector3f up)
	{
		this.pos = pos;
		this.forward = forward.normalized();
		this.up = up.normalized();
	}

	public void input()
	{
		float sensitivity = 0.5f;
		float moveAmount = (float) (10 * Time.getDelta());

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

		if (Input.getKey(Keyboard.KEY_W))
			move(getForward(), moveAmount);
		if (Input.getKey(Keyboard.KEY_S))
			move(getForward(), -moveAmount);
		if (Input.getKey(Keyboard.KEY_A))
			move(getLeft(), moveAmount);
		if (Input.getKey(Keyboard.KEY_D))
			move(getRight(), moveAmount);

		if (mouseLocked)
		{
			Vector2f deltaPos = Input.getMousePosition().subtract(centerPos);

			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;

			if (rotY)
				rotateY(deltaPos.getX() * sensitivity);
			if (rotX)
				rotateX(-deltaPos.getY() * sensitivity);

			if (rotY || rotX)
				Input.setMousePosition(centerPos);
		}
	}

	public void move(Vector3f dir, float amount)
	{
		pos = pos.add(dir.multiply(amount));
	}

	public void rotateY(float angle)
	{
		Vector3f hAxis = yAxis.cross(forward).normalized();

		forward = forward.rotate(angle, yAxis).normalized();

		up = forward.cross(hAxis).normalized();
	}

	public void rotateX(float angle)
	{
		Vector3f hAxis = yAxis.cross(forward).normalized();

		forward = forward.rotate(angle, hAxis).normalized();

		up = forward.cross(hAxis).normalized();
	}

	public Vector3f getLeft()
	{
		return forward.cross(up).normalized();
	}

	public Vector3f getRight()
	{
		return up.cross(forward).normalized();
	}

	public Vector3f getPos()
	{
		return pos;
	}

	public void setPos(Vector3f pos)
	{
		this.pos = pos;
	}

	public Vector3f getForward()
	{
		return forward;
	}

	public void setForward(Vector3f forward)
	{
		this.forward = forward;
	}

	public Vector3f getUp()
	{
		return up;
	}

	public void setUp(Vector3f up)
	{
		this.up = up;
	}
}
