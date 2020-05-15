package com.seguros.presupuestos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class VerificarWifi extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
    context.startService(new Intent(context,Publicaciones.class));
 	 
    	
    }
}