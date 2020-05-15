package com.seguros.Cocherias;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.seguros.presupuestos.R;

public class BuscaxDni extends Activity {
ImageButton bbuscarok;
EditText nrodoc;
RadioButton masc, fem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscax_dni);
        bbuscarok = (ImageButton) findViewById(R.id.bbuscarok);
        nrodoc    = (EditText) findViewById(R.id.nrodoc);
        masc      = (RadioButton) findViewById(R.id.masc);
        fem       = (RadioButton) findViewById(R.id.fem);

        bbuscarok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent resultData = new Intent();
                resultData.putExtra("dni", nrodoc.getText().toString());
                String sexo = "1";
                if (fem.isChecked())
                    sexo = "2";

                resultData.putExtra("dni" , nrodoc.getText().toString());
                resultData.putExtra("sexo",sexo);
                setResult(10,resultData);
                finish();
            }
        });
    }
}
