package com.qdocs.smartschool.students;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.textfield.TextInputEditText;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import static android.widget.Toast.makeText;

public class StudentAddTimeLine extends AppCompatActivity {
    public ImageView backBtn;
    Bitmap selectedImageString = null;
    public String defaultDateFormat, currency,startweek;
    Context mContext = this;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    protected FrameLayout mDrawerLayout, actionBar;
    String applydate = "";
    String formdate = "";
    String extension="",name="";
    String filePath;
    ProgressDialog progress;
    String todate = "";
    private boolean isfromDateSelected = false;
    private boolean istoDateSelected = false;
    File f;
    Bitmap bitmap;
    Button submit;
    private static final int CAMERA_REQUEST = 1888;
    String url;
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    String file_path="";
    RequestBody file_body;
    EditText reason;
    Uri uri;
    ImageView imageView;
    EditText title;
    TextView textView;
    public TextView titleTV,buttonSelectImage;
    Button buttonUploadImage;
    private static final String TAG = "StudentAddLeave";
    TextInputEditText titleET, dateET, descriptionET;
    public static Boolean camera = false;
    public static Boolean gallery = false;
    boolean isKitKat = false;
    String[] mimeTypes =
            {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                    "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                    "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                    "text/plain",
                    "application/pdf",
                    "application/zip","image/*"};
    CardView card_view_outer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_timeline);
        backBtn = findViewById(R.id.actionBar_backBtn);
      //  mDrawerLayout = findViewById(R.id.container);
        actionBar = findViewById(R.id.actionBarSecondary);
        titleTV = findViewById(R.id.actionBar_title);

        defaultDateFormat = Utility.getSharedPreferences(getApplicationContext(), "dateFormat");
        currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
        startweek = Utility.getSharedPreferences(getApplicationContext(), "startWeek");

        decorate();
        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        card_view_outer = findViewById(R.id.card_view_outer);
        card_view_outer.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        titleTV.setText(getApplicationContext().getString(R.string.timeLine));
        titleET = findViewById(R.id.titleET);
        dateET = findViewById(R.id.dateET);
        descriptionET = findViewById(R.id.descriptionET);
        imageView =  findViewById(R.id.imageView);
        textView =  findViewById(R.id.textview);
        //title =  findViewById(R.id.title);
       // buttonUploadImage =  findViewById(R.id.buttonUploadImage);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        submit = findViewById(R.id.addLeave_dialog_submitBtn);

        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Else", "Else");
                ActivityCompat.requestPermissions(StudentAddTimeLine.this, permissions(), 1);
                showFileChooser();
            }
        });

        submit.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                   if(titleET.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.titlefield), Toast.LENGTH_LONG).show();
                    }else if(!isfromDateSelected) {
                        Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.dateError), Toast.LENGTH_LONG).show();
                    }else if(descriptionET.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.descriptionfield), Toast.LENGTH_LONG).show();
                    } else {
                        if(Utility.isConnectingToInternet(getApplicationContext())){
                            uploadBitmap();
                        }else{
                            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mDay   = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mYear  = mcurrentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(StudentAddTimeLine.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        //month = month + 1;
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(selectedyear, selectedmonth, selectedday);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        formdate=sdf.format(newDate.getTime());
                        SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd");
                        dateET.setText(Utility.parseDate("yyyy-MM-dd", defaultDateFormat,sdfdate.format(newDate.getTime())));
                        isfromDateSelected=true;
                    }
                }, mYear, mMonth, mDay);
                if(startweek.equals("Monday")){
                    datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
                }else if(startweek.equals("Tuesday")){
                    datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.TUESDAY);
                }else if(startweek.equals("Wednesday")){
                    datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.WEDNESDAY);
                }else if(startweek.equals("Thursday")){
                    datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.THURSDAY);
                }else if(startweek.equals("Friday")){
                    datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.FRIDAY);
                }else if(startweek.equals("Saturday")){
                    datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.SATURDAY);
                }else if(startweek.equals("Sunday")){
                    datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.SUNDAY);
                }
                datePickerDialog.show();
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.
                    READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return;
            }
        }

    }

    private void decorate() {
        actionBar.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        }
    }

    private void showFileChooser() {
        final Dialog dialog = new Dialog(StudentAddTimeLine.this);
        dialog.setContentView(R.layout.choose_file);
        dialog.setCanceledOnTouchOutside(false);
        RelativeLayout headerLay = (RelativeLayout) dialog.findViewById(R.id.addTask_dialog_header);
        final LinearLayout takephoto = (LinearLayout) dialog.findViewById(R.id.takephoto);
        final LinearLayout choosegallery = (LinearLayout) dialog.findViewById(R.id.gallery);
        ImageView closeBtn = (ImageView) dialog.findViewById(R.id.addTask_dialog_crossIcon);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camerapic();
                camera = true;
                gallery = false;
                dialog.dismiss();
            }
        });
        choosegallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opengallery();
                gallery = true;
                camera = false;
                dialog.dismiss();
            }
        });

        headerLay.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        dialog.show();
    }

    void camerapic() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void opengallery() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                if (mimeTypes.length > 0) {
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                }
            } else {
                String mimeTypesStr = "";
                for (String mimeType : mimeTypes) {
                    mimeTypesStr += mimeType + "|";
                }
                intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
            }
            isKitKat = true;
            startActivityForResult(Intent.createChooser(intent, "Select file"), PICK_IMAGE_REQUEST);
        } else {
            isKitKat = false;
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                if (mimeTypes.length > 0) {
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                }
            } else {
                String mimeTypesStr = "";
                for (String mimeType : mimeTypes) {
                    mimeTypesStr += mimeType + "|";
                }
                intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
            }
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    public String getgalleryRealPathFromURI(Context context, Uri contentUri) {
        OutputStream out;
        File file = new File(getFilename(context));

        try {
            if (file.createNewFile()) {
                InputStream iStream = context != null ? context.getContentResolver().openInputStream(contentUri) : context.getContentResolver().openInputStream(contentUri);
                byte[] inputData = getBytes(iStream);
                out = new FileOutputStream(file);
                out.write(inputData);
                out.close();
                return file.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    private String getFilename(Context context) {
        File mediaStorageDir = new File(context.getExternalFilesDir(""), "Soers_Images");
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        String mImageName=name+"."+extension;
        System.out.println("mImageName=="+mImageName);
        System.out.println("Image=="+mediaStorageDir.getAbsolutePath() + "/" + mImageName);
        return mediaStorageDir.getAbsolutePath() + "/" + mImageName;
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @TargetApi(19)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            System.out.println("uri=="+uri);

            String path = new File(uri.getPath()).getAbsolutePath();
            System.out.println("path=="+path);

            if(path != null){
                uri = data.getData();

                String filenames;
                Cursor cursor = getContentResolver().query(uri,null,null,null,null);

                if(cursor == null) filenames=uri.getPath();
                else{
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                    filenames = cursor.getString(idx);
                    cursor.close();
                }

                name = filenames.substring(0,filenames.lastIndexOf("."));
                System.out.println("name=="+name);
                extension = filenames.substring(filenames.lastIndexOf(".")+1);
                System.out.println("extension=="+extension);
            }else{
                makeText(this, "Please select file", Toast.LENGTH_SHORT).show();
             }

            try {
                selectedImageString = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            textView.setText(getApplicationContext().getString(R.string.fileselected));

            filePath = getgalleryRealPathFromURI(StudentAddTimeLine.this, uri);
            if(extension.equals("jpg")||extension.equals("png")||extension.equals("jpeg")){
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(selectedImageString);
            }else if(extension.equals("PDF")||extension.equals("pdf")||extension.equals("doc")||extension.equals("docx")||extension.equals("txt")){
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.selected_file));
            }
            f = new File(filePath);
            System.out.println("file=="+filePath);
            String mimeType = URLConnection.guessContentTypeFromName(f.getName());
            file_body = RequestBody.create(MediaType.parse(mimeType), f);
            System.out.println("file_bodypathasd" + file_body);
            System.out.println("bitmap image==" + selectedImageString);
        }else if(resultCode != RESULT_CANCELED) {
            if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if (bitmap != null) {
                    progress = new ProgressDialog(StudentAddTimeLine.this);
                    progress.setTitle("uploading");
                    progress.setMessage("Please Wait....");
                    progress.show();
                    imageView.setVisibility(View.VISIBLE);
                    textView.setText(getApplicationContext().getString(R.string.fileselected));
                    imageView.setImageBitmap(bitmap);
                    Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                    filePath = getRealPathFromURI(tempUri);
                    System.out.println("pathasd" + filePath);
                    File f = new File(filePath);
                    String mimeType = URLConnection.guessContentTypeFromName(f.getName());
                    file_body = RequestBody.create(MediaType.parse(mimeType), f);
                    System.out.println("file_bodypathasd" + file_body);
                    progress.dismiss();
                }
            }
        }
    }
    public static String[] storage_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storage_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storage_permissions_33;
        } else {
            p = storage_permissions;
        }
        return p;
    }
    private void uploadBitmap() throws IOException{
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl") + Constants.addedittimelineUrl;
        OkHttpClient client=new OkHttpClient();
        Log.i("url=", url);

        if(filePath==null || file_body==null){

            RequestBody requestBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("id","")
                    .addFormDataPart("title",titleET.getText().toString())
                    .addFormDataPart("description",descriptionET.getText().toString())
                    .addFormDataPart("timeline_date",formdate)
                    .addFormDataPart("timeline_doc","")
                    .addFormDataPart("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId))
                    .build();

            Request request=new Request.Builder()
                    .url(url)
                    .header("Client-Service", Constants.clientService)
                    .header("Auth-Key", Constants.authKey)
                    .header("User-ID",Utility.getSharedPreferences(getApplicationContext(), "userId"))
                    .header("Authorization",Utility.getSharedPreferences(getApplicationContext(), "accessToken"))
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) { }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    pd.dismiss();
                    ResponseBody body = response.body();
                    if(body != null) {
                        try {
                            String jsonData = response.body().string();
                            try {
                                final JSONObject Jobject = new JSONObject(jsonData);
                                String Jarray = Jobject.getString("status");
                                if(Jarray.equals("1")){
                                    runOnUiThread(new Runnable(){
                                        public void run() {
                                            Toast.makeText(mContext, getApplicationContext().getString(R.string.submit_success), Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                }else{
                                    runOnUiThread(new Runnable(){
                                        public void run() {
                                            try {
                                                JSONObject error = Jobject.getJSONObject("error");
                                                Toast.makeText(mContext, error.getString("reason"), Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            });

        }else{
            String file_name=filePath.substring(filePath.lastIndexOf("/")+1);
            System.out.println("file_name== "+file_name);

            RequestBody requestBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("id","")
                    .addFormDataPart("title",titleET.getText().toString())
                    .addFormDataPart("description",descriptionET.getText().toString())
                    .addFormDataPart("timeline_date",formdate)
                    .addFormDataPart("timeline_doc",file_name,file_body)
                    .addFormDataPart("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId))
                    .build();

            Request request=new Request.Builder()
                    .url(url)
                    .header("Client-Service", Constants.clientService)
                    .header("Auth-Key", Constants.authKey)
                    .header("User-ID",Utility.getSharedPreferences(getApplicationContext(), "userId"))
                    .header("Authorization",Utility.getSharedPreferences(getApplicationContext(), "accessToken"))
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) { }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    pd.dismiss();
                    ResponseBody body = response.body();
                    if(body != null) {
                        try {
                            String jsonData = response.body().string();
                            System.out.println("jsonData=="+jsonData);
                            try {
                                final JSONObject Jobject = new JSONObject(jsonData);
                                String Jarray = Jobject.getString("status");

                                if(Jarray.equals("1")){
                                    runOnUiThread(new Runnable(){
                                        public void run() {
                                            Toast.makeText(mContext, getApplicationContext().getString(R.string.submit_success), Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });

                                }else{
                                    runOnUiThread(new Runnable(){
                                        public void run() {
                                            try {
                                                JSONObject error = Jobject.getJSONObject("error");
                                                Toast.makeText(mContext, error.getString("reason"), Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            });
        }

    }

    private String getMimeType(String path) {
        String extension= MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
}



