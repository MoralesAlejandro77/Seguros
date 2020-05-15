package com.seguros.presupuestos;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public abstract class MaskWatcher {

    public static TextWatcher insert(final String mask, final EditText et) {
        return new TextWatcher() {
            boolean isUpdating;
            String oldTxt = "";
            public void onTextChanged( CharSequence s, int start, int before,int count) {
                int tot = countChar(s.toString(),'/');
                if (tot < 3)
                {
                    String str = MaskWatcher.unmask(s.toString());
                    String maskCurrent = "";
                    if (isUpdating) {
                        oldTxt = str;
                        isUpdating = false;
                        return;
                    }
                    int i = 0;
                    for (char m : mask.toCharArray()) {
                        if (m != '#' && str.length() > oldTxt.length()) {
                            maskCurrent += m;
                            continue;
                        }
                        try {
                            maskCurrent += str.charAt(i);
                        } catch (Exception e) {
                            break;
                        }
                        i++;
                    }
                    isUpdating = true;
                    et.setText(maskCurrent);
                    et.setSelection(maskCurrent.length());
                }

            }
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}
        };
    }
    private static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "");
    }

//*******************************************************
public static int countChar(String str, char c)
{
    int count = 0;

    for(int i=0; i < str.length(); i++)
    {    if(str.charAt(i) == c)
        count++;
    }

    return count;
}
}