package com.seguros.presupuestos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.seguros.MainActivity;

public class InfoPushActivity extends AppCompatActivity {
TextView ptitulo, pmensaje, irapp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_info);

        ptitulo        = (TextView) findViewById(R.id.ptitulo);
        pmensaje       = (TextView) findViewById(R.id.pmensaje);
        irapp          = (TextView) findViewById(R.id.irapp);

        this.setTitle("");
        Intent i = getIntent();
        if (i.getStringExtra("titulo")=="")
            finish();
        ptitulo.setText(i.getStringExtra("titulo"));

        String formattedText = i.getStringExtra("mensaje");
        pmensaje.setText(formattedText);

        irapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }
        });

/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pmensaje.setText(Html.fromHtml(formattedText, Html.FROM_HTML_MODE_LEGACY));
        } else {
            pmensaje.setText(Html.fromHtml(formattedText));
        }
*/
    }
}
