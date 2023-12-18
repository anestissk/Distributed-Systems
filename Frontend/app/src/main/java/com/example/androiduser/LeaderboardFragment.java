package com.example.androiduser;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.HashSet;
import java.util.Set;

public class LeaderboardFragment extends Fragment {

    private TableLayout tableLayout;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        tableLayout = view.findViewById(R.id.tableLayout);

        // Retrieve the usernames from SharedPreferences
        Set<String> leaderboard = sharedPreferences.getStringSet("leaderboard", new HashSet<String>());

        // Create the leaderboard table
        createLeaderboardTable(leaderboard);
    }

    private void createLeaderboardTable(Set<String> leaderboard) {
        // Clear the existing table rows
        tableLayout.removeAllViews();

        Integer count = 1;
        // Create table rows and add them to the table layout
        for (String userRec: leaderboard) {
            TableRow tableRow = new TableRow(requireContext());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            TextView rankTextView = new TextView(requireContext());
            rankTextView.setText(String.valueOf(count));
            tableRow.addView(rankTextView);

            TextView usernameTextView = new TextView(requireContext());
            usernameTextView.setText(userRec);
            tableRow.addView(usernameTextView);

            tableLayout.addView(tableRow);
            count = count + 1;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tableLayout = null;
    }
}
