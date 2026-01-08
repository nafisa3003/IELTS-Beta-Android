package com.ieltsbeta.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView txtTitle, txtMotivation;
    private TextView txtTarget, txtCurrent, txtDays, txtTests;
    private RecyclerView rvWords, rvSituations, rvChallenges;

    private FirebaseUser currentUser;
    private DatabaseReference userRef;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Views
        txtTitle = view.findViewById(R.id.txt_home_title);
        txtMotivation = view.findViewById(R.id.txt_motivation);

        txtTarget = view.findViewById(R.id.txt_target_score);
        txtCurrent = view.findViewById(R.id.txt_current_score);
        txtDays = view.findViewById(R.id.txt_days_active);
        txtTests = view.findViewById(R.id.txt_tests_taken);

        rvWords = view.findViewById(R.id.rv_words);
        rvSituations = view.findViewById(R.id.rv_situations);
        rvChallenges = view.findViewById(R.id.rv_challenges);

        Button btnPractice = view.findViewById(R.id.btn_practice);
        Button btnProgress = view.findViewById(R.id.btn_progress);

        //Greeting
        txtTitle.setText(getGreeting());

        //LayoutManagers for carousels
        rvWords.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        rvSituations.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        rvChallenges.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        //Quick Actions Navigation
        btnPractice.setOnClickListener(v -> {
            BottomNavigationView nav =
                    requireActivity().findViewById(R.id.bottom_navigation);
            nav.setSelectedItemId(R.id.nav_practice);
        });

        btnProgress.setOnClickListener(v -> {
            BottomNavigationView nav =
                    requireActivity().findViewById(R.id.bottom_navigation);
            nav.setSelectedItemId(R.id.nav_progress);
        });

        //Firebase user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            FirebaseDatabase db = FirebaseDatabase.getInstance(
                    "https://ieltsbeta-default-rtdb.asia-southeast1.firebasedatabase.app/"
            );
            userRef = db.getReference("users").child(currentUser.getUid());

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String targetStr = snapshot.child("targetScore").getValue(String.class);
                    String currentStr = snapshot.child("currentScore").getValue(String.class);

                    Integer days = snapshot.child("daysActive").getValue(Integer.class);
                    Integer tests = snapshot.child("testsTaken").getValue(Integer.class);

                    // Defaults (IELTS never starts at 0.0)
                    double target = targetStr != null ? Double.parseDouble(targetStr) : 1.0;
                    double current = currentStr != null ? Double.parseDouble(currentStr) : 1.0;

                    txtTarget.setText(String.valueOf(target));
                    txtCurrent.setText(String.valueOf(current));
                    txtDays.setText(days != null ? String.valueOf(days) : "0");
                    txtTests.setText(tests != null ? String.valueOf(tests) : "0");

                    updateMotivationText(target, current);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        }

        //Load Home Cards from Firebase
        setupHomeCards();

        return view;
    }

    //Greeting Logic
    private String getGreeting() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (hour >= 5 && hour < 12) return "Good morning 👋";
        if (hour >= 12 && hour < 17) return "Good afternoon ☀️";
        if (hour >= 17 && hour < 22) return "Good evening 🌙";
        return "Welcome back 👋";
    }

    //Motivation Text
    private void updateMotivationText(double target, double current) {
        double diff = target - current;

        if (diff <= 0) {
            txtMotivation.setText("🎉 Goal achieved! Set a new target?");
        } else {
            txtMotivation.setText(
                    "Only " + String.format("%.1f", diff) + " band to go 🚀"
            );
        }
    }

    //Load all carousels from Firebase
    private void setupHomeCards() {

        DatabaseReference homeCardsRef =
                FirebaseDatabase.getInstance(
                        "https://ieltsbeta-default-rtdb.asia-southeast1.firebasedatabase.app/"
                ).getReference("homeCards");

        loadCarousel(homeCardsRef.child("words"), rvWords, true);       // true = use fallback if empty
        loadCarousel(homeCardsRef.child("situations"), rvSituations, false);
        loadCarousel(homeCardsRef.child("challenges"), rvChallenges, false);
    }

    private void loadCarousel(DatabaseReference ref, RecyclerView recyclerView, boolean useWordFallback) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Word> list = new ArrayList<>();

                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        String word = child.child("word").getValue(String.class);
                        String meaning = child.child("meaning").getValue(String.class);
                        String example = child.child("example").getValue(String.class);

                        // For situations/challenges, fallback to title/desc
                        if (word == null) word = child.child("title").getValue(String.class);
                        if (meaning == null) meaning = child.child("desc").getValue(String.class);
                        if (example == null) example = "";

                        list.add(new Word(word, meaning, example));
                    }
                }

                // If word carousel (firebase) empty, add default 3 words
                if (useWordFallback && list.isEmpty()) {
                    list.add(new Word(
                            "Coherent",
                            "Logical and well-organized",
                            "Her essay was coherent and easy to follow."
                    ));
                    list.add(new Word(
                            "Persuasive",
                            "Able to convince others",
                            "He presented a persuasive argument."
                    ));
                    list.add(new Word(
                            "Resilient",
                            "Able to recover quickly from difficulties",
                            "She remained resilient despite challenges."
                    ));
                }

                recyclerView.setAdapter(new WordAdapter(list));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}