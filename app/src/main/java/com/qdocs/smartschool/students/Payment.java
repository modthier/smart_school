package com.qdocs.smartschool.students;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.qdocs.smartschool.BaseActivity;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import com.qdocs.smartschool.R;
import static android.widget.Toast.makeText;
import androidx.appcompat.widget.LinearLayoutCompat;


public class Payment extends BaseActivity {
    WebView webView;
    String feesId, feesTypeId,url,paymenttype,transfeesIdList;

    private static boolean firstTime = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.payment_activity, null, false);
        mDrawerLayout.addView(contentView, 0);
        webView = new WebView(this);
        if (firstTime) {
            // Recreate if loaded for the first time to prevent localisation issue.
            recreate();
            firstTime = false;
            return;
        }
        LinearLayoutCompat webviewWrapper = findViewById(R.id.webview_wrapper);
        webView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        webviewWrapper.addView(webView);

       // webView = findViewById(R.id.payment_webview);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }
        titleTV.setText(getApplicationContext().getString(R.string.payFees));

        feesId = getIntent().getStringExtra("feesId");
        feesTypeId = getIntent().getStringExtra("feesTypeId");
        paymenttype = getIntent().getStringExtra("paymenttype");
        transfeesIdList = getIntent().getStringExtra("transfeesIdList");
        if (Utility.isConnectingToInternet(getApplicationContext())) {
            url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl") + Constants.paymentGatewayUrl;
            if(paymenttype.equals("fees")){
                url += feesId + "/" + feesTypeId + "/" + Utility.getSharedPreferences(getApplicationContext(), "studentId")+ "/"+"";
            }else{
                url += "0" + "/" + "0" + "/" + Utility.getSharedPreferences(getApplicationContext(), "studentId")+ "/"+transfeesIdList;
            }

            Log.e("Payment URL", url);
            System.out.println("Payment URL=="+url);
        } else {
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }

        final ProgressDialog pd = ProgressDialog.show(Payment.this, "", "Loading..", true);
        webView.getSettings().setJavaScriptEnabled(true); // enable javascript
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setUserAgentString(String.valueOf(Utility.getSharedPreferences(getApplicationContext(), Constants.langCode)));
        webView.getSettings().setLoadWithOverviewMode(true);

        final Activity activity = this;
        webView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();
            }
        });
        Log.e("Payment URL", "URL " + url);
        webView.loadUrl(url);
    }

}
