package com.example.snoremore;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class Maps_Fragment extends Fragment {
    double lat, lng;
    private SharedPreferenceManager preferenceManager_d,  preferenceManager_sunrise, preferenceManager_sunset;
    TextView date_message, sunrise_message, sunset_message;
    public long data_d;
    public String sunrise;
    public String sunset;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps_, container, false);
        date_message = v.findViewById(R.id.textview_date);
        sunrise_message = v.findViewById(R.id.textview_sunrise);
        sunset_message = v.findViewById(R.id.textview_sunset);
        ImageView imageView = v.findViewById(R.id.image_view);

        Picasso.with(getContext())
                .load(R.drawable.star_background)
                .resize(800, 600)
                .into(imageView);

        //Display date
        preferenceManager_d = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_DATE);
        data_d = preferenceManager_d.getValue_long(KeyString.PREFERENCE_DATE, 0L);

        preferenceManager_sunrise = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_SUNRISE);
        preferenceManager_sunset  = new SharedPreferenceManager(getActivity(), KeyString.PREFERENCE_SUNSET);
        //Today date
        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date(data_d));
        date_message.setText(currentDateTimeString);
        //Sunrise and sunset
        sunrise = preferenceManager_sunrise.getValue(KeyString.PREFERENCE_SUNRISE, "");
        sunset  = preferenceManager_sunset.getValue(KeyString.PREFERENCE_SUNSET, "");
        sunrise_message.setText("Sunrise Time: " + sunrise);
        sunset_message.setText("Sunset Time: " + sunset);

        //Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //When map is loaded
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onMapClick(LatLng latLng) {
                        //When click on map, initiate marker option
                        MarkerOptions markerOptions = new MarkerOptions();
                        //Set position on marker
                        markerOptions.position(latLng);
                        //Set the title of marker
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                        lat = latLng.latitude;
                        lng = latLng.longitude;

                        //Initialize parameters for GET request
                        //https://api.sunrise-sunset.org/json?lat=36.7201600&lng=-4.4203400
                        String link = "https://api.sunrise-sunset.org/json?lat=" +  lat + "&lng=" + lng + "&date=today";
                        String inline = "";

                        //Send GET request to retrieve sunrise and sunset
                        try {
                            URL url = new URL(link);
                            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.connect();
                            Scanner scan = new Scanner(url.openStream());
                            while (scan.hasNext()) {
                                inline += scan.nextLine();
                            }
                            JSONObject json = new JSONObject(inline);
                            sunrise = (String) json.getJSONObject("results").get("sunrise");
                            sunset = (String) json.getJSONObject("results").get("sunset");
                            scan.close();
                        }
                        catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat myFormat = new SimpleDateFormat("hh:mm:ss aa");

                        // SUNRISE
                        Date sunrise_time = null;
                        try {
                            sunrise_time = myFormat.parse(sunrise);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar sunrise = Calendar.getInstance();
                        sunrise.setTime(sunrise_time);
                        LocalDateTime local_sunrise = LocalDateTime.now();
                        local_sunrise = local_sunrise.withHour(sunrise.get(Calendar.HOUR_OF_DAY)).withMinute(sunrise.get(Calendar.MINUTE)).withSecond(0).withNano(0);
                        local_sunrise = local_sunrise.minusHours(8);

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("KK:mm a");
                        String formattedSunrise = local_sunrise.format(formatter);

                        // SUNSET
                        Date sunset_time = null;
                        try {
                            sunset_time = myFormat.parse(sunset);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar sunset = Calendar.getInstance();
                        sunset.setTime(sunset_time);
                        LocalDateTime local_sunset = LocalDateTime.now();
                        local_sunset = local_sunset.withHour(sunset.get(Calendar.HOUR_OF_DAY)).withMinute(sunset.get(Calendar.MINUTE)).withSecond(0).withNano(0);
                        local_sunset = local_sunset.minusHours(8);

                        String formattedSunset = local_sunset.format(formatter);
                        sunset_message.setText("Sunset time: " + formattedSunset);
                        sunrise_message.setText("Sunrise time: " + formattedSunrise);

                        preferenceManager_sunrise.setValue(KeyString.PREFERENCE_SUNRISE, formattedSunrise);
                        preferenceManager_sunset.setValue(KeyString.PREFERENCE_SUNSET, formattedSunset);

                        //sunset_message.setText("Sunset Time: " + newSunSet);

                        //Remove all marker
                        googleMap.clear();
                        //Animating to zoom the marker
                        googleMap.animateCamera((CameraUpdateFactory.newLatLngZoom(
                                latLng, 10
                        )));
                        //Add marker on map
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        } );
        // Inflate the layout for this fragment
        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.button_eight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Maps_Fragment.this)
                        .navigate(R.id.action_maps_Fragment_to_thirdFragment22);
            }
        });
        view.findViewById(R.id.button_ninth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Calendar calendar = Calendar.getInstance();
//
//                Intent intent  = new Intent(getContext().getApplicationContext(), NotificationReciever.class);
//
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext().getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);

                NavHostFragment.findNavController(Maps_Fragment.this)
                        .navigate(R.id.action_maps_Fragment_to_alarmFragment);
            }
        });

    }
}
