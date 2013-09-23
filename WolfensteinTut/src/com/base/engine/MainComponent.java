package com.base.engine;

import com.base.engine.components.Time;
import com.base.engine.util.RenderUtil;

public class MainComponent
{
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final String TITLE = "3D Engine";
	public static final double FRAME_RATE = 5000.0;

	private boolean isRunning;

	private Game game;

	public MainComponent()
	{
		System.out.println(RenderUtil.getOpenGLVersion());
		RenderUtil.initGraphics();
		isRunning = false;
		game = new Game();
	}

	public void start()
	{
		if (isRunning)
			return;

		isRunning = true;

		run();
	}

	public void stop()
	{
		if (!isRunning)
			return;

		isRunning = false;
	}

	private void run()
	{
		final double frameTime = 1.0 / FRAME_RATE;

		long lastTime = Time.getTime();
		double unprocessedTime = 0;

		int frames = 0;
		long frameCounter = 0;

		while (isRunning)
		{
			boolean render = false;

			long startTime = Time.getTime();
			long passedTime = startTime - lastTime;
			lastTime = startTime;

			unprocessedTime += passedTime / (double) Time.SECOND;
			frameCounter += passedTime;

			while (unprocessedTime > frameTime)
			{
				render = true;

				unprocessedTime -= frameTime;

				if (Window.isCloseRequested())
				{
					stop();
				}

				Time.setDelta(frameTime);

				game.input();
				Input.update();

				game.update();

				if (frameCounter >= Time.SECOND)
				{
					System.out.println(frames);
					frames = 0;
					frameCounter = 0;
				}
			}
			if (render)
			{
				render();
				frames++;
			} else
			{
				try
				{
					Thread.sleep(1);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}

		cleanUp();
	}

	private void render()
	{
		RenderUtil.clearScreen();
		game.render();
		Window.render();
	}

	private void cleanUp()
	{
		Window.cleanUp();
	}

	public static void main(String[] args)
	{
		Window.createWindow(WIDTH, HEIGHT, TITLE);

		MainComponent game = new MainComponent();

		game.start();
	}
}
