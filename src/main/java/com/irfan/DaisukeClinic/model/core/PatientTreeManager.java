package com.irfan.DaisukeClinic.model.core;

import com.irfan.DaisukeClinic.model.Patient;
import com.irfan.DaisukeClinic.model.structures.MyBST; // Nama kelas tetap MyBST

public class PatientTreeManager {
    private MyBST<Patient> patientTree;
    private PatientManager patientManager;

    public PatientTreeManager(PatientManager patientManager) {
        this.patientTree = new MyBST<>();
        this.patientManager = patientManager;
        loadPatientsToTree();
    }

    private void loadPatientsToTree() {
        for (Patient patient : patientManager.getPatients()) {
            patientTree.insert(patient);
        }
    }

    public void insertPatient(Patient patient) {
        patientTree.insert(patient);
        System.out.println("Pasien " + patient.getName() + " ditambahkan ke BST (AVL).");
    }

    public boolean deletePatient(int id) {
        Patient dummyPatient = new Patient(id, "", 0, "", "");
        Patient foundPatient = patientTree.search(dummyPatient);
        if (foundPatient != null) {
            patientTree.delete(dummyPatient);
            System.out.println("Pasien dengan ID " + id + " dihapus dari BST (AVL).");
            return true;
        } else {
            System.out.println("Pasien dengan ID " + id + " tidak ditemukan di BST (AVL).");
            return false;
        }
    }

    public Patient searchPatientById(int id) {
        Patient dummyPatient = new Patient(id, "", 0, "", "");
        Patient foundPatient = patientTree.search(dummyPatient);
        if (foundPatient != null) {
            System.out.println("Pasien ditemukan di BST (AVL): " + foundPatient.getName());
        } else {
            System.out.println("Pasien dengan ID " + id + " tidak ditemukan di BST (AVL).");
        }
        return foundPatient;
    }

    public void inOrderDisplay() {
        if (patientTree == null) {
            System.out.println("BST (AVL) kosong.");
            return;
        }
        System.out.println("\n--- Tampilan Pasien di BST (AVL - In-Order) ---");
        patientTree.inOrderDisplay();
        System.out.println("-----------------------------------------\n");
    }
}