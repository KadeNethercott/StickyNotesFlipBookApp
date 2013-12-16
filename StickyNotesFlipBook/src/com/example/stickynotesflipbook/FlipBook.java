package com.example.stickynotesflipbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;


public class FlipBook {

	private int cPos;
	private ArrayList<Bitmap> bitMapArray;
	private Bitmap bMap;
	private int viewHeight;
		private int viewWidth;
		static final String LOGTAG = FlipBook.class.getSimpleName() + "_TAG";
		
	public FlipBook(int w, int h){
		bitMapArray = new ArrayList<Bitmap>();
		cPos=0;
		viewHeight=h;
		viewWidth=w;
		bMap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		bitMapArray.add(bMap);
	
	}
	public Bitmap getCurrentNote(){
		return bitMapArray.get(cPos);
	}
	public Bitmap getPrevNote(Bitmap canvasBitmap){
		bitMapArray.set(cPos, canvasBitmap);
		if(cPos>0){
			
			cPos--;
			return bitMapArray.get(cPos);
		}
		else{
			Bitmap newBMap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
			bitMapArray.add(cPos, newBMap);
			return bitMapArray.get(cPos);
		}
	}
	public void replaceNote(Bitmap canvasBitmap){
		bitMapArray.set(cPos, canvasBitmap);
	}
	public void deleteNote(){

		bitMapArray.remove(cPos);	
		if(cPos==0 && bitMapArray.size()==0){
			bMap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
			bitMapArray.add(cPos, bMap);
		}
		else if(cPos==bitMapArray.size()){
			cPos--;
		}
	}
	public void insertNote(){
		bMap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
		bitMapArray.add(++cPos, bMap);
	}
	public Bitmap getNextNote(Bitmap canvasBitmap){
		bitMapArray.set(cPos, canvasBitmap);
		if(cPos == bitMapArray.size()-1){
			Bitmap newBMap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
			
			bitMapArray.add(newBMap); 
			cPos++;
			return bitMapArray.get(cPos);
		}
		else{
			cPos++;
			return bitMapArray.get(cPos);
		}
	}
	public int getArraySize(){
		return bitMapArray.size();
	}
	public ArrayList<Bitmap> getNotes(){
		return bitMapArray;
	}
	public void setNotes(ArrayList<Bitmap> bM){
		bitMapArray = bM;
	}
	public void setViewWidthHeight(int w, int h){
		viewWidth = w;
		viewHeight= h;
	}
	public void setLocation(int loc){
		cPos = loc;
	}
	public void clearFlipBook(){
		cPos= 0;
		bitMapArray.clear();
		bMap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
		bitMapArray.add(bMap);
	}
	public boolean saveFB(String name, Bitmap canvasBitmap){
		bitMapArray.set(cPos, canvasBitmap);
		File dir = new File(Environment.getExternalStorageDirectory() + "/StickyNoteFlipBook/" + name + "/");
		if(!dir.exists()){
			dir.mkdirs();
		}
		for(int i = 0; i< bitMapArray.size(); ++i){
				Log.d(LOGTAG, "^^^^^^^^Trying to save^^^^^^^^");
			try{
				File fbFile = new File(dir, "note"+ i + ".png");
				FileOutputStream fileOut = new FileOutputStream(fbFile);
				
				Log.d(LOGTAG, "^^^^^^^^Trying to save^^^^^^^^");
			    bitMapArray.get(i).compress(Bitmap.CompressFormat.PNG, 90, fileOut);
			    Log.d(LOGTAG, "^^^^^^^^Trying to save^^^^^^^^");
			    fileOut.flush();
			    fileOut.close();
			}
			catch(IOException e){
				Log.d(LOGTAG, "^^^^^^^^Error Message^^^^^^^^" + e.getMessage());
				return false;
			}
		}
		return true;
	}
	
	@SuppressLint("NewApi")
	public boolean loadFB(String name){
		Log.d(LOGTAG, "^^^^^^^^starting load^^^^^^^^");
		try{
			File dir = new File(Environment.getExternalStorageDirectory() + "/StickyNoteFlipBook/"+ name + "/");
			if(dir.exists()){
				Log.d(LOGTAG, "^^^^^^^^dir exists^^^^^^^^");
				File [] notes = dir.listFiles();
				if(notes.length>0){
					bitMapArray.clear();
					for(int i = 0; i< notes.length; ++i){
						Log.d(LOGTAG, "^^^^^^^^get file path^^^^^^^^" + notes.length);
						String path = Environment.getExternalStorageDirectory() + "/StickyNoteFlipBook/"+ name + "/note" + i + ".png";

						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inPreferredConfig = Bitmap.Config.ARGB_8888;
						options.outHeight = viewHeight;
						options.outWidth = viewWidth;
						options.inMutable = true;
						Log.d(LOGTAG, "^^^^^^^^decoding file^^^^^^^^");
						Bitmap newBitmap= BitmapFactory.decodeFile(path, options);
						Log.d(LOGTAG, "^^^^^^^^adding to bitmapArray^^^^^^^^");
						if(newBitmap!= null){
							Log.d(LOGTAG, "^*****newBitmap^^^^^^^^" + newBitmap.toString());
							bitMapArray.add(newBitmap);
						}
						else{
							Log.d(LOGTAG, "^^^^^^^^bitmap is null^^^^^^^^");
	
						}
							
		
					}
					cPos = 0;
					return true;
				}
				else{
					Log.d(LOGTAG, "^^^^^^^^Error Message^^^^^^^^: File does not exist");
					return false;
				}
			}
			else{
				Log.d(LOGTAG, "^^^^^^^^Error Message^^^^^^^^: File does not exist");
				return false;
			}
		}
		catch(Exception e){
			Log.d(LOGTAG, "^^^^^error ^^^^" + e.getMessage());
			return false;
	}
	}
}


