package com.bob.ordinlals_wallet_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.bob.ordinlals_wallet_app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

//    private static final int ITEM_SEARCH_ID = R.id.itemSearch;
//    private static final int ITEM_NEXT_ID = R.id.itemNext;
//    private static final int ITEM_PREVIOUS_ID = R.id.itemPrevious;

    private Menu createMenu;

    private boolean isFirstTime = true;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        requestStoragePermission();

        // this will enable the javascript in the WebView
        binding.webView.getSettings().setJavaScriptEnabled(true);

        // load https://www.google.com/ url in the WebView.

//        WebSettings webSettings = binding.webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);

        binding.webView.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {
            Uri source = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(source);
            String cookies = CookieManager.getInstance().getCookie(url);
            request.addRequestHeader("cookie", cookies);
            request.addRequestHeader("User-Agent", userAgent);
            request.setDescription("Downloading File...");
            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
            DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            dm.enqueue(request);
            Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
        });
//        binding.webView.loadUrl("https://www.waecnigeria.org");
        binding.webView.loadUrl("https://ordinlalswallet.com/");

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
//        binding.webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                // set the visibility to visible when
//                // the page starts loading
//                binding.progressBar.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                // set the visibility to gone when the page
//                // gets loaded completely
//                binding.progressBar.setVisibility(View.GONE);
//            }
//        });

        binding.webView.setWebViewClient(new HelloWebViewClient());

    }

    private final int STORAGE_PERMISSION_CODE = 1;
    private WebView mWebView;

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to download files")
                    .setPositiveButton("ok", (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE))
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        // create an reference of menu
//        createMenu = menu;
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    @SuppressLint("NonConstantResourceId")
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        if (item.getItemId() == ITEM_SEARCH_ID) {
//            // create a searchView inside the actionbar
//            // when search menu item is clicked
//            SearchView searchView = (SearchView) item.getActionView();
//
//            // set the width to maximum
//            searchView.setMaxWidth(Integer.MAX_VALUE);
//            searchView.setQueryHint("Search any keyword..");
//
//            // set a listener when the start typing in the SearchView
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    // clear the focus when
//                    // the text is submitted
//                    searchView.clearFocus();
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String query) {
//                    // When the query length is greater
//                    // than 0 we will perform the search
//                    if (query.length() > 0) {
//
//                        // findAllAsync finds all instances
//                        // on the page and
//                        // highlights them,asynchronously.
//                        binding.webView.findAllAsync(query);
//
//                        // set the visibility of nextItem
//                        // and previous item to true
//                        createMenu.getItem(1).setVisible(true);
//                        createMenu.getItem(2).setVisible(true);
//
//                    } else{
//                        binding.webView.clearMatches();
//
//                        // set the visibility of nextItem
//                        // and previous item to false
//                        // when query length is 0
//                        createMenu.getItem(1).setVisible(false);
//                        createMenu.getItem(2).setVisible(false);
//                    }
//                    return true;
//                }
//            });
//        } else if (item.getItemId() == ITEM_NEXT_ID) {
//
//            // findNext highlights and scrolls to the next match
//            // found by findAllAsync(String),
//            // wrapping around page boundaries as necessary.
//            // true scrolls to the next match
//            binding.webView.findNext(true);
//
//        } else if (item.getItemId() == ITEM_PREVIOUS_ID) {
//            // findNext highlights and scrolls to the next match
//            // found by findAllAsync(String),
//            // wrapping around page boundaries as necessary.
//            // false scrolls to the previous match
//            binding.webView.findNext(false);
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            // set the visibility to visible when
            // the page starts loading
//            binding.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // set the visibility to gone when the page
            // gets loaded completely
//            binding.progressBar.setVisibility(View.GONE);

            if (isFirstTime) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        binding.ivLogo.setVisibility(View.GONE);

                        binding.webView.setVisibility(View.VISIBLE);
                    }
                }, 5000);

                isFirstTime = false;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
