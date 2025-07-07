package com.qdocs.smartschool.students;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.qdocs.smartschool.BaseActivity;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.utils.Utility;
import static android.widget.Toast.makeText;

public class StudentVideoTutorialPlay extends BaseActivity {

    RelativeLayout image_layout,youtube_layout;
    WebView webView;
    TextView videoNameTV,videoDetailsdescription;
    String title,titlepath,videolink,videourl;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_student_video_tutorial_play, null, false);
        mDrawerLayout.addView(contentView, 0);
        titleTV.setText(getApplicationContext().getString(R.string.videoTutorial));

        title = getIntent().getExtras().getString("title");
        titlepath = getIntent().getExtras().getString("titlepath");
        videolink = getIntent().getExtras().getString("videolink");
        pd=new ProgressDialog(StudentVideoTutorialPlay.this);

        image_layout=findViewById(R.id.image_layout);
        youtube_layout=findViewById(R.id.youtube_layout);
        webView=findViewById(R.id.webView);
        videoNameTV=findViewById(R.id.videoNameTV);
        videoDetailsdescription=findViewById(R.id.videoDetailsdescription);
        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }

        webView.getSettings().setJavaScriptEnabled(true); // enable javascript
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new ChromeClient());


        videoNameTV.setText(title);


        if (Utility.isConnectingToInternet(getApplicationContext())) {
           videourl = "http://www.youtube.com/embed/" + videolink.replace("https://www.youtube.com/watch?v=", "") + "?autoplay=1&vq=small";

            System.out.println("videourl=="+videourl);
        } else {
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }

        webView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) { }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) { }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) { }

            @Override
            public void onPageFinished(WebView view, String url) { }
        });
        Log.e("Video URL", "URL " + videourl);
        webView.loadUrl(videourl);
    }


    public class ChromeClient extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        public Bitmap getDefaultVideoPoster() {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            // setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            //this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        }
    }
}

