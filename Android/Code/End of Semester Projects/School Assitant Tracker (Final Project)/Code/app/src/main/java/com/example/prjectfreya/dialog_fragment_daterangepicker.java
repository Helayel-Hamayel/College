package com.example.prjectfreya;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

public class dialog_fragment_daterangepicker extends DialogFragment {

    private DataPickerListener mListener;

    static String TAG = "fragmentdate";
    Boolean isitFirstValueNow = true; //Locker key

    TextView DateA;
    private Button SaveData,CancelDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_daterangepicker,container,false);

        DateA = v.findViewById(R.id.dateAb);

        SaveData = v.findViewById(R.id.SaveButt);
        CancelDialog = v.findViewById(R.id.CancelButt);

        SaveData = v.findViewById(R.id.SaveButt);
        CancelDialog = v.findViewById(R.id.CancelButt);

        SaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.saveTheDate(DateA.getText().toString());
                dismiss();
            }
        });

        CancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Date today = new Date();
        Calendar nextMonth = Calendar.getInstance();
        nextMonth.add(Calendar.MONTH, 2);
        final CalendarPickerView datePicker = v.findViewById(R.id.calendar);
        datePicker.init(today, nextMonth.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE);

        final Calendar FRST_calSelected = Calendar.getInstance();
        FRST_calSelected.setTime(today);


        datePicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {

                Calendar SCND_calSelected = Calendar.getInstance();

                //the lock
                if(isitFirstValueNow) {
                    FRST_calSelected.setTime(date);
                    isitFirstValueNow = false;
                }
                else{
                    SCND_calSelected.setTime(date);
                }

                String FRST_selectedDate = "NaN";
                String SCND_selectedDate = "NaN";


                if(FRST_calSelected.get(Calendar.YEAR) == SCND_calSelected.get(Calendar.YEAR)){//Year check
                    if(FRST_calSelected.get(Calendar.MONTH) == SCND_calSelected.get(Calendar.MONTH)){//Month check
                        if(FRST_calSelected.get(Calendar.DAY_OF_MONTH) == SCND_calSelected.get(Calendar.DAY_OF_MONTH)){//Day check

                            FRST_selectedDate = FRST_calSelected.get(Calendar.DAY_OF_MONTH)
                                    + "/" + (FRST_calSelected.get(Calendar.MONTH) + 1)
                                    + "/" + FRST_calSelected.get(Calendar.YEAR);

                            Log.d(TAG,"The SINGLE date is : "+FRST_selectedDate);
                            DateA.setText(FRST_selectedDate);
                        }
                        else if(FRST_calSelected.get(Calendar.DAY_OF_MONTH) < SCND_calSelected.get(Calendar.DAY_OF_MONTH)){
                            FRST_selectedDate = FRST_calSelected.get(Calendar.DAY_OF_MONTH)
                                    + "/" + (FRST_calSelected.get(Calendar.MONTH) + 1)
                                    + "/" + FRST_calSelected.get(Calendar.YEAR);

                            SCND_selectedDate = SCND_calSelected.get(Calendar.DAY_OF_MONTH)
                                    + "/" + (SCND_calSelected.get(Calendar.MONTH) + 1)
                                    + "/" + SCND_calSelected.get(Calendar.YEAR);

                            Log.d(TAG,"The RANGE date is : "+FRST_selectedDate +" TO "+SCND_selectedDate);
                            DateA.setText(FRST_selectedDate +" => "+SCND_selectedDate);
                            isitFirstValueNow = true;
                        }
                        else {FRST_calSelected.setTime(date);}
                    }
                    else if(FRST_calSelected.get(Calendar.MONTH) < SCND_calSelected.get(Calendar.MONTH)){
                        FRST_selectedDate = FRST_calSelected.get(Calendar.DAY_OF_MONTH)
                                + "/" + (FRST_calSelected.get(Calendar.MONTH) + 1)
                                + "/" + FRST_calSelected.get(Calendar.YEAR);

                        SCND_selectedDate = SCND_calSelected.get(Calendar.DAY_OF_MONTH)
                                + "/" + (SCND_calSelected.get(Calendar.MONTH) + 1)
                                + "/" + SCND_calSelected.get(Calendar.YEAR);

                        Log.d(TAG,"The RANGE date is : "+FRST_selectedDate +" TO "+SCND_selectedDate);
                        DateA.setText(FRST_selectedDate +" TO "+SCND_selectedDate);
                        isitFirstValueNow = true;
                    }
                    else{FRST_calSelected.setTime(date);}
                }
                else if(FRST_calSelected.get(Calendar.YEAR) < SCND_calSelected.get(Calendar.YEAR)){
                    FRST_selectedDate = FRST_calSelected.get(Calendar.DAY_OF_MONTH)
                            + "/" + (FRST_calSelected.get(Calendar.MONTH) + 1)
                            + "/" + FRST_calSelected.get(Calendar.YEAR);

                    SCND_selectedDate = SCND_calSelected.get(Calendar.DAY_OF_MONTH)
                            + "/" + (SCND_calSelected.get(Calendar.MONTH) + 1)
                            + "/" + SCND_calSelected.get(Calendar.YEAR);

                    Log.d(TAG,"The RANGE date is : "+FRST_selectedDate +" TO "+SCND_selectedDate);
                    DateA.setText(FRST_selectedDate +" TO "+SCND_selectedDate);
                    isitFirstValueNow = true;

                }
                else{ FRST_calSelected.setTime(date); }


            }
            @Override
            public void onDateUnselected(Date date) {
            }
        });

        return v;
    }

    public interface DataPickerListener {
        void saveTheDate(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (dialog_fragment_daterangepicker.DataPickerListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
        }
    }
}
