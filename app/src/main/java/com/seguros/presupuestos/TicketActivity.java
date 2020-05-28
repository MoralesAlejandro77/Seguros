package com.seguros.presupuestos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.seguros.Scanner.SimpleScannerActivity;


public class TicketActivity extends AppCompatActivity {
    Button bcamara;
    EditText ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        bcamara = (Button) findViewById(R.id.bcamara);
        ticket = (EditText) findViewById(R.id.ticket);

        bcamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TicketActivity.this, SimpleScannerActivity.class);
                startActivityForResult(i,200);
            }
        });
    }

    //*******************************************************************************************************************************
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200)
        {
            String codigo = data.getExtras().getString("ResultText");
            ticket.setText(codigo);
        }
    }


//*************************************************************************

}
