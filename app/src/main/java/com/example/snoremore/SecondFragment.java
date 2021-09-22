package com.example.snoremore;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class SecondFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private TextView dateText, goalText;
    private SharedPreferenceManager preferenceManager_d, preferenceManager_goal, preferenceManager_gain,
            preferenceManager_lose, preferenceManager_maintain;
    private long data_d;
    private String currentDateString,currentTimeString, data_goal, goal_string;
    private boolean data_gain, data_lose, data_maintain;

    private RadioButton Rgain_weight;
    private RadioButton Rlose_weight;
    private RadioButton Rmtain_weight;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        dateText = view.findViewById(R.id.textview_date_text);
        goalText = view.findViewById(R.id.textview_goal_text);
        ImageView imageView = view.findViewById(R.id.image_view);

        Rgain_weight = view.findViewById(R.id.gain_weight);
        Rlose_weight = view.findViewById(R.id.lose_weight);
        Rmtain_weight = view.findViewById(R.id.maintain_weight);

        preferenceManager_d = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_DATE);
        preferenceManager_goal = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_GOAL);
        preferenceManager_gain = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_GAIN);
        preferenceManager_lose = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_LOSE);
        preferenceManager_maintain = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_MAINTAIN);

        data_d = preferenceManager_d.getValue_long(KeyString.PREFERENCE_DATE, 0L);
        data_goal = preferenceManager_goal.getValue(KeyString.PREFERENCE_GOAL, "yyy-MM-dd");

        data_gain = preferenceManager_gain.getValue_bool(KeyString.PREFERENCE_GAIN, false);
        data_lose = preferenceManager_lose.getValue_bool(KeyString.PREFERENCE_LOSE, false);
        data_maintain = preferenceManager_maintain.getValue_bool(KeyString.PREFERENCE_MAINTAIN, false);

        currentDateString = new SimpleDateFormat("yyyy-MM-dd").format(new Date(data_d));

        Rgain_weight.setChecked(data_gain);
        Rlose_weight.setChecked(data_lose);
        Rmtain_weight.setChecked(data_maintain);

        if (currentDateString.equals("1969-12-31")) {
            dateText.setText("");
        }
        else {
            dateText.setText(currentDateString);
        }
        goalText.setText(data_goal);

        Rgain_weight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean gain_isChecked) {
                preferenceManager_gain.setValue_bool(KeyString.PREFERENCE_GAIN, gain_isChecked);
            }
        });

        Rlose_weight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean lose_isChecked) {
                preferenceManager_lose.setValue_bool(KeyString.PREFERENCE_LOSE, lose_isChecked);
            }
        });

        Rmtain_weight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean maintain_isChecked) {
                preferenceManager_maintain.setValue_bool(KeyString.PREFERENCE_MAINTAIN, maintain_isChecked);
            }
        });

        view.findViewById(R.id.date_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        // Inflate the layout for this fragment
        Picasso.with(getContext())
                .load(R.drawable.background_image)
                .resize(600, 600)
                .into(imageView);
        return view;
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this.getContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
        view.findViewById(R.id.button_third).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_thirdFragment2);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar goal_date = Calendar.getInstance();
        goal_date.set(Calendar.YEAR, year);
        goal_date.set(Calendar.MONTH, month);
        goal_date.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        goal_string = new SimpleDateFormat("yyyy-MM-dd").format(goal_date.getTimeInMillis());
        preferenceManager_goal.setValue(KeyString.PREFERENCE_GOAL, goal_string);

        Date currentDate = new Date();
        preferenceManager_d.setValue_Calendar(KeyString.PREFERENCE_DATE, currentDate);
        data_d = preferenceManager_d.getValue_long(KeyString.PREFERENCE_DATE, 0L);
        currentDateString = new SimpleDateFormat("yyyy-MM-dd").format(new Date(data_d));

        dateText.setText(currentDateString);

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        Date goalDate = null;
        try {
            date = myFormat.parse(currentDateString);
            goalDate = myFormat.parse(data_goal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar current_date = Calendar.getInstance();
        current_date.setTime(date);

        // Get the represented date in milliseconds
        long millis1 = goal_date.getTimeInMillis();
        long millis2 = current_date.getTimeInMillis();
        //Calculate difference in milliseconds
        long diff = millis1 - millis2;
        // Calculate difference in days
        long diffDays = diff / (24 * 60 * 60 * 1000);

        data_goal = preferenceManager_goal.getValue(KeyString.PREFERENCE_GOAL, "");

        String res = ", " + diffDays + " days away";
        goalText.setText(data_goal + " " + res);
    }
}
