package com.webs.itmexicali.colorized.gamestates;

import com.webs.itmexicali.colorized.GameActivity;
import com.webs.itmexicali.colorized.GameView;
import com.webs.itmexicali.colorized.Preferences;
import com.webs.itmexicali.colorized.R;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.view.MotionEvent;

public class TutoState extends BaseState{
	
	public TutoState(){
		mID = statesIDs.TUTO;
	}

	enum innerStates{INIT, FIRST, SEC, THIRD, FOURTH, FIFTH, BACK, FINAL}
	
	private innerStates mInnerState, mPreState;
	
	//A reference to GameState, so we can paint it before painting this UI
	private GameState pGame;
	
	//rects to be used on canvas
	protected Rect			mRectFs[];
	
	@Override
	public void onPopped() {
		//Log.d(TutoState.class.getSimpleName(),"onPopped");	
		pGame = null;
	}	

	@Override
	public void onPushed() {
		//Log.d(TutoState.class.getSimpleName(),"onPushed");
		pGame = (GameState) StateMachine.getIns().getPrevioustState();
		
		mInnerState = innerStates.INIT;
	}
	
	public void resize(float width, float height){
		//Log.v("TutoState","canvas size: "+width+"x"+height);
		pGame.resize(width, height);
		
		mRectFs = new Rect[3];
		mRectFs[0] = new Rect(0, (int)(2*height/5+23*width/48), (int)(width), (int)(2*height/5+31*width/48));
		
		float boardPixels = pGame.mRectFs[0].width()/Preferences.getIns().getBoardSize();
		mRectFs[1] = new Rect((int)(width/16), (int)(5*height/16-5*width/16), 
				(int)(width/16 + boardPixels), (int)(5*height/16-5*width/16 + boardPixels));
		
		mRectFs[2] = new Rect((int)(3*GameView.width/16), 0,
				(int)(13*GameView.width/16),(int)(GameView.height/96+GameView.width/8));
		
		
	}

	@Override
	public void draw(Canvas canvas, boolean isPortrait) {
		//canvas.drawRect(new Rect(0,0,(int)GameView.width,(int)GameView.height),mPaints[2]);
		pGame.draw(canvas, isPortrait);
		
		canvas.drawColor(pGame.mPaints[11].getColor());
		
		StaticLayout mTextLayout;
		canvas.save();
		switch(mInnerState){
		case INIT:// touch to continue
			
			canvas.drawText(GameActivity.instance.getString(R.string.tutorial),
					GameView.width/2, GameView.height/3,	pGame.mPaints[8]);
			mTextLayout = getTutoLayout(GameActivity.instance.getString(R.string.tuto0));

			canvas.translate(0, GameView.height/2-pGame.mPaints[10].getTextSize());
			mTextLayout.draw(canvas);
			break;
			
		case FIRST://fill the board
			mTextLayout = getTutoLayout(GameActivity.instance.getString(R.string.tuto1));

			canvas.drawRoundRect(pGame.mRectFs[0],0,0,pGame.mPaints[10]);
			pGame.drawBoard(canvas);
			canvas.translate(0, 5*GameView.height/16+5*GameView.width/8);
			mTextLayout.draw(canvas);
			
			break;
		case SEC: // start with first tile
			mTextLayout = getTutoLayout(GameActivity.instance.getString(R.string.tuto2));

			canvas.drawRect(mRectFs[1],pGame.mPaints[10]);
			canvas.drawRect(mRectFs[1],pGame.mPaints[pGame.getFirstTileColor()]);
			canvas.translate(0, GameView.height/2 - 3*pGame.mPaints[10].getTextSize());
			mTextLayout.draw(canvas);
			break;
			
		case THIRD:	// color picker
			mTextLayout = getTutoLayout(GameActivity.instance.getString(R.string.tuto3));

			canvas.drawRoundRect(pGame.mRectFs[1], 50.0f, 40.0f, pGame.mPaints[6]);
			canvas.drawRect(mRectFs[0],pGame.mPaints[10]);
			pGame.drawButtons(canvas);
			canvas.translate(0, GameView.height/2-3*pGame.mPaints[10].getTextSize());
				mTextLayout.draw(canvas);
			break;
			
		case FOURTH:	//keep changing
			mTextLayout = getTutoLayout(GameActivity.instance.getString(R.string.tuto4));
			
			canvas.drawRoundRect(pGame.mRectFs[0],0,0,pGame.mPaints[10]);
			canvas.drawRect(pGame.mRectFs[0],pGame.mPaints[pGame.getLastTileColor()]);
			canvas.translate(0, 5*GameView.height/16+5*GameView.width/8);
			mTextLayout.draw(canvas);
			break;
			
		case FIFTH: // run out of moves
			mTextLayout = getTutoLayout(GameActivity.instance.getString(R.string.tuto5));

			//draw moves counter
			//TODO change hardcoded value
			canvas.drawText("Moves: "+pGame.getMoves()+"/21",
					GameView.width/2, GameView.height/16,pGame.mPaints[8]);
			
			canvas.drawRect(mRectFs[2], pGame.mPaints[10]); // highlight moves counter
			
			canvas.translate(0, GameView.height/2 - 5*pGame.mPaints[10].getTextSize());
			mTextLayout.draw(canvas);
			break;
		
		case FINAL: //final text, have fun
			mTextLayout = getTutoLayout(GameActivity.instance.getString(R.string.tuto6));
			canvas.translate(0, GameView.height/2 - 4*pGame.mPaints[10].getTextSize());
			mTextLayout.draw(canvas);
			break;
			
			
		case BACK:// want to exit tutorial?
			mTextLayout = getTutoLayout(GameActivity.instance.getString(R.string.tuto_exit));
			canvas.translate(0, GameView.height/2 - 3*pGame.mPaints[10].getTextSize());
			mTextLayout.draw(canvas);
			break;
		}
		canvas.restore();
	}
	
	private StaticLayout getTutoLayout(String text){
		return new StaticLayout(
				text, pGame.mPaints[9],
				(int)GameView.width, Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
	}
	

	private void saveTutorialFinish(){
		//Log.i(TutoState.class.getSimpleName(),"tutorial FInished");
		Preferences.getIns().setTutorialCompleted(true);
	}

	@Override
	public boolean touch(MotionEvent me) {
		int action = me.getAction() & MotionEvent.ACTION_MASK;
		if(action == MotionEvent.ACTION_UP ||
			action == MotionEvent.ACTION_POINTER_UP ||
			action == MotionEvent.ACTION_CANCEL){
			switch(mInnerState){
			case INIT: mInnerState = innerStates.FIRST; break;
			case FIRST: mInnerState = innerStates.SEC; break;
			case SEC: mInnerState = innerStates.THIRD; break;
			case THIRD: mInnerState = innerStates.FOURTH; break;
			case FOURTH: mInnerState = innerStates.FIFTH; break;
			case FIFTH: mInnerState = innerStates.FINAL; break;
			case BACK: mInnerState = mPreState; break;
			case FINAL: saveTutorialFinish(); 
					StateMachine.getIns().popState(); break;
			}
			//Log.d(TutoState.class.getSimpleName(),"mState: "+mInnerState);
		}
		return true;
	}
	
	public boolean onBackPressed(){
		if(mInnerState != innerStates.BACK){
			mPreState = mInnerState;
			mInnerState = innerStates.BACK;
			return true;
		}			
		saveTutorialFinish();
		return false;
	}
	
}
