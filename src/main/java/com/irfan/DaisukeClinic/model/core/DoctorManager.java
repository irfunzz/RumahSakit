package com.irfan.DaisukeClinic.model.core;

import com.irfan.DaisukeClinic.model.Doctor;
import com.irfan.DaisukeClinic.model.ScheduleSlot;
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

public class DoctorManager {
    private MyLinkedList<Doctor> allDoctors;
    private MyLinkedList<Doctor> loggedInDoctors;
    private MyLinkedList<String> doctorActivityLog;

    private static final String DOCTOR_FILE_PATH = "src/main/resources/assets/doctor_records.txt";
    private static final String LOGGED_IN_DOCTORS_FILE_PATH = "src/main/resources/assets/logged_in_doctors.txt";
    private static final String DOCTOR_ACTIVITY_LOG_FILE_PATH = "src/main/resources/assets/doctor_activity_log.txt";

    private static final DateTimeFormatter SCHEDULE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public DoctorManager() {
        this.allDoctors = new MyLinkedList<>();
        this.loggedInDoctors = new MyLinkedList<>();
        this.doctorActivityLog = new MyLinkedList<>();

        loadDoctorsFromFile();
        loadDoctorActivityLogFromFile();
        loadLoggedInDoctorsFromFile();
    }

    public void addDoctorToRecords(Doctor doctor) {
        if (findDoctorById(doctor.getId()) != null) {
            System.out.println("Dokter dengan ID " + doctor.getId() + " sudah ada dalam catatan.");
            return;
        }
        allDoctors.add(doctor);
        saveDoctorsToFile();
        doctorActivityLog.add("REGISTER: Doctor " + doctor.getName() + " (ID: " + doctor.getId() + ") registered at " + LocalDateTime.now());
        saveDoctorActivityLogToFile();
        System.out.println("Dokter " + doctor.getName() + " ditambahkan ke catatan.");
    }

    public Doctor findDoctorById(int id) {
        for (Doctor doctor : allDoctors) {
            if (doctor.getId() == id) {
                return doctor;
            }
        }
        return null;
    }

    public Doctor findDoctorByName(String name) {
        for (Doctor doctor : allDoctors) {
            if (doctor.getName().equalsIgnoreCase(name)) {
                return doctor;
            }
        }
        return null;
    }

    public MyLinkedList<Doctor> findDoctorsBySpecialization(String specialization) {
        MyLinkedList<Doctor> suitableDoctors = new MyLinkedList<>();
        if (specialization == null || specialization.trim().isEmpty()) {
            return suitableDoctors;
        }

        for (Doctor doctor : allDoctors) {
            if (doctor.getSpecialization().equalsIgnoreCase(specialization.trim())) {
                suitableDoctors.add(doctor);
            }
        }
        return suitableDoctors;
    }

    public boolean updateDoctor(int id, String newName, String newSpecialization) {
        Doctor doctor = findDoctorById(id);
        if (doctor != null) {
            doctor.setName(newName);
            doctor.setSpecialization(newSpecialization);
            saveDoctorsToFile();
            doctorActivityLog.add("UPDATE: Doctor " + doctor.getName() + " (ID: " + doctor.getId() + ") updated at " + LocalDateTime.now());
            saveDoctorActivityLogToFile();
            System.out.println("Detail dokter ID " + id + " berhasil diperbarui.");
            return true;
        }
        System.out.println("Dokter dengan ID " + id + " tidak ditemukan dalam catatan.");
        return false;
    }

    public boolean addDoctorSchedule(int doctorId, ScheduleSlot slot) {
        Doctor doctor = findDoctorById(doctorId);
        if (doctor != null) {
            for (ScheduleSlot existingSlot : doctor.getSchedule()) {
                if (existingSlot.overlapsWith(slot)) {
                    System.out.println("Gagal menambahkan jadwal: Slot waktu tumpang tindih dengan jadwal yang sudah ada.");
                    return false;
                }
            }
            doctor.addScheduleSlot(slot);
            saveDoctorsToFile();
            doctorActivityLog.add("SCHEDULE: Doctor " + doctor.getName() + " (ID: " + doctor.getId() + ") added schedule " + slot.toString() + " at " + LocalDateTime.now());
            saveDoctorActivityLogToFile();
            System.out.println("Jadwal dokter " + doctor.getName() + " berhasil ditambahkan.");
            return true;
        }
        System.out.println("Dokter dengan ID " + doctorId + " tidak ditemukan untuk ditambahkan jadwal.");
        return false;
    }


