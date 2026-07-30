package edu.utsa.cs3443.group6_project.model;

import android.content.Context;
import java.io.*;
import java.util.ArrayList;



/**
 * OpsFitManager acts as the central Model controller in the MVC design pattern.
 * Manages user progress state (XP, streak), holds collection lists, and handles
 * internal persistent storage using CSV file I/O operations.
 *
 * CS 3443 - Final Application
 */

public class OpsFitManager {
    private ArrayList<Workout> workoutHistory;
    private int userXp;
    private int userStreak;
    private final String filename = "workouts.csv";
    private ArrayList<SquadMember> squadList;
    private Context context;

    public OpsFitManager(Context context) {
        this.context = context;
        this.workoutHistory = new ArrayList<>();
        this.userXp = 250;
        this.userStreak = 2;
        this.squadList = new ArrayList<>();
        loadHistoryFromFile();
        initializeMockSquad();
    }

    private void initializeMockSquad() {
        squadList.add(new SquadMember("Jacob", 480, 2, "#2ECC71"));
        squadList.add(new SquadMember("Daryl", 180, 1, "#E67E22"));
    }

    public void addWorkout(Workout w) {
        workoutHistory.add(w);
        userXp += 100;
        userStreak++;
        saveHistoryToFile();
    }

    public ArrayList<Workout> getWorkoutHistory() { return workoutHistory; }
    public ArrayList<SquadMember> getSquadList() { return squadList; }
    public int getUserXp() { return userXp; }
    public int getUserStreak() { return userStreak; }

    public String getRank() {
        if (userXp >= 1200) return "Lieutenant";
        if (userXp >= 900) return "Sergeant";
        if (userXp >= 600) return "Corporal";
        if (userXp >= 400) return "Private";
        if (userXp >= 200) return "Recruit Rank 2";
        return "Recruit Rank 1";
    }

    private void saveHistoryToFile() {
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(fos))) {
            pw.println(userXp + "," + userStreak);
            for (Workout w : workoutHistory) {
                pw.println(w.toCSVRow());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadHistoryFromFile() {
        File file = new File(context.getFilesDir(), filename);
        if (!file.exists()) return;

        try (FileInputStream fis = context.openFileInput(filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
            String metadata = br.readLine();
            if (metadata != null && !metadata.isEmpty()) {
                String[] metaTokens = metadata.split(",");
                this.userXp = Integer.parseInt(metaTokens[0]);
                this.userStreak = Integer.parseInt(metaTokens[metaTokens.length - 1]);
            }

            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Workout w = Workout.fromCSVRow(line);
                    if (w != null) workoutHistory.add(w);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}