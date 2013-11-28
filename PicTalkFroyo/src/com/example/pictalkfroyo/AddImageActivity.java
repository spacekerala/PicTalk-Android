package com.example.pictalkfroyo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class AddImageActivity extends Activity {
	
	//static int i = 0; //iterator
	static int REQEUST_CODE_CAMERA = 1000;
	static int REQEUST_CODE_AUDIO = 2000;
	static int REQEUST_CODE_PIC_SELECT = 3000;
	static int REQEUST_CODE_AUDIO_SELECT = 4000;
    
    CheckBox cbFolder;
    EditText etPicture;
    ImageView ivCamera;
    ImageView ivPicBrowse;  
    EditText[] arrETWord;
    EditText[] arrETAudio;
    ImageView[] arrIVMic;
    ImageView[] arrIVAudioBrowse;
    Button button;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_image);
                
        // enter words in different languages
        arrETWord = new EditText[MainActivity.languages.length];     
        //etPicture = new EditText[MainActivity.languages.length];
        //ivCamera = new ImageView[MainActivity.languages.length];
        //ivPicBrowse = new ImageView[MainActivity.languages.length];  
        arrETAudio = new EditText[MainActivity.languages.length];
        arrIVMic = new ImageView[MainActivity.languages.length];
        arrIVAudioBrowse = new ImageView[MainActivity.languages.length];
        View ruler;
        LinearLayout linearLayout;
        TextView textView;
        
        // scroll view to have scroll
        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        // linearlayout1 - whole screen
        LinearLayout linearLayout1 = new LinearLayout(this);
        linearLayout1.setOrientation(LinearLayout.VERTICAL);
        linearLayout1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        linearLayout1.setBackgroundColor(Color.WHITE);
        
        cbFolder = new CheckBox(this);
        cbFolder.setText(R.string.folder);
        linearLayout1.addView(cbFolder);

        // capture or select picture
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, MainActivity.screenHeight*2/20));
        

    	etPicture =  new EditText(this);
    	etPicture.setEnabled(false);
    	//etPicture.setClickable(false);
    	//etPicture.setSelected(false);
    	etPicture.setLayoutParams(new LayoutParams(MainActivity.screenWidth*6/10, LayoutParams.MATCH_PARENT));
    	
    	ivCamera = new ImageView(this);            
        ivCamera.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.screenWidth*2/10, LayoutParams.MATCH_PARENT));
        ivCamera.setScaleType(ImageView.ScaleType.CENTER_CROP);        
        ivCamera.setPadding(8, 8, 8, 8);
        ivCamera.setImageResource(R.drawable.camera);
        //ivCamera.setTag(Integer.valueOf(i));
        ivCamera.setOnClickListener(new View.OnClickListener() {    			
			public void onClick(View view) {				    				
				
				// check if english word entered
				if( arrETWord[0].getText().toString().equalsIgnoreCase("")){
					Toast.makeText(AddImageActivity.this, "Enter english word to continue", Toast.LENGTH_SHORT).show();
					return;
				}
				
				// tag keeping language position
				//int tag = (Integer)view.getTag();
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//String filePath = MainActivity.currentDirectory + "/" + arrETWord[0].getText() + "." + MainActivity.PICTURE_FORMAT;
				//String filePath = MainActivity.TEMP_DIRECTORY + "/" + arrETWord[0].getText() + "." + MainActivity.PICTURE_FORMAT;
				String englishWord = arrETWord[0].getText().toString();
				String filePath = MainActivity.TEMP_DIRECTORY + "/" + englishWord + "." + MainActivity.PICTURE_FORMAT;
				Log.w("com.example.pictalk", "filepath="+filePath);
				intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(filePath)));
				Log.w("com.example.pictalk", "uri="+Uri.fromFile(new File(filePath)).toString());
				//etPicture.setText(filePath);
				startActivityForResult(intent, AddImageActivity.REQEUST_CODE_CAMERA);
			}
		});

    	ivPicBrowse = new ImageView(this);            
    	ivPicBrowse.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.screenWidth*2/10, LayoutParams.MATCH_PARENT));
    	ivPicBrowse.setScaleType(ImageView.ScaleType.CENTER_CROP);        
    	ivPicBrowse.setPadding(8, 8, 8, 8);
    	ivPicBrowse.setImageResource(R.drawable.browse_file);
    	ivPicBrowse.setOnClickListener(new View.OnClickListener() {    			
			public void onClick(View arg0) {				
				// check if english word entered
				if( arrETWord[0].getText().toString().equalsIgnoreCase("")){
					Toast.makeText(AddImageActivity.this, "Enter english word to continue", Toast.LENGTH_SHORT).show();
					return;
				}

				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), AddImageActivity.REQEUST_CODE_PIC_SELECT);
			}
		});
    	
