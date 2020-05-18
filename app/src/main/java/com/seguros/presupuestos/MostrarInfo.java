package com.seguros.presupuestos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MostrarInfo extends Activity {
TextView ptitulo, pmensaje;
Button boton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_info);

        ptitulo        = (TextView) findViewById(R.id.ptitulo);
        pmensaje       = (TextView) findViewById(R.id.pmensaje);
        boton          = (Button) findViewById(R.id.button3);
        this.setTitle("");
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
