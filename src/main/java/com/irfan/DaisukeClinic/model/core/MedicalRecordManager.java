package com.irfan.DaisukeClinic.model.core;

import com.irfan.DaisukeClinic.model.MedicalRecord;
import com.irfan.DaisukeClinic.model.structures.MyLinkedList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MedicalRecordManager {
    private MyLinkedList<MedicalRecord> medicalRecords;
    private static final String MEDICAL_RECORD_FILE_PATH = "src/main/resources/assets//medical_records.txt";
    private int lastRecordId;

    public MedicalRecordManager() {
        this.medicalRecords = new MyLinkedList<>();
        this.lastRecordId = 0;
        loadMedicalRecordsFromFile();
    }

    public void addMedicalRecord(MedicalRecord record) {
        if (isRecordIdExists(record.getRecordId())) {
            System.out.println("Catatan medis dengan ID " + record.getRecordId() + " sudah ada.");
            return;
        }
        medicalRecords.add(record);
        if (record.getRecordId() > this.lastRecordId) {
            this.lastRecordId = record.getRecordId();
        }
        saveMedicalRecordsToFile();
        System.out.println("Catatan medis ditambahkan: " + record.getRecordId());
    }

    public MedicalRecord findMedicalRecordById(int id) {
        for (MedicalRecord record : medicalRecords) {
            if (record.getRecordId() == id) {
                return record;
            }
        }
        return null;
    }

    public MyLinkedList<MedicalRecord> getRecordsByPatientId(int patientId) {
        MyLinkedList<MedicalRecord> patientRecords = new MyLinkedList<>();
        for (MedicalRecord record : medicalRecords) {
            if (record.getPatientId() == patientId) {
                patientRecords.add(record);
            }
        }
        return patientRecords;
    }

    public MyLinkedList<MedicalRecord> getAllMedicalRecords() {
        return medicalRecords;
    }

    public int getNextRecordId() {
        return ++lastRecordId;
    }

    public boolean isRecordIdExists(int id) {
        for (MedicalRecord record : medicalRecords) {
            if (record.getRecordId() == id) {
                return true;
            }
        }
        return false;
    }

    private void loadMedicalRecordsFromFile() {
        try {
            Files.createDirectories(Paths.get("src/main/resources/assets"));
        } catch (IOException e) {
            System.err.println("Gagal membuat direktori src/main/resources/assets: " + e.getMessage());
        }

        int maxId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(MEDICAL_RECORD_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                // Format: ID|PatientID|DoctorID|RecordTime|Complaint|Diagnosis|Medication
                if (parts.length >= 7) {
                    try {
                        int recId = Integer.parseInt(parts[0]);
                        int patId = Integer.parseInt(parts[1]);
                        int docId = Integer.parseInt(parts[2]);
                        LocalDateTime recTime = LocalDateTime.parse(parts[3], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        String complaint = parts[4];
                        String diagnosis = parts[5];
                        String medication = parts[6];
                        MedicalRecord record = new MedicalRecord(recId, patId, docId, recTime, complaint, diagnosis, medication);
                        medicalRecords.add(record);
                        if (recId > maxId) maxId = recId;
                    } catch (NumberFormatException | DateTimeParseException e) {
                        System.err.println("Invalid format in medical record file: " + line + ". " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Could not load medical records data from " + MEDICAL_RECORD_FILE_PATH + ": " + e.getMessage());
        }
        this.lastRecordId = maxId;
    }

    public void saveMedicalRecordsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MEDICAL_RECORD_FILE_PATH))) {
            for (MedicalRecord record : medicalRecords) {
                writer.write(record.getRecordId() + "|" + record.getPatientId() + "|" + record.getDoctorId() + "|" +
                        record.getRecordTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "|" +
                        record.getComplaint() + "|" + record.getDiagnosis() + "|" + record.getMedication() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving medical record data to " + MEDICAL_RECORD_FILE_PATH + ": " + e.getMessage());
        }
    }
}