package com.example.snoremore;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SharedPreferenceManager preferenceManager_calories, preferenceManager_distance;
    private int data_calories, data_distance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable HTTP GET Request
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        readExerciseData();
        readHWeightData();
        calculateBmi();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               Snackbar.make(view, "Set a bedtime reminder using SnoreMore!", Snackbar.LENGTH_LONG)
//                       .setAction("Action", null).show();
//           }
//        });
    }

    private List<HWeightSample> HeightWeightSamples = new ArrayList<>();
    private void readHWeightData(){
        InputStream is = getResources().openRawResource(R.raw.weight_and_height);
        BufferedReader reader1 = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line1 = "";
        try{
            reader1.readLine();
            reader1.readLine();
            while((line1 = reader1.readLine()) != null){
                // Split by ','
                String[] tokens = line1.split(",");
                // Read the data
                HWeightSample sample1 = new HWeightSample();
                sample1.setHeight(Double.parseDouble(tokens[20]));
                sample1.setWeight(Double.parseDouble(tokens[5]));
                HeightWeightSamples.add(sample1);

                Log.d("MyActivity", "Just created: " + sample1);
            }
        } catch (IOException e){
            Log.wtf("MyActivity", "Error reading data file on line " + line1, e);
            e.printStackTrace();
        }
    }


    private List<ExerciseSample> exerciseSamples = new ArrayList<>();
    private double averageDistance = 0.0, averageCalories = 0.0;
    private double standardDeviationDistance = 0.0, standardDeviationCalories = 0.0;
    private int distanceBoolean = 0, caloriesBoolean = 0;
    private void readExerciseData(){
        InputStream is = getResources().openRawResource(R.raw.exercise);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";
        try{
            reader.readLine();
            reader.readLine();
            while((line = reader.readLine()) != null){
                // Split by ','
                String[] tokens = line.split(",");
                // Read the data
                ExerciseSample sample = new ExerciseSample();
                sample.setDistance(Double.parseDouble(tokens[3]));
                sample.setCalories(Double.parseDouble(tokens[35]));
                exerciseSamples.add(sample);

                Log.d("MyActivity", "Just created: " + sample);
            }
        } catch (IOException e){
            Log.wtf("MyActivity", "Error reading data file on line " + line, e);
            e.printStackTrace();
        }
        int exerciseSamplesSize = exerciseSamples.size();
        for (int i = 0; i < exerciseSamplesSize; ++i)
        {
            averageCalories += exerciseSamples.get(i).getCalories();
            averageDistance += exerciseSamples.get(i).getDistance();
        }
        averageCalories /= exerciseSamplesSize;
        averageDistance /= exerciseSamplesSize;

        for (int i = 0; i < exerciseSamplesSize; ++i)
        {
            double caloriesValue = exerciseSamples.get(i).getCalories() - averageCalories;
            double distanceValue = exerciseSamples.get(i).getDistance() - averageDistance;
            standardDeviationCalories += (caloriesValue * caloriesValue);
            standardDeviationDistance += (distanceValue * distanceValue);
        }
        standardDeviationCalories /= exerciseSamplesSize;
        standardDeviationDistance /= exerciseSamplesSize;

        double mostRecentDistance = exerciseSamples.get(exerciseSamplesSize - 1).getDistance();
        double mostRecentCalories = exerciseSamples.get(exerciseSamplesSize - 1).getCalories();

        if (mostRecentDistance > averageDistance + standardDeviationDistance)
            distanceBoolean = 1;
        else if (mostRecentDistance < averageDistance - standardDeviationDistance)
            distanceBoolean = -1;

        if (mostRecentCalories < averageCalories - standardDeviationCalories)
            caloriesBoolean = -1;


        System.out.println("distanceBoolean = " + distanceBoolean);
        System.out.println("caloriesBoolean = " + caloriesBoolean);

        //save to sharepreference
        preferenceManager_calories = new SharedPreferenceManager(this, KeyString.PREFERENCE_CALORIES);
        preferenceManager_distance = new SharedPreferenceManager(this, KeyString.PREFERENCE_DISTANCE);
        preferenceManager_calories.setValue_int(KeyString.PREFERENCE_CALORIES, caloriesBoolean);
        preferenceManager_distance.setValue_int(KeyString.PREFERENCE_DISTANCE, distanceBoolean);





    }

     private double height;
     private double weight;
     private double BMI;
     private void calculateBmi() {
        height = HeightWeightSamples.get(0).getHeight();
        weight = HeightWeightSamples.get(0).getWeight();
        BMI = (weight / height / height) * 10000;
        System.out.println("The BMI is: " + BMI);
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
