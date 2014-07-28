package com.webs.itmexicali.colorized;

import com.webs.itmexicali.colorized.drawcomps.DrawButtonContainer;
import com.webs.itmexicali.colorized.drawcomps.DrawButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;


@SuppressLint("WrongCall")
public class GameView extends SurfaceView implements Callback, Runnable{
	
	//this clas TAG
	protected String TAG = Const.TAG + "-GameView";
	
	// mPortrait true to indicate that the width is smaller than the heigth
	public boolean mPortrait;
	
	public 	  boolean 	run, selected, surfaceCreated=false;
	protected SurfaceHolder sh;
	
	//Paints to be used to Draw text and shapes
	protected Paint 	mPaints[];
	protected Bitmap	mBitmaps[];
	protected Rect		mRects[];
	protected RectF		mRectFs[];
	private Thread		tDraw;
	DrawButtonContainer dbc;
	
	
	protected int bgColor=Color.WHITE;
	
	protected float width, height, ratio;
	
	//the matrix of color
	private ColorBoard mColorBoard = null;
	
	//canvas to be drawn on
	protected Canvas canvas;
	
	//Context of the instance that instantiated this class
	protected Context  mContext;
	
	//this clas instance
	private static GameView instance = null;
	
	/********************************************CONSTRUCTORS*****************************/
	public GameView(Context context) {
		super(context);
		mContext = context;
		initHolder(context);
	}
	
	public GameView(Context context, AttributeSet attrs, int defStyle){
        super( context , attrs , defStyle );
        mContext = context;
        initHolder(context);
    }

    public GameView(Context context, AttributeSet attrs){
        super( context , attrs );
        mContext = context;
        initHolder(context);
    }
    /**************************************************************************************/

    public static GameView getIns(){
    	return instance;
    }
    
    /** Init this SurfaceView's Holder    */
	public final void initHolder(Context context){
		instance = this;
		if(mColorBoard == null){
			mColorBoard = new ColorBoard(12);
		}
		sh = getHolder();
		sh.setFormat(PixelFormat.TRANSLUCENT);
		sh.addCallback(this);
		
	}
    
    /***************************SURFACE HOLDER CALLBACK METHODS*****************************/
    
    @Override
	public void surfaceCreated(SurfaceHolder holder) {
		//Log.d(TAG,"SurfaceCreated "+window);
		canvas = null;
		surfaceCreated=true;

		mRectFs = new RectF[2];
		dbc = new DrawButtonContainer(6,true);
		
		//register actions to the buttons created:
		dbc.setOnActionListener(0, DrawButtonContainer.RELEASE_EVENT, new DrawButton.ActionListener(){
			@Override public void onActionPerformed() {
				new Thread(new Runnable(){public void run(){
					mColorBoard.colorize(0);
				}}).start();}
		});
		
		dbc.setOnActionListener(1, DrawButtonContainer.RELEASE_EVENT, new DrawButton.ActionListener(){
			@Override public void onActionPerformed() {
				new Thread(new Runnable(){public void run(){
					mColorBoard.colorize(1);
				}}).start();}
		});
		
		dbc.setOnActionListener(2, DrawButtonContainer.RELEASE_EVENT, new DrawButton.ActionListener(){
			@Override public void onActionPerformed() {
				new Thread(new Runnable(){public void run(){
					mColorBoard.colorize(2);
				}}).start();}
		});
		
		dbc.setOnActionListener(3, DrawButtonContainer.RELEASE_EVENT, new DrawButton.ActionListener(){
			@Override public void onActionPerformed() {
				new Thread(new Runnable(){public void run(){
					mColorBoard.colorize(3);
				}}).start();}
		});
		
		dbc.setOnActionListener(4, DrawButtonContainer.RELEASE_EVENT, new DrawButton.ActionListener(){
			@Override public void onActionPerformed() {
				new Thread(new Runnable(){public void run(){
					mColorBoard.colorize(4);
				}}).start();}
		});
		dbc.setOnActionListener(5, DrawButtonContainer.RELEASE_EVENT, new DrawButton.ActionListener(){
			@Override public void onActionPerformed() {
				new Thread(new Runnable(){public void run(){
					mColorBoard.colorize(5);
				}}).start();}
		});
	}
    
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
		//Log.v(TAG, "SurfaceChanged "+window );
		this.width = width; 
		this.height = height;
		ratio = ((float) width) / height;
		mPortrait = ratio > 1.0f ? false : true;
		//GameActivity.getIns().setAdsPosition(mPortrait);
		if(Const.D)
			Log.v(TAG,"ratio: "+ratio+". width = "+width+", height = "+height+" so, portrait = "+mPortrait);
		
