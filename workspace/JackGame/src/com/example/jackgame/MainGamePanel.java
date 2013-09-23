package com.example.jackgame;

//This was taken/adapted from the tutorial at obviam.net

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.app.Activity;

public class MainGamePanel extends SurfaceView implements
		SurfaceHolder.Callback
{
	private MainThread thread;
	private static final String TAG = MainGamePanel.class.getSimpleName();
	private Boomrang boomrang;

	public MainGamePanel(Context context)
	{
		super(context);
		getHolder().addCallback(this);
		boomrang = new Boomrang(BitmapFactory.decodeResource(getResources(), R.drawable.fillboom), 50, 50);
		thread = new MainThread(getHolder(), this);
		setFocusable(true);
		
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height)
	{
	}

	public void surfaceCreated(SurfaceHolder holder)
	{
		thread.setRunning(true);
		thread.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder)
	{
		Log.d(TAG, "Surface is being destroyed");
		boolean retry = true;
		while (retry)
		{
			try
			{
				thread.join();
				retry = false;
			} catch (InterruptedException e)
			{
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}

	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			boomrang.handleActionDown((int)event.getX(), (int)event.getY());
			if (event.getY() > getHeight() - 50)
			{
				thread.setRunning(false);
				((Activity) getContext()).finish();
			} else
			{
				Log.d(TAG, "Coords: x=" + event.getX() + ", y=" + event.getY());
			}
			if(event.getAction() == MotionEvent.ACTION_MOVE)
			{
				if(boomrang.isTouched())
				{
					boomrang.setX((int)event.getX());
					boomrang.setY((int)event.getY());
				}
			}
			if(event.getAction() == MotionEvent.ACTION_UP)
			{
				if(boomrang.isTouched())
				{
					boomrang.setTouched(false);
				}
			}
		}
		return true;
	}

	public void onDraw(Canvas canvas)
	{
		canvas.drawColor(Color.BLACK);
		boomrang.draw(canvas);
	}
}
