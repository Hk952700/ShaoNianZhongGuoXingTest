package com.huaxia.exam.shaonianwebversion;

import android.app.Activity;
import android.content.Context;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class TestActivity extends Activity {
    //master
    private WebView mWebView;
    private ProgressBar mWebProgres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mWebView = (WebView) findViewById(R.id.wb_banner);

        mWebProgres = (ProgressBar) findViewById(R.id.pb_web);
        try {
            setData("http://www.cctv-snzgx.com");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setData(String url) throws Exception {
        WebSettings settings = mWebView.getSettings();
        //settings.setUserAgentString(settings.getUserAgentString()+"app/" + SharedPreUtils.getString(context, "sessionId") + ";" + SharedPreUtils.getInt(context, "userId"));
        // 设置可以支持缩放
        mWebView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        // 为图片添加放大缩小功能
        mWebView.setInitialScale(100);   //100代表不缩放
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowContentAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setSupportMultipleWindows(false);
        settings.setAppCachePath(this.getDir("cache", Context.MODE_PRIVATE).getPath());
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setUseWideViewPort(true);
        mWebView.setFocusable(true);
        mWebView.requestFocus();

        mWebView.setWebViewClient(new WebViewClient() {
            //覆写shouldOverrideUrlLoading实现内部显示网页
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);
                //return true;
                return false;  //网页重定向按返回键无效相关
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String userAgent = view.getSettings().getUserAgentString();
                //Log.i("BannerWebActivityPresenter", userAgent);
            }
        });


        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mWebProgres.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    mWebProgres.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    mWebProgres.setProgress(newProgress);//设置进度值
                }

            }
        });

        mWebView.loadUrl(url);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁webview防止内存泄漏
        destroyWebView();
    }

    public void destroyWebView() {

        mWebView.removeAllViews();

        if (mWebView != null) {
            mWebView.clearHistory();
            mWebView.clearCache(true);
            mWebView.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
            mWebView.freeMemory();
            mWebView.pauseTimers();
            mWebView = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing
        }

    }

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("jiao", "onKeyDown: " + mWebView.canGoBack());
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            Log.i("jiao", "onKeyDown: " + secondTime);
            if (secondTime - firstTime > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
