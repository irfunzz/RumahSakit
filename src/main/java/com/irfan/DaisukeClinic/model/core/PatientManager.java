package com.irfan.DaisukeClinic.model.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.irfan.DaisukeClinic.model.Patient;
import com.irfan.DaisukeClinic.model.structures.MyLinkedList;

public class PatientManager {
    private MyLinkedList<Patient> patients;
    private PatientTreeManager patientTreeManager;
    private static final String PATIENT_FILE_PATH = "src/main/resources/assets/patient_records.txt";

    public PatientManager() {
        this.patients = new MyLinkedList<>();
        loadPatientsFromFile();
    }

    public void setPatientTreeManager(PatientTreeManager patientTreeManager) {
        this.patientTreeManager = patientTreeManager;
    }

    public void addPatient(Patient patient) {
        if (findPatientById(patient.getId()) != null) {
            System.out.println("Pasien dengan ID " + patient.getId() + " sudah ada.");
            return;
        }
        patients.add(patient);
        savePatientsToFile();
        if (patientTreeManager != null) {
            patientTreeManager.insertPatient(patient);
        }
        System.out.println("Pasien ditambahkan: " + patient.getName());
    }

    public boolean removePatientById(int id) {
        Patient patientToRemove = findPatientById(id);
        if (patientToRemove != null) {
            if (patients.remove(patientToRemove)) {
                savePatientsToFile();
                if (patientTreeManager != null) {
                    patientTreeManager.deletePatient(id);
                }
                System.out.println("Pasien dengan ID " + id + " (" + patientToRemove.getName() + ") berhasil dihapus.");
                return true;
            }
        }
        System.out.println("Pasien dengan ID " + id + " tidak ditemukan.");
        return false;
    }

    public Patient findPatientById(int id) {
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                return patient;
            }
        }
        return null;
    }

    public Patient findPatientByName(String name) {
        for (Patient patient : patients) {
            if (patient.getName().equalsIgnoreCase(name)) {
                return patient;
            }
        }
        return null;
    }

    public MyLinkedList<Patient> getPatients() {
        return patients;
    }

    public void displayAllPatients() {
        if (patients.isEmpty()) {
            System.out.println("Tidak ada pasien dalam daftar.");
            return;
        }
        System.out.println("\n--- Daftar Semua Pasien ---");
        patients.displayAll();
        System.out.println("---------------------------\n");
    }

    public int getNextPatientId() {
        int maxId = 0;
        for (Patient patient : patients) {
            if (patient.getId() > maxId) {
                maxId = patient.getId();
            }
        }
        return maxId + 1;
    }

    private void loadPatientsFromFile() {
        try {
            Files.createDirectories(Paths.get("src/main/resources/assets"));
        } catch (IOException e) {
            System.err.println("Gagal membuat direktori resources/assets: " + e.getMessage());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(PATIENT_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 7) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        String name = parts[1];
                        int age = Integer.parseInt(parts[2]);
                        String address = parts[3];
                        String phoneNumber = parts[4];
                        String medicalHistory = parts[5];
                        String allergies = parts[6];
                        patients.add(new Patient(id, name, age, address, phoneNumber, medicalHistory, allergies));
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format in patient file: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Could not load patient data from " + PATIENT_FILE_PATH + ": " + e.getMessage());
        }
    }

    public void savePatientsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATIENT_FILE_PATH))) {
            for (Patient patient : patients) {
                writer.write(patient.getId() + "|" + patient.getName() + "|" + patient.getAge() + "|" +
                        patient.getAddress() + "|" + patient.getPhoneNumber() + "|" +
                        patient.getMedicalHistory() + "|" + patient.getAllergies() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving patient data to " + PATIENT_FILE_PATH + ": " + e.getMessage());
        }
    }
}