package com.seguros.presupuestos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.seguros.Datos.Datos;
import com.seguros.Datos.DatosBDTablas;

public class SepelioCalc extends FragmentActivity {
    TextView edad, total;
    String resultado;
    RadioButton idplana, idplanc, idplang, idtarifa3, idtarifa29;
    Button idborrar,bok;
    CheckBox chk_parcela, chk_luto, gf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sepelio_calc);
        edad          = (TextView)    findViewById(R.id.calcedad);
        total         = (TextView)    findViewById(R.id.idtotal);
        idplana       = (RadioButton) findViewById(R.id.idplana);
        idplanc       = (RadioButton) findViewById(R.id.idplanc);
        idplang       = (RadioButton) findViewById(R.id.idplang);
        idtarifa3     = (RadioButton) findViewById(R.id.idtarifa3);
        idtarifa29    = (RadioButton) findViewById(R.id.idtarifa29);
        idborrar      = (Button)      findViewById(R.id.idbborrar);
        bok           = (Button)      findViewById(R.id.bok);
        gf            = (CheckBox)    findViewById(R.id.gf);
        chk_parcela   = (CheckBox)    findViewById(R.id.chk_parcela);
        chk_luto      = (CheckBox)    findViewById(R.id.chk_luto);
        resultado  = "";
        total.setText("0,00");
        this.setTitle("");



        idborrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String palabra = edad.getText().toString();
                int longitud = palabra.length();
                if (longitud <= 1){
                    palabra = "";
                    total.setText("0.00");
                }
                else
                    palabra = palabra.substring(0,longitud-1);
                resultado = palabra;
                edad.setText(resultado);
            }
        });

        bok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String p = "A";
                if (idplanc.isChecked())
                    p = "C";
                if (idplang.isChecked())
                    p = "G";

                String t = "3";
                if (idtarifa29.isChecked())
                    t = "29";

                Intent i = new Intent();
                i.putExtra("tarifa", t);
                i.putExtra("plan", p);
                i.putExtra("gf", gf.isChecked());
                i.putExtra("parcela", chk_parcela.isChecked());
                i.putExtra("luto", chk_luto.isChecked());

                String e = edad.getText().toString();
                if (edad.equals(""))
                    e = "0";

                i.putExtra("edad", e);

                setResult(77,i);

                finish();

            }
        });



/**************************************************************/
        String tarifa = "";
        idtarifa3.setChecked(false);
        idtarifa29.setChecked(false);

        try {
            tarifa = this.getIntent().getExtras().getString("tarifa");
        } catch (Exception e) {
        }
        if (tarifa.equals("3")  )
            idtarifa3.setChecked(true);
        if (tarifa.equals("29")  )
            idtarifa29.setChecked(true);

/**************************************************************/
        idplana.setChecked(false);
        idplanc.setChecked(false);
        idplang.setChecked(false);

        String plan = "";
        try {
            plan = this.getIntent().getExtras().getString("plan");
        } catch (Exception e) {
        }
        if (plan.equals("A") || plan.equals("B") )
            idplana.setChecked(true);
        if (plan.equals("C"))
            idplanc.setChecked(true);
        if (plan.equals("G"))
            idplang.setChecked(true);
/**************************************************************/

        String sgf = "NO";
        try {
            if (this.getIntent().getExtras().getBoolean("gf"))
                sgf = "SI";
        } catch (Exception e) {
        }
        if (sgf.equals("SI"))
            gf.setChecked(true);
/**************************************************************/
        chk_parcela.setChecked(this.getIntent().getExtras().getBoolean("parcela"));
        chk_luto.setChecked(this.getIntent().getExtras().getBoolean("luto"));


    }

    //*******************************************************************************************************************************
    public void calcular(View view) {
        String digito = ((Button)view).getText().toString();

        resultado = resultado + digito;
     //   Toast.makeText(getApplicationContext(),resultado,Toast.LENGTH_LONG).show();
        edad.setText(resultado);
        calcularPrima();



    }
/**********************************************************************************/
public void calcular_valor(View v) {
    calcularPrima();
}
    /**********************************************************************************/

    private void calcularPrima() {
        total.setText("0.00");
        int ttarifa = 3;
        if (idtarifa29.isChecked())
            ttarifa = 29;

        String p = "A";
        if (idplanc.isChecked())
            p = "C";
        if (idplang.isChecked())
            p = "G";

        int ed = 0;
        try
        {
            ed  = Integer.valueOf(edad.getText().toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
        if (gf.isChecked())
           ed = 999;


        DatosBDTablas db = new DatosBDTablas(getApplicationContext());
        db.open();
        float [] valores = Datos.calcular_sepelio_completo(ttarifa,db, ed, p);
        db.close();
        float sepelio = valores[0];
        float parcela = valores[1];
        float luto    = valores[2];
        float prima   = valores[3];

        if (!chk_parcela.isChecked())
            parcela = 0;

        if (!chk_luto.isChecked())
            luto = 0;
        total.setText(String.format("%.02f",(sepelio + parcela + luto)));

    }

    public void planes(View v) {
        switch (v.getId()) {
            case R.id.idplana:
                idplana.setChecked(true);
                idplanc.setChecked(false);
                idplang.setChecked(false);
                break;
            case R.id.idplanc:
                idplana.setChecked(false);
                idplanc.setChecked(true);
                idplang.setChecked(false);
                break;
            case R.id.idplang:
                idplana.setChecked(false);
                idplanc.setChecked(false);
                idplang.setChecked(true);

                break;
        }
        calcularPrima();
    }

    public void tarifas(View v) {
        switch (v.getId()) {
            case R.id.idtarifa3:
                idtarifa3.setChecked(true);
                idtarifa29.setChecked(false);
                break;
            case R.id.idtarifa29:
                idtarifa3.setChecked(false);
                idtarifa29.setChecked(true);
                break;

        }
        calcularPrima();
    }
}