//        // ruler
//    	ruler = new View(this);
//    	ruler.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 10));

    	
    	linearLayout.addView(etPicture);
    	linearLayout.addView(ivCamera);
    	linearLayout.addView(ivPicBrowse);        	
    	linearLayout1.addView(linearLayout);
    	scrollView.addView(linearLayout1);
//    	linearLayout1.addView(ruler);
        
        
        for( int i=0; i<MainActivity.languages.length; i++){
        	// for each language
        	
        	// heading for each language
        	textView = new TextView(this);
        	textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        	textView.setText(MainActivity.languages[i]);
        	
        	linearLayout1.addView(textView);
        	
        	// enter word
        	arrETWord[i] =  new EditText(this);
        	arrETWord[i].setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        	arrETWord[i].setHint("Enter " + MainActivity.languages[i] + " word here");   
//        	if(i==0){
//        		arrETWord[i].setOnEditorActionListener(new OnEditorActionListener() {
//					
//					public boolean onEditorAction(TextView tv, int arg1, KeyEvent arg2) {
//						// TODO Auto-generated method stub
//						for(int i=0; i<MainActivity.languages.length; i++){
//							arrETAudio[i].setText(tv.getText());
//						}
//						return false;
//					}
//				});
//        	}
        	linearLayout1.addView(arrETWord[i]);
        	
//            // capture or select picture
//            linearLayout = new LinearLayout(this);
//            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//            linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, MainActivity.screenHeight/20));
//            
//
//        	etPicture =  new EditText(this);
//        	etPicture.setEnabled(false);
//        	etPicture.setLayoutParams(new LayoutParams(MainActivity.screenWidth*6/10, LayoutParams.MATCH_PARENT));
//        	
//        	ivCamera = new ImageView(this);            
//            ivCamera.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.screenWidth*2/10, LayoutParams.MATCH_PARENT));
//            ivCamera.setScaleType(ImageView.ScaleType.CENTER_CROP);        
//            ivCamera.setPadding(8, 8, 8, 8);
//            ivCamera.setImageResource(R.drawable.camera);
//            ivCamera.setTag(Integer.valueOf(i));
//            ivCamera.setOnClickListener(new View.OnClickListener() {    			
//    			public void onClick(View view) {				    				
//    				
//    				// tag keeping language position
//    				int tag = (Integer)view.getTag();
//    				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//    				String filePath = MainActivity.currentDirectory + "/" + arrETWord[tag].getText() + "." + MainActivity.PICTURE_FORMAT;
//    				Log.w("com.example.pictalk", "filepath="+filePath);
//    				intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(filePath)s));
//    				Log.w("com.example.pictalk", "uri="+Uri.fromFile(new File(filePath)).toString());
//    				etPicture.setText(filePath);
//    				startActivityForResult(intent, AddImageActivity.REQEUST_CODE_CAMERA+ tag);
//    			}
//    		});
//
//        	ivPicBrowse = new ImageView(this);            
//        	ivPicBrowse.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.screenWidth*2/10, LayoutParams.MATCH_PARENT));
//        	ivPicBrowse.setScaleType(ImageView.ScaleType.CENTER_CROP);        
//        	ivPicBrowse.setPadding(8, 8, 8, 8);
//        	ivPicBrowse.setImageResource(R.drawable.browse_file);
//        	ivPicBrowse.setOnClickListener(new View.OnClickListener() {    			
//    			public void onClick(View arg0) {				
//    				Intent intent = new Intent();
//    				intent.setType("image/*");
//    				intent.setAction(Intent.ACTION_GET_CONTENT);
//    				startActivityForResult(Intent.createChooser(intent, "Select Picture"), AddImageActivity.REQEUST_CODE_PIC_SELECT+i);
//    			}
//    		});
//        	
////            // ruler
////        	ruler = new View(this);
////        	ruler.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 10));
//
//        	
//        	linearLayout.addView(etPicture);
//        	linearLayout.addView(ivCamera);
//        	linearLayout.addView(ivPicBrowse);        	
//        	linearLayout1.addView(linearLayout);
////        	linearLayout1.addView(ruler);

            linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, MainActivity.screenHeight*2/20));

            arrETAudio[i] =  new EditText(this);
            arrETAudio[i].setEnabled(false);
            arrETAudio[i].setLayoutParams(new LayoutParams(MainActivity.screenWidth*6/10, LayoutParams.MATCH_PARENT));
        	
            arrIVMic[i] = new ImageView(this);            
            arrIVMic[i].setLayoutParams(new LinearLayout.LayoutParams(MainActivity.screenWidth*2/10, LayoutParams.MATCH_PARENT));
            arrIVMic[i].setScaleType(ImageView.ScaleType.CENTER_CROP);        
            arrIVMic[i].setPadding(8, 8, 8, 8);
            arrIVMic[i].setImageResource(R.drawable.mic);            
            arrIVMic[i].setTag(Integer.valueOf(i));
            arrIVMic[i].setOnClickListener(new View.OnClickListener() {    			
    			public void onClick(View view) {
    				// check if english word entered
    				if( arrETWord[0].getText().toString().equalsIgnoreCase("")){
    					Toast.makeText(AddImageActivity.this, "Enter english word to continue", Toast.LENGTH_SHORT).show();
    					return;
    				}

    				int tag = (Integer)view.getTag();
    				Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
    				startActivityForResult(intent, AddImageActivity.REQEUST_CODE_AUDIO+tag);
    			}
    		});

            arrIVAudioBrowse[i] = new ImageView(this);            
            arrIVAudioBrowse[i].setLayoutParams(new LinearLayout.LayoutParams(MainActivity.screenWidth*2/10, LayoutParams.MATCH_PARENT));
            arrIVAudioBrowse[i].setScaleType(ImageView.ScaleType.CENTER_CROP);        
            arrIVAudioBrowse[i].setPadding(8, 8, 8, 8);
            arrIVAudioBrowse[i].setImageResource(R.drawable.browse_file);
            arrIVAudioBrowse[i].setTag(Integer.valueOf(i));
            arrIVAudioBrowse[i].setOnClickListener(new View.OnClickListener() {    			
    			public void onClick(View view) {			
    				// check if english word entered
    				if( arrETWord[0].getText().toString().equalsIgnoreCase("")){
    					Toast.makeText(AddImageActivity.this, "Enter english word to continue", Toast.LENGTH_SHORT).show();
    					return;
    				}

    				int tag = (Integer)view.getTag();
    				Intent intent = new Intent();
    				intent.setAction(Intent.ACTION_PICK);
    				intent.setType(MediaStore.Audio.Media.CONTENT_TYPE);
    				intent.setData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
    				startActivityForResult(Intent.createChooser(intent, "Ringtone"), REQEUST_CODE_AUDIO_SELECT+tag); 
    			}
    		});
            