    public boolean removeDoctor(int id) {
        Doctor doctorToRemove = findDoctorById(id);
        if (doctorToRemove != null) {
            if (allDoctors.remove(doctorToRemove)) {
                saveDoctorsToFile();
                doctorActivityLog.add("REMOVE: Doctor " + doctorToRemove.getName() + " (ID: " + doctorToRemove.getId() + ") removed at " + LocalDateTime.now());
                saveDoctorActivityLogToFile();
                System.out.println("Dokter dengan ID " + id + " (" + doctorToRemove.getName() + ") berhasil dihapus dari catatan.");
                return true;
            }
        }
        System.out.println("Dokter dengan ID " + id + " tidak ditemukan dalam catatan.");
        return false;
    }

    public MyLinkedList<Doctor> getAllDoctors() {
        return allDoctors;
    }

    public void displayAllDoctors() {
        if (allDoctors.isEmpty()) {
            System.out.println("Tidak ada dokter dalam catatan.");
            return;
        }
        System.out.println("\n--- Daftar Semua Dokter ---");
        allDoctors.displayAll();
        System.out.println("---------------------------\n");
    }

    public void loginDoctor(Doctor doctor) {
        if (findLoggedInDoctorById(doctor.getId()) == null) {
            Doctor fullDoctorDetails = findDoctorById(doctor.getId());
            if (fullDoctorDetails != null) {
                fullDoctorDetails.setLoginTime(LocalDateTime.now());
                loggedInDoctors.add(fullDoctorDetails);
                doctorActivityLog.add("LOGIN: Doctor " + fullDoctorDetails.getName() + " (ID: " + fullDoctorDetails.getId() + ") logged in at " + fullDoctorDetails.getLoginTime());
                saveDoctorActivityLogToFile();
                saveLoggedInDoctorsToFile();
                System.out.println("Dokter " + fullDoctorDetails.getName() + " berhasil login.");
            } else {
                System.out.println("Error: Dokter dengan ID " + doctor.getId() + " tidak ditemukan dalam catatan dokter.");
            }
        } else {
            System.out.println("Dokter " + doctor.getName() + " sudah login.");
        }
    }

    public boolean logoutDoctor(int doctorId) {
        Doctor doctorToLogout = findLoggedInDoctorById(doctorId);
        if (doctorToLogout != null) {
            doctorToLogout.setLogoutTime(LocalDateTime.now());
            loggedInDoctors.remove(doctorToLogout);
            doctorActivityLog.add("LOGOUT: Doctor " + doctorToLogout.getName() + " (ID: " + doctorToLogout.getId() + ") logged out at " + doctorToLogout.getLogoutTime());
            saveDoctorActivityLogToFile();
            saveLoggedInDoctorsToFile();
            System.out.println("Dokter dengan ID " + doctorId + " berhasil logout.");
            return true;
        } else {
            System.out.println("Dokter dengan ID " + doctorId + " tidak ditemukan sebagai dokter yang login.");
            return false;
        }
    }

    public Doctor findLoggedInDoctorByName(String name) {
        for (Doctor doctor : loggedInDoctors) {
            if (doctor.getName().equals(name)) {
                return doctor;
            }
        }
        return null;
    }

    public Doctor findLoggedInDoctorById(int id) {
        for (Doctor doctor : loggedInDoctors) {
            if (doctor.getId() == id) {
                return doctor;
            }
        }
        return null;
    }

    public void displayAllLoggedInDoctors() {
        if (loggedInDoctors.isEmpty()) {
            System.out.println("Tidak ada dokter yang sedang login.");
            return;
        }
        System.out.println("\n--- Dokter yang Sedang Login ---");
        loggedInDoctors.displayAll();
        System.out.println("--------------------------------\n");
    }

