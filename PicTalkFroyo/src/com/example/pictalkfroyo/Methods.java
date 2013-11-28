package com.example.pictalkfroyo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class Methods {
		
	// If targetLocation does not exist, it will be created.
	public static void copyDirectory(File sourceLocation , File targetLocation)
	throws IOException {

	    if (sourceLocation.isDirectory()) { 
	        if (!targetLocation.exists() && !targetLocation.mkdirs()) {
	            throw new IOException("Cannot create dir " + targetLocation.getAbsolutePath());
	        }

	        String[] children = sourceLocation.list();
	        for (int i=0; i<children.length; i++) {
	            copyDirectory(new File(sourceLocation, children[i]),
	                    new File(targetLocation, children[i]));
	        }
	    } else {

	        // make sure the directory we plan to store the recording in exists
	        File directory = targetLocation.getParentFile();
	        if (directory != null && !directory.exists() && !directory.mkdirs()) {
	            throw new IOException("Cannot create dir " + directory.getAbsolutePath());
	        }

	        InputStream in = new FileInputStream(sourceLocation);
	        OutputStream out = new FileOutputStream(targetLocation);

	        // Copy the bits from instream to outstream
	        byte[] buf = new byte[1024];
	        int len;
	        while ((len = in.read(buf)) > 0) {
	            out.write(buf, 0, len);
	        }
	        in.close();
	        out.close();
	    }
	}
		
	public static String getRealPathFromURI_Images(Uri contentUri, Activity activity ) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

	public static String getRealPathFromURI_Audio(Uri contentUri, Activity activity ) {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = activity.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }	

	// Delete a file or directory
	public static void deleteFileOrDirectory(File fileOrDirectory) {
	    if (fileOrDirectory.isDirectory())
	        for (File child : fileOrDirectory.listFiles())
	        	deleteFileOrDirectory(child);

	    fileOrDirectory.delete();
	}
	
	// Get extension of a file
	public static String getFileExtension(File directory, String fileName){
		
		for(String str: directory.list()){
			String[] parts = str.split("\\.");  
			if (parts[0].equalsIgnoreCase(fileName)) if(parts.length!=1) return parts[1];
		}
		return null;
	}
	
	// Get language word from words file
	public static String getLanguageWord(String word, String language){
		
        File file = new File(MainActivity.PICTALK_DIRECTORY+"/"+MainActivity.WORDS_FILE);
		FileInputStream fis;
		try { 
			fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

			String aDataRow;
			while ((aDataRow = br.readLine()) != null) {
				String[] words = aDataRow.split(";");
				if (words[0].trim().equalsIgnoreCase(word)){
					for(int i=0; i<MainActivity.languages.length; i++){
						if(MainActivity.languages[i].equalsIgnoreCase(language)) return words[i].trim();
					}
				}
			}
			br.close(); 
			fis.close();

		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}















