package com.qdocs.smartschool.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import com.qdocs.smartschool.R;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import static android.content.Context.DOWNLOAD_SERVICE;

public class Utility {

	public static Context appContext;
	private static String PREFERENCE="SmartSchool";
	static File directory,photoDirectory;
	static String path;

	public static void setSharedPreference(Context context, String name, String value) {
		appContext = context;
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
		SharedPreferences.Editor editor = settings.edit();
		// editor.clear();
		editor.putString(name, value);
		editor.commit();
	}
	
	public static void setIntegerSharedPreference(Context context, String name, int value) {
		appContext = context;
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
		SharedPreferences.Editor editor = settings.edit();
		// editor.clear();
		editor.putInt(name, value);
		editor.commit();
	}

	public static String getSharedPreferences(Context context, String name) {
		SharedPreferences settings = context
				.getSharedPreferences(PREFERENCE, 0);
		return settings.getString(name, "");
	}

	public static int getIntegerSharedPreferences(Context context, String name) {
		SharedPreferences settings = context
				.getSharedPreferences(PREFERENCE, 0);
		return settings.getInt(name, 1);
	}

	public static void setSharedPreferenceBoolean(Context context, String name, boolean value) {
		appContext = context;
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(name, value);
		editor.commit();
	}


	public static boolean getSharedPreferencesBoolean(Context context, String name) {
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
		return settings.getBoolean(name, false);
	}

    public static void clearPreference(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }

	public static boolean isConnectingToInternet(Context c){
		ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

		return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
	}

	public static String parseDate(String orignalFormat, String newFormat, String date) {
        String formattedDate;
        DateFormat targetFormat;
        DateFormat originalFormat = new SimpleDateFormat(orignalFormat, Locale.ENGLISH);
        try {
            targetFormat = new SimpleDateFormat(newFormat, Locale.ENGLISH);
        } catch (IllegalArgumentException IAE) {
            newFormat = newFormat.replace("Y", "y");
            targetFormat = new SimpleDateFormat(newFormat, Locale.ENGLISH);
        }
        try{
            Date newDate = originalFormat.parse(date);
            formattedDate = targetFormat.format(newDate);  // 20120821
        } catch (ParseException E) {
            formattedDate = "";
        }
        return  formattedDate;
    }


	public static String changeAmount(String amount, String currency,String base_price) {
		System.out.println("Actual Amount=="+amount);
		System.out.println("Actual base price=="+base_price);
		System.out.println("Actual currency=="+currency);
		double amounts = 0;
		double USD = Double.parseDouble(amount);
		double price = Double.parseDouble(base_price);
		amounts = price * USD;

		System.out.println("converted amount= "+amounts);
		//return new DecimalFormat("####0.00").format(Double.parseDouble(String.valueOf(amounts)));
		return String.valueOf(amounts);
	}


	public static String changeAmounttousd(String amount, String currency,String base_price) {
		System.out.println("Actual Amount=="+amount);
		System.out.println("Actual base price=="+base_price);
		System.out.println("Actual currency=="+currency);
		double amounts = 0;
		double USD = Double.parseDouble(amount);
		double price = Double.parseDouble(base_price);
		amounts = USD / price;

		System.out.println("converted amount= "+amounts);
		return String.valueOf(amounts);
	}
	public static long beginDownload(Context context, String filePath, String urlStr){

		String[] fileNameArray = filePath.split("/");
		String fileName = fileNameArray[fileNameArray.length-1];

		Log.e("fileName", fileName);

		String path = Environment.getExternalStorageDirectory()+"/"+Constants.downloadDirectory;

		File file=new File(path);

		if(!file.exists()) {
			file.mkdir();
		}

		File downloadFile = new File(path+"/"+fileName);

		Uri Download_Uri = Uri.parse(urlStr);

		DownloadManager.Request request=new DownloadManager.Request(Download_Uri)
				.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI)
				.setTitle(context.getApplicationContext().getString(R.string.app_name))
				.setDescription("Downloading " + fileName)
				.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
				.setDestinationUri(Uri.fromFile(downloadFile))
				.setDestinationInExternalFilesDir(context,path, fileName)
				.setAllowedOverMetered(true)
				.setVisibleInDownloadsUi(true)
				.setAllowedOverRoaming(true);
		DownloadManager downloadManager= (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);

		return downloadManager.enqueue(request);

	}
     //Update Starts
	 public static SecretKey generateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
				SecureRandom random = new SecureRandom();
				byte[] salt = new byte[16];
				random.nextBytes(salt);
				return new SecretKeySpec(salt, "AES");
	}


	public static void setLocale(Context context, String localeName) {
		if(localeName.isEmpty() || localeName.equals("null")) {
			localeName = "en";
			Log.e("localName status", "empty");
		}
		Locale myLocale = new Locale(localeName);
		Locale.setDefault(myLocale);
		Resources res = context.getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = myLocale;
		res.updateConfiguration(conf, dm);
		Log.e("Utility Status", "Locale updated!");
	}




}// final class ends here

