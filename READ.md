# O.P.S. Fit - Android Fitness Application

## Description
O.P.S. Fit is a military-style gamified workout tracking application built for Android. It allows users to log workouts (Lifting, Cardio, Bodyweight), track consistency streaks, earn XP to climb ranks, and compare progress against squad members on a live leaderboard.

## Contributors
* [Cody Armstrong]
* [Daryl Awuku]
* [Jacob D Campos]

## Instructions for Running the Application
1. Clone this repository into Android Studio.
2. Ensure you are using Android Studio Electric Eel or newer with Java 8+ / JDK 11 support.
3. Build and run the project on an Android Emulator or connected device running **Android 8.0 (API Level 26)** or higher.
4. **Special Files / Requirements:** No external network or internet connection required. Application data is saved and loaded locally via internal file I/O (`workouts.csv`).

## Known Issues
* The XP status bar uses fixed pixel/density conversions for width calculation, which may vary on non-standard screen aspect ratios.
* Squad leaderboard entries currently use local mock data paired with active user XP progression.
