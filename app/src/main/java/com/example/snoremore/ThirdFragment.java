package com.example.snoremore;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ThirdFragment extends DialogFragment {
    EditText name, wake, sleep, age;
    Button button;
    TextView wake_message, name_message, sleep_message, age_message;
    public String data_w, data_n, data_s, data_age, wake_time, sleep_time;
    private SharedPreferenceManager preferenceManager_n, preferenceManager_w,
            preferenceManager_s, preferenceManager_a;
    int wake_hour, wake_minute, sleep_hour, sleep_minute;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View v = inflater.inflate(R.layout.fragment_third, container, false);
        name = v.findViewById(R.id.input_User);
        age  = v.findViewById(R.id.input_age);
        name_message = v.findViewById(R.id.username_message);
        wake_message = v.findViewById(R.id.wake_message);
        sleep_message = v.findViewById(R.id.sleep_message);
        age_message = v.findViewById(R.id.age_message);
        ImageView imageView = v.findViewById(R.id.image_view);

        Picasso.with(getContext())
                .load(R.drawable.background_image)
                .resize(600, 600)
                .into(imageView);

        preferenceManager_n = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_NAME);
        preferenceManager_w = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_WAKE);
        preferenceManager_s = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_SLEEP);
        preferenceManager_a = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_AGE);

        data_n = preferenceManager_n.getValue(KeyString.PREFERENCE_NAME, "Text");
        name_message.setText("Name "+ data_n);
        data_w = preferenceManager_w.getValue(KeyString.PREFERENCE_WAKE, "Text");
        wake_message.setText("Wake Time "+ data_w);
        data_s = preferenceManager_s.getValue(KeyString.PREFERENCE_SLEEP, "Text");
        sleep_message.setText("Sleep Time " + data_s);
        data_age = preferenceManager_a.getValue(KeyString.PREFERENCE_AGE, "Text");
        age_message.setText("Age " + data_age);

        v.findViewById(R.id.wakeup_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar wake_time_cal = Calendar.getInstance();
                                wake_time_cal.set(Calendar.SECOND, 0);
                                wake_time_cal.set(Calendar.MINUTE, minute);
                                wake_time_cal.set(Calendar.HOUR_OF_DAY, hourOfDay);

                                wake_time = new SimpleDateFormat("hh:mm aa").format(wake_time_cal.getTimeInMillis());
                                preferenceManager_w.setValue(KeyString.PREFERENCE_WAKE, wake_time);
                                wake_message.setText("Wake Time: " +  wake_time);

                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(wake_hour, wake_minute);
                timePickerDialog.show();
            }
        });

        v.findViewById(R.id.sleep_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar sleep_time_cal = Calendar.getInstance();
                                sleep_time_cal.set(Calendar.SECOND, 0);
                                sleep_time_cal.set(Calendar.MINUTE, minute);
                                sleep_time_cal.set(Calendar.HOUR_OF_DAY, hourOfDay);

                                sleep_time = new SimpleDateFormat("hh:mm aa").format(sleep_time_cal.getTimeInMillis());
                                preferenceManager_s.setValue(KeyString.PREFERENCE_SLEEP, sleep_time);
                                sleep_message.setText("Sleep Time: " +  sleep_time);
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(sleep_hour, sleep_minute);
                timePickerDialog.show();
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.button_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name_message.setText("Name " + name.getText().toString());
                age_message.setText("Age " + age.getText().toString());
                preferenceManager_n.setValue(KeyString.PREFERENCE_NAME, name.getText().toString());
                preferenceManager_a.setValue(KeyString.PREFERENCE_AGE, age.getText().toString());
            }
        });
        //Changing the button
        view.findViewById(R.id.button_Fifth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ThirdFragment.this)
                        .navigate(R.id.action_thirdFragment2_to_maps_Fragment2);
            }
        });
        view.findViewById(R.id.button_Fourth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ThirdFragment.this)
                        .navigate(R.id.action_thirdFragment2_to_SecondFragment);
            }
        });
    }


}
