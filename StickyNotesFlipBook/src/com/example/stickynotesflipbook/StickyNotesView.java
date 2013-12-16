package com.example.stickynotesflipbook;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;

public class StickyNotesView extends View{

		static final String LOGTAG = StickyNotesView.class.getSimpleName() + "_TAG";
		//drawing path
		private Path drawPath;
		//drawing and canvas paint
		private Paint drawPaint, canvasPaint;
		//initial color
		private int paintColor = 0xFF660000;
		//canvas
		private Canvas drawCanvas;
		//canvas bitmap
		private Bitmap canvasBitmap, copiedBitmap;
		private float brushSize, lastBrushSize;
		private boolean erase=false;
		private FlipBook flipBk;
		
		private Integer mFlipSpeed = 250;
		
		public StickyNotesView(Context context, AttributeSet attrs){
			super(context, attrs);
			setUpDrawing();
			
		}

		private void setUpDrawing(){
			brushSize = getResources().getInteger(R.integer.medium_size);
			lastBrushSize = brushSize;
			drawPath = new Path();
			drawPaint = new Paint();
			drawPaint.setColor(paintColor);
			drawPaint.setAntiAlias(true);
			drawPaint.setStrokeWidth(brushSize);
			drawPaint.setStyle(Paint.Style.STROKE);
			drawPaint.setStrokeJoin(Paint.Join.ROUND);
			drawPaint.setStrokeCap(Paint.Cap.ROUND);
			canvasPaint = new Paint(Paint.DITHER_FLAG);
		}
		
		public void setBrushSize(float newSize){
			//update size
			float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				    newSize, getResources().getDisplayMetrics());
				brushSize=pixelAmount;
				drawPaint.setStrokeWidth(brushSize);
		}
		
		public void setLastBrushSize(float lastSize){
		    lastBrushSize=lastSize;
		}
		public float getLastBrushSize(){
		    return lastBrushSize;
		}
		
		public void setErase(boolean isErase){
			//set erase true or false
			erase=isErase;
			if(erase){ 
				drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			}
			else{ 
				drawPaint.setXfermode(null);
			}
		}
		
		public void startNew(){
			
		    flipBk.clearFlipBook();
			drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		    invalidate();
		    
		}
		
		public boolean saveFlipBook(String name){
			
			return flipBk.saveFB(name, canvasBitmap);
		
		}
		public boolean loadFlipBook(String name){
			boolean success = flipBk.loadFB(name);
			if(success){
				Log.d(LOGTAG, "^^^^ load Success, now tyring to draw^^^^^^");
				drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
				flipBk.setLocation(0);
				canvasBitmap = flipBk.getCurrentNote();
				try{
				drawCanvas = new Canvas(canvasBitmap);
				}
				catch(Exception e){
					Log.d(LOGTAG, "^^^^ error:^^^" + e.getMessage());
				}
				drawCanvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);	
	 			invalidate();
			}
			return success;
		}
		public void copyNote(){
			copiedBitmap = Bitmap.createBitmap(canvasBitmap);
		}
		public void pasteNote(){
			drawCanvas.drawBitmap(copiedBitmap, 0, 0, canvasPaint);	
			invalidate();
		}
		public void insertN(){
			flipBk.insertNote();
			canvasBitmap = flipBk.getCurrentNote();
			drawCanvas = new Canvas(canvasBitmap);
			drawCanvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
			invalidate();
		}
		public void deleteN(){
			flipBk.deleteNote();
			canvasBitmap = flipBk.getCurrentNote();
			drawCanvas = new Canvas(canvasBitmap);
			drawCanvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
			invalidate();
		}
		public void nextNote(){
			
			drawCanvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
			BitmapDrawable bground = new BitmapDrawable(this.getContext().getResources(),canvasBitmap);
			bground.setAlpha(50);
			this.setBackground(bground);
			
			
			Log.d(LOGTAG,"******** clicked Next note *******");
			
			canvasBitmap = flipBk.getNextNote(canvasBitmap);
			drawCanvas = new Canvas(canvasBitmap);
			drawCanvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
			
			invalidate();
			
		}
		
		public void prevNote(){
			
			this.setBackgroundColor(Color.parseColor("#F9F3A3"));
			canvasBitmap = flipBk.getPrevNote(canvasBitmap);
			drawCanvas = new Canvas(canvasBitmap);
			//this.draw(drawCanvas);
			drawCanvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
			invalidate();
			//drawCanvas.drawPath(drawPath, drawPaint);
		}
		public void flipNotes(){
			this.setBackgroundColor(Color.parseColor("#F9F3A3"));
			int size = flipBk.getArraySize();
			Handler handler = new Handler();
			flipBk.setLocation(0);
			canvasBitmap = flipBk.getCurrentNote();
			drawCanvas = new Canvas(canvasBitmap);
			drawCanvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
			invalidate();
			int waitTime = mFlipSpeed;
			for(int i =1; i<size; ++i){
				final int num =i;
				
			    handler.postDelayed(new Runnable() { 
			         public void run() { 
			        	flipBk.setLocation(num);
			  			canvasBitmap = flipBk.getCurrentNote();
			  			drawCanvas = new Canvas(canvasBitmap);
			  			drawCanvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
			 			//Log.d(LOGTAG, "***** alpha: " + canvasPaint.getAlpha());
			 			postInvalidate();
			 			
			         } 
			    }, waitTime);
			   
				waitTime += mFlipSpeed;
			}
					
		}

		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			//view given size
			super.onSizeChanged(w, h, oldw, oldh);
			//canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			flipBk = new FlipBook(w,h);
			canvasBitmap = flipBk.getCurrentNote();
			drawCanvas = new Canvas(canvasBitmap);
			}
		protected void onDraw(Canvas canvas) {
			//draw view
			canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
			canvas.drawPath(drawPath, drawPaint);
			
			}
		public boolean onTouchEvent(MotionEvent event) {
			//detect user touch
			float touchX = event.getX();
			float touchY = event.getY();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			    drawPath.moveTo(touchX, touchY);
			    drawCanvas.drawPoint(touchX, touchY, drawPaint);
			    break;
			case MotionEvent.ACTION_MOVE:
			    drawPath.lineTo(touchX, touchY);
			    break;
			case MotionEvent.ACTION_UP:
			    drawCanvas.drawPath(drawPath, drawPaint);
			    drawPath.reset();
			    break;
			default:
			    return false;
			}
			invalidate();
			return true;
		}
		public void setColor(String newColor){
			//set color
			invalidate();
			paintColor = Color.parseColor(newColor);
			drawPaint.setColor(paintColor);
			}
		public void setSpeed(Integer S){
			mFlipSpeed = S;
		}
	}



