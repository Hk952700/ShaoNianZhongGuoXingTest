package com.huaxia.exam.shaonianwebversion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private WebView main_web_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_web_view = (WebView) findViewById(R.id.main_web_view);
        WebSettings settings = main_web_view.getSettings();
        settings.setJavaScriptEnabled(true);
        //settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存
        settings.setJavaScriptEnabled(true);//设置能够解析Javascript
        settings.setDomStorageEnabled(true);//设置适应Html5 //重点是这个设置

        main_web_view.setWebViewClient(new WebViewClient());
        main_web_view.loadUrl("https://www.baidu.com");
    }
}