    public MyLinkedList<Doctor> getLoggedInDoctorsList() {
        return loggedInDoctors;
    }

    public int getNextDoctorId() {
        int maxId = 0;
        for (Doctor doctor : allDoctors) {
            if (doctor.getId() > maxId) {
                maxId = doctor.getId();
            }
        }
        return maxId + 1;
    }

    public void displayDoctorActivityLog() {
        if (doctorActivityLog.isEmpty()) {
            System.out.println("Log aktivitas dokter kosong.");
            return;
        }
        System.out.println("\n--- Log Aktivitas Dokter (Terlama ke Terbaru) ---");
        doctorActivityLog.displayAll();
        System.out.println("-------------------------------------------------\n");
    }

    // --- Persistensi Data Dokter Terdaftar ---
    private void loadDoctorsFromFile() {
        try {
            Files.createDirectories(Paths.get("resources/assets"));
            if (!Files.exists(Paths.get(DOCTOR_FILE_PATH))) {
                try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(DOCTOR_FILE_PATH))) {
                    writer.write("ID|Name|Specialization|Schedule\n");
                }
                return;
            }
        } catch (IOException e) {
            System.err.println("Failed to create directories or file: " + e.getMessage());
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DOCTOR_FILE_PATH))) {
            String line = reader.readLine();
            if (line == null || !line.startsWith("ID|Name|Specialization|Schedule")) {
                System.err.println("Invalid file header. Creating new file.");
                saveDoctorsToFile();
                return;
            }

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\|", -1);
                if (parts.length < 3) {
                    System.err.println("Skipping invalid line: " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String specialization = parts[2];
                    Doctor doctor = new Doctor(id, name, specialization);

                    if (parts.length >= 4 && !parts[3].isEmpty()) {
                        parseAndAddSchedule(doctor, parts[3]);
                    }
                    allDoctors.add(doctor);
                } catch (NumberFormatException e) {
                    System.err.println("Skipping line with invalid ID: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading doctors: " + e.getMessage());
        }
    }

    private void parseAndAddSchedule(Doctor doctor, String scheduleString) {
        String[] slotStrings = scheduleString.split(",");
        MyLinkedList<ScheduleSlot> doctorSchedule = new MyLinkedList<>();

        for (String slotStr : slotStrings) {
            slotStr = slotStr.trim();
            if (slotStr.isEmpty()) continue;

            // Split on the hyphen that separates the two timestamps
            String[] timeParts = slotStr.split("(?<=\\d{2}:\\d{2})-");
            if (timeParts.length != 2) {
                System.err.println("Invalid slot format (expected exactly two time parts): " + slotStr);
                continue;
            }

            String startStr = timeParts[0].trim();
            String endStr = timeParts[1].trim();

            try {
                LocalDateTime start = LocalDateTime.parse(startStr, SCHEDULE_FORMATTER);
                LocalDateTime end = LocalDateTime.parse(endStr, SCHEDULE_FORMATTER);

                if (end.isBefore(start)) {
                    System.err.println("Invalid time range (end before start): " + slotStr);
                    continue;
                }

                doctorSchedule.add(new ScheduleSlot(start, end));
                System.out.println("Successfully parsed slot: " + startStr + " to " + endStr);
            } catch (DateTimeParseException e) {
                System.err.println("Failed to parse slot: " + slotStr + " - " + e.getMessage());
            }
        }

        doctor.setSchedule(doctorSchedule);
    }

    public void saveDoctorsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOCTOR_FILE_PATH))) {
            writer.write("ID|Name|Specialization|Schedule\n");
            for (Doctor doctor : allDoctors) {
                StringBuilder scheduleSb = new StringBuilder();
                boolean first = true;
                for (ScheduleSlot slot : doctor.getSchedule()) {
                    if (!first) scheduleSb.append(",");
                    scheduleSb.append(slot.getStartTime().format(SCHEDULE_FORMATTER)).append("-").append(slot.getEndTime().format(SCHEDULE_FORMATTER));
                    first = false;
                }
                writer.write(doctor.getId() + "|" + doctor.getName() + "|" + doctor.getSpecialization() + "|" + scheduleSb.toString() + "\n");
            }
            System.out.println("DEBUG DManager Save: Doctors data saved.");
        } catch (IOException e) {
            System.err.println("ERROR DManager Save: Error saving doctor data to " + DOCTOR_FILE_PATH + ": " + e.getMessage());
        }
    }

    private void loadDoctorActivityLogFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DOCTOR_ACTIVITY_LOG_FILE_PATH))) {
            String line = reader.readLine();
            if (line == null || !line.startsWith("Log Entry")) {
                System.err.println("Peringatan: File " + DOCTOR_ACTIVITY_LOG_FILE_PATH + " tidak memiliki header yang diharapkan. Membuat file baru.");
                return;
            }

            while ((line = reader.readLine()) != null) {
                doctorActivityLog.add(line);
            }
        } catch (IOException e) {
            System.err.println("Could not load doctor activity log from " + DOCTOR_ACTIVITY_LOG_FILE_PATH + ": " + e.getMessage());
        }
    }

    public void saveDoctorActivityLogToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOCTOR_ACTIVITY_LOG_FILE_PATH))) {
            writer.write("Log Entry\n");
            for (String logEntry : doctorActivityLog) {
                writer.write(logEntry + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving doctor activity log to " + DOCTOR_ACTIVITY_LOG_FILE_PATH + ": " + e.getMessage());
        }
    }

    private void loadLoggedInDoctorsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LOGGED_IN_DOCTORS_FILE_PATH))) {
            String line = reader.readLine();
            if (line == null || !line.startsWith("ID|Name|Specialization|LoginTime")) {
                System.err.println("Peringatan: File " + LOGGED_IN_DOCTORS_FILE_PATH + " tidak memiliki header yang diharapkan. Membuat file baru.");
                return;
            }

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length >= 4) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        String name = parts[1];
                        String specialization = parts[2];
                        LocalDateTime loginTime = LocalDateTime.parse(parts[3]);

                        Doctor loggedInDoc = new Doctor(id, name, specialization);
                        loggedInDoc.setLoginTime(loginTime);
                        loggedInDoctors.add(loggedInDoc);
                    } catch (NumberFormatException | DateTimeParseException e) {
                        System.err.println("Invalid format in logged-in doctors file: " + line + ". " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Could not load logged-in doctors data from " + LOGGED_IN_DOCTORS_FILE_PATH + ": " + e.getMessage());
        }
    }

    public void saveLoggedInDoctorsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOGGED_IN_DOCTORS_FILE_PATH))) {
            writer.write("ID|Name|Specialization|LoginTime\n");
            for (Doctor doctor : loggedInDoctors) {
                writer.write(doctor.getId() + "|" + doctor.getName() + "|" + doctor.getSpecialization() + "|" + doctor.getLoginTime().toString() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving logged-in doctors data to " + LOGGED_IN_DOCTORS_FILE_PATH + ": " + e.getMessage());
        }
    }

    public void logoutAllDoctorsOnAppExit() {
        if (!loggedInDoctors.isEmpty()) {
            System.out.println("Aplikasi ditutup. Melakukan auto-logout untuk semua dokter yang login...");
            MyLinkedList<Doctor> tempLogoutList = new MyLinkedList<>();
            for (Doctor doctor : loggedInDoctors) {
                tempLogoutList.add(doctor);
            }

            for (Doctor doctor : tempLogoutList) {
                doctor.setLogoutTime(LocalDateTime.now());
                doctorActivityLog.add("AUTO-LOGOUT: Doctor " + doctor.getName() + " (ID: " + doctor.getId() + ") auto-logged out on app exit at " + doctor.getLogoutTime());
            }
            loggedInDoctors = new MyLinkedList<>();
            saveDoctorActivityLogToFile();
            saveLoggedInDoctorsToFile();
            System.out.println("Semua dokter berhasil di-logout dan status disimpan.");
        } else {
            System.out.println("Tidak ada dokter yang login saat aplikasi ditutup.");
        }
    }
}