//            // ruler
//        	ruler = new View(this);
//        	ruler.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 10));
            
        	linearLayout.addView(arrETAudio[i]);
        	linearLayout.addView(arrIVMic[i]);
        	linearLayout.addView(arrIVAudioBrowse[i]);
            linearLayout1.addView(linearLayout);
//            linearLayout1.addView(ruler);
        	
        }
        
        button = new Button(this);
        button.setText(R.string.add_new_item);         
        //button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        button.setWidth(LayoutParams.MATCH_PARENT);
        button.setHeight(LayoutParams.WRAP_CONTENT);
        button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				// check if mandatory items are entered before saving
				if(etPicture.getText().toString().equalsIgnoreCase("") ||
				arrETWord[0].getText().toString().equalsIgnoreCase("")){
					Toast.makeText(AddImageActivity.this, "Enter english word and take picture to continue", Toast.LENGTH_SHORT).show();
					return;
				}
				
				boolean folder = cbFolder.isChecked();
				if (folder){
					String folderName = etPicture.getText().toString().split("\\.")[0];
					String folderDestination = MainActivity.currentDirectory + "/" + folderName;
					File fileFolder = new File(folderDestination);
					fileFolder.mkdir();
				}
				// store picture file
				String picFile = etPicture.getText().toString();
				String picSource = MainActivity.TEMP_DIRECTORY + "/" + picFile;
				String picDestination = MainActivity.currentDirectory + "/" + picFile;
				try {
					Methods.copyDirectory(new File(picSource), new File(picDestination));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				
				// store audio files
				for(int i=0; i<MainActivity.languages.length; i++){
					// check if audio file present
					if (arrETAudio[i].getText().toString().equalsIgnoreCase("")) continue;
					
					String fileName = arrETAudio[i].getText().toString().split("/")[1];
					Log.w("com.example.pictalk", "fileName=" + fileName);
					String source = MainActivity.TEMP_DIRECTORY + "/" + MainActivity.languages[i] + "-" + fileName;
					String destination = MainActivity.PICTALK_DIRECTORY + "/" + MainActivity.languages[i] + "/" + fileName;
					try {
						Methods.copyDirectory(new File(source), new File(destination));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}

				// clean temp directory
				File tempDirectory = new File(MainActivity.TEMP_DIRECTORY);
				for(File file: tempDirectory.listFiles()){
					file.delete();
				}
				
				// write words to words file
				// check if word already exists
				File file = new File(MainActivity.PICTALK_DIRECTORY + "/" + MainActivity.WORDS_FILE);
				try {
					Scanner scanner = new Scanner(file);

			    	//now read the file line by line...
			    	while (scanner.hasNextLine()) {
			    	    String line = scanner.nextLine();
			    	    String delim = ";";
			    	    String[] splitstrings = line.split(delim);
			    	    if (splitstrings[0].trim().equalsIgnoreCase(arrETWord[0].getText().toString())){
			    	    	Toast.makeText(AddImageActivity.this, "The english word is already used" , Toast.LENGTH_SHORT).show();
			    	    	return;
			    	    }			    	    	
			    	}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					//File file = new File(MainActivity.PICTALK_DIRECTORY + "/" + MainActivity.WORDS_FILE);
					FileOutputStream fos;
					boolean append = true;
					fos = new FileOutputStream(file, append);
					OutputStreamWriter writer = new OutputStreamWriter(fos);
					String newLine = "";
					for(int i=0; i<MainActivity.languages.length; i++){
						if (arrETWord[i].getText().toString().equalsIgnoreCase("")) newLine = newLine + MainActivity.SINGLE_SPACE; 
						else newLine = newLine + arrETWord[i].getText().toString();
						if (i != MainActivity.languages.length-1) newLine = newLine + ";";
					}
					newLine = newLine + "\n";
					writer.append(newLine);
					writer.close();
					fos.close();
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Toast.makeText(AddImageActivity.this, arrETWord[0].getText().toString() + " picture added", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(AddImageActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});
        linearLayout1.addView(button);
        
        // capture or select picture
//        EditText[] arrTVPicture = new EditText[MainActivity.languages.length];
//        ImageView[] arrIVCamera = new ImageView[MainActivity.languages.length];
//        ImageView[] arrIVPicBrowse = new ImageView[MainActivity.languages.length];      
//        for( int i=0; i<MainActivity.languages.length; i++){
//            // each row
//            LinearLayout linearLayout = new LinearLayout(this);
//            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//            linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, MainActivity.screenHeight/20));
//
//        	arrTVPicture[i] =  new EditText(this);
//        	arrTVPicture[i].setEnabled(false);
//        	arrTVPicture[i].setLayoutParams(new LayoutParams(MainActivity.screenWidth*6/10, LayoutParams.MATCH_PARENT));
//        	
//        	arrIVCamera[i] = new ImageView(this);            
//            arrIVCamera[i].setLayoutParams(new LinearLayout.LayoutParams(MainActivity.screenWidth*2/10, LayoutParams.MATCH_PARENT));
//            arrIVCamera[i].setScaleType(ImageView.ScaleType.CENTER_CROP);        
//            arrIVCamera[i].setPadding(8, 8, 8, 8);
//            arrIVCamera[i].setImageResource(R.drawable.camera);
//            arrIVCamera[i].setOnClickListener(new View.OnClickListener() {    			
//    			public void onClick(View arg0) {				    				
//    				//MainActivity.this.showDialog(1234);    				
//    			}
//    		});
//
//        	arrIVPicBrowse[i] = new ImageView(this);            
//        	arrIVPicBrowse[i].setLayoutParams(new LinearLayout.LayoutParams(MainActivity.screenWidth*2/10, LayoutParams.MATCH_PARENT));
//        	arrIVPicBrowse[i].setScaleType(ImageView.ScaleType.CENTER_CROP);        
//        	arrIVPicBrowse[i].setPadding(8, 8, 8, 8);
//        	arrIVPicBrowse[i].setImageResource(R.drawable.browse_file);
//        	arrIVPicBrowse[i].setOnClickListener(new View.OnClickListener() {    			
//    			public void onClick(View arg0) {				    				
//    				//MainActivity.this.showDialog(1234);    				
//    			}
//    		});
//        	
//        	View ruler = new View(this);
//        	ruler.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 10));
//        	//ruler.setBackgroundColor(0xFF00FF00);
//        	
//        	linearLayout.addView(arrTVPicture[i]);
//        	linearLayout.addView(arrIVCamera[i]);
//        	linearLayout.addView(arrIVPicBrowse[i]);        	
//        	linearLayout1.addView(linearLayout);
//        	linearLayout1.addView(ruler);
//        }
//        
//        // record or select audio
//        TextView[] arrTVAudio = new TextView[MainActivity.languages.length];
//        ImageView[] arrIVMic = new ImageView[MainActivity.languages.length];
//        ImageView[] arrIVAudioBrowse = new ImageView[MainActivity.languages.length];      
//        for( int i=0; i<MainActivity.languages.length; i++){
//            // each row
//            linearLayout = new LinearLayout(this);
//            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//            linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, MainActivity.screenHeight/20));
//
//            arrTVAudio[i] =  new TextView(this);
//            arrTVAudio[i].setLayoutParams(new LayoutParams(MainActivity.screenWidth*6/10, LayoutParams.MATCH_PARENT));
//        	
//            arrIVMic[i] = new ImageView(this);            
//            arrIVMic[i].setLayoutParams(new LinearLayout.LayoutParams(MainActivity.screenWidth*2/10, LayoutParams.MATCH_PARENT));
//            arrIVMic[i].setScaleType(ImageView.ScaleType.CENTER_CROP);        
//            arrIVMic[i].setPadding(8, 8, 8, 8);
//            arrIVMic[i].setImageResource(R.drawable.mic);
//            arrIVMic[i].setOnClickListener(new View.OnClickListener() {    			
//    			public void onClick(View arg0) {				    				
//    				//MainActivity.this.showDialog(1234);    				
//    			}
//    		});
//
//            arrIVAudioBrowse[i] = new ImageView(this);            
//            arrIVAudioBrowse[i].setLayoutParams(new LinearLayout.LayoutParams(MainActivity.screenWidth*2/10, LayoutParams.MATCH_PARENT));
//            arrIVAudioBrowse[i].setScaleType(ImageView.ScaleType.CENTER_CROP);        
//            arrIVAudioBrowse[i].setPadding(8, 8, 8, 8);
//            arrIVAudioBrowse[i].setImageResource(R.drawable.browse_file);
//            arrIVAudioBrowse[i].setOnClickListener(new View.OnClickListener() {    			
//    			public void onClick(View arg0) {				    				
//    				//MainActivity.this.showDialog(1234);    				
//    			}
//    		});
//            
//        	linearLayout.addView(arrTVAudio[i]);
//        	linearLayout.addView(arrIVMic[i]);
//        	linearLayout.addView(arrIVAudioBrowse[i]);
//            linearLayout1.addView(linearLayout);
//        }

        setContentView(scrollView);
        
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_image, menu);
        return true;
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//super.onActivityResult(requestCode, resultCode, data);
		
		String englishWord = arrETWord[0].getText().toString();
		if(requestCode == REQEUST_CODE_CAMERA && resultCode == Activity.RESULT_OK){
			//String path = Methods.getRealPathFromURI_Images(data.getData(), this);
			
			etPicture.setText(englishWord + "." + MainActivity.PICTURE_FORMAT);
		}
		if(requestCode == REQEUST_CODE_PIC_SELECT && resultCode == Activity.RESULT_OK){
			
			String source = Methods.getRealPathFromURI_Images(data.getData(), this);			
			String extension = source.substring(source.lastIndexOf(".")+1);
			String destination = MainActivity.TEMP_DIRECTORY + "/" + englishWord + "." + extension;


			try {
				Methods.copyDirectory(new File(source), new File(destination));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Methods.deleteFileOrDirectory(new File(source));
			//Log.w("com.example.pictalk", "path="+path);
			
			etPicture.setText(englishWord + "." + extension);
		}
		
		for(int i=0; i<MainActivity.languages.length; i++){
			
			if(requestCode == REQEUST_CODE_AUDIO+i && resultCode == Activity.RESULT_OK){
				String source = Methods.getRealPathFromURI_Images(data.getData(), this);				
				String extension = source.substring(source.lastIndexOf(".")+1);
				String destination = MainActivity.TEMP_DIRECTORY + "/" + MainActivity.languages[i] + "-" + englishWord + "." + extension;

				try {
					Methods.copyDirectory(new File(source), new File(destination));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				Methods.deleteFileOrDirectory(new File(source));
				//Log.w("com.example.pictalk", "path="+path);
				
				arrETAudio[i].setText(MainActivity.languages[i] + "/" + englishWord + "." + extension);
			}
			if(requestCode == REQEUST_CODE_AUDIO_SELECT+i && resultCode == Activity.RESULT_OK){
				String source = Methods.getRealPathFromURI_Images(data.getData(), this);				
				String extension = source.substring(source.lastIndexOf(".")+1);
				String destination = MainActivity.TEMP_DIRECTORY + "/" + MainActivity.languages[i] + "-" + englishWord + "." + extension;

				try {
					Methods.copyDirectory(new File(source), new File(destination));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				Methods.deleteFileOrDirectory(new File(source));
				//Log.w("com.example.pictalk", "path="+path);
				
				arrETAudio[i].setText(MainActivity.languages[i] + "/" + englishWord + "." + extension);

			}
		} 
		
//		if (requestCode == REQCODE_TAKEPICTURE && resultCode == Activity.RESULT_OK){
//			bitmap = (Bitmap) data.getExtras().get("data");
//			pictureSent=true;
//		}
//		startActivity(new Intent(this, MainActivity.class));

	}
}
