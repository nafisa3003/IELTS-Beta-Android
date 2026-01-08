package com.ieltsbeta.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class GoalFragment extends Fragment {

    private AutoCompleteTextView currentDropdown, targetDropdown;
    private FirebaseUser currentUser;
    private DatabaseReference userRef;

    //start from 1.0
    private final String[] scores = {
            "1.0", "1.5", "2.0", "2.5",
            "3.0", "3.5", "4.0", "4.5",
            "5.0", "5.5", "6.0", "6.5",
            "7.0", "7.5", "8.0", "8.5", "9.0"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_goal, container, false);

        currentDropdown = view.findViewById(R.id.dropdown_current_score);
        targetDropdown = view.findViewById(R.id.dropdown_target_score);
        Button btnSave = view.findViewById(R.id.btn_save_scores);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                scores
        );

        currentDropdown.setAdapter(adapter);
        targetDropdown.setAdapter(adapter);

        // Default = 1.0
        currentDropdown.setText("1.0", false);
        targetDropdown.setText("1.0", false);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return view;

        FirebaseDatabase db = FirebaseDatabase.getInstance(
                "https://ieltsbeta-default-rtdb.asia-southeast1.firebasedatabase.app/"
        );
        userRef = db.getReference("users").child(currentUser.getUid());

        // Load from Firebase
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String current = snapshot.child("currentScore").getValue(String.class);
                String target = snapshot.child("targetScore").getValue(String.class);

                if (current != null) currentDropdown.setText(current, false);
                if (target != null) targetDropdown.setText(target, false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to load scores", Toast.LENGTH_SHORT).show();
            }
        });

        // Save button with FIX logic
        btnSave.setOnClickListener(v -> {
            double current = Double.parseDouble(currentDropdown.getText().toString());
            double target = Double.parseDouble(targetDropdown.getText().toString());

            // 🔒 Fix rule: target >= current
            if (target < current) {
                target = current;
                targetDropdown.setText(String.valueOf(target), false);
                Toast.makeText(getActivity(),
                        "Target score can’t be lower than current score",
                        Toast.LENGTH_SHORT).show();
            }

            userRef.child("currentScore").setValue(String.valueOf(current));
            userRef.child("targetScore").setValue(String.valueOf(target))
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(getActivity(), "Scores updated!", Toast.LENGTH_SHORT).show()
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(getActivity(), "Failed to save scores", Toast.LENGTH_SHORT).show()
                    );
        });

        return view;
    }
}