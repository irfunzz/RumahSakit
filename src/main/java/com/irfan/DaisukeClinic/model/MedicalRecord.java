package com.irfan.DaisukeClinic.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MedicalRecord implements Serializable {
    private int recordId;
    private int patientId;
    private int doctorId;
    private LocalDateTime recordTime;
    private String complaint;
    private String diagnosis;
    private String medication;

    public MedicalRecord(int recordId, int patientId, int doctorId, LocalDateTime recordTime, String complaint, String diagnosis, String medication) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.recordTime = recordTime;
        this.complaint = complaint;
        this.diagnosis = diagnosis;
        this.medication = medication;
    }

    // Getters
    public int getRecordId() { return recordId; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public LocalDateTime getRecordTime() { return recordTime; }
    public String getComplaint() { return complaint; }
    public String getDiagnosis() { return diagnosis; }
    public String getMedication() { return medication; }

    // Setters (jika memungkinkan untuk diupdate)
    public void setRecordId(int recordId) { this.recordId = recordId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public void setRecordTime(LocalDateTime recordTime) { this.recordTime = recordTime; }
    public void setComplaint(String complaint) { this.complaint = complaint; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setMedication(String medication) { this.medication = medication; }

    @Override
    public String toString() {
        return "ID = " + recordId +
                "\n Patient ID = " + patientId +
                "\n Doctor ID = " + doctorId +
                "\n Time=" + recordTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                "\n Complaint = '" + complaint + '\'' +
                "\n Diagnosis = '" + diagnosis + '\'' +
                "\n Medication = '" + medication + '\'' ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalRecord that = (MedicalRecord) o;
        return recordId == that.recordId;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(recordId);
    }
}