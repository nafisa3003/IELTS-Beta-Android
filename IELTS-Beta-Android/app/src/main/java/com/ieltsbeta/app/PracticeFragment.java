package com.ieltsbeta.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class PracticeFragment extends Fragment {

    private MaterialButton btnListening, btnReading, btnWriting, btnSpeaking;
    private TextView txtSkillHeader;

    private RecyclerView rvPracticeTests, rvTips;
    private PracticeAdapter adapterTests, adapterTips;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_practice, container, false);

        btnListening = view.findViewById(R.id.btn_listening);
        btnReading = view.findViewById(R.id.btn_reading);
        btnWriting = view.findViewById(R.id.btn_writing);
        btnSpeaking = view.findViewById(R.id.btn_speaking);
        txtSkillHeader = view.findViewById(R.id.txt_skill_header);

        rvPracticeTests = view.findViewById(R.id.rv_practice_tests);
        rvTips = view.findViewById(R.id.rv_tips);

        LinearLayoutManager layoutTests = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutTips = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvPracticeTests.setLayoutManager(layoutTests);
        rvTips.setLayoutManager(layoutTips);

        // Snap helper for carousel feel
        PagerSnapHelper snapHelperTests = new PagerSnapHelper();
        snapHelperTests.attachToRecyclerView(rvPracticeTests);

        PagerSnapHelper snapHelperTips = new PagerSnapHelper();
        snapHelperTips.attachToRecyclerView(rvTips);

        btnListening.setOnClickListener(v -> showSkill("Listening"));
        btnReading.setOnClickListener(v -> showSkill("Reading"));
        btnWriting.setOnClickListener(v -> showSkill("Writing"));
        btnSpeaking.setOnClickListener(v -> showSkill("Speaking"));

        return view;
    }

    private void showSkill(String skill) {
        txtSkillHeader.setText("🎯 " + skill + " Practice");

        List<PracticeItem> tests = new ArrayList<>();
        List<PracticeItem> tips = new ArrayList<>();

        if (skill.equalsIgnoreCase("Listening")) {
            // Tests
            tests.add(new PracticeItem("Listening", "Test", "Full Listening Test 2024", "Practice listening skills with answers (E2 IELTS).", "https://www.youtube.com/watch?v=VUtUOTrJ2Kk"));
            tests.add(new PracticeItem("Listening", "Test", "E2 IELTS Listening Practice Test", "Extended listening practice.", "https://www.youtube.com/watch?v=0z-3y81KoeU"));
            // Tips
            tips.add(new PracticeItem("Listening", "Tip", "Listening Tips by IELTS Liz", "Avoid common traps in listening.", "https://www.youtube.com/watch?v=q8qmJeBxk4Q"));
            tips.add(new PracticeItem("Listening", "Tip", "E2 IELTS Listening Strategies", "Improve focus & keywords.", "https://www.youtube.com/@E2IELTS"));

        } else if (skill.equalsIgnoreCase("Reading")) {
            // Tests
            tests.add(new PracticeItem("Reading", "Test", "IELTS Reading Practice Test", "Practice identifying main ideas.", "https://www.youtube.com/watch?v=kCthrwUz68w"));
            tests.add(new PracticeItem("Reading", "Test", "True/False/Not Given Practice", "Learn this important question type.", "https://www.youtube.com/watch?v=NUIU22KcJ4Q"));
            // Tips
            tips.add(new PracticeItem("Reading", "Tip", "Skimming & Scanning Tips", "Skim and scan effectively.", "https://www.youtube.com/watch?v=WYl9PX7Ua_Q"));
            tips.add(new PracticeItem("Reading", "Tip", "IELTS Advantage Reading Strategies", "Highlight keywords and save time.", "https://www.youtube.com/c/Ieltsadvantage/playlists"));

        } else if (skill.equalsIgnoreCase("Writing")) {
            // Tests
            tests.add(new PracticeItem("Writing", "Test", "Task 1 Writing Practice", "Describe graphs or charts.", "https://www.youtube.com/watch?v=oTe_2hmSHvw"));
            tests.add(new PracticeItem("Writing", "Test", "Task 2 Writing Practice", "Opinion essay example.", "https://www.youtube.com/watch?v=k_ucGw9cXDI"));
            // Tips
            tips.add(new PracticeItem("Writing", "Tip", "Writing Tips by IELTS Liz", "Plan before writing.", "https://www.youtube.com/@ieltsliz"));
            tips.add(new PracticeItem("Writing", "Tip", "IELTS Advantage Writing Tips", "Task 1 & 2 strategies.", "https://www.youtube.com/c/Ieltsadvantage/playlists"));

        } else if (skill.equalsIgnoreCase("Speaking")) {
            // Tests
            tests.add(new PracticeItem("Speaking", "Test", "IELTS Speaking Mock Test", "Full speaking test example.", "https://www.youtube.com/watch?v=n5ohxW5lTIs"));
            tests.add(new PracticeItem("Speaking", "Test", "Band 9 Speaking Sample", "Practice with real answers.", "https://www.youtube.com/watch?v=k4715CJ0Ii8"));
            // Tips
            tips.add(new PracticeItem("Speaking", "Tip", "Speaking Tips by IELTS Liz", "Improve fluency & coherence.", "https://www.youtube.com/@ieltsliz"));
            tips.add(new PracticeItem("Speaking", "Tip", "IELTS Advantage Speaking Tips", "Mock speaking tests & advice.", "https://www.youtube.com/c/Ieltsadvantage/playlists"));
        }

        adapterTests = new PracticeAdapter(getActivity(), tests);
        adapterTips = new PracticeAdapter(getActivity(), tips);

        rvPracticeTests.setAdapter(adapterTests);
        rvTips.setAdapter(adapterTips);
    }
}