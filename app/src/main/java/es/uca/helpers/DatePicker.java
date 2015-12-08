package es.uca.helpers;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.uca.gestionproyectos.R;

public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private Activity activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        activity = getActivity();

        return new DatePickerDialog(activity, this, year, month, day);
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        TextView textFecha = (TextView)activity.findViewById(R.id.text_fecha);

        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        textFecha.setText(sdf.format(c.getTime()));
    }
}