package edu.utsa.cs3443.group6_project;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import edu.utsa.cs3443.group6_project.model.OpsFitManager;
import edu.utsa.cs3443.group6_project.model.SquadMember;
import edu.utsa.cs3443.group6_project.model.Workout;

import java.time.LocalDate;

/**
 * MainActivity serves as the main Controller in the MVC architecture for O.P.S. Fit.
 * It manages user interface components, navigation events, user input collection,
 * and dynamic leaderboard rendering while delegating business logic to OpsFitManager.
 *
 * CS 3443 - Final Application
 */





public class MainActivity extends AppCompatActivity {

    private OpsFitManager manager;

    private View homeView, logView, progressView, squadView;
    private CardView rewardModalOverlay;

    private TextView lblHeaderRank, lblXpStatus;
    private View xpFillBar;

    private Spinner comboType, comboExercise;
    private EditText txtMetric1, txtMetric2;

    private TextView lblModalStreak;
    private LinearLayout vboxLeaderboardContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = new OpsFitManager(this);

        initViews();
        setupSpinners();
        setupNavigation();
        showHomeView();
    }

    private void initViews() {
        homeView = findViewById(R.id.homeView);
        logView = findViewById(R.id.logView);
        progressView = findViewById(R.id.progressView);
        squadView = findViewById(R.id.squadView);
        rewardModalOverlay = findViewById(R.id.rewardModalOverlay);

        lblHeaderRank = findViewById(R.id.lblHeaderRank);
        lblXpStatus = findViewById(R.id.lblXpStatus);
        xpFillBar = findViewById(R.id.xpFillBar);

        comboType = findViewById(R.id.comboType);
        comboExercise = findViewById(R.id.comboExercise);
        txtMetric1 = findViewById(R.id.txtMetric1);
        txtMetric2 = findViewById(R.id.txtMetric2);

        lblModalStreak = findViewById(R.id.lblModalStreak);
        vboxLeaderboardContainer = findViewById(R.id.vboxLeaderboardContainer);

        findViewById(R.id.btnActiveMission).setOnClickListener(v -> showLogView());
        findViewById(R.id.btnSaveWorkout).setOnClickListener(v -> handleSaveWorkout());
        findViewById(R.id.btnCloseModal).setOnClickListener(v -> handleCloseModal());
    }

    private void setupSpinners() {
        String[] types = {"Lifting", "Cardio", "Bodyweight"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        comboType.setAdapter(typeAdapter);

        comboType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = types[position];
                String[] exercises;
                if (selected.equals("Lifting")) {
                    exercises = new String[]{"Bench Press", "Squat", "Deadlift"};
                } else if (selected.equals("Cardio")) {
                    exercises = new String[]{"Running", "Cycling", "Rowing"};
                } else {
                    exercises = new String[]{"Push-up", "Pull-up", "Burpee"};
                }
                ArrayAdapter<String> exAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, exercises);
                comboExercise.setAdapter(exAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupNavigation() {
        findViewById(R.id.navHome).setOnClickListener(v -> showHomeView());
        findViewById(R.id.navLog).setOnClickListener(v -> showLogView());
        findViewById(R.id.navProgress).setOnClickListener(v -> showProgressView());
        findViewById(R.id.navSquad).setOnClickListener(v -> showSquadView());
    }

    private void updateDashboardUI() {
        lblHeaderRank.setText("Rank: " + manager.getRank());
        lblXpStatus.setText(manager.getUserXp() + " / 500 XP to Next Rank");

        double progressRatio = Math.min(1.0, manager.getUserXp() / 500.0);
        int maxBarWidthDp = 300;
        int targetWidthPx = (int) (maxBarWidthDp * progressRatio * getResources().getDisplayMetrics().density);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) xpFillBar.getLayoutParams();
        params.width = targetWidthPx;
        xpFillBar.setLayoutParams(params);
    }

    private void handleSaveWorkout() {
        try {
            String date = LocalDate.now().toString();
            String type = comboType.getSelectedItem().toString();
            String exercise = comboExercise.getSelectedItem().toString();
            double m1 = Double.parseDouble(txtMetric1.getText().toString());
            int m2 = Integer.parseInt(txtMetric2.getText().toString());

            Workout w = new Workout(date, type, exercise, m1, m2);
            manager.addWorkout(w);

            lblModalStreak.setText("🔥 Streak Tracker updated to " + manager.getUserStreak() + " Days");
            rewardModalOverlay.setVisibility(View.VISIBLE);
            updateDashboardUI();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please fill in valid numerical fields.", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCloseModal() {
        rewardModalOverlay.setVisibility(View.GONE);
        showHomeView();
    }

    public void showHomeView() {
        homeView.setVisibility(View.VISIBLE);
        logView.setVisibility(View.GONE);
        progressView.setVisibility(View.GONE);
        squadView.setVisibility(View.GONE);
        updateDashboardUI();
    }

    public void showLogView() {
        homeView.setVisibility(View.GONE);
        logView.setVisibility(View.VISIBLE);
        progressView.setVisibility(View.GONE);
        squadView.setVisibility(View.GONE);
    }

    public void showProgressView() {
        homeView.setVisibility(View.GONE);
        logView.setVisibility(View.GONE);
        progressView.setVisibility(View.VISIBLE);
        squadView.setVisibility(View.GONE);
    }

    public void showSquadView() {
        homeView.setVisibility(View.GONE);
        logView.setVisibility(View.GONE);
        progressView.setVisibility(View.GONE);
        squadView.setVisibility(View.VISIBLE);
        renderLeaderboard();
    }

    private void renderLeaderboard() {
        vboxLeaderboardContainer.removeAllViews();

        vboxLeaderboardContainer.addView(createLeaderboardRow("Cody (You)", manager.getUserXp(), manager.getRank(), "#2A629A"));

        for (SquadMember member : manager.getSquadList()) {
            vboxLeaderboardContainer.addView(createLeaderboardRow(member.getName(), member.getXp(), member.getRankName(), member.getAvatarColor()));
        }
    }

    private View createLeaderboardRow(String name, int xp, String rankStr, String colorHex) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(30, 30, 30, 30);

        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rowParams.setMargins(0, 0, 0, 20);
        row.setLayoutParams(rowParams);
        row.setBackgroundColor(Color.parseColor("#2A3644"));

        View avatar = new View(this);
        LinearLayout.LayoutParams avParams = new LinearLayout.LayoutParams(80, 80);
        avatar.setLayoutParams(avParams);
        avatar.setBackgroundColor(Color.parseColor(colorHex));

        LinearLayout textContainer = new LinearLayout(this);
        textContainer.setOrientation(LinearLayout.VERTICAL);
        textContainer.setPadding(30, 0, 0, 0);

        TextView tvName = new TextView(this);
        tvName.setText(name);
        tvName.setTextColor(Color.WHITE);
        tvName.setTypeface(null, Typeface.BOLD);

        TextView tvXp = new TextView(this);
        tvXp.setText(xp + " XP");
        tvXp.setTextColor(Color.LTGRAY);

        textContainer.addView(tvName);
        textContainer.addView(tvXp);

        TextView tvRank = new TextView(this);
        tvRank.setText(rankStr);
        tvRank.setTextColor(Color.WHITE);
        tvRank.setPadding(20, 10, 20, 10);
        tvRank.setBackgroundColor(Color.parseColor("#C5A059"));

        LinearLayout.LayoutParams rankParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rankParams.weight = 1;
        tvRank.setLayoutParams(rankParams);

        row.addView(avatar);
        row.addView(textContainer);
        row.addView(tvRank);

        return row;
    }
}