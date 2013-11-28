package com.example.pictalkfroyo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	// define constants
	public static final String PREFS_NAME = "MyPrefsFile";
//	public static final String MALAYALAM_FONT = "AnjaliOldLipi.ttf";
//	public static final String HINDI_FONT = "mangal.ttf";
//	public static final String AUDIO_FORMAT = "mp3"; 
	public static final String PICTURE_FORMAT = "jpeg"; 
	public static final String FONT_FORMAT = "ttf"; 
	public static final String SINGLE_SPACE = " ";
	
	static boolean inFavorites = false;
	static String[] languages;
	static String language = "english";
	static int languagePosition = 0;
	static final String ROOT_SD = Environment.getExternalStorageDirectory().toString();
	static final String PICTALK_DIRECTORY = ROOT_SD + "/PicTalk";
	static final String HOME_DIRECTORY = ROOT_SD + "/PicTalk/home";
	static String currentDirectory = HOME_DIRECTORY;
	static final String WORDS_FILE = "words.txt";
	static final String LANGUAGES_FILE = "languages.txt";
	static final String DEFAULT_AUDIO_FILE = "beep.mp3";
	static final String AUDIOENGLISH_DIRECTORY = ROOT_SD + "/PicTalk/english";
	static final String AUDIOMALAYALAM_DIRECTORY = ROOT_SD + "/PicTalk/malayalam";
	static final String AUDIOHINDI_DIRECTORY = ROOT_SD + "/PicTalk/hindi";
	static final String FAVORITES_DIRECTORY = ROOT_SD + "/PicTalk/favorites";
	static final String FONTS_DIRECTORY = PICTALK_DIRECTORY + "/fonts";
	static final String TEMP_DIRECTORY = PICTALK_DIRECTORY + "/temp";
	// zoom factors
	static int listZoomFactor = 1; // 1 or 2 - 1/10 or 2/10 of screen height
	static int gridZoomFactor = 3; // 3 to 6 images in a row
	static final int LISTZOOMFACTOR_MIN = 1;
	static final int LISTZOOMFACTOR_MAX = 3;
	static final int GRIDZOOMFACTOR_MIN = 2;
	static final int GRIDZOOMFACTOR_MAX = 7;
	


	// constants
    static final String DELETE = "<-"; 
    static final String PLUS = "(+)";
    static final String MINUS = "(-)";
    static final String HOME = "START";
    static final String NOW = "NOW";
    static final String BACK = "BACK";
    static final String SETTINGS = "(*)";
    
    // activity variables
    static TextView tvNOW;
    static LinearLayout linearLayout22;
    static GridView gridView;

    static MediaPlayer mediaPlayer;
    static String[] songURLs;
    static int songNumber;

	static int screenHeight; 
	static int screenWidth;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get language information
        File file = new File(PICTALK_DIRECTORY+"/"+LANGUAGES_FILE);
		FileInputStream fis;
		try { 
			fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(fis));
			String aDataRow = "";
			//String aBuffer = "";
			while ((aDataRow = br.readLine()) != null) {
				languages = aDataRow.split(";");
				//aBuffer += aDataRow + "\n";
			}
			fis.close();
			br.close();
		
		
