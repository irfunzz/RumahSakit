package com.irfan.DaisukeClinic.model.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.irfan.DaisukeClinic.model.Doctor;
import com.irfan.DaisukeClinic.model.Patient;

public class AccountManager {

    private static final String ACCOUNT_FILE_PATH = "src/main/resources/assets/account.txt";

    public AccountManager() {
        try {
            Files.createDirectories(Paths.get("src/main/resources/assets/"));
        } catch (IOException e) {
            System.err.println("Gagal membuat direktori src/main/resources/assets/: " + e.getMessage());
        }
    }

    public Object authenticateUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNT_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                // Format: ID|Nama|Password|Role
                if (parts.length >= 4 && parts[1].equalsIgnoreCase(username) && parts[2].equals(password)) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        String role = parts[3].toLowerCase();
                        if (role.equals("doctor")) {
                            return new Doctor(id, username, "");
                        } else if (role.equals("patient")) {
                            return new Patient(id, username, 0, "", "");
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing ID from file for line: " + line + ". " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error membaca file akun (" + ACCOUNT_FILE_PATH + "): " + e.getMessage());
        }
        return null;
    }

    public boolean registerUser(int id, String username, String password, String role) {
        if (isIdExists(id)) {
            System.out.println("ID " + id + " sudah terdaftar. Harap gunakan ID lain.");
            return false;
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(ACCOUNT_FILE_PATH, true)))) {
            out.println(id + "|" + username + "|" + password + "|" + role);
            return true;
        } catch (IOException e) {
            System.err.println("Error saat mendaftarkan akun: " + e.getMessage());
            return false;
        }
    }

    private boolean isIdExists(int id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNT_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length > 0) {
                    try {
                        if (Integer.parseInt(parts[0]) == id) {
                            return true;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Peringatan: Baris dengan ID tidak valid di file akun: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking ID existence: " + e.getMessage());
        }
        return false;
    }

    private boolean isRoleSame(int id, String role) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNT_FILE_PATH))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length >= 4 && Integer.parseInt(parts[0]) == id && parts[3].equalsIgnoreCase(role)) {
                    return true;
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error checking role: " + e.getMessage());
        }
        return false;
    }
}