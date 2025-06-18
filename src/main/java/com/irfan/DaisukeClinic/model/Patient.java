package com.irfan.DaisukeClinic.model;

import java.io.Serializable;

public class Patient implements Serializable, Comparable<Patient> {
    private int id;
    private String name;
    private int age;
    private String address;
    private String phoneNumber;
    private String medicalHistory;
    private String allergies;

    // Konstruktor utama
    public Patient(int id, String name, int age, String address, String phoneNumber, String medicalHistory, String allergies) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.medicalHistory = medicalHistory;
        this.allergies = allergies;
    }

    // Konstruktor tambahan untuk BST (hanya butuh ID untuk pencarian)
    public Patient(int id, String name, int age, String address, String phoneNumber) {
        this(id, name, age, address, phoneNumber, "", "");
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getMedicalHistory() { return medicalHistory; }
    public String getAllergies() { return allergies; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
    public void setAllergies(String allergies) { this.allergies = allergies; }

    @Override
    public int compareTo(Patient other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Patient patient = (Patient) obj;
        return id == patient.id;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }

    @Override
    public String toString() {
        return "id = " + id +
                "\n name = '" + name + '\'' +
                "\n age = " + age +
                "\n address = '" + address + '\'' +
                "\n phoneNumber = '" + phoneNumber + '\'' +
                "\n medicalHistory = '" + medicalHistory + '\'' +
                "\n allergies = '" + allergies + "\'\n";
    }
}