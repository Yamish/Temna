package com.example.yami.temna;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;


/**
 * Created by Yami on 25/08/2015.
 */
public class MainActivity extends Activity {
    private WebView myWebView;
    private Handler hl_timeout = new Handler();
    private Dialog builder;
    private String uriimage = "@drawable/home";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_main);
        PrefUtils.setKioskModeActive(true, getApplicationContext());



        String url = "http://www.temna-djerba.com/client";
        myWebView = (WebView) findViewById(R.id.webview);
        if (savedInstanceState != null)
            myWebView.restoreState(savedInstanceState);
        else
            myWebView.loadUrl(url);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebChromeClient(new WebChromeClient());
        myWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (Build.VERSION.SDK_INT >= 11){
            myWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        hl_timeout.postDelayed(DoOnTimeOut, 60000); // The DoOnTimOut will be triggered after 10sec

    }

    @Override
    public void onBackPressed() {
        // nothing to do here
        // … really
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus) {
            // Close every kind of system dialog
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the state of the WebView
        myWebView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        // Restore the state of the WebView
        myWebView.restoreState(savedInstanceState);
    }

    private Runnable DoOnTimeOut = new Runnable()
    {
        public void run()
        {
            showImage();
        }
    };

    @Override
    public void onUserInteraction()
    {
        super.onUserInteraction();
        //Remove any previous callback
        hl_timeout.removeCallbacks(DoOnTimeOut);
        hl_timeout.postDelayed(DoOnTimeOut, 60000);

    }

    public void showImage() {

        builder=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });
        int imageResource = getResources().getIdentifier(uriimage, null, getPackageName());
        ImageView imageView = new ImageView(this);
        Drawable res = getResources().getDrawable(imageResource);
        imageView.setImageDrawable(res);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));


        try {
            builder.show();
        } catch(Exception e){
            // WindowManager$BadTokenException will be caught and the app would not display
            // the 'Force Close' message
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                builder.dismiss();
            }
        });
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( builder!=null && builder.isShowing() ){
            builder.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if ( builder!=null && builder.isShowing() ){
            builder.cancel();
        }
    }
}

