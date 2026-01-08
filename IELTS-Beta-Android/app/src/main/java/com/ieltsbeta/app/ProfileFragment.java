package com.ieltsbeta.app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class ProfileFragment extends Fragment {

    private TextView txtTargetScore, txtDaysActive, txtTestsTaken;
    private LinearLayout btnGoal, btnNotify, btnTheme, btnHelp, btnAbout, btnLogout;

    private FirebaseUser currentUser;
    private DatabaseReference userRef;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Bind UI
        txtTargetScore = view.findViewById(R.id.txt_target_score);
        txtDaysActive = view.findViewById(R.id.txt_days_active);
        txtTestsTaken = view.findViewById(R.id.txt_tests_taken);

        btnGoal = view.findViewById(R.id.btn_goal);
        btnNotify = view.findViewById(R.id.btn_notify);
        btnTheme = view.findViewById(R.id.btn_theme);
        btnHelp = view.findViewById(R.id.btn_help);
        btnAbout = view.findViewById(R.id.btn_about);
        btnLogout = view.findViewById(R.id.btn_logout);

        // Firebase Realtime DB
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return view;

        FirebaseDatabase db = FirebaseDatabase.getInstance(
                "https://ieltsbeta-default-rtdb.asia-southeast1.firebasedatabase.app/"
        );
        userRef = db.getReference("users").child(currentUser.getUid());

        // Listen for realtime updates
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String target = snapshot.child("targetScore").getValue(String.class);
                Integer days = snapshot.child("daysActive").getValue(Integer.class);
                Integer tests = snapshot.child("testsTaken").getValue(Integer.class);

                txtTargetScore.setText(target != null ? target : "0.0");
                txtDaysActive.setText(days != null ? String.valueOf(days) : "0");
                txtTestsTaken.setText(tests != null ? String.valueOf(tests) : "0");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });

        // Click listeners
        btnGoal.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new GoalFragment())
                        .addToBackStack(null)
                        .commit()
        );

        btnNotify.setOnClickListener(v -> showSimpleDialog("Notifications", "Coming soon"));
        btnTheme.setOnClickListener(v -> showSimpleDialog("Theme", "Coming soon"));
        btnHelp.setOnClickListener(v -> showFaqDialog());
        btnAbout.setOnClickListener(v -> showAboutDialog());
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            requireActivity().finish();
        });

        return view;
    }

    // ====================== DIALOGS ======================

    private void showAboutDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("IELTS Beta")
                .setMessage(
                        "IELTS Beta is a companion learning app designed to support students " +
                                "preparing for the IELTS exam by providing easy and organized access " +
                                "to resources from their coaching centers.\n\n" +

                                "The app aims to simplify learning and enhance student growth through " +
                                "a range of smart and interactive features.\n\n" +

                                "Key Features\n\n" +

                                "• Past Question Papers\n" +
                                "Access previous years’ IELTS question papers to practice and understand exam patterns.\n\n" +

                                "• Topic-Wise Video Lectures\n" +
                                "Watch recorded classes organized by topics for easier learning and revision.\n\n" +

                                "• Direct Teacher Communication\n" +
                                "Built-in chat feature that allows students to connect with teachers and receive quicker responses.\n\n" +

                                "• Live Class Support\n" +
                                "Participate in live sessions conducted by coaching centers directly through the app.\n\n" +

                                "• Dedicated Helpline\n" +
                                "Get assistance for technical issues or app-related queries with a dedicated support system.\n\n" +

                                "Our Goal\n\n" +

                                "IELTS Beta is built to make IELTS preparation more accessible, organized, " +
                                "and effective — all in one place.\n\n" +

                                "We continuously work on improving the app to provide a better learning experience for students."
                )
                .setPositiveButton("OK", null)
                .show();
    }

    private void showFaqDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("FAQ & Help")
                .setMessage(
                        "What is IELTS Beta?\n" +
                                "• IELTS Beta is a learning app designed to help students prepare for the IELTS exam.\n" +
                                "• It provides practice, guidance, and tools to improve English skills required for IELTS.\n\n" +

                                "Who can use IELTS Beta?\n" +
                                "• Anyone preparing for the IELTS exam.\n" +
                                "• Beginners, intermediate learners, and advanced students can all benefit.\n" +
                                "• Suitable for both Academic and General Training test takers.\n\n" +

                                "Do I need an account to use IELTS Beta?\n" +
                                "• Yes, creating an account allows you to save progress and access personalized features.\n" +
                                "• Logging in ensures your learning data is stored securely.\n\n" +

                                "Is IELTS Beta free to use?\n" +
                                "• IELTS Beta offers free access to core learning features.\n" +
                                "• Some advanced features may be added in future updates.\n\n" +

                                "What skills does IELTS Beta help improve?\n" +
                                "• Reading comprehension\n" +
                                "• Writing skills\n" +
                                "• Vocabulary and grammar\n" +
                                "• Overall English proficiency for IELTS\n\n" +

                                "Can I use IELTS Beta offline?\n" +
                                "• Some features may require an internet connection.\n" +
                                "• Offline support may be limited or added in future versions.\n\n" +

                                "Is my personal information safe?\n" +
                                "• Yes. Your personal data is handled securely.\n" +
                                "• IELTS Beta does not share your information with third parties.\n\n" +

                                "How do I log out of my account?\n" +
                                "• Go to the Profile tab.\n" +
                                "• Tap the Logout button to safely exit your account.\n\n" +

                                "Who should I contact for help or support?\n" +
                                "• Visit the Help & Support section in your profile.\n" +
                                "• You can find guidance and answers to common issues there.\n\n" +

                                "Will IELTS Beta be updated?\n" +
                                "• Yes. The app will receive updates to improve features and fix issues.\n" +
                                "• New content and improvements may be added over time."
                )
                .setPositiveButton("Got it", null)
                .show();
    }

    private void showSimpleDialog(String title, String message) {
        new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}