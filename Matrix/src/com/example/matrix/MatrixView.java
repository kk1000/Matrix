package com.example.matrix;


import java.io.IOException;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MatrixView extends View implements OnTouchListener {

	Path myPath;
	Paint myPaint;
	GestureDetector mGestureDetector;
	MatrixView theViewObject = null;
	MediaPlayer mySwushMp;
	float myTopLeftCardPosX, myTopLeftCardPosY;
	float myCardWidth;
	float myCardHeight;
	int myGridSize = MatrixCardManager.GRID_SIZE;
	public MatrixView(Context context) {
		super(context);
		initView(context, null, 0);
	}

	public MatrixView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs, 0);
	}

	public MatrixView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context, attrs, defStyle);
	}

	private void initView(Context context, AttributeSet attrs, int defStyle){
		myPath = new Path();
		myPaint = new Paint();
		setBackgroundColor(Color.rgb(230,230,250));
		mySwushMp = MediaPlayer.create(getContext(), R.raw.swush);
		theViewObject = this;
		setOnTouchListener(this); 
		mGestureDetector = new GestureDetector(context, 
				new GestureDetector.SimpleOnGestureListener (){       	
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			public boolean onDoubleTap(MotionEvent e) {
				return (theViewObject.onDoubleTap(e));
			}	
		});
	}
	
	private void drawTheGrid(Canvas canvas){
		myPaint.setColor(Color.BLACK);
		myPaint.setStrokeWidth(2);
		int numLinesX = myGridSize+2, numLinesY = myGridSize+2;
		float minX = 0 , minY = 0, maxX = getWidth(), maxY = getHeight();
		float incrementX = (maxX-minX)/(numLinesX-1), incrementY = (maxY-minY)/(numLinesY-1);
		myTopLeftCardPosX = minX;
		myTopLeftCardPosY = minY;		
		myCardWidth = incrementX;
		myCardHeight = incrementY;
		//Vertical lines
		for (int i = 0; i< numLinesX; i++){
			float xPos = minX+(i*incrementX);
			canvas.drawLine(xPos, minY, xPos, maxY, myPaint);
		}
		//Horizontal lines		
		for (int i = 0; i< numLinesY; i++){
			float yPos = minY+(i*incrementY);
			canvas.drawLine(minX, yPos, maxX, yPos, myPaint);
		}
		myPaint.setStrokeWidth(0);
	}
	
	/* (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {		
		super.onDraw(canvas);	
		
		drawTheGrid(canvas);
		float firstTileCenterX =  myTopLeftCardPosX + (myCardWidth / 2);
		float firstTileCenterY =  myTopLeftCardPosY + (myCardHeight / 2);
		MatrixCardManager.updateScreenParams(myCardWidth, myCardHeight, getX() + myTopLeftCardPosX, getY() + myTopLeftCardPosY);		
		MatrixCardManager.drawTheRowTitles(canvas, firstTileCenterX, firstTileCenterY);
		MatrixCardManager.drawTheColumnTitles(canvas, firstTileCenterX, firstTileCenterY);		
	}
	
	public boolean onTouch(View v, MotionEvent event) {		
		mGestureDetector.onTouchEvent(event);
		return true;
	}
	public boolean onDoubleTap(MotionEvent e) {		
		MatrixCardManager.showNextCard();		        
        mySwushMp.start();
		return true;
	}
}