		initPaints();
		reloadByResize();
		
		refreshUI();
		//startThread();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		//Log.v(TAG, "surfaceDestroyed "+window);
		surfaceCreated = false;
		stopThread();
	}
	

	/** Init the paints to be used on canvas */
	public void initPaints() {
		mPaints = new Paint[9];
		mPaints[0] = new Paint();
		mPaints[0].setColor(Color.RED);
		mPaints[0].setStyle(Paint.Style.FILL);
		mPaints[0].setAntiAlias(true);
		
		mPaints[1] = new Paint();
		mPaints[1].setColor(Color.rgb(0, 162, 232));
		mPaints[1].setStyle(Paint.Style.FILL);
		mPaints[1].setAntiAlias(true);		
		
		mPaints[2] = new Paint();
		mPaints[2].setColor(Color.YELLOW);
		mPaints[2].setStyle(Paint.Style.FILL);
		mPaints[2].setAntiAlias(true);
		
		mPaints[3] = new Paint();
		mPaints[3].setColor(Color.rgb(163, 73, 164));
		mPaints[3].setStyle(Paint.Style.FILL);
		mPaints[3].setAntiAlias(true);
		
		mPaints[4] = new Paint();
		mPaints[4].setColor(Color.LTGRAY);
		mPaints[4].setStyle(Paint.Style.FILL);
		mPaints[4].setAntiAlias(true);
		
		mPaints[5] = new Paint();
		mPaints[5].setColor(Color.rgb(21, 183, 46));
		mPaints[5].setStyle(Paint.Style.FILL);
		mPaints[5].setAntiAlias(true);
		
		mPaints[6] = new Paint();
		Shader sh = null;
		sh = new SweepGradient(5, 5, new int[] {Color.BLUE,	Color.RED, Color.GREEN}, null);
		//mPaints[6].setShader(new LinearGradient( 0, 0, 0, 2, Color.CYAN, Color.GREEN, Shader.TileMode.CLAMP));
		//mPaints[6].setShader(new LinearGradient(0, 1, 1, 0, Color.BLUE, Color.RED, Shader.TileMode.CLAMP));
		mPaints[6].setShader(sh);
		mPaints[6].setStyle(Paint.Style.FILL);
		mPaints[6].setAntiAlias(true);
		
		mPaints[7] = new Paint();
		mPaints[7].setColor(Color.DKGRAY);
		mPaints[7].setStyle(Paint.Style.FILL);
		mPaints[7].setAntiAlias(true);
		mPaints[7].setAlpha(120);
		
		mPaints[8] = new Paint();
		mPaints[8].setColor(Color.BLACK);
		mPaints[8].setStyle(Paint.Style.STROKE);
		mPaints[8].setTextSize(width/12);
		mPaints[8].setTextAlign(Align.CENTER);
		mPaints[8].setAntiAlias(true);
	}
	
	/** This is what is going to be shown on the canvas
	 * @see android.view.View#onDraw(android.graphics.Canvas)	 */
	public void onDraw(Canvas canvas) {
		try {
			//Background
			canvas.drawColor(bgColor);
			//canvas.drawColor(mPaints[(int)(Math.random()*8)].getColor());
			
			if( mPortrait ) drawPortrait(canvas);
			else	drawLandscape(canvas);
			
		} catch (Exception e) {
			Log.e(TAG, "onDraw(canvas)");
			e.printStackTrace();
		}
	}
	
	/** draw the UI in portrait mode*/
	/** Draw the UI in Portrait mode */
	public void drawPortrait(Canvas canvas){
		//Color Div
		canvas.drawRect(mRectFs[0].left-10, mRectFs[0].top-10, mRectFs[0].right+10, mRectFs[0].bottom+10, mPaints[6]);
		canvas.drawRoundRect(mRectFs[1], 50.0f, 40.0f, mPaints[6]);
		
		mColorBoard.updateBoard(canvas, mRectFs[0], mPaints);
		
		canvas.drawText("Moves: "+mColorBoard.getMoves()+"/21", width/2, 2*height/5-15*width/32, mPaints[8]);
		
		/*  //Picture  - 2.0f*w, 2.0f*w, 40.0f*w, 20.0f*height
		if ( mBitmaps[0] != null )
			canvas.drawBitmap(mBitmaps[0], 50, 50, mPaints[5]);
			float val = mPortrait ? width/8 : height/8;
			canvas.drawRect(50f,50f,50f+val,50f+val,mPaints[1]);
		*/
		
		
		for(int i =0 ; i < dbc.getButtonsCount();i++){
			canvas.drawRoundRect(dbc.getDButton(i), 25.0f, 20.0f, mPaints[i]);
			if( dbc.getDButton(i).isPressed() )
				canvas.drawRoundRect(dbc.getDButton(i), 25.0f, 20.0f, mPaints[7]);
		}
		
	}
	
	/** draw the UI in landscape mode*/
	/** Draw the UI in Landscape mode */
	public void drawLandscape(Canvas canvas){
		
	}
	
	
	/** This method will be called when the surface has been resized, so all
	 * screen width and height dependents must be reloaded - 
	 * NOTE: DO NOT INCLUDE initPaints()*/
	protected void reloadByResize() {		
		if( mPortrait){
			mRectFs[0] = new RectF(width/16, 2*height/5-7*width/16, 15*width/16, 2*height/5+7*width/16);
			mRectFs[1] = new RectF(0, height/2+6*width/16, width, 9*height/10);
			
			dbc.repositionDButton(0, width/14, height/2+6*width/16, 3*width/14, 9*height/10);
			dbc.repositionDButton(1, 3*width/14, height/2+6*width/16, 5*width/14, 9*height/10);
			dbc.repositionDButton(2, 5*width/14, height/2+6*width/16, width/2, 9*height/10);
			dbc.repositionDButton(3, width/2, height/2+6*width/16, 9*width/14, 9*height/10);
			dbc.repositionDButton(4, 9*width/14, height/2+6*width/16, 11*width/14, 9*height/10);
			dbc.repositionDButton(5, 11*width/14, height/2+6*width/16, 13*width/14, 9*height/10);
		}
		else{
			mRectFs[0] = new RectF(0, 0, height, height);
		}
		/*
		mBitmaps = new Bitmap[1];
		float val = mPortrait ? width/8 : height/8;
		mBitmaps[0] = BitmapLoader.resizeImage(GameActivity.getIns(),R.drawable.ic_launcher, val, val);
		*/
	}
	
	
	
	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouchEvent( MotionEvent event) {
		//new Thread(new Runnable(){
			//public void run(){
				int action = event.getAction() & MotionEvent.ACTION_MASK;
				int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
				int pointerId = event.getPointerId(pointerIndex);
				switch (action) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_POINTER_DOWN:
					dbc.onPressUpdate(event, pointerIndex, pointerId);
					break;
	
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
				case MotionEvent.ACTION_CANCEL:	
					dbc.onReleaseUpdate(event, pointerIndex, pointerId);
					break;
	
				case MotionEvent.ACTION_MOVE:
					dbc.onMoveUpdate(event, pointerIndex);
					break;
				}
				refreshUI_newThread();
		//	}
		//}).start();
		return true;
	}
	
	
	/** Start a new thread to keep the UI refreshing constantly
	 * restrict to create and start a thread JUST when:
	 * There is no other thread running  and
	 * The SurfaceView has been created */
	public final synchronized void startThread(){
		if(run == false){
			tDraw = new Thread(this);
			run = true;
			tDraw.start();
		}
	}
	
	/** Stop any thread in charge of refreshing the UI*/
	public final synchronized void stopThread(){
		if(run){
			run = false;
			boolean retry = true;
			while (retry) {
				try {
					if (tDraw != null)
						tDraw.join();
					retry = false;
				} catch (InterruptedException e) {
					Log.e(TAG, "stopThread: " + e.getMessage());
				}
			}
		}
	}

	/** this thread is responsible for updating the canvas
	 * @see java.lang.Runnable#run() */
	public final void run() {		
		while (run && surfaceCreated) {
			try {
				//sleep 20 millis to get around 50 FPS
				Thread.sleep(20);
			} catch (InterruptedException e) { }
			refreshUI();
		}
	}
	
	/** Refresh the User Interface to show the updates*/

	/** Refresh the User Interface to show the updates*/
	public final synchronized void refreshUI() {
		Log.v(TAG,"Refreshing UI GameView");
		canvas = null;
		if (surfaceCreated && sh != null){
			try {
				canvas = sh.lockCanvas(null);
				if(canvas != null)
					synchronized (sh) {
						onDraw(canvas);
					}
			} finally {
				if (canvas != null)
					sh.unlockCanvasAndPost(canvas);
			}	
		}
		else{
			if(Const.D)
				Log.e(TAG,"Refreshing UI GameView CANCELLED because surface is not created");
		}
		canvas = null;
	}
	
	/** Refresh the User Interface to show the updates in a new thread
	 * {@link refreshUI}*/
	public final void refreshUI_newThread() {
		new Thread(new Runnable(){
			public void run(){
				refreshUI();
			}
		}).start();
	}

	
	public void boardOpFinish(boolean won){
		
		refreshUI();
	}
}
