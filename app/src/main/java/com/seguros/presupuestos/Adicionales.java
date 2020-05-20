package com.seguros.presupuestos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Adicionales extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionales);
        webView = (WebView) findViewById(R.id.webView);
        String tipo = "1";
        try {
            tipo = this.getIntent().getExtras().getString("ayuda");
        } catch (Exception e) {
        }
        String url = "file:///android_asset/adicionales.html";
        if (tipo.equals("2"))
            url = "file:///android_asset/adicionalessepelio.html";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.loadUrl(url);
        webView.setWebViewClient((new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //Al dar clic en un link se obligará a cargar dentro del WebView.
                view.loadUrl(url);
                return true;
            }
        }));
    }

}
