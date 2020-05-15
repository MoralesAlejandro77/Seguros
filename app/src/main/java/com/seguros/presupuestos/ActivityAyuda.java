package com.seguros.presupuestos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ActivityAyuda extends AppCompatActivity {
    WebView webView;
    String url;
    int resultado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_ayuda);
        webView = (WebView) findViewById(R.id.webView);

        url = "https://tresprovinciassa.com.ar";

        try {
            resultado = Integer.valueOf(this.getIntent().getExtras().getString("ayuda"));

        } catch (Exception e) {
            resultado = 0;
        }

       if (resultado == 1) //vida
           url = "https://tresprovinciassa.com.ar/seguros/seguros-familiares/#side_tab961";

        if (resultado == 2) //sepelio
            url = "https://tresprovinciassa.com.ar/seguros/seguros-familiares/#side_tab210";

        if (resultado == 3) //compania
            url = "https://tresprovinciassa.com.ar/compania/";

        if (resultado == 4) //contactenos
            url = "https://tresprovinciassa.com.ar/contacto/";

        if (resultado == 5) //preguntas frecuentes
            url = "https://tresprovinciassa.com.ar/preguntas-frecuentes/";

        if (resultado == 6) //Pago electronico
            url = "https://tresprovinciassa.com.ar/pago-seguro-en-linea/";

        if (resultado == 9) //Pago electronico
            url = "https://tresprovinciassa.com.ar/pago-online/";

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        if (resultado == 7) {//fcm asegurados
            url = "https://tresprovinciassa.com.ar/Seguros/ws/asegurados/mensajes.html";
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        if (resultado == 8) {//Log asegurados
            url = "https://tresprovinciassa.com.ar/Seguros/ws/asegurados/logsAsegurados.php";
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

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
//********************************************
       @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
 //******************************************

}
