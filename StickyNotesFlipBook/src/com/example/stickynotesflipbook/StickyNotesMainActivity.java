package com.example.stickynotesflipbook;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;  

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.widget.Toast;


public class StickyNotesMainActivity extends Activity implements OnClickListener{

	
		static final String LOGTAG = StickyNotesMainActivity.class.getSimpleName() + "_TAG";
		private StickyNotesView stickyNotesView;
		private ImageButton currPaint, flipBtn;
		private LinearLayout paintLayout;
		private Button fileBtn, editBtn, toolsBtn, nextBtn, backBtn, colorBtn;
		private float smallBrush, mediumBrush, largeBrush;
		private String mStickyNotesName = "";
		private Integer mFlipSpeed=250;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			try{
				
			
			setContentView(R.layout.color_chooser);
			paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
			currPaint = (ImageButton)findViewById(R.id.black);
			currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
			
			setContentView(R.layout.sticky_notes_main);
			stickyNotesView = (StickyNotesView)findViewById(R.id.drawing);

			smallBrush = getResources().getInteger(R.integer.small_size);
			mediumBrush = getResources().getInteger(R.integer.medium_size);
			largeBrush = getResources().getInteger(R.integer.large_size);
		
			nextBtn = (Button)findViewById(R.id.next_btn);
			nextBtn.setOnClickListener(this);
			
			backBtn = (Button)findViewById(R.id.back_btn);
			backBtn.setOnClickListener(this);
			
			fileBtn = (Button)findViewById(R.id.file_btn);
			fileBtn.setOnClickListener(this);
			
			editBtn = (Button)findViewById(R.id.edit_btn);
			editBtn.setOnClickListener(this);
			
			toolsBtn = (Button)findViewById(R.id.tools_btn);
			toolsBtn.setOnClickListener(this);
			
			colorBtn = (Button)findViewById(R.id.color_btn);
			colorBtn.setOnClickListener(this);
			
			flipBtn = (ImageButton)findViewById(R.id.flip_btn);
			flipBtn.setOnClickListener(this);
		
			stickyNotesView.setBrushSize(mediumBrush);
			}
			catch(Exception e){
				Log.d(LOGTAG, "*****Error: *****" + e.getLocalizedMessage());
			}
		}

		
		@Override
		public void onClick(View view){
		//respond to clicks
			Log.d(LOGTAG, "********* view ID: " + view.getId());
			if(view.getId()==R.id.file_btn){
				//Creating the instance of PopupMenu  
	            PopupMenu popup = new PopupMenu(this, fileBtn);  
	            //Inflating the Popup using xml file  
	            popup.getMenuInflater().inflate(R.menu.file_menu, popup.getMenu());  
	           
	            //registering popup with OnMenuItemClickListener  
	            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
	             public boolean onMenuItemClick(MenuItem item) {
	            	
	            	// get prompts.xml view
	            	if(item.getItemId()==R.id.file_new){
	            		
	            		showPrompt("new");
	            		  
	            	}
	            	else if(item.getItemId()==R.id.file_open){
	            		
	            		showPrompt("open");
	            		  
	            	}
	            	else if(item.getItemId()==R.id.file_save){
	            		
	            		if(mStickyNotesName==""){
	            			showPrompt("saveas");
	            		}
	            		//showPrompt("new");
	            		else if(!stickyNotesView.saveFlipBook(mStickyNotesName)){
	            			Toast.makeText(StickyNotesMainActivity.this, "Unable to Save flip book", Toast.LENGTH_LONG).show();
	            		}
	            		  
	            	}
	            	else if(item.getItemId()==R.id.file_saveas){
	            		
	            		showPrompt("saveas");
	            		  
	            	}
	             
	              //Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();  
	              return true;  
	             }  
	            });  
	  
	            popup.show();//showing popup menu     
			}
			else if(view.getId()==R.id.edit_btn){
				//Creating the instance of PopupMenu  
	            PopupMenu popup = new PopupMenu(StickyNotesMainActivity.this, editBtn);  
	            //Inflating the Popup using xml file  
	            popup.getMenuInflater().inflate(R.menu.edit_menu, popup.getMenu());  
	           
	            //registering popup with OnMenuItemClickListener  
	            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
	             public boolean onMenuItemClick(MenuItem item) {
	            	 if(item.getItemId()==R.id.edit_copy){
	            		 stickyNotesView.copyNote();
	             		  
	             	}
	             	else if(item.getItemId()==R.id.edit_paste){
	             		
	             		stickyNotesView.pasteNote();
	             		  
	             	}
	             	else if(item.getItemId()==R.id.edit_insert){             		
	             		  stickyNotesView.insertN();
	             	}
	             	else if(item.getItemId()==R.id.edit_delete){
	             		stickyNotesView.deleteN();
	             		  
	             	}
	             // Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();  
	              return true;  
	             }  
	            });  
	  
	            popup.show();//showing popup menu     
			}
			else if(view.getId()==R.id.tools_btn){
				//Creating the instance of PopupMenu  
	            PopupMenu popup = new PopupMenu(StickyNotesMainActivity.this, toolsBtn);  
	            //Inflating the Popup using xml file  
	            popup.getMenuInflater().inflate(R.menu.tools_menu, popup.getMenu());  
	           
	            //registering popup with OnMenuItemClickListener  
	            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
	             public boolean onMenuItemClick(MenuItem item) {
	            	
	            	if(item.getItemId()==R.id.tools_brush){
	            		//Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show(); 
	            		final Dialog brushDialog = new Dialog(StickyNotesMainActivity.this);
	        			brushDialog.setTitle("Brush size:");
	        			brushDialog.setContentView(R.layout.brush_chooser);
	        			
	        			ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
	        			smallBtn.setOnClickListener(new OnClickListener(){
	        			    @Override
	        			    public void onClick(View v) {
	        			        stickyNotesView.setBrushSize(smallBrush);
	        			        stickyNotesView.setLastBrushSize(smallBrush);
	        			        stickyNotesView.setErase(false);
	        			        brushDialog.dismiss();
	        			    }
	        			});
	        			
	        			ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
	        			mediumBtn.setOnClickListener(new OnClickListener(){
	        			    @Override
	        			    public void onClick(View v) {
	        			        stickyNotesView.setBrushSize(mediumBrush);
	        			        stickyNotesView.setLastBrushSize(mediumBrush);
	        			        stickyNotesView.setErase(false);
	        			        brushDialog.dismiss();
	        			    }
	        			});
	        			
	        			ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
	        			largeBtn.setOnClickListener(new OnClickListener(){
	        			    @Override
	        			    public void onClick(View v) {
	        			        stickyNotesView.setBrushSize(largeBrush);
	        			        stickyNotesView.setLastBrushSize(largeBrush);
	        			        stickyNotesView.setErase(false);
	        			        brushDialog.dismiss();
	        			    }
	        			});
	        			
	        			brushDialog.show();
	            	}
	            	else if(item.getItemId()==R.id.tools_eraser){
	            		//Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show(); 
	            		final Dialog brushDialog = new Dialog(StickyNotesMainActivity.this);
	        			brushDialog.setTitle("Eraser size:");
	        			brushDialog.setContentView(R.layout.brush_chooser);
	        			
	        			ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
	        			smallBtn.setOnClickListener(new OnClickListener(){
	        			    @Override
	        			    public void onClick(View v) {
	        			        stickyNotesView.setErase(true);
	        			        stickyNotesView.setBrushSize(smallBrush);
	        			        brushDialog.dismiss();
	        			    }
	        			});
	        			ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
	        			mediumBtn.setOnClickListener(new OnClickListener(){
	        			    @Override
	        			    public void onClick(View v) {
	        			        stickyNotesView.setErase(true);
	        			        stickyNotesView.setBrushSize(mediumBrush);
	        			        brushDialog.dismiss();
	        			    }
	        			});
	        			ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
	        			largeBtn.setOnClickListener(new OnClickListener(){
	        			    @Override
	        			    public void onClick(View v) {
	        			        stickyNotesView.setErase(true);
	        			        stickyNotesView.setBrushSize(largeBrush);
	        			        brushDialog.dismiss();
	        			    }
	        			});
	        			brushDialog.show();
	            	
	            	}
	            	else if(item.getItemId()==R.id.tools_speed){
	            		showSpeedPrompt();
	            	
	            	}
	               
	              return true;  
	             }  
	            });  
	  
	            popup.show();//showing popup menu     
			}
			else if(view.getId()==R.id.color_btn){
				
	            final Dialog colorDialog = new Dialog(StickyNotesMainActivity.this);
	        	colorDialog.setTitle("Pick A Color");
	        	colorDialog.setContentView(R.layout.color_chooser);
	        	ImageButton brownBtn = (ImageButton)colorDialog.findViewById(R.id.brown);
				brownBtn.setOnClickListener(new OnClickListener(){
				    @Override
				    public void onClick(View v) {
				        paintClicked(v);
				        colorDialog.dismiss();
				    }
				});
				ImageButton redBtn = (ImageButton)colorDialog.findViewById(R.id.red);
				redBtn.setOnClickListener(new OnClickListener(){
				    @Override
				    public void onClick(View v) {
				       paintClicked(v);
				        colorDialog.dismiss();
				    }
				});
				ImageButton orangeBtn = (ImageButton)colorDialog.findViewById(R.id.orange);
				orangeBtn.setOnClickListener(new OnClickListener(){
				    @Override
				    public void onClick(View v) {
				        paintClicked(v);
				        colorDialog.dismiss();
				    }
				});
				ImageButton blackBtn = (ImageButton)colorDialog.findViewById(R.id.black);
				blackBtn.setOnClickListener(new OnClickListener(){
				    @Override
				    public void onClick(View v) {
				        paintClicked(v);
				        colorDialog.dismiss();
				    }
				});
				ImageButton whiteBtn = (ImageButton)colorDialog.findViewById(R.id.white);
				whiteBtn.setOnClickListener(new OnClickListener(){
				    @Override
				    public void onClick(View v) {
				        paintClicked(v);
				        colorDialog.dismiss();
				    }
				});
				ImageButton yellowBtn = (ImageButton)colorDialog.findViewById(R.id.yellow);
				yellowBtn.setOnClickListener(new OnClickListener(){
				    @Override
				    public void onClick(View v) {
				        paintClicked(v);
				        colorDialog.dismiss();
				    }
				});
				ImageButton skyBlueBtn = (ImageButton)colorDialog.findViewById(R.id.skyblue);
				skyBlueBtn.setOnClickListener(new OnClickListener(){
				    @Override
				    public void onClick(View v) {
				        paintClicked(v);
				        colorDialog.dismiss();
				    }
				});
				ImageButton blueBtn = (ImageButton)colorDialog.findViewById(R.id.blue);
				blueBtn.setOnClickListener(new OnClickListener(){
				    @Override
				    public void onClick(View v) {
				        paintClicked(v);
				        colorDialog.dismiss();
				    }
				});
				ImageButton greyBtn = (ImageButton)colorDialog.findViewById(R.id.grey);
				greyBtn.setOnClickListener(new OnClickListener(){
				    @Override
				    public void onClick(View v) {
				        paintClicked(v);
				        colorDialog.dismiss();
				    }
				});
				ImageButton magentaBtn = (ImageButton)colorDialog.findViewById(R.id.magenta);
				magentaBtn.setOnClickListener(new OnClickListener(){
				    @Override
				    public void onClick(View v) {
				        paintClicked(v);
				        colorDialog.dismiss();
				    }
				});
				ImageButton pinkBtn = (ImageButton)colorDialog.findViewById(R.id.pink);
				pinkBtn.setOnClickListener(new OnClickListener(){
				    @Override
				    public void onClick(View v) {
				        paintClicked(v);
				        colorDialog.dismiss();
				    }
				});
				ImageButton greenBtn = (ImageButton)colorDialog.findViewById(R.id.green);
				greenBtn.setOnClickListener(new OnClickListener(){
				    @Override
				    public void onClick(View v) {
				        paintClicked(v);
				        colorDialog.dismiss();
				    }
				});
	        	colorDialog.show();
	          
			}
			
			else if(view.getId()==R.id.next_btn){
				//Log.d(LOGTAG,"****CLICKED NEXT***");
				stickyNotesView.nextNote();
			
			}
			else if(view.getId()==R.id.back_btn){
				//Log.d(LOGTAG,"****CLICKED BACK***");
				stickyNotesView.prevNote();
				
			}
			else if(view.getId()==R.id.flip_btn){
				//Log.d(LOGTAG,"****CLICKED  flip it***");
				stickyNotesView.flipNotes();
				
			}
		}
		public void paintClicked(View view){
		    //use chosen color
			stickyNotesView.setErase(false);
			stickyNotesView.setBrushSize(stickyNotesView.getLastBrushSize());
			if(view!=currPaint){
				//update color
				ImageButton imgView = (ImageButton)view;
				String color = view.getTag().toString();
				stickyNotesView.setColor(color);
				//Toast.makeText(MainActivity.this,"Color : " + color,Toast.LENGTH_LONG).show(); 
				imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
				currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
				currPaint=(ImageButton)view;
			}
		}
		
		public void showPrompt(String fa){
			final String fileAction = fa;
			LayoutInflater li = LayoutInflater.from(StickyNotesMainActivity.this);
			View promptsView = li.inflate(R.layout.file_prompts, null);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				StickyNotesMainActivity.this);

		// set prompts.xml to alertdialog builder
			alertDialogBuilder.setView(promptsView);

			final EditText userInput = (EditText) promptsView
				.findViewById(R.id.editTextDialogUserInput);

		// set dialog message
			alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// get user input and set it to result
						// edit text
						//result.setText();
						mStickyNotesName = "" + userInput.getText();
						mStickyNotesName = mStickyNotesName.replaceAll("\\s+", "_");
						if(fileAction=="new"){
							stickyNotesView.startNew();
							Toast.makeText(StickyNotesMainActivity.this,"Created: " + mStickyNotesName,Toast.LENGTH_SHORT).show();
						}
						else if(fileAction =="open"){
							if(!stickyNotesView.loadFlipBook(mStickyNotesName)){
		            			Toast.makeText(StickyNotesMainActivity.this, "Unable to Load: " + mStickyNotesName, Toast.LENGTH_LONG).show();
		            		}
							//Toast.makeText(MainActivity.this,"file action open",Toast.LENGTH_SHORT).show();
						}
						else if(fileAction =="saveas"){
							if(!stickyNotesView.saveFlipBook(mStickyNotesName)){
		            			Toast.makeText(StickyNotesMainActivity.this, "Unable to Save flip book", Toast.LENGTH_LONG).show();
		            		}
							
						}
						
	            		//Toast.makeText(MainActivity.this,"You Entered: " + mStickyNotesName,Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				})
				.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				});
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();

		}
		
public void showSpeedPrompt(){
			
		LayoutInflater li = LayoutInflater.from(StickyNotesMainActivity.this);
		View promptsView = li.inflate(R.layout.speed_prompt, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
			StickyNotesMainActivity.this);

	// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView
			.findViewById(R.id.editSpeedDialogUserInput);

	// set dialog message
		alertDialogBuilder
			.setCancelable(false)
			.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// get user input and set it to result
					// edit text
					//result.setText();
					float speed = Float.parseFloat("" + userInput.getText());
					
					if(speed < .1){
						speed=10;
					}
					else if(speed > 10){
						speed = 1000;
					}
					else{
						mFlipSpeed = (int)(speed *100);
					}

					stickyNotesView.setSpeed(mFlipSpeed);
					Toast.makeText(StickyNotesMainActivity.this,"New Speed: " + mFlipSpeed +" mSec",Toast.LENGTH_SHORT).show();
					
            		//Toast.makeText(MainActivity.this,"You Entered: " + mStickyNotesName,Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				}
			})
			.setNegativeButton("Cancel",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();

	}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.sticky_notes_main, menu);
	return true;
}


}
