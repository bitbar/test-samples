package com.testdroid.sample.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;

import utils.Helpers;

import static android.webkit.WebView.setWebContentsDebuggingEnabled;

/**
 * @author Saad Chaudhry <saad.chaudry@bitbar.com>
 */
public class HY_Hybrid extends Activity implements View.OnClickListener {

    private static final String TAG = HY_Hybrid.class.getName();

    private InputMethodManager inputMethodManager;
    Menu menu;

    private boolean isLocalWeb = false;
    private boolean isClearHistoryAfterLoad = false;


    // UI Widgets
    private static FrameLayout fl_urlPanel;
    private static WebView wv_webView;
    private static ProgressBar pb_loading;
    private static EditText et_url;
    private static ImageButton ib_loadUrl;
    private static ImageButton ib_navigateBackward;
    private static ImageButton ib_navigateForward;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hy_layout);

        inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        fl_urlPanel = (FrameLayout) findViewById(R.id.hy_fl_urlPanel);
        wv_webView = (WebView) findViewById(R.id.hy_wv_webview);
        pb_loading = (ProgressBar) findViewById(R.id.hy_pb_loading);
        et_url = (EditText) findViewById(R.id.hy_et_url);
        ib_loadUrl = (ImageButton) findViewById(R.id.hy_ib_loadUrl);
        ib_navigateBackward = (ImageButton) findViewById(R.id.hy_ib_navigateBack);
        ib_navigateForward = (ImageButton) findViewById(R.id.hy_ib_navigateForward);

        et_url.setOnClickListener(this);
        ib_loadUrl.setOnClickListener(this);
        ib_navigateBackward.setOnClickListener(this);
        ib_navigateForward.setOnClickListener(this);

        et_url.setOnFocusChangeListener(this.onFocusChangeListener);

        // setting WebView
        {
            WebSettings webSettings = wv_webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            wv_webView.setWebViewClient(this.webViewClient);
        }

        // enable WebView debugging - for Appium to be able to switch to WebView context
        setWebContentsDebuggingEnabled(true);

        // don't auto-pop keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // setting presets
        setNavigationButtons();

        ib_loadUrl.performClick();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.hy_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.hy_menu_type:
                switchWebSource();
                break;
            case R.id.hy_menu_clearCache:
                clearCache();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hy_et_url:
                urlBarClicked();
                break;
            case R.id.hy_ib_loadUrl:
                loadUrl();
                break;
            case R.id.hy_ib_navigateBack:
                navigateBack();
                break;
            case R.id.hy_ib_navigateForward:
                navigateForward();
                break;
        }

    }

    View.OnFocusChangeListener onFocusChangeListener = (v, hasFocus) -> {
        if (hasFocus) {
            urlBarClicked();
        }
    };

    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            pb_loading.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            pb_loading.setVisibility(View.GONE);
            if (url.startsWith("http")) {
                et_url.setText(url);
            }
            if (isClearHistoryAfterLoad) {
                wv_webView.clearHistory();
                isClearHistoryAfterLoad = false;
            }
            setNavigationButtons();

            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            wv_webView.loadUrl("file:///android_asset/www/error.html");
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
        }
    };

    private void urlBarClicked() {
        et_url.setText("");
    }

    private void loadUrl() {
        inputMethodManager.hideSoftInputFromWindow(et_url.getWindowToken(), 0);

        String url = et_url.getText().toString().replace(" ", "");

        if (url.equals("")) {
            url = getResources().getString(R.string.hy_urlHint);
            et_url.setText(url);
        }

        try {
            URI uri = new URI(url);
        } catch (URISyntaxException e) {
            Log.d(TAG, "Invalid URL", e);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_invalidUrl), Toast.LENGTH_LONG).show();
        }

        wv_webView.loadUrl(url);
    }

    private void navigateBack() {
        wv_webView.goBack();
    }

    private void navigateForward() {
        wv_webView.goForward();
    }

    private void setNavigationButtons() {
        if (wv_webView.canGoBack()) {
            ib_navigateBackward.setEnabled(true);
            ib_navigateBackward.setAlpha(1.0f);
        } else {
            ib_navigateBackward.setEnabled(false);
            ib_navigateBackward.setAlpha(0.5f);
        }
        if (wv_webView.canGoForward()) {
            ib_navigateForward.setEnabled(true);
            ib_navigateForward.setAlpha(1.0f);
        } else {
            ib_navigateForward.setEnabled(false);
            ib_navigateForward.setAlpha(0.5f);
        }
    }

    private void switchWebSource() {

        MenuItem menuItem = menu.findItem(R.id.hy_menu_type);

        if (isLocalWeb) {
            isLocalWeb = false;
            fl_urlPanel.setVisibility(View.VISIBLE);
            menuItem.setTitle(getResources().getString(R.string.hy_menu_type_local));
            loadUrl();
            Helpers.toastDefault(getApplicationContext(), "Switched to Internet", Toast.LENGTH_SHORT);
        } else {
            isLocalWeb = true;
            fl_urlPanel.setVisibility(View.GONE);
            menuItem.setTitle(getResources().getString(R.string.hy_menu_type_internet));
            wv_webView.loadUrl("file:///android_asset/www/index.html");
            Helpers.toastDefault(getApplicationContext(), "Switched to local web", Toast.LENGTH_SHORT);
        }

        isClearHistoryAfterLoad = true;
        wv_webView.clearHistory();
    }

    private void clearCache() {
        wv_webView.clearCache(true);
        Helpers.toastDefault(getApplicationContext(), "Cache cleared", Toast.LENGTH_SHORT);
    }
}
