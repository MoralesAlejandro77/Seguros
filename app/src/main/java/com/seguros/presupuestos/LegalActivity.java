package com.seguros.presupuestos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LegalActivity extends AppCompatActivity {
    Button button3;
    int resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resultado = 0;

        try {
            resultado = Integer.valueOf(this.getIntent().getExtras().getString("tipo"));

        } catch (Exception e) {
            resultado = 0;
        }
        if (resultado == 2){
            setContentView(R.layout.activity_legal2);
        }
        else
        {
            setContentView(R.layout.activity_legal);
        }

        button3 = (Button) findViewById(R.id.button3);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
