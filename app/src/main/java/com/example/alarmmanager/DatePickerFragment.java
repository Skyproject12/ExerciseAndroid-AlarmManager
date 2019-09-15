package com.example.alarmmanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

// make like popoup to choose date
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DialogDateListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context!=null){
            mListener= (DialogDateListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // ketika mListener kosong
        if(mListener!=null){
            mListener=null;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar= Calendar.getInstance();
        // call value form datepicker
        int year= calendar.get(Calendar.YEAR);
        int month= calendar.get(Calendar.MONTH);
        int date= calendar.get(Calendar.DATE);
        // give return of value
        return new DatePickerDialog(getActivity(), this, year, month,date);
    }

    // berfungsi memilih tanggal yang diinginkan lalu mengirim ke main
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        mListener.onDialogDataSet(getTag(),year, month, dayOfMonth);
    }

    public interface DialogDateListener{
        void onDialogDataSet(String tag, int year, int month, int dayOfMonth);
    }

}
