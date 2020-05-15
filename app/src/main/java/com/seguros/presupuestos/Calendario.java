package com.seguros.presupuestos;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Calendario extends AppCompatActivity {
CalendarView calendar;
TextView fecha;
EditText anio;
Button boton, bactualizar;
int dia, mes, ano;
String sdia, smes, sano;

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_calendario);

calendar       = findViewById(R.id.calender);
fecha          = findViewById(R.id.dateView);
boton          = findViewById(R.id.button);
bactualizar    = findViewById(R.id.bactualizar);
anio           = findViewById(R.id.anio);


SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
String fechaActual = sdf.format(new Date());


String valores[] = fechaActual.split("/");
dia  = Integer.valueOf(valores[0]);
mes  = Integer.valueOf(valores[1]);
ano  = Integer.valueOf(valores[2]);

anio.setText(valores[2]);

sdia = String.valueOf(dia);
smes = String.valueOf((mes) );
sano = String.valueOf(ano);

if (dia < 10)
    sdia = "0" + sdia;

if (mes < 10)
    smes = "0" + smes;

String sfecha = sdia + "/" + smes + "/" + sano;
fecha.setText(sfecha);




calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                sdia = String.valueOf(dayOfMonth);
                smes = String.valueOf((month + 1) );
                sano = String.valueOf(year);

                dia  = dayOfMonth;
                mes  = month + 1;
                ano  = year;

                if (dia < 10)
                    sdia = "0" + sdia;

                if (mes < 10)
                    smes = "0" + smes;

                String sfecha = sdia + "/" + smes + "/" + sano;

                fecha.setText(sfecha);
            }
        });

//*************************************************************************************
boton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent i = new Intent();
        i.putExtra("fecha", fecha.getText().toString());
        setResult(77,i);

        finish();


    }
});





    bactualizar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ano = Integer.valueOf(anio.getText().toString());
            Calendar fcalendar = Calendar.getInstance();
            fcalendar.set(Calendar.YEAR, ano);
            fcalendar.set(Calendar.MONTH, mes);
            fcalendar.set(Calendar.DAY_OF_MONTH, dia);

            long milliTime = fcalendar.getTimeInMillis();
            calendar.setDate (milliTime, true, true);

        }
    });

verificaparametro();
    }
//**********************************************************
    private void verificaparametro() {
        String fec = "";
        try {
            fec = this.getIntent().getExtras().getString("fecha");

            if (!EsErrorfecha(fec)){

                String xvalores[] = fec.split("/");
                dia  = Integer.valueOf(xvalores[0]);
                mes  = Integer.valueOf(xvalores[1]);
                ano  = Integer.valueOf(xvalores[2]);


                anio.setText(String.valueOf(ano));

                Calendar pcalendar = Calendar.getInstance();
                pcalendar.set(Calendar.YEAR, ano);
                pcalendar.set(Calendar.MONTH, mes-1);
                pcalendar.set(Calendar.DAY_OF_MONTH, dia);

                long milliTime = pcalendar.getTimeInMillis();
                calendar.setDate(milliTime, true, true);


            }





        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }


    }

    //******************************************************
    private boolean EsErrorfecha(String fec) {
    boolean error = false;

        if (!error && fec.isEmpty())
        {
            error = true;
        }

        if (!error && fec.length() > 9){

            String valores[] = fec.toString().split("/");
            int dia  = Integer.valueOf(valores[0]);
            int mes  = Integer.valueOf(valores[1]);
            int ano  = Integer.valueOf(valores[2]);

            if (!error && ((dia == 0) || (dia > 31)))
            {
                error = true;
            }

            if (!error && ((mes == 0) || (mes > 12)))
            {
                error = true;
            }

            if (!error && ((ano == 0) || (ano > 2030)))
            {
                error = true;
            }


        }
        if (!error && fec.length() < 9){
            error = true;
        }

    return error;
}
}