//			file = new File(PICTALK_DIRECTORY+"/"+"temp.txt");
//			file.createNewFile();
//			FileOutputStream fos = new FileOutputStream(file);
//			OutputStreamWriter osw = 
//									new OutputStreamWriter(fos);
//			//myOutWriter.append(txtData.getText());
//			osw.write(aBuffer);
//			osw.close();
//			fos.close();
			
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        language = settings.getString("language", "english");
        
        // set language position
        for(int i=0; i< languages.length; i++){
        	if(languages[i].equalsIgnoreCase(language)) languagePosition = i;
        }
        
        //Get screen height and width
        screenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
        screenWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
        
        // set layout for the whole activity
        
        // linearlayout1 - whole screen
        LinearLayout linearLayout1 = new LinearLayout(this);
        linearLayout1.setOrientation(LinearLayout.VERTICAL);
        linearLayout1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        linearLayout1.setBackgroundColor(Color.WHITE);
        
        // linear layout2 - upper tray
        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, screenHeight*listZoomFactor/10));

        // linear layout21 - max and min buttons 
        LinearLayout linearLayout21 = new LinearLayout(this);
        linearLayout21.setOrientation(LinearLayout.VERTICAL);
        linearLayout21.setLayoutParams(new LayoutParams(screenWidth/10, screenHeight*listZoomFactor/10));
       
        // maximize button
        ImageView ivPLUS = new ImageView(this);
        LinearLayout.LayoutParams layoutParams;
        layoutParams = new LinearLayout.LayoutParams(screenWidth*1/10, screenHeight*listZoomFactor/20);
        ivPLUS.setLayoutParams(layoutParams);
        ivPLUS.setScaleType(ImageView.ScaleType.CENTER_CROP);        
        ivPLUS.setPadding(8, 8, 8, 8);
        ivPLUS.setImageResource(R.drawable.plus);
        ivPLUS.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				
				if(gridZoomFactor>GRIDZOOMFACTOR_MIN){
					gridZoomFactor--;
					gridView.setColumnWidth(screenWidth/gridZoomFactor);
					gridView.setAdapter(new ImageAdapter(MainActivity.this));
				}
				
			}
		});
        
        // minimise button
        ImageView ivMINUS = new ImageView(this);
        layoutParams = new LinearLayout.LayoutParams(screenWidth*1/10, screenHeight*listZoomFactor/20);
        ivMINUS.setLayoutParams(layoutParams);
        ivMINUS.setScaleType(ImageView.ScaleType.CENTER_CROP);        
        ivMINUS.setPadding(8, 8, 8, 8);
        ivMINUS.setImageResource(R.drawable.minus);
        ivMINUS.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				if(gridZoomFactor<GRIDZOOMFACTOR_MAX){
					gridZoomFactor++;
					gridView.setColumnWidth(screenWidth/gridZoomFactor);
					gridView.setAdapter(new ImageAdapter(MainActivity.this));
				}
				
			}
		});


        // add max and min to layout
        linearLayout21.addView(ivPLUS);
        linearLayout21.addView(ivMINUS);
        
        // for horizontally scrolling tray
        HorizontalScrollView horScrollView = new HorizontalScrollView(this);
        horScrollView.setLayoutParams(new LayoutParams(screenWidth*8/10, screenHeight*listZoomFactor/10));
        horScrollView.setBackgroundColor(Color.WHITE);
        
        // tray
        linearLayout22 = new LinearLayout(this);
        linearLayout22.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout22.setLayoutParams(new LayoutParams(screenWidth*8/10, screenHeight*listZoomFactor/10));
        
        // add tray to scroll view
        horScrollView.addView(linearLayout22);
        
        // delete button
        ImageView ivDELETE = new ImageView(this);
        layoutParams = new LinearLayout.LayoutParams(screenWidth*1/10, screenHeight*listZoomFactor/10);
        ivDELETE.setLayoutParams(layoutParams);
        ivDELETE.setScaleType(ImageView.ScaleType.CENTER_CROP);        
        ivDELETE.setPadding(8, 8, 8, 8);
        ivDELETE.setImageResource(R.drawable.delete);
        ivDELETE.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {

				if (linearLayout22.getChildCount()!=0){
					linearLayout22.removeViewAt(linearLayout22.getChildCount()-1);
				}
				
			}
		});
               
        // add max,min; tray; delete to layout
        linearLayout2.addView(linearLayout21);
        linearLayout2.addView(horScrollView);
        linearLayout2.addView(ivDELETE);
        
        
        // middle panel
        LinearLayout linearLayout3 = new LinearLayout(this);
        linearLayout3.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout3.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, screenHeight/20));

        
        // settings button
        ImageView ivSETTINGS = new ImageView(this);
        layoutParams = new LinearLayout.LayoutParams(screenWidth*1/10, screenHeight*listZoomFactor/20);
        ivSETTINGS.setLayoutParams(layoutParams);
        ivSETTINGS.setScaleType(ImageView.ScaleType.CENTER_CROP);        
        ivSETTINGS.setPadding(8, 8, 8, 8);
        ivSETTINGS.setImageResource(R.drawable.settings);
        ivSETTINGS.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {				
				//MainActivity.this.onCreateDialog(4321);
				MainActivity.this.showDialog(1234);
				
			}
		});

        
        // add picture
        ImageView ivADDPICTURE = new ImageView(this);
        layoutParams = new LinearLayout.LayoutParams(screenWidth*2/10, screenHeight*listZoomFactor/20);
        ivADDPICTURE.setLayoutParams(layoutParams);
        ivADDPICTURE.setScaleType(ImageView.ScaleType.CENTER_CROP);        
        ivADDPICTURE.setPadding(8, 8, 8, 8);
        ivADDPICTURE.setImageResource(R.drawable.add);
        ivADDPICTURE.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				
				Intent intent = new Intent(MainActivity.this, AddImageActivity.class);
				startActivity(intent);
			}
		});

        // current folder text
        tvNOW = new TextView(this);
        tvNOW.setWidth(screenWidth*3/10);
        tvNOW.setHeight(screenHeight/20);
        String[] parts = MainActivity.currentDirectory.split("\\/");
        
        String str = null;
        try {
        	str = getLocalLanguage(parts[parts.length-1]);
			
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}
        Typeface typeFace = null;
        //if (language.equalsIgnoreCase("malayalam")) typeFace = Typeface.createFromAsset(getAssets(),"AnjaliOldLipi.ttf");
        //if (language.equalsIgnoreCase("hindi")) typeFace = Typeface.createFromAsset(getAssets(),"mangal.ttf");
        //if (!language.equalsIgnoreCase("english")) Typeface.createFromAsset(getAssets(),language + "." + FONT_FORMAT);
        if (!language.equalsIgnoreCase("english")) typeFace = Typeface.createFromFile(FONTS_DIRECTORY + "/" + language + "." + FONT_FORMAT);
         
        
        tvNOW.setText(str);
        tvNOW.setTypeface(typeFace);
        
        tvNOW.setGravity(Gravity.CENTER);
        //tvNOW.setBackgroundColor(Color.MAGENTA);

        // favorites button
        ImageView ivFAV = new ImageView(this);
        layoutParams = new LinearLayout.LayoutParams(screenWidth*2/10, screenHeight*listZoomFactor/20);
        ivFAV.setLayoutParams(layoutParams);
        ivFAV.setScaleType(ImageView.ScaleType.CENTER_CROP);        
        ivFAV.setPadding(8, 8, 8, 8);
        ivFAV.setImageResource(R.drawable.favorites);
        ivFAV.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				
				MainActivity.inFavorites = true;
				currentDirectory = FAVORITES_DIRECTORY;
				String[] parts = currentDirectory.split("/");
				
		        String str = null;
		        try {
		        	str = getLocalLanguage(parts[parts.length-1]);
					
				} catch (FileNotFoundException e1) {
					
					e1.printStackTrace();
				}
		        Typeface typeFace = null;
		        //if (language.equalsIgnoreCase("malayalam")) typeFace = Typeface.createFromAsset(getAssets(),MALAYALAM_FONT);
		        //if (language.equalsIgnoreCase("hindi")) typeFace = Typeface.createFromAsset(getAssets(),HINDI_FONT);
		        if (!language.equalsIgnoreCase("english")) typeFace = Typeface.createFromFile(FONTS_DIRECTORY + "/" + language + "." + FONT_FORMAT);		        
		        tvNOW.setText(str);
		        tvNOW.setTypeface(typeFace);
							
				gridView.setAdapter(new ImageAdapter(MainActivity.this));
			}
		});
        
        // start button
        ImageView ivSTART = new ImageView(this);
        layoutParams = new LinearLayout.LayoutParams(screenWidth*2/10, screenHeight*listZoomFactor/20);
        ivSTART.setLayoutParams(layoutParams);
        ivSTART.setScaleType(ImageView.ScaleType.CENTER_CROP);        
        ivSTART.setPadding(8, 8, 8, 8);
        ivSTART.setImageResource(R.drawable.start);
        ivSTART.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				
				MainActivity.inFavorites = false;
				currentDirectory = HOME_DIRECTORY;
				String[] parts = currentDirectory.split("\\/");
				
		        String str = null;
		        try {
		        	str = getLocalLanguage(parts[parts.length-1]);
					
				} catch (FileNotFoundException e1) {
					
					e1.printStackTrace();
				}
		        Typeface typeFace = null;
		        //if (language.equalsIgnoreCase("malayalam")) typeFace = Typeface.createFromAsset(getAssets(),MALAYALAM_FONT);
		        //if (language.equalsIgnoreCase("hindi")) typeFace = Typeface.createFromAsset(getAssets(),HINDI_FONT);
		        if (!language.equalsIgnoreCase("english")) typeFace = Typeface.createFromFile(FONTS_DIRECTORY + "/" + language + "." + FONT_FORMAT);
		        
		        
		        tvNOW.setText(str);
		        tvNOW.setTypeface(typeFace);
							
				gridView.setAdapter(new ImageAdapter(MainActivity.this));
			}
		});
        
        // add buttons to middle panel
        linearLayout3.addView(ivSETTINGS);
        linearLayout3.addView(ivADDPICTURE);
        linearLayout3.addView(tvNOW);
        linearLayout3.addView(ivFAV);
        linearLayout3.addView(ivSTART);
        
                       
        // gridview 
        gridView = new GridView(this);
        gridView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));    
        gridView.setBackgroundColor(Color.BLACK);
        gridView.setHorizontalSpacing(5);
        gridView.setVerticalSpacing(5);
        gridView.setGravity(Gravity.CENTER);
        gridView.setNumColumns(GridView.AUTO_FIT);
        gridView.setColumnWidth(screenWidth/gridZoomFactor);
        gridView.setAdapter(new ImageAdapter(this));
        
        // add top panel, middle panel and gridview to screen
        linearLayout1.addView(linearLayout2);
        linearLayout1.addView(linearLayout3);
        linearLayout1.addView(gridView);
                
        setContentView(linearLayout1);
        
        // talk when clicking tray
        linearLayout22.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
				
				
				LinearLayout layout = (LinearLayout) view;
				songNumber = 1;
				String prefix = null;

				prefix = PICTALK_DIRECTORY + "/" + language;
				//if (language.equalsIgnoreCase("english")) prefix = AUDIOENGLISH_DIRECTORY;
				//if (language.equalsIgnoreCase("malayalam")) prefix = AUDIOMALAYALAM_DIRECTORY;
				//if (language.equalsIgnoreCase("hindi")) prefix = AUDIOMALAYALAM_DIRECTORY;
				songURLs = new String[layout.getChildCount()];
				for(int i=0; i<layout.getChildCount(); i++){
					ImageView iv = (ImageView) layout.getChildAt(i);
					//songURLs[i] = prefix + "/" + (String) iv.getTag() + "." + AUDIO_FORMAT;
					String fileName = (String)iv.getTag();
					String audioFormat = Methods.getFileExtension(new File(prefix), fileName);
					if (audioFormat==null) songURLs[i] = PICTALK_DIRECTORY + "/" + DEFAULT_AUDIO_FILE;
					else songURLs[i] = prefix + "/" + fileName + "." + audioFormat;		
				}
				mediaPlayer = new MediaPlayer();
				try {
					mediaPlayer.setDataSource(songURLs[0]);
					mediaPlayer.prepare();
					Log.w("com.example.pictalk","before start");
					mediaPlayer.start();
					Log.w("com.example.pictalk","after start");
				} catch (IllegalArgumentException e) {
					
					e.printStackTrace();
				} catch (IllegalStateException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				// pictures continue to talk one after the other 
				mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					
					public void onCompletion(MediaPlayer arg0) {
						
						Log.w("com.example.pictalk","before stop");
						mediaPlayer.stop();
						Log.w("com.example.pictalk","before stop");
						if (songNumber < songURLs.length){
							songNumber++;
							try {
								mediaPlayer.reset();
								mediaPlayer.setDataSource(songURLs[songNumber-1]);
								mediaPlayer.prepare();
								mediaPlayer.start();
							} catch (IllegalArgumentException e) {
								
								e.printStackTrace();
							} catch (IllegalStateException e) {
								
								e.printStackTrace();
							} catch (IOException e) {
								
								e.printStackTrace();
							}
							
						}
						else mediaPlayer.release();
						
					}
				});
				
			}
		});

	}

    // function returns regional word for the given english word
    static String getLocalLanguage(String strEnglish) throws FileNotFoundException{
    	
    	
    	File file = new File(PICTALK_DIRECTORY + "/" + WORDS_FILE);
    	Scanner scanner = new Scanner(file);

    	//now read the file line by line...
    	while (scanner.hasNextLine()) {
    	    String line = scanner.nextLine();
    	    String delim = ";";
    	    String[] splitstrings = line.split(delim);
    	    if (splitstrings[0].trim().equalsIgnoreCase(strEnglish)){
    	    	//if(language.equalsIgnoreCase("malayalam")) return splitstrings[1].trim();
    	    	//if(language.equalsIgnoreCase("hindi")) return splitstrings[2].trim();
    	    	return splitstrings[MainActivity.languagePosition].trim();
    	    }
    	    
    	}
    	return strEnglish;
    	
    }

    // called when settings button clicked to change language
	@Override
	protected Dialog onCreateDialog(int id) {		
		
		final EditText etNewLanguage = new EditText(this); 
	    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
	    builder.setTitle(R.string.select_language)
	    	   .setView(etNewLanguage)
	    	   .setPositiveButton("Add language", new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					String newLanguage = etNewLanguage.getText().toString();
					if (newLanguage.equalsIgnoreCase("")){
						Toast.makeText(MainActivity.this, "Enter language", Toast.LENGTH_SHORT).show();
						return;
					}
					
					// add language folder
					new File(MainActivity.PICTALK_DIRECTORY + "/" + newLanguage).mkdirs();
					
					// add new language to languages file
					try {
						File file = new File(MainActivity.PICTALK_DIRECTORY + "/" + MainActivity.LANGUAGES_FILE);
						FileOutputStream fos;
						fos = new FileOutputStream(file);
						OutputStreamWriter writer = new OutputStreamWriter(fos);
						String line="";
						for(int i=0; i<MainActivity.languages.length; i++){
							line = line + MainActivity.languages[i] + ";";
						}
						line = line + newLanguage;
						writer.write(line);
						writer.close();
						fos.close();
						
						startActivity(new Intent(MainActivity.this, MainActivity.class));
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// add single space default words for existing words for new language
					File wordsFile = new File(MainActivity.PICTALK_DIRECTORY + "/" + MainActivity.WORDS_FILE);
					File tmpFile = new File(MainActivity.TEMP_DIRECTORY + "/" + MainActivity.WORDS_FILE);
					FileOutputStream fos;
					OutputStreamWriter writer;
					FileInputStream fis;
					BufferedReader reader;				
					try {
						
						fos = new FileOutputStream(tmpFile);
						writer = new OutputStreamWriter(fos);
						fis = new FileInputStream(wordsFile);
						reader = new BufferedReader(new InputStreamReader(fis));

						String aDataRow = "";
						while ((aDataRow = reader.readLine()) != null) {
							writer.write(aDataRow + "; \n");							
						}					
						reader.close();
						fis.close();
						writer.close();
						fos.close();
						
						Methods.copyDirectory(tmpFile, wordsFile);	
						tmpFile.delete();
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		
					
					Toast.makeText(MainActivity.this, newLanguage + " language added", Toast.LENGTH_SHORT).show();

					
				}
	    	   }) 
	           .setItems(languages, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	               // The 'which' argument contains the index position
	               // of the selected item
	            	      SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	            	      SharedPreferences.Editor editor = settings.edit();
	            	   String selectedValue = null;
	            	   for(int i=0; i<languages.length; i++){
	            		   if(which==i) selectedValue = languages[i];
	            	   } 
//	            	   switch(which){   
//	            	   case 0 : selectedValue = languages[0];
//	            	   			break;
//	            	   case 1 : selectedValue = languages[1];
//	            	   			break;
//	            	   case 2 :	selectedValue = "hindi";
//	            	   			break;
//	            	   }
	            	   editor.putString("language", selectedValue);
	            	   editor.commit();
	            	   finish();
//	            	   language = selectedValue; 
//	                   // set language position
//	                   for(int i=0; i< languages.length; i++){
//	                   	if(languages[i].equalsIgnoreCase(language)) languagePosition = i;
//	                   }
	            	   startActivity(new Intent(MainActivity.this, MainActivity.class));
	         
	           }
	    });
	    builder.create();
	    return builder.show();
	}
    
    
    
}

