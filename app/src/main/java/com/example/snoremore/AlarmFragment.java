package com.example.snoremore;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class AlarmFragment extends Fragment {
    Button button;
    TextView sleep_message, wake_message, name_message, hour_message, recommend_message1,
            recommend_message2, recommend_message3, recommend_sleep;
    public String data_w, data_n, data_s, data_h, data_age, data_sunrise;
    private SharedPreferenceManager preferenceManager_w, preferenceManager_s, preferenceManager_age,
            preferenceManager_n,  preferenceManager_h, preferenceManager_sunrise, preferenceManager_calories,
            preferenceManager_distance, preferenceManager_gain;
    private int calories_boolean, distance_boolean;
    private boolean gain_boolean;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alarm, container, false);
        name_message = v.findViewById(R.id.name_message);
        sleep_message = v.findViewById(R.id.alarm_sleep_message);
        wake_message = v.findViewById(R.id.alarm_wake_message);
        hour_message = v.findViewById(R.id.alarm_sleep_hour);
        recommend_message1 = v.findViewById(R.id.alarm_recommendation1);
        recommend_message2 = v.findViewById(R.id.alarm_recommendation2);
        recommend_message3 = v.findViewById(R.id.alarm_recommendation3);
        recommend_sleep = v.findViewById(R.id.recommend_sleep_hour);

        ImageView imageView = v.findViewById(R.id.image_view);
        Picasso.with(getContext())
                .load(R.drawable.background_image)
                .resize(600, 600)
                .into(imageView);

        preferenceManager_w = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_WAKE);
        preferenceManager_s = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_SLEEP);
        preferenceManager_n = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_NAME);
        preferenceManager_h = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_HOUR);
        preferenceManager_age = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_AGE);
        preferenceManager_sunrise = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_SUNRISE);
        preferenceManager_calories = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_CALORIES);
        preferenceManager_distance = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_DISTANCE);
        preferenceManager_gain = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_GAIN);


        //String wake, and string sleep
        data_w = preferenceManager_w.getValue(KeyString.PREFERENCE_WAKE, "text");
        data_s = preferenceManager_s.getValue(KeyString.PREFERENCE_SLEEP, "text");
        data_n = preferenceManager_n.getValue(KeyString.PREFERENCE_NAME, "text");
        data_age = preferenceManager_age.getValue(KeyString.PREFERENCE_AGE, "text");
        data_sunrise = preferenceManager_sunrise.getValue(KeyString.PREFERENCE_SUNRISE, "text");

        name_message.setText("Name: " + data_n);
        wake_message.setText("Wake Time: " + data_w);
        sleep_message.setText("Sleep Time: " + data_s);

        SimpleDateFormat myFormat = new SimpleDateFormat("KK:mm aa");
        Date sleep_time = null;
        Date wake_time = null;
        Date sun_rise = null;
        try {
            sun_rise = myFormat.parse(data_sunrise);
            sleep_time = myFormat.parse(data_s);
            wake_time = myFormat.parse(data_w);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //get the wake time in calendar
        Calendar wake = Calendar.getInstance();
        wake.setTime(wake_time);
        //get the sleep time in calendar
        Calendar sleep = Calendar.getInstance();
        sleep.setTime(sleep_time);
        //get the sleep time in calendar
        Calendar sunrise_time = Calendar.getInstance();
        sunrise_time.setTime(sun_rise);

        LocalDateTime local_sunrise =  LocalDateTime.now();
        local_sunrise = local_sunrise.withHour(sunrise_time.get(Calendar.HOUR_OF_DAY)).withMinute(sunrise_time.get(Calendar.MINUTE)).withSecond(0).withNano(0);

        LocalDateTime local_sleep = LocalDateTime.now();
        local_sleep = local_sleep.withHour(sleep.get(Calendar.HOUR_OF_DAY)).withMinute(sleep.get(Calendar.MINUTE)).withSecond(0).withNano(0);

        LocalDateTime local_wake = LocalDateTime.now();
        if ((int) sleep.get(Calendar.HOUR_OF_DAY) >= 6)
            local_wake = local_wake.plusDays(1).withHour(wake.get(Calendar.HOUR_OF_DAY)).withMinute(wake.get(Calendar.MINUTE)).withSecond(0).withNano(0);
        else
            local_wake = local_wake.withHour(wake.get(Calendar.HOUR_OF_DAY)).withMinute(wake.get(Calendar.MINUTE)).withSecond(0).withNano(0);
        Duration diff = Duration.between(local_wake, local_sleep);
        Duration diff_sunrise = Duration.between(local_wake, local_sunrise);

        System.out.println("local_sleep = " + local_sleep);
        System.out.println("local_wake = " + local_wake);


        System.out.println("diff_sunrise = " + (double) diff_sunrise.toMinutes());
        int sunriseBoolean = 0;
        if ((double) diff_sunrise.toMinutes() < -30)
            sunriseBoolean = 1;
        else if ((double) diff_sunrise.toMinutes() > 30)
            sunriseBoolean = -1;


        //Display difference in hour
        double difference_hours = (double) diff.toMinutes();
        difference_hours /= 60;

        if (difference_hours < 0)
            difference_hours *= -1;

        System.out.println("difference_hours = " + difference_hours);
        String time = "Sleep Amount: " + difference_hours + " hours";
        preferenceManager_h.setValue(KeyString.PREFERENCE_HOUR, time);
        data_h = preferenceManager_h.getValue(KeyString.PREFERENCE_HOUR, "");
        hour_message.setText(time);

        int min_sleep_time = 0, mid_sleep_time = 0, max_sleep_time = 0;
        double rec_sleep_time = 0.0;
        int int_age = Integer.parseInt(data_age);

        if (int_age >= 1 && int_age <= 2)
        {
            min_sleep_time = 9;
            mid_sleep_time = 13;
            max_sleep_time = 16;
        }
        else if (int_age >= 3 && int_age <= 5)
        {
            min_sleep_time = 8;
            mid_sleep_time = 11;
            max_sleep_time = 14;
        }
        else if (int_age >= 6 && int_age <= 13)
        {
            min_sleep_time = 7;
            mid_sleep_time = 10;
            max_sleep_time = 12;
        }
        else if (int_age >= 14 && int_age <= 17)
        {
            min_sleep_time = 7;
            mid_sleep_time = 9;
            max_sleep_time = 11;
        }
        else if (int_age >= 18 && int_age <= 25)
        {
            min_sleep_time = 6;
            mid_sleep_time = 9;
            max_sleep_time = 11;
        }
        else {
            min_sleep_time = 5;
            mid_sleep_time = 7;
            max_sleep_time = 9;
        }
        /*if 1 or 2 years:
          Sleep 9-16 (13) hours
          if 3-5 years:
          Sleep 8-14 (11) hours
          if 6-13 years:
          Sleep 7-12 (10) hours
          if 14-17 years:
          Sleep 7-11 (9) hours
          if 18-25 years:
          Sleep 6-11 (9) hours
          if 26+ years:
          Sleep 5-9 (7) hours
         */
        //(From preferred wake up time, subtract the higher threshold and those will be the potential results. We will rank the results after based on their goals)
        //Display recommendation message
        //if 1 or 2 years:

        if (difference_hours < (double) min_sleep_time) {
            String message1 = "You are not getting enough sleep.";
            String message2 = "If you continue to operate without enough sleep,";
            String message3 = "You may see more long-term or serious health problems.";
            recommend_message1.setText(message1);
            recommend_message2.setText(message2);
            recommend_message3.setText(message3);
            rec_sleep_time = (double) min_sleep_time;
        } else if (difference_hours > (double) max_sleep_time) {
            String message1 = "You are getting too much sleep.";
            String message2 = "If you continue to operate with too much sleep,";
            String message3 = "You may see more long-term or serious health problems.";
            recommend_message1.setText(message1);
            recommend_message2.setText(message2);
            recommend_message3.setText(message3);
            rec_sleep_time = (double) max_sleep_time;
        }
        else {
            String message1 = "You are getting healthy, balanced sleep.";
            String message2 = "Congratulations, keep up your good work!";
            String message3 = "Remember that sleeping is important.";
            recommend_message1.setText(message1);
            recommend_message2.setText(message2);
            recommend_message3.setText(message3);
            rec_sleep_time = difference_hours;
        }
        System.out.println("rec_sleep_time = " + rec_sleep_time);

        calories_boolean = preferenceManager_calories.getValue_int(KeyString.PREFERENCE_CALORIES, 0);
        distance_boolean = preferenceManager_distance.getValue_int(KeyString.PREFERENCE_DISTANCE, 0);
        gain_boolean = preferenceManager_gain.getValue_bool(KeyString.PREFERENCE_GAIN, false);

        int gain_value = 0;
        if (gain_boolean)
            gain_value = -1;

        sunriseBoolean = 1;

        System.out.println("distance_boolean = " + distance_boolean);
        System.out.println("calories_boolean = " + calories_boolean);
        System.out.println("gain_boolean = " + gain_boolean);

        double sleep_change = 0.0;
        sleep_change += (0.05 * rec_sleep_time) * sunriseBoolean;
        sleep_change += (0.05 * rec_sleep_time) * calories_boolean;
        sleep_change += (0.05 * rec_sleep_time) * distance_boolean;
        sleep_change += (0.05 * rec_sleep_time) * gain_value;
        rec_sleep_time += sleep_change;

        if (rec_sleep_time < min_sleep_time)
            rec_sleep_time = min_sleep_time;
        else if (rec_sleep_time > max_sleep_time)
            rec_sleep_time = max_sleep_time;

        long rec_sleep_time_hours = (long) rec_sleep_time;
        double temp = rec_sleep_time - ((int) rec_sleep_time);
        temp *= 60;
        long rec_sleep_time_minutes = (long) temp;

        System.out.println("rec_sleep_time_hours = " + rec_sleep_time_hours);
        System.out.println("rec_sleep_time_minutes = " + rec_sleep_time_minutes);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("KK:mm a");
        local_sleep = local_wake.minusHours(rec_sleep_time_hours);
        local_sleep = local_sleep.minusMinutes(rec_sleep_time_minutes);
        String formattedDateTime = local_sleep.format(formatter);
        String sleep_recommend = formattedDateTime;
        recommend_sleep.setText(sleep_recommend);
        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Changing the button
        view.findViewById(R.id.button_tenth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AlarmFragment.this)
                        .navigate(R.id.action_alarmFragment_to_maps_Fragment);
            }
        });
    }
}
