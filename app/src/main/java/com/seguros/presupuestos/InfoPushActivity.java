package com.seguros.presupuestos;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

public class InfoPushActivity extends AppCompatActivity {
TextView ptitulo, pmensaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_info);

        ptitulo        = (TextView) findViewById(R.id.ptitulo);
        pmensaje       = (TextView) findViewById(R.id.pmensaje);

        this.setTitle("");
        Intent i = getIntent();
        if (i.getStringExtra("titulo")=="")
            finish();
        ptitulo.setText(i.getStringExtra("titulo"));

        String formattedText = i.getStringExtra("mensaje");
        pmensaje.setText(formattedText);

/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pmensaje.setText(Html.fromHtml(formattedText, Html.FROM_HTML_MODE_LEGACY));
        } else {
            pmensaje.setText(Html.fromHtml(formattedText));
        }
*/
    }
}
