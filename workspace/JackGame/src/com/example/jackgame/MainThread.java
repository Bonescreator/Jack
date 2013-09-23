package com.example.jackgame;

import android.view.SurfaceHolder;
import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;

//This was taken/adapted from the tutorial at obviam.net
public class MainThread extends Thread
{
	private static final String TAG = MainThread.class.getSimpleName();
	private boolean running;
	private SurfaceHolder surfaceHolder;
	private MainGamePanel gamePanel;

	public void setRunning(boolean running)
	{
		this.running = running;
	}

	@SuppressLint("WrongCall")
	public void run()
	{
		Canvas canvas;
		Log.d(TAG, "Starting game loop");
		while (running)
		{
			canvas = null;
			try
			{
				canvas = this.surfaceHolder.lockCanvas();
				synchronized(surfaceHolder)
				{
					this.gamePanel.onDraw(canvas);
				}
			} finally
			{
				if(canvas != null)
				{
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}

	public MainThread(SurfaceHolder surfaceHolder, MainGamePanel mainGamePanel)
	{
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
	}
}
