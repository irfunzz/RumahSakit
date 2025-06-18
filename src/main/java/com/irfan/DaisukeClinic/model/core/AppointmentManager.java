package com.irfan.DaisukeClinic.model.core;

import com.irfan.DaisukeClinic.model.Appointment;
import com.irfan.DaisukeClinic.model.structures.MyPriorityQueue;
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

public class AppointmentManager {
    private MyPriorityQueue appointmentsQueue;
    private static final String APPOINTMENT_FILE_PATH = "src/main/resources/assets/appointment_records.txt";
    private int lastAppointmentId;

    public AppointmentManager() {
        this.appointmentsQueue = new MyPriorityQueue(200);
        this.lastAppointmentId = 0;
        loadAppointmentsFromFile();
    }

    public void scheduleAppointment(Appointment appointment) {
        if (isAppointmentIdExists(appointment.getAppointmentId())) {
            System.out.println("Gagal menjadwalkan janji temu: ID Janji Temu " + appointment.getAppointmentId() + " sudah ada.");
            return;
        }

        if (isAppointmentTimeConflict(appointment.getPatientId(), appointment.getDoctorId(), appointment.getAppointmentTime())) {
            System.out.println("Gagal menjadwalkan janji temu: Pasien atau Dokter sudah memiliki janji temu pada waktu yang sama.");
            return;
        }

        if (appointmentsQueue.isFull()) {
            System.out.println("Gagal menjadwalkan janji temu: Antrean penuh.");
            return;
        }

        appointmentsQueue.enqueue(appointment);
        if (appointment.getAppointmentId() > this.lastAppointmentId) {
            this.lastAppointmentId = appointment.getAppointmentId();
        }
        saveAppointmentsToFile();
        System.out.println("Janji temu berhasil dijadwalkan: " + appointment.toString());
    }

    public Appointment processNextAppointment() {
        Appointment processedAppointment = appointmentsQueue.dequeue();
        if (processedAppointment != null) {
            processedAppointment.setStatus("Completed");
            saveAppointmentsToFile();
            System.out.println("Janji temu diproses: " + processedAppointment.toString());
        } else {
            System.out.println("Tidak ada janji temu yang dapat diproses.");
        }
        return processedAppointment;
    }

    public Appointment peekNextAppointment() {
        return appointmentsQueue.peek();
    }

    public void viewUpcomingAppointments() {
        if (appointmentsQueue.isEmpty()) {
            System.out.println("Tidak ada janji temu mendatang.");
            return;
        }
        System.out.println("\n--- Janji Temu Mendatang (Berdasarkan Prioritas Waktu) ---");
        MyPriorityQueue tempQueue = new MyPriorityQueue(appointmentsQueue.size());

        Appointment[] currentApps = appointmentsQueue.toArray();
        if (currentApps != null) {
            for (Appointment app : currentApps) {
                if (app != null) tempQueue.enqueue(app);
            }
        }

        while (!tempQueue.isEmpty()) {
            System.out.println(tempQueue.dequeue().toString());
        }
        System.out.println("-----------------------------------------\n");
    }

    public MyLinkedList<Appointment> getAppointmentsByPatientId(int patientId) {
        MyLinkedList<Appointment> patientAppointments = new MyLinkedList<>();
        Appointment[] currentAppointmentsArray = appointmentsQueue.toArray();

        if (currentAppointmentsArray != null) {
            for (Appointment obj : currentAppointmentsArray) {
                if (obj != null && obj.getPatientId() == patientId) {
                    patientAppointments.add(obj);
                }
            }
        }
        return patientAppointments;
    }

    public MyLinkedList<Appointment> getAppointments() {
        MyLinkedList<Appointment> allAppointments = new MyLinkedList<>();
        Appointment[] currentAppointmentsArray = appointmentsQueue.toArray();
        if (currentAppointmentsArray != null) {
            for (Appointment app : currentAppointmentsArray) {
                if (app != null) {
                    allAppointments.add(app);
                }
            }
        }
        return allAppointments;
    }

    public int getNextId() {
        return this.lastAppointmentId + 1;
    }

    public boolean isAppointmentIdExists(int id) {
        Appointment[] currentApps = appointmentsQueue.toArray();
        if (currentApps != null) {
            for (Appointment app : currentApps) {
                if (app != null && app.getAppointmentId() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAppointmentTimeConflict(int patientId, int doctorId, LocalDateTime appointmentTime) {
        Appointment[] currentApps = appointmentsQueue.toArray();
        if (currentApps != null) {
            for (Appointment app : currentApps) {
                if (app != null) {
                    if (app.getPatientId() == patientId && app.getAppointmentTime().isEqual(appointmentTime)) {
                        System.out.println("Konflik waktu: Pasien " + patientId + " sudah memiliki janji temu pada waktu " + appointmentTime);
                        return true;
                    }
                    if (app.getDoctorId() == doctorId && app.getAppointmentTime().isEqual(appointmentTime)) {
                        System.out.println("Konflik waktu: Dokter " + doctorId + " sudah memiliki janji temu pada waktu " + appointmentTime);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void loadAppointmentsFromFile() {
        try {
            Files.createDirectories(Paths.get("resources/assets"));
        } catch (IOException e) {
            System.err.println("Gagal membuat direktori resources/assets: " + e.getMessage());
        }

        int maxId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(APPOINTMENT_FILE_PATH))) {
            String line = reader.readLine();
            if (line == null || !line.startsWith("AppointmentID|PatientID|DoctorID|Time|Status")) {
                System.err.println("Peringatan: File " + APPOINTMENT_FILE_PATH + " tidak memiliki header yang diharapkan. Membuat file baru.");
                return;
            }

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length >= 5) {
                    try {
                        int appId = Integer.parseInt(parts[0]);
                        int patId = Integer.parseInt(parts[1]);
                        int docId = Integer.parseInt(parts[2]);
                        LocalDateTime appTime = LocalDateTime.parse(parts[3], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        String status = parts[4];
                        Appointment app = new Appointment(appId, patId, docId, appTime);
                        app.setStatus(status);
                        appointmentsQueue.enqueue(app);
                        if (appId > maxId) maxId = appId;
                    } catch (NumberFormatException | DateTimeParseException e) {
                        System.err.println("Invalid format in appointment file: " + line + ". " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Could not load appointment data from " + APPOINTMENT_FILE_PATH + ": " + e.getMessage());
        }
        this.lastAppointmentId = maxId;
    }

    public void saveAppointmentsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPOINTMENT_FILE_PATH))) {
            writer.write("AppointmentID|PatientID|DoctorID|Time|Status\n");
            Appointment[] currentAppointments = appointmentsQueue.toArray();
            if (currentAppointments != null) {
                for (Appointment app : currentAppointments) {
                    if (app != null) {
                        writer.write(app.getAppointmentId() + "|" + app.getPatientId() + "|" + app.getDoctorId() + "|" +
                                app.getAppointmentTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "|" +
                                app.getStatus() + "\n");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving appointment data to " + APPOINTMENT_FILE_PATH + ": " + e.getMessage());
        }
    }
}