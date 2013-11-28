package com.example.pictalkfroyo;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;    
    private File[] list;
    private GestureDetector gestureDetector;
    
    static View viewFrameLayout;
    

    public ImageAdapter(Context c) {    	
        mContext = c;
        gestureDetector = new GestureDetector(mContext, new MyGestureDetector());
        
        File file = new File( MainActivity.currentDirectory ) ;       
        
        // get list of all directories and folders in current directory        
        list = file.listFiles();

        // directory pictures removed from list for convenient filling of grid
        int noOfDirectories = 0;
        for(int i=0; i<list.length; i++){
        	if (list[i].isDirectory()) noOfDirectories++;
        }
        
        File[] list1 = new File[list.length-noOfDirectories];
        int x=0;
        boolean flag=false;
        for(int i=0; i<list.length; i++){
        	if (list[i].isFile()){
        		String[] str = list[i].getName().split("\\.");
        		for(int j=0; j<list.length; j++){
        			if (list[j].isDirectory() && list[j].getName().equalsIgnoreCase(str[0])) flag=true;
        		}
        	}
        	if (!flag){
        		list1[x]=list[i];
        		x++;        		
        	}
        	flag = false;
        }
        list = list1;

    }

    public int getCount() {
        return list.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

    	
    	boolean isDirectory=false;
    	String itemName=null;
    	
    	// check if item is directory or picture
    	// set name for the item
    	if (list[position].isDirectory()){
    		isDirectory = true;
    		itemName = list[position].getName();
    	}
    	if (list[position].isFile()){
    		isDirectory = false;
    		String[] str = list[position].getName().split("\\.");
    		itemName = str[0];
    		
    		//confirm a directory doesn't exist with the name of the file
    		for(int i=0; i<list.length; i++){
    			if(list[i].isDirectory() && list[i].getName().equalsIgnoreCase(itemName)) return null;
    		}
    	}
    	
        FrameLayout fl=null;
        TextView tv=null;    	
	      ImageView imageView1=null;
	      ImageView imageView2=null;
	      GridView.LayoutParams params;
      
      if (convertView == null) { 
    	  
    	// when view object not recycled
    	// create new object
      	fl= new FrameLayout(mContext);
      	params = new GridView.LayoutParams(MainActivity.screenWidth/MainActivity.gridZoomFactor, MainActivity.screenWidth/MainActivity.gridZoomFactor); 
      	fl.setLayoutParams(params);
      	
      	tv= new TextView(mContext);
      	params = new GridView.LayoutParams(MainActivity.screenWidth/MainActivity.gridZoomFactor, MainActivity.screenWidth/MainActivity.gridZoomFactor); 
      	tv.setLayoutParams(params);
      	tv.setGravity(Gravity.BOTTOM|Gravity.RIGHT);
      	tv.setPadding(8, 8, 8, 8);
    	  
          imageView1 = new ImageView(mContext);
          params = new GridView.LayoutParams(MainActivity.screenWidth/MainActivity.gridZoomFactor, MainActivity.screenWidth/MainActivity.gridZoomFactor); 
          imageView1.setLayoutParams(params);
          imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
          imageView1.setPadding(8, 8, 8, 8);
          
          // for directory folder icon has to be displayed
          if (isDirectory){
              imageView2 = new ImageView(mContext);
              params = new GridView.LayoutParams(MainActivity.screenWidth/(MainActivity.gridZoomFactor*4), MainActivity.screenWidth/(MainActivity.gridZoomFactor*4));
              imageView2.setLayoutParams(params);
              imageView2.setScaleType(ImageView.ScaleType.FIT_START);        	  
          }         
          
      } else {
    	  
    	  //use recycled view object
          fl = (FrameLayout) convertView;
          if (fl.getChildCount()==3){
	          imageView1 = (ImageView)fl.getChildAt(0);
	          imageView2 = (ImageView)fl.getChildAt(1);
	          tv = (TextView)fl.getChildAt(2);
          }
          if (fl.getChildCount()==2){
	          imageView1 = (ImageView)fl.getChildAt(0);
	          tv = (TextView)fl.getChildAt(1);        	  
          }
          
          //remove all children before adding the new ones
          fl.removeAllViews();
      }      
      
      String extension = Methods.getFileExtension(new File(MainActivity.currentDirectory), itemName);
      Bitmap bmp = BitmapFactory.decodeFile(MainActivity.currentDirectory+"/"+itemName+"." + extension);      
      imageView1.setImageBitmap(bmp);

      // for directories add folder icon image
      if (isDirectory) imageView2.setImageResource(R.drawable.folder);
      
      // get current language
      String str = null;
      try {
      	str = MainActivity.getLocalLanguage(itemName);
			
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}
      
      // set typeface for text
      Typeface typeFace = null;
      //if (MainActivity.language.equalsIgnoreCase("malayalam")) typeFace = Typeface.createFromAsset(mContext.getAssets(),"AnjaliOldLipi.ttf");
      //if (MainActivity.language.equalsIgnoreCase("hindi")) typeFace = Typeface.createFromAsset(mContext.getAssets(),"mangal.ttf");
      if (!MainActivity.language.equalsIgnoreCase("english")) typeFace = Typeface.createFromFile(MainActivity.FONTS_DIRECTORY + "/" + MainActivity.language + "." + MainActivity.FONT_FORMAT);
      
      tv.setText(str);
      tv.setTypeface(typeFace);

      // add components to the layout
      fl.addView(imageView1);
      if (isDirectory) fl.addView(imageView2);
      fl.addView(tv);
      if (isDirectory) fl.setTag("dir-"+itemName);
      else fl.setTag("file-"+itemName);
      
          	
    	fl.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View view, MotionEvent event) {

				ImageAdapter.viewFrameLayout = view;
				gestureDetector.onTouchEvent(event);
				return true;
			}
		});
    	
        return fl;
    }
    
    public class MyGestureDetector extends SimpleOnGestureListener {

    	@Override
    	public boolean onDoubleTap(MotionEvent e) {

    		Log.w("com.example.pictalk","double tapped");
    		// disable editing in favorites
    		if (MainActivity.inFavorites) return true;
    		
    		//add to favourites
			FrameLayout fl1 = (FrameLayout) viewFrameLayout;
			boolean isDirectory = false;
			String itemName = null;
			
			// get item name
			// see if item is directory or file
			String tag = (String)fl1.getTag();
			String[] parts = tag.split("-");
    		
			if(parts[0].equalsIgnoreCase("dir")){
				isDirectory = true;
			}
			if(parts[0].equalsIgnoreCase("file")){
				isDirectory = false;
			}
			itemName = parts[1];
			
			// on picture click, add to tray
			if(!isDirectory){ 
				String extension = Methods.getFileExtension(new File(MainActivity.currentDirectory), itemName);
				String srcFile = MainActivity.currentDirectory+"/"+itemName+ "." + extension;
				//String destFile = MainActivity.FAVENGLISH_DIRECTORY+"/"+itemName+ "." + MainActivity.PICTURE_FORMAT;
				String destFile = MainActivity.FAVORITES_DIRECTORY+"/"+itemName+ "." + extension;
					try {
						Methods.copyDirectory(new File(srcFile), new File(destFile));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				Toast.makeText(mContext, "Added "+itemName+" to favorites", Toast.LENGTH_SHORT).show();

			}
			
			// on folder click, open the folder
			if(isDirectory){
				
				String srcDirectory = MainActivity.currentDirectory+"/"+itemName;
				String destDirectory = MainActivity.FAVORITES_DIRECTORY+"/"+itemName;
				String extension = Methods.getFileExtension(new File(MainActivity.currentDirectory), itemName);
				String srcFile = MainActivity.currentDirectory+"/"+itemName+ "." + extension;
				String destFile = MainActivity.FAVORITES_DIRECTORY+"/"+itemName+ "." + extension;
				
				try {
					Methods.copyDirectory(new File(srcDirectory), new File(destDirectory));
					Methods.copyDirectory(new File(srcFile), new File(destFile));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Toast.makeText(mContext, "Added "+itemName+" to favorites", Toast.LENGTH_SHORT).show();
			}
    		
    		return true;
    	}

    	@Override
    	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
    			float velocityY) {

    		Log.w("com.example.pictalk","flinged");
    		return true;
    		
    	}

    	@Override
    	public void onLongPress(MotionEvent e) {

    		Log.w("com.example.pictalk","long pressed");    		
			// on long clicking a folder, move folder to tray
    		
			FrameLayout fl1 = (FrameLayout) viewFrameLayout;
			boolean isDirectory = false;
			String itemName = null;
			
			// get item name
			// see if item is directory or file
			String tag = (String)fl1.getTag();
			String[] parts = tag.split("-");
			
			if(parts[0].equalsIgnoreCase("dir")){
				isDirectory = true;
			}
			if(parts[0].equalsIgnoreCase("file")){
				isDirectory = false;
			}
			itemName = parts[1];
			
			if(isDirectory){
				
		            ImageView imageView = new ImageView(mContext);
		            LinearLayout.LayoutParams layoutParams;
		            layoutParams = new LinearLayout.LayoutParams(MainActivity.screenHeight*MainActivity.listZoomFactor/10, MainActivity.screenHeight*MainActivity.listZoomFactor/10);
		            imageView.setLayoutParams(layoutParams);
		            imageView.setBackgroundColor(Color.BLACK);     
		            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		            imageView.setPadding(8, 8, 8, 8);
		            
		            String extension = Methods.getFileExtension(new File(MainActivity.currentDirectory), itemName);
		            Bitmap bmp = BitmapFactory.decodeFile(MainActivity.currentDirectory+"/"+itemName+"." + extension);      
			        imageView.setImageBitmap(bmp);
			        imageView.setTag(itemName);
			        
			        MainActivity.linearLayout22.addView(imageView);
			}
			
			// do nothing for long clicking a picture					
    	
    	}

    	@Override
    	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
    			float distanceY) {

    		Log.w("com.example.pictalk","scrolled");

    		// disable editing in favorites
    		if (MainActivity.inFavorites) return true;
    		
    		// edit an item clicked    		    		
			FrameLayout fl1 = (FrameLayout) viewFrameLayout;
			boolean isDirectory = false;
			String itemName = null;
			
			// get item name
			// see if item is directory or file
			String tag = (String)fl1.getTag();
			String[] parts = tag.split("-");
			
			if(parts[0].equalsIgnoreCase("dir")){
				isDirectory = true;
			}
			if(parts[0].equalsIgnoreCase("file")){
				isDirectory = false;
			}
			itemName = parts[1];
			
			Intent intent = new Intent(mContext, EditImageActivity.class);
			intent.putExtra("itemName",	itemName);
			intent.putExtra("isDirectory", isDirectory);
			mContext.startActivity(intent);
			
    		return true;

    	}

    	@Override
    	public boolean onSingleTapConfirmed(MotionEvent e) {

    		Log.w("com.example.pictalk","single tapped");
    		// on single click select a picture or open a folder
    		
			FrameLayout fl1 = (FrameLayout) viewFrameLayout;
			boolean isDirectory = false;
			String itemName = null;
			
			// get item name
			// see if item is directory or file
			String tag = (String)fl1.getTag();
			String[] parts = tag.split("-");
    		
			if(parts[0].equalsIgnoreCase("dir")){
				isDirectory = true;
			}
			if(parts[0].equalsIgnoreCase("file")){
				isDirectory = false;
			}
			itemName = parts[1];
			
			// on picture click, add to tray
			if(!isDirectory){		          
				
		            ImageView imageView = new ImageView(mContext);
		            LinearLayout.LayoutParams layoutParams;
		            layoutParams = new LinearLayout.LayoutParams(MainActivity.screenHeight*MainActivity.listZoomFactor/10, MainActivity.screenHeight*MainActivity.listZoomFactor/10);
		            imageView.setLayoutParams(layoutParams);
		            imageView.setBackgroundColor(Color.BLACK);     
		            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		            imageView.setPadding(8, 8, 8, 8);
				
		            String extension = Methods.getFileExtension(new File(MainActivity.currentDirectory), itemName);
		            Bitmap bmp = BitmapFactory.decodeFile(MainActivity.currentDirectory+"/"+itemName+ "." + extension);      
			        imageView.setImageBitmap(bmp);
			        imageView.setTag(itemName);
			        
			        MainActivity.linearLayout22.addView(imageView);
			}
			
			// on folder click, open the folder
			if(isDirectory){
				
				MainActivity.currentDirectory = MainActivity.currentDirectory + "/" + itemName;
				
		        String str = null;
		        try {
		        	str = MainActivity.getLocalLanguage(itemName);
					
				} catch (FileNotFoundException e1) {
					
					e1.printStackTrace();
				}
		        Typeface typeFace = null;
		        //if (MainActivity.language.equalsIgnoreCase("malayalam")) typeFace = Typeface.createFromAsset(mContext.getAssets(),"AnjaliOldLipi.ttf");
		        //if (MainActivity.language.equalsIgnoreCase("hindi")) typeFace = Typeface.createFromAsset(mContext.getAssets(),"mangal.ttf");
		        if (!MainActivity.language.equalsIgnoreCase("english")) typeFace = Typeface.createFromFile(MainActivity.FONTS_DIRECTORY + "/" + MainActivity.language + "." + MainActivity.FONT_FORMAT);
		        
		        
		        MainActivity.tvNOW.setText(str);
		        MainActivity.tvNOW.setTypeface(typeFace);				
				MainActivity.gridView.setAdapter(new ImageAdapter(mContext));
			}
    		    		
    		
    		return true;

    	}
    	

    }


}
