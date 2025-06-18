package com.irfan.DaisukeClinic.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Appointment implements Serializable {
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private LocalDateTime appointmentTime;
    private String status;

    public Appointment(int appointmentId, int patientId, int doctorId, LocalDateTime appointmentTime) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentTime = appointmentTime;
        this.status = "Pending";
    }

    public int getAppointmentId() { return appointmentId; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public String getStatus() { return status; }

    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public void setAppointmentTime(LocalDateTime appointmentTime) { this.appointmentTime = appointmentTime; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Appointment ID: " + appointmentId +
                "\n Patient ID: " + patientId +
                "\n Doctor ID: " + doctorId +
                "\n Time: " + appointmentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                "\n Status: " + status;
    }
}