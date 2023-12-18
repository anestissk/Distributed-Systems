package com.example.androiduser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import common.Result;
import com.example.androiduser.databinding.FragmentFirstBinding;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class FirstFragment extends Fragment {

    private static final int REQUEST_CODE_SETTINGS = 2;

    private FragmentFirstBinding binding;
    private Uri selectedGpxUri;
    private SharedPreferences sharedPreferences;
    private String hostName;
    private Integer portNumber;

    private ActivityResultLauncher<String> filePickerLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onStart() {
        super.onStart();
        hostName = sharedPreferences.getString("ipAddress", "");
        portNumber = sharedPreferences.getInt("portNumber", 0);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        // Retrieve the hostname and port number from SharedPreferences
        binding.buttonSelectGpx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hostName != null && !hostName.isEmpty() && portNumber != null) {
                    openFilePicker();
                } else {
                    Toast.makeText(requireContext(), "Please set the hostname and port number first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.buttonShowStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedGpxUri != null) {
                    // Perform actions with the selected GPX file, such as parsing and displaying statistics
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);
                } else {
                    Toast.makeText(requireContext(), "Please select a GPX file first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.buttonLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLeaderboard();
            }
        });

        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        selectedGpxUri = result;
                        String filename = selectedGpxUri.getLastPathSegment();
                        String message = "GPX file: '" + filename + "' selected.";
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

                        new Thread(() -> {
                            try {
                                Looper.prepare();
                                String fileContent = readFileAsString(selectedGpxUri);
                                Socket clientSocket = new Socket(hostName, portNumber);
                                clientSocket.getOutputStream().write(fileContent.getBytes());
                                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                                Result result2 = (Result) inputStream.readObject();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putFloat("averageUserDistance", result2.getStat().averageUserDistance.floatValue());
                                editor.putFloat("averageUserElevation", result2.getStat().averageUserElevation.floatValue());
                                editor.putFloat("averageUserElapsedTime", result2.getStat().averageUserElapsedTime.floatValue());
                                editor.putFloat("averageTotalDistance", result2.getStat().averageTotalDistance.floatValue());
                                editor.putFloat("averageTotalElevation", result2.getStat().averageTotalElevation.floatValue());
                                editor.putFloat("averageTotalElapsedTime", result2.getStat().averageTotalElapsedTime.floatValue());
                                Set<String> ranking = new HashSet<>();
                                Map<String, Double> sortedMap = result2.getStat().leaderboard.entrySet()
                                        .stream()
                                        .sorted(Map.Entry.comparingByValue())
                                        .collect(Collectors.toMap(
                                                Map.Entry::getKey,
                                                Map.Entry::getValue,
                                                (oldValue, newValue) -> oldValue,
                                                LinkedHashMap::new
                                        ));
                                for (Map.Entry<String, Double> entry: sortedMap.entrySet()) {
                                    String key = entry.getKey();
                                    Double value = entry.getValue();
                                    String formattedString = key + ": " + value;
                                    ranking.add(formattedString);
                                }
                                editor.putStringSet("leaderboard", ranking);
                                editor.apply();
                                System.out.println("[CLIENT-THREAD] Received result: " + result2);
                                Toast.makeText(requireContext(), result2.toString(), Toast.LENGTH_LONG).show();
                                clientSocket.close();
                            } catch (IOException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }
                });
    }

    private void openFilePicker() {
        filePickerLauncher.launch("*/*");
    }

    private void openLeaderboard() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_FirstFragment_to_LeaderboardFragment);
    }

    private void openSettings() {
        Intent intent = new Intent(requireContext(), SettingsActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SETTINGS);
    }

    private String readFileAsString(Uri uri) throws IOException {
        InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
        byte[] encoded = new byte[inputStream.available()];
        inputStream.read(encoded);
        inputStream.close();
        return new String(encoded);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}