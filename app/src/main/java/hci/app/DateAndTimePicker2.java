package hci.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Calendar;

public class DateAndTimePicker2 extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

        final Calendar cEnd = Calendar.getInstance();
        final Calendar cStart = Calendar.getInstance();


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar cEnd = Calendar.getInstance();
        final Calendar cStart = Calendar.getInstance();

        if(/*button start time*/){
            int year = cStart.get(Calendar.YEAR);
            int month = cStart.get(Calendar.MONTH);
            int day = cStart.get(Calendar.DAY_OF_MONTH);

        } else if(/*button start time*/){
            int year = cStart.get(Calendar.YEAR);
            int month = cStart.get(Calendar.MONTH);
            int day = cStart.get(Calendar.DAY_OF_MONTH);

        }


    }



    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

    }
}
