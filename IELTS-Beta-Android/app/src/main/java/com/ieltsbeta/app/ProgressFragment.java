package com.ieltsbeta.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class ProgressFragment extends Fragment {

    private CircularProgressIndicator overallProgress;
    private TextView tvOverallText;

    private ProgressBar listeningProgress, readingProgress, writingProgress, speakingProgress;
    private TextView tvListening, tvReading, tvWriting, tvSpeaking;

    private FirebaseUser currentUser;
    private DatabaseReference userRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        // Overall
        overallProgress = view.findViewById(R.id.progress_overall);
        tvOverallText = view.findViewById(R.id.tv_overall_text);

        // Skills (dummy)
        listeningProgress = view.findViewById(R.id.progress_listening);
        readingProgress = view.findViewById(R.id.progress_reading);
        writingProgress = view.findViewById(R.id.progress_writing);
        speakingProgress = view.findViewById(R.id.progress_speaking);

        tvListening = view.findViewById(R.id.tv_listening_percent);
        tvReading = view.findViewById(R.id.tv_reading_percent);
        tvWriting = view.findViewById(R.id.tv_writing_percent);
        tvSpeaking = view.findViewById(R.id.tv_speaking_percent);

        // Set dummy skill values
        setDummySkills();

        // Firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return view;

        FirebaseDatabase db = FirebaseDatabase.getInstance(
                "https://ieltsbeta-default-rtdb.asia-southeast1.firebasedatabase.app/"
        );

        userRef = db.getReference("users").child(currentUser.getUid());

        loadOverallProgressRealtime();

        return view;
    }

    // 🔥 REALTIME overall progress
    private void loadOverallProgressRealtime() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String currentStr = snapshot.child("currentScore").getValue(String.class);
                String targetStr = snapshot.child("targetScore").getValue(String.class);

                if (currentStr == null || targetStr == null) return;

                double current = Double.parseDouble(currentStr);
                double target = Double.parseDouble(targetStr);

                if (target <= 0) return;

                int percent = (int) ((current / target) * 100);
                percent = Math.min(percent, 100); // cap at 100%

                overallProgress.setProgress(percent);
                tvOverallText.setText(percent + "%");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    // 🎯 Dummy skill bars (unchanged)
    private void setDummySkills() {
        setSkill(listeningProgress, tvListening, 70);
        setSkill(readingProgress, tvReading, 80);
        setSkill(writingProgress, tvWriting, 65);
        setSkill(speakingProgress, tvSpeaking, 75);
    }

    private void setSkill(ProgressBar bar, TextView tv, int value) {
        bar.setProgress(value);
        tv.setText(value + "%");
    }
}