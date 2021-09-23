# SnoreMore
> SnooreMore is an intelligent sleep advisor that helps you schedule your sleep time based on your lifestyle, weight, diet, location, sleeping history, and exercise history.
To see our app's functionality in action, watch our video: https://drive.google.com/file/d/1HbGAR_a2PcX5G-A_JgE5Zc6BTEeYhWXB/view?usp=sharing

## Overview
This is Group 12's project code for our SnoreMore app, which will recommend sleep times according to a user's weight goals.

We are writing this code on Android Studio and are all collaborating through a website called Floobits (similar to GitHub). However, since the professor expressed a 
preference for GitHub, I will be uploading our code here for the professor, TAs, and reader to access. However we are all collaborating equally on this project and
have been working through each feature together.

Our team has four people: Paul Le, Colin Tran, Yerline Herrera, and Duy Ly


## Motivation

University students and wage-earners often neglect the importance of sleeping due to their rigorous schedule that keeps them buried with busy work. Many do not realize the negative impacts caused by inadequate sleep experience that could affect their ability to accomplish daily tasks effectively. A good sleeping schedule is important because it nurtures our physical and mental health and enhances our

productivity throughout the day. Therefore, our team seeks to design an application to help people track their sleep and build better sleeping schedules 


## Goals

The goal of our system is to offer suggestions for sleeping and waking hours that consider the user’s lifestyle, weight, diet, location, sleeping history, and exercise history. Our project is trying to answer the query: When is the best time to sleep (for the given user)? We gather data from the user’s Samsung Health app and current GPS information to log weight, amount of food, time of sunrise and sunset for current location, and exercise history to be used as contextual information to back our sleep recommendations. 


## Component and Functionalities

##### Extraction of height/weight information, daily calories burned, and daily exercise from the Samsung Health app CSV files

##### Present + Save the user personal information

##### Gets the user’s location in order to calculate the sunset and sunrise time 


## How we will use this information to recommend sleep times:

We are currently storing all this information in a database class which we will use when ranking and filtering the possible sleep times.

Our potential query results are the different hours a user can fall asleep (i.e. 1am, 2am, etc.)

All information is stored and retrieved via the SharePreferences API. SharedPreferences helps us store user data so that it persists across the app session, even if the app is killed or the device is rebooted. Each user’s information contains a key/value pair which points to a SharedPreferences object. With the key/value pairs in the SharedPreferences API, our app easily saves the user’s data and retrieves it when necessary.  

Recommendation is presented in our mobile app at the end. The recommendation is based on a predefined set of potential recommendation and a prefiltering strategy. First, we filter potential results based on the user’s age according to Sleep.org’s recommendation.  

> 1-2 year old should sleep 9-16 hours 

> 3-5 year old should sleep 8-14 hours 

> 6-13 year old should sleep 7-12 hours 

> 14-17 year old should sleep 7-11 hours 

> 18-25 year old should sleep 6-11 hours 

> 26+ year old should sleep 5-9 hours 

Then we further filter results based on the user’s health goal. According to Sleep.org, if someone sleeps less, they are hungrier throughout the day which affects their weight in the long run. If users want to gain weight we recommend less hours of sleep and if they want to lose/ maintain their current weight we recommend a sleep duration in between their age group. Then, our app compares the user’s calorie intake and exercise amount for that day with their average. If it is outside of a standard deviation, we alter their sleep time accordingly. If a user did more exercise on a given day, we will recommend more hours of sleep so their body can recover. If they exercised less than the standard deviation, we recommend they wake up earlier so that they can be more active. We take a similar approach when considering their calorie intake. Lastly, we compare their location’s sunrise time with their preferred wake up time. Since humans tend to wake up around sunrise time naturally, if a user’s preferred wake up time is after the sun rises, we recommend they sleep a little earlier in case they happen to wake up earlier. 


## Tasks Completed

##### User Interface

##### Data Entry Requiring User Input

##### User Height and Weight Collected Automatically via Health Data

##### Automated Data Collection for Calories and Exercise

##### Location Gathering through User Selection

##### Data Collection via API

##### Recommendation Algorithm and Ranking System

> Current UCI students, please do not copy our project as that is considered plagiarism.
