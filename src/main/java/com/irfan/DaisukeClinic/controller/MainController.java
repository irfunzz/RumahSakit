package com.irfan.DaisukeClinic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import com.irfan.DaisukeClinic.model.Appointment;
import com.irfan.DaisukeClinic.model.Doctor;
import com.irfan.DaisukeClinic.model.Patient;
import com.irfan.DaisukeClinic.model.MedicalRecord;
import com.irfan.DaisukeClinic.model.ScheduleSlot;
import com.irfan.DaisukeClinic.model.core.AccountManager;
import com.irfan.DaisukeClinic.model.core.AppointmentManager;
import com.irfan.DaisukeClinic.model.core.DoctorManager;
import com.irfan.DaisukeClinic.model.core.PatientManager;
import com.irfan.DaisukeClinic.model.core.PatientTreeManager;
import com.irfan.DaisukeClinic.model.core.MedicalRecordManager;
import com.irfan.DaisukeClinic.model.structures.MyLinkedList;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MainController {

    @FXML
    private StackPane contentPane;

    private TextArea currentOutputArea;

    // Managers
    private AccountManager accountManager;
    private DoctorManager doctorManager;
    private PatientManager patientManager;
    private AppointmentManager appointmentManager;
    private PatientTreeManager patientTreeManager;
    private MedicalRecordManager medicalRecordManager;

    private Doctor currentLoggedInDoctor = null;
    private Patient currentLoggedInPatient = null;

    // Register Patient
    private TextField regPatientIdField;
    private TextField regPatientNameField;
    private TextField regPatientPasswordField;
    private TextField regPatientAgeField;
    private TextField regPatientAddressField;
    private TextField regPatientPhoneField;
    private TextField regPatientMedicalHistoryField;
    private TextField regPatientAllergiesField;

    private TextField removePatientIdField;
    private TextField searchPatientNameField;

    // Schedule Appointment Form fields
    private TextField schAppointmentIdField;
    private TextField schPatientIdField;
    private TextField schIllnessField;
    private TextField schDoctorIdField;
    private TextField schDateField;
    private TextField schTimeField;

    // Schedule slot
    private TextField initialDoctorIdField;
    private VBox doctorScheduleDisplayBox;
    private TextField selectedScheduleSlotField;

    // Medical Record Form fields
    private TextField mrRecordIdField;
    private TextField mrPatientIdField;
    private TextField mrDoctorIdField;
    private TextField mrComplaintField;
    private TextField mrDiagnosisField;
    private TextField mrMedicationField;

    private TextField viewMrPatientIdField;

    private TextField loginUsernameField;
    private TextField loginPasswordField;

    // Register Doctor
    private TextField regDoctorIdField;
    private TextField regDoctorNameField;
    private TextField regDoctorPasswordField;
    private TextField regDoctorSpecialtyField;

    // Add doctor slot
    private TextField addScheduleDoctorIdField;
    private TextField addScheduleStartDateField;
    private TextField addScheduleStartTimeField;
    private TextField addScheduleEndDateField;
    private TextField addScheduleEndTimeField;

    private TextField searchPatientByIdBSTField;


    public MainController() {
        this.accountManager = new AccountManager();
        this.doctorManager = new DoctorManager();
        this.patientManager = new PatientManager();
        this.appointmentManager = new AppointmentManager();
        this.medicalRecordManager = new MedicalRecordManager();
        this.patientTreeManager = new PatientTreeManager(patientManager);
        this.patientManager.setPatientTreeManager(patientTreeManager);
    }

    @FXML
    public void initialize() {
        showDashboardView();
    }

    // --- Helper Methods ---
    private void appendOutput(String message) {
        if (currentOutputArea != null) {
            currentOutputArea.appendText(message + "\n");
        } else {
            System.out.println("OutputArea tidak diinisialisasi: " + message);
        }
    }

    private void clearOutput() {
        if (currentOutputArea != null) {
            currentOutputArea.clear();
        }
    }

    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void saveAllManagerDataOnExit() {
        doctorManager.logoutAllDoctorsOnAppExit();
        patientManager.savePatientsToFile();
        appointmentManager.saveAppointmentsToFile();
        medicalRecordManager.saveMedicalRecordsToFile();
    }


    // --- Manajemen Tampilan (Views) ---

    @FXML
    private void showDashboardView() {
        VBox dashboardView = new VBox(20);
        dashboardView.setPadding(new Insets(20));
        dashboardView.setAlignment(Pos.TOP_CENTER);
        dashboardView.getStyleClass().add("form-container");

        Label title = new Label("Dashboard Overview");
        title.getStyleClass().add("content-title");

        GridPane statsGrid = new GridPane();
        statsGrid.getStyleClass().add("dashboard-grid");
        statsGrid.setAlignment(Pos.CENTER);

        VBox patientCard = new VBox(5);
        patientCard.getStyleClass().add("dashboard-card");
        Label patientTitle = new Label("Total Patients");
        patientTitle.getStyleClass().add("card-title");
        Label totalPatientsLabel = new Label(String.valueOf(patientManager.getPatients().size()));
        totalPatientsLabel.getStyleClass().add("card-value");
        patientCard.getChildren().addAll(patientTitle, totalPatientsLabel);
        statsGrid.add(patientCard, 0, 0);

        VBox doctorCard = new VBox(5);
        doctorCard.getStyleClass().add("dashboard-card");
        Label doctorTitle = new Label("Total Doctors");
        doctorTitle.getStyleClass().add("card-title");
        Label totalDoctorsLabel = new Label(String.valueOf(doctorManager.getAllDoctors().size()));
        totalDoctorsLabel.getStyleClass().add("card-value");
        doctorCard.getChildren().addAll(doctorTitle, totalDoctorsLabel);
        statsGrid.add(doctorCard, 1, 0);

        VBox appointmentCard = new VBox(5);
        appointmentCard.getStyleClass().add("dashboard-card");
        Label appointmentTitle = new Label("Total Appointments");
        appointmentTitle.getStyleClass().add("card-title");
        Label totalAppointmentsLabel = new Label(String.valueOf(appointmentManager.getAppointments().size()));
        totalAppointmentsLabel.getStyleClass().add("card-value");
        appointmentCard.getChildren().addAll(appointmentTitle, totalAppointmentsLabel);
        statsGrid.add(appointmentCard, 0, 1);

        VBox loggedInDoctorCard = new VBox(5);
        loggedInDoctorCard.getStyleClass().add("dashboard-card");
        Label loggedInDoctorTitle = new Label("Logged-in Doctors");
        loggedInDoctorTitle.getStyleClass().add("card-title");
        Label loggedInDoctorValue = new Label(String.valueOf(doctorManager.getLoggedInDoctorsList().size()));
        loggedInDoctorValue.getStyleClass().add("card-value");
        loggedInDoctorCard.getChildren().addAll(loggedInDoctorTitle, loggedInDoctorValue);
        statsGrid.add(loggedInDoctorCard, 1, 1);


        Label logTitle = new Label("Recent Activity Log");
        logTitle.getStyleClass().add("content-title");
        TextArea dashboardLogArea = new TextArea();
        dashboardLogArea.setEditable(false);
        dashboardLogArea.getStyleClass().add("text-area");
        dashboardLogArea.setPrefHeight(200);
        dashboardLogArea.setPrefWidth(Double.MAX_VALUE);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);
        doctorManager.displayDoctorActivityLog();
        System.out.flush();
        System.setOut(old);
        dashboardLogArea.setText(baos.toString());

        currentOutputArea = dashboardLogArea;

        dashboardView.getChildren().addAll(title, statsGrid, logTitle, dashboardLogArea);

        contentPane.getChildren().clear();
        contentPane.getChildren().add(dashboardView);
    }

    @FXML
    private void showAddNewPatientView() {
        showRegisterPatientView();
    }

    @FXML
    private void showRemovePatientView() {
        VBox formView = new VBox(15);
        formView.setPadding(new Insets(20));
        formView.setAlignment(Pos.TOP_CENTER);
        formView.getStyleClass().add("form-container");

        Label title = new Label("Remove Patient by ID");
        title.getStyleClass().add("content-title");

        GridPane formGrid = new GridPane();
        formGrid.getStyleClass().add("form-grid");
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        removePatientIdField = new TextField();
        removePatientIdField.setPromptText("Patient ID");
        formGrid.add(new Label("Patient ID:"), 0, 0);
        formGrid.add(removePatientIdField, 1, 0);

        Button submitButton = new Button("Remove Patient");
        submitButton.getStyleClass().add("form-button");
        submitButton.setOnAction(event -> {
            try {
                int patientIdToRemove = Integer.parseInt(removePatientIdField.getText().trim());
                if (patientManager.removePatientById(patientIdToRemove)) {
                    showAlert(AlertType.INFORMATION, "Sukses", null, "Pasien dengan ID " + patientIdToRemove + " berhasil dihapus.");
                    appendOutput("Pasien dengan ID " + patientIdToRemove + " berhasil dihapus.");
                } else {
                    showAlert(AlertType.WARNING, "Gagal", null, "Pasien dengan ID " + patientIdToRemove + " tidak ditemukan.");
                    appendOutput("Pasien dengan ID " + patientIdToRemove + " tidak ditemukan.");
                }
            }
            catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Input Tidak Valid", null, "ID harus berupa angka.");
                appendOutput("Input ID tidak valid. ID harus berupa angka.");
            }
            removePatientIdField.clear();
        });

        TextArea formOutputArea = new TextArea();
        formOutputArea.setEditable(false);
        formOutputArea.getStyleClass().add("text-area");
        formOutputArea.setPrefHeight(100);
        currentOutputArea = formOutputArea;

        formView.getChildren().addAll(title, formGrid, submitButton, formOutputArea);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(formView);
    }

    @FXML
    private void showSearchPatientByNameView() {
        VBox formView = new VBox(15);
        formView.setPadding(new Insets(20));
        formView.setAlignment(Pos.TOP_CENTER);
        formView.getStyleClass().add("form-container");

        Label title = new Label("Search Patient by Name");
        title.getStyleClass().add("content-title");

        GridPane formGrid = new GridPane();
        formGrid.getStyleClass().add("form-grid");
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        searchPatientNameField = new TextField();
        searchPatientNameField.setPromptText("Patient Name");
        formGrid.add(new Label("Patient Name:"), 0, 0);
        formGrid.add(searchPatientNameField, 1, 0);

        Button submitButton = new Button("Search Patient");
        submitButton.getStyleClass().add("form-button");
        submitButton.setOnAction(event -> {
            String patientNameToSearch = searchPatientNameField.getText().trim();
            if (patientNameToSearch.isEmpty()) {
                showAlert(AlertType.WARNING, "Input Kosong", null, "Nama pasien tidak boleh kosong.");
                appendOutput("Nama pasien tidak boleh kosong.");
                return;
            }
            Patient foundPatient = patientManager.findPatientByName(patientNameToSearch);
            if (foundPatient != null) {
                appendOutput("Pasien ditemukan: " + foundPatient.toString());
                showAlert(AlertType.INFORMATION, "Sukses", null, "Pasien ditemukan: " + foundPatient.getName());
            } else {
                appendOutput("Pasien dengan nama '" + patientNameToSearch + "' tidak ditemukan.");
                showAlert(AlertType.INFORMATION, "Tidak Ditemukan", null, "Pasien dengan nama '" + patientNameToSearch + "' tidak ditemukan.");
            }
            searchPatientNameField.clear();
        });

        TextArea formOutputArea = new TextArea();
        formOutputArea.setEditable(false);
        formOutputArea.getStyleClass().add("text-area");
        formOutputArea.setPrefHeight(100);
        currentOutputArea = formOutputArea;

        formView.getChildren().addAll(title, formGrid, submitButton, formOutputArea);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(formView);
    }

    @FXML
    private void showDisplayAllPatientsView() {
        VBox displayView = new VBox(10);
        displayView.setPadding(new Insets(20));
        displayView.setAlignment(Pos.TOP_CENTER);
        displayView.getStyleClass().add("form-container");

        Label title = new Label("All Patients Records");
        title.getStyleClass().add("content-title");

        TextArea patientListArea = new TextArea();
        patientListArea.setEditable(false);
        patientListArea.getStyleClass().add("text-area");
        patientListArea.setPrefHeight(400);

        MyLinkedList<Patient> allPatients = patientManager.getPatients();
        StringBuilder sb = new StringBuilder();
        if (allPatients.isEmpty()) {
            sb.append("Tidak ada pasien dalam daftar.");
        } else {
            sb.append("--- Daftar Semua Pasien ---\n");
            for (Patient p : allPatients) {
                sb.append(p.toString()).append("\n");
            }
            sb.append("---------------------------\n");
        }
        patientListArea.setText(sb.toString());

        currentOutputArea = patientListArea;

        displayView.getChildren().addAll(title, patientListArea);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(displayView);
    }

    @FXML
    private void showLoginView() {
        VBox formView = new VBox(15);
        formView.setPadding(new Insets(20));
        formView.setAlignment(Pos.TOP_CENTER);
        formView.getStyleClass().add("form-container");

        Label title = new Label("Login");
        title.getStyleClass().add("content-title");

        GridPane formGrid = new GridPane();
        formGrid.getStyleClass().add("form-grid");
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        loginUsernameField = new TextField();
        loginUsernameField.setPromptText("Username (Name)");
        loginPasswordField = new TextField();
        loginPasswordField.setPromptText("Password");

        formGrid.add(new Label("Username:"), 0, 0);
        formGrid.add(loginUsernameField, 1, 0);
        formGrid.add(new Label("Password:"), 0, 1);
        formGrid.add(loginPasswordField, 1, 1);

        Button submitButton = new Button("Login");
        submitButton.getStyleClass().add("form-button");
        submitButton.setOnAction(event -> {
            String username = loginUsernameField.getText().trim();
            String password = loginPasswordField.getText().trim();

            Object authenticatedUser = accountManager.authenticateUser(username, password);

            if (authenticatedUser instanceof Doctor) {
                Doctor doctor = (Doctor) authenticatedUser;
                doctorManager.loginDoctor(doctor);
                currentLoggedInDoctor = doctor;
                currentLoggedInPatient = null;
                showAlert(AlertType.INFORMATION, "Login Berhasil", null, "Selamat datang, Dokter " + doctor.getName() + ".");
                appendOutput("Dokter " + doctor.getName() + " berhasil login.");
                showDashboardView();
            } else if (authenticatedUser instanceof Patient) {
                Patient patient = (Patient) authenticatedUser;
                currentLoggedInPatient = patient;
                currentLoggedInDoctor = null;
                showAlert(AlertType.INFORMATION, "Login Berhasil", null, "Selamat datang, Pasien " + patient.getName() + ".");
                appendOutput("Pasien " + patient.getName() + " berhasil login.");
                showDashboardView();
            } else {
                showAlert(AlertType.ERROR, "Login Gagal", null, "Nama pengguna atau kata sandi tidak valid.");
                appendOutput("Login gagal.");
            }
            loginUsernameField.clear();
            loginPasswordField.clear();
        });

        TextArea formOutputArea = new TextArea();
        formOutputArea.setEditable(false);
        formOutputArea.getStyleClass().add("text-area");
        formOutputArea.setPrefHeight(100);
        currentOutputArea = formOutputArea;

        formView.getChildren().addAll(title, formGrid, submitButton, formOutputArea);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(formView);
    }


    @FXML
    private void showDoctorRegisterView() {
        VBox formView = new VBox(15);
        formView.setPadding(new Insets(20));
        formView.setAlignment(Pos.TOP_CENTER);
        formView.getStyleClass().add("form-container");

        Label title = new Label("Doctor Register");
        title.getStyleClass().add("content-title");

        GridPane formGrid = new GridPane();
        formGrid.getStyleClass().add("form-grid");
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        regDoctorIdField = new TextField();
        regDoctorIdField.setPromptText("Auto-generated");
        regDoctorIdField.setEditable(false);
        regDoctorIdField.setText(String.valueOf(doctorManager.getNextDoctorId()));

        regDoctorNameField = new TextField();
        regDoctorNameField.setPromptText("Doctor Name");
        regDoctorPasswordField = new TextField();
        regDoctorPasswordField.setPromptText("Password");
        regDoctorSpecialtyField = new TextField();
        regDoctorSpecialtyField.setPromptText("Specialization");

        formGrid.add(new Label("Doctor ID:"), 0, 0);
        formGrid.add(regDoctorIdField, 1, 0);
        formGrid.add(new Label("Name:"), 0, 1);
        formGrid.add(regDoctorNameField, 1, 1);
        formGrid.add(new Label("Password:"), 0, 2);
        formGrid.add(regDoctorPasswordField, 1, 2);
        formGrid.add(new Label("Specialization:"), 0, 3);
        formGrid.add(regDoctorSpecialtyField, 1, 3);

        Button submitButton = new Button("Register Doctor");
        submitButton.getStyleClass().add("form-button");
        submitButton.setOnAction(event -> {
            int doctorId = Integer.parseInt(regDoctorIdField.getText());
            String doctorName = regDoctorNameField.getText().trim();
            String doctorPassword = regDoctorPasswordField.getText().trim();
            String doctorSpecialization = regDoctorSpecialtyField.getText().trim();

            if (doctorName.isEmpty() || doctorPassword.isEmpty() || doctorSpecialization.isEmpty()) {
                showAlert(AlertType.WARNING, "Input Tidak Lengkap", null, "Semua bidang harus diisi.");
                return;
            }

            boolean accountSuccess = accountManager.registerUser(doctorId, doctorName, doctorPassword, "doctor");

            if (accountSuccess) {
                Doctor newDoctor = new Doctor(doctorId, doctorName, doctorSpecialization);
                doctorManager.addDoctorToRecords(newDoctor);
                showAlert(AlertType.INFORMATION, "Sukses", null, "Dokter " + doctorName + " berhasil didaftarkan.");
                appendOutput("Dokter " + doctorName + " berhasil didaftarkan.");
            } else {
                showAlert(AlertType.ERROR, "Gagal", null, "Pendaftaran dokter gagal. ID mungkin sudah terdaftar atau terjadi kesalahan.");
                appendOutput("Pendaftaran dokter gagal.");
            }
            regDoctorIdField.setText(String.valueOf(doctorManager.getNextDoctorId()));
            regDoctorNameField.clear();
            regDoctorPasswordField.clear();
            regDoctorSpecialtyField.clear();
        });

        TextArea formOutputArea = new TextArea();
        formOutputArea.setEditable(false);
        formOutputArea.getStyleClass().add("text-area");
        formOutputArea.setPrefHeight(100);
        currentOutputArea = formOutputArea;

        formView.getChildren().addAll(title, formGrid, submitButton, formOutputArea);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(formView);
    }

    @FXML
    private void showRegisterPatientView() {
        VBox formView = new VBox(15);
        formView.setPadding(new Insets(20));
        formView.setAlignment(Pos.TOP_CENTER);
        formView.getStyleClass().add("form-container");

        Label title = new Label("Patient Register");
        title.getStyleClass().add("content-title");

        GridPane formGrid = new GridPane();
        formGrid.getStyleClass().add("form-grid");
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        regPatientIdField = new TextField();
        regPatientIdField.setPromptText("Auto-generated");
        regPatientIdField.setEditable(false);
        regPatientIdField.setText(String.valueOf(patientManager.getNextPatientId()));

        regPatientNameField = new TextField();
        regPatientNameField.setPromptText("Patient Name");
        regPatientPasswordField = new TextField();
        regPatientPasswordField.setPromptText("Password");
        regPatientAgeField = new TextField();
        regPatientAgeField.setPromptText("Age");
        regPatientAddressField = new TextField();
        regPatientAddressField.setPromptText("Address");
        regPatientPhoneField = new TextField();
        regPatientPhoneField.setPromptText("Phone Number");
        regPatientMedicalHistoryField = new TextField();
        regPatientMedicalHistoryField.setPromptText("Medical History (Optional)");
        regPatientAllergiesField = new TextField();
        regPatientAllergiesField.setPromptText("Allergies (Optional)");

        formGrid.add(new Label("Patient ID:"), 0, 0);
        formGrid.add(regPatientIdField, 1, 0);
        formGrid.add(new Label("Name:"), 0, 1);
        formGrid.add(regPatientNameField, 1, 1);
        formGrid.add(new Label("Password:"), 0, 2);
        formGrid.add(regPatientPasswordField, 1, 2);
        formGrid.add(new Label("Age:"), 0, 3);
        formGrid.add(regPatientAgeField, 1, 3);
        formGrid.add(new Label("Address:"), 0, 4);
        formGrid.add(regPatientAddressField, 1, 4);
        formGrid.add(new Label("Phone Number:"), 0, 5);
        formGrid.add(regPatientPhoneField, 1, 5);
        formGrid.add(new Label("Medical History:"), 0, 6);
        formGrid.add(regPatientMedicalHistoryField, 1, 6);
        formGrid.add(new Label("Allergies:"), 0, 7);
        formGrid.add(regPatientAllergiesField, 1, 7);

        Button submitButton = new Button("Register Patient");
        submitButton.getStyleClass().add("form-button");
        submitButton.setOnAction(event -> handleRegisterPatientSubmit());

        TextArea formOutputArea = new TextArea();
        formOutputArea.setEditable(false);
        formOutputArea.getStyleClass().add("text-area");
        formOutputArea.setPrefHeight(100);
        currentOutputArea = formOutputArea;

        formView.getChildren().addAll(title, formGrid, submitButton, formOutputArea);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(formView);
    }

    private void handleRegisterPatientSubmit() {
        if (regPatientIdField == null || regPatientNameField == null) {
            showAlert(AlertType.ERROR, "Internal Error", null, "Form fields not initialized.");
            return;
        }

        int patientId = Integer.parseInt(regPatientIdField.getText());
        String patientName = regPatientNameField.getText().trim();
        String patientPassword = regPatientPasswordField.getText().trim();
        int age = -1;
        try {
            age = Integer.parseInt(regPatientAgeField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Input Tidak Valid", null, "Usia harus berupa angka.");
            return;
        }
        String address = regPatientAddressField.getText().trim();
        String phoneNumber = regPatientPhoneField.getText().trim();
        String medicalHistory = regPatientMedicalHistoryField.getText().trim();
        String allergies = regPatientAllergiesField.getText().trim();

        if (patientName.isEmpty() || patientPassword.isEmpty() || address.isEmpty() || phoneNumber.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Tidak Lengkap", null, "Semua bidang wajib (Nama, Kata Sandi, Alamat, Telepon) harus diisi.");
            return;
        }

        boolean accountSuccess = accountManager.registerUser(patientId, patientName, patientPassword, "patient");

        if (accountSuccess) {
            Patient newPatient = new Patient(patientId, patientName, age, address, phoneNumber, medicalHistory, allergies);
            patientManager.addPatient(newPatient);
            showAlert(AlertType.INFORMATION, "Sukses", null, "Pasien " + patientName + " berhasil didaftarkan.");
            appendOutput("Pasien " + patientName + " berhasil didaftarkan.");
            regPatientIdField.setText(String.valueOf(patientManager.getNextPatientId()));
            regPatientNameField.clear();
            regPatientPasswordField.clear();
            regPatientAgeField.clear();
            regPatientAddressField.clear();
            regPatientPhoneField.clear();
            regPatientMedicalHistoryField.clear();
            regPatientAllergiesField.clear();
        } else {
            showAlert(AlertType.ERROR, "Gagal", null, "Pendaftaran pasien gagal. ID mungkin sudah terdaftar atau terjadi kesalahan.");
            appendOutput("Pendaftaran pasien gagal.");
        }
    }


    @FXML
    private void showDoctorLogoutView() {
        VBox view = new VBox(15);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.TOP_CENTER);
        view.getStyleClass().add("form-container");

        Label title = new Label("Doctor Logout");
        title.getStyleClass().add("content-title");

        TextArea formOutputArea = new TextArea();
        formOutputArea.setEditable(false);
        formOutputArea.getStyleClass().add("text-area");
        formOutputArea.setPrefHeight(150);
        currentOutputArea = formOutputArea;

        MyLinkedList<Doctor> loggedInDoctors = doctorManager.getLoggedInDoctorsList();
        if (loggedInDoctors.isEmpty()) {
            formOutputArea.setText("Tidak ada dokter yang sedang login.");
            view.getChildren().addAll(title, formOutputArea);
            contentPane.getChildren().clear();
            contentPane.getChildren().add(view);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("--- Dokter yang Sedang Login ---\n");
        int count = 1;
        MyLinkedList<Integer> doctorIdsInList = new MyLinkedList<>();
        for (Doctor d : loggedInDoctors) {
            sb.append(count++).append(". ID: ").append(d.getId())
                    .append(" | Nama: ").append(d.getName())
                    .append(" (Login: ").append(d.getLoginTime().format(DateTimeFormatter.ofPattern("HH:mm"))).append(")\n");
            doctorIdsInList.add(d.getId());
        }
        sb.append("--------------------------------\n");
        formOutputArea.setText(sb.toString());

        Label promptLabel = new Label("Masukkan ID dokter yang ingin logout:");
        TextField doctorIdToLogoutField = new TextField();
        doctorIdToLogoutField.setPromptText("Doctor ID");

        Button logoutButton = new Button("Logout Doctor");
        logoutButton.getStyleClass().add("form-button");
        logoutButton.setOnAction(event -> {
            String idStr = doctorIdToLogoutField.getText().trim();
            if (idStr.isEmpty()) {
                showAlert(AlertType.WARNING, "Input Kosong", null, "Masukkan ID dokter yang ingin logout.");
                return;
            }
            try {
                int idToLogout = Integer.parseInt(idStr);

                // Validasi: Pastikan ID ada di daftar dokter yang sedang login
                boolean idFoundInList = false;
                for (Integer docId : doctorIdsInList) {
                    if (docId == idToLogout) {
                        idFoundInList = true;
                        break;
                    }
                }

                if (!idFoundInList) {
                    showAlert(AlertType.ERROR, "Logout Gagal", null, "Dokter dengan ID " + idToLogout + " tidak ditemukan dalam daftar yang sedang login.");
                    appendOutput("Logout gagal: ID tidak valid atau tidak login.");
                    return;
                }

                // Lakukan proses logout
                if (doctorManager.logoutDoctor(idToLogout)) {
                    showAlert(AlertType.INFORMATION, "Sukses", null, "Dokter dengan ID " + idToLogout + " berhasil logout.");
                    appendOutput("Dokter ID " + idToLogout + " berhasil logout.");
                    // Opsional: Jika dokter yang logout adalah currentLoggedInDoctor, bersihkan referensinya
                    if (currentLoggedInDoctor != null && currentLoggedInDoctor.getId() == idToLogout) {
                        currentLoggedInDoctor = null;
                    }
                    showDoctorLogoutView();
                } else {
                    showAlert(AlertType.ERROR, "Gagal", null, "Logout gagal. Ada masalah saat menghapus dari daftar login.");
                    appendOutput("Logout gagal.");
                }
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Input Tidak Valid", null, "ID harus berupa angka.");
            }
            doctorIdToLogoutField.clear();
        });

        view.getChildren().addAll(title, formOutputArea, promptLabel, doctorIdToLogoutField, logoutButton);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(view);
    }

    @FXML
    private void showViewLoggedInDoctorsView() {
        VBox displayView = new VBox(10);
        displayView.setPadding(new Insets(20));
        displayView.setAlignment(Pos.TOP_CENTER);
        displayView.getStyleClass().add("form-container");

        Label title = new Label("Logged-in Doctors");
        title.getStyleClass().add("content-title");

        TextArea doctorListArea = new TextArea();
        doctorListArea.setEditable(false);
        doctorListArea.getStyleClass().add("text-area");
        doctorListArea.setPrefHeight(400);

        MyLinkedList<Doctor> loggedInDoctors = doctorManager.getLoggedInDoctorsList();
        StringBuilder sb = new StringBuilder();
        if (loggedInDoctors.isEmpty()) {
            sb.append("Tidak ada dokter yang sedang login.");
        } else {
            sb.append("--- Dokter yang Sedang Login ---\n");
            for (Doctor d : loggedInDoctors) {
                sb.append(d.toString()).append("\n");
            }
            sb.append("--------------------------------\n");
        }
        doctorListArea.setText(sb.toString());

        currentOutputArea = doctorListArea;

        displayView.getChildren().addAll(title, doctorListArea);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(displayView);
    }

    @FXML
    private void showViewDoctorActivityLogView() {
        VBox displayView = new VBox(10);
        displayView.setPadding(new Insets(20));
        displayView.setAlignment(Pos.TOP_CENTER);
        displayView.getStyleClass().add("form-container");

        Label title = new Label("Doctor Activity Log");
        title.getStyleClass().add("content-title");

        TextArea logArea = new TextArea();
        logArea.setEditable(false);
        logArea.getStyleClass().add("text-area");
        logArea.setPrefHeight(400);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        doctorManager.displayDoctorActivityLog();

        System.out.flush();
        System.setOut(old);

        logArea.setText(baos.toString());

        currentOutputArea = logArea;

        displayView.getChildren().addAll(title, logArea);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(displayView);
    }

    @FXML
    private void showDisplayAllDoctorsView() {
        VBox displayView = new VBox(10);
        displayView.setPadding(new Insets(20));
        displayView.setAlignment(Pos.TOP_CENTER);
        displayView.getStyleClass().add("form-container");

        Label title = new Label("All Registered Doctors");
        title.getStyleClass().add("content-title");

        TextArea doctorListArea = new TextArea();
        doctorListArea.setEditable(false);
        doctorListArea.getStyleClass().add("text-area");
        doctorListArea.setPrefHeight(400);

        MyLinkedList<Doctor> allDoctors = doctorManager.getAllDoctors();
        StringBuilder sb = new StringBuilder();
        if (allDoctors.isEmpty()) {
            sb.append("Tidak ada dokter dalam catatan.");
        } else {
            sb.append("--- Daftar Semua Dokter --- \n");
            for (Doctor d : allDoctors) {
                sb.append(d.toString()).append("\n");
            }
            sb.append("---------------------------\n");
        }
        doctorListArea.setText(sb.toString());

        currentOutputArea = doctorListArea;

        displayView.getChildren().addAll(title, doctorListArea);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(displayView);
    }

    // --- Manajemen Jadwal Dokter ---
    @FXML
    private void showAddDoctorScheduleView() {
        VBox formView = new VBox(15);
        formView.setPadding(new Insets(20));
        formView.setAlignment(Pos.TOP_CENTER);
        formView.getStyleClass().add("form-container");

        Label title = new Label("Add Doctor Schedule");
        title.getStyleClass().add("content-title");

        GridPane formGrid = new GridPane();
        formGrid.getStyleClass().add("form-grid");
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        addScheduleDoctorIdField = new TextField();
        addScheduleDoctorIdField.setPromptText("Doctor ID");
        if (currentLoggedInDoctor != null) {
            addScheduleDoctorIdField.setText(String.valueOf(currentLoggedInDoctor.getId()));
            addScheduleDoctorIdField.setEditable(false);
        } else {
            addScheduleDoctorIdField.setEditable(true);
        }

        addScheduleStartDateField = new TextField();
        addScheduleStartDateField.setPromptText("YYYY-MM-DD");
        addScheduleStartDateField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        addScheduleStartTimeField = new TextField();
        addScheduleStartTimeField.setPromptText("HH:MM");
        addScheduleStartTimeField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

        addScheduleEndDateField = new TextField();
        addScheduleEndDateField.setPromptText("YYYY-MM-DD");
        addScheduleEndDateField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        addScheduleEndTimeField = new TextField();
        addScheduleEndTimeField.setPromptText("HH:MM");
        addScheduleEndTimeField.setText(LocalDateTime.now().plusHours(1).format(DateTimeFormatter.ofPattern("HH:mm")));

        formGrid.add(new Label("Doctor ID:"), 0, 0);
        formGrid.add(addScheduleDoctorIdField, 1, 0);
        formGrid.add(new Label("Start Date:"), 0, 1);
        formGrid.add(addScheduleStartDateField, 1, 1);
        formGrid.add(new Label("Start Time:"), 0, 2);
        formGrid.add(addScheduleStartTimeField, 1, 2);
        formGrid.add(new Label("End Date:"), 0, 3);
        formGrid.add(addScheduleEndDateField, 1, 3);
        formGrid.add(new Label("End Time:"), 0, 4);
        formGrid.add(addScheduleEndTimeField, 1, 4);

        Button submitButton = new Button("Add Schedule");
        submitButton.getStyleClass().add("form-button");
        submitButton.setOnAction(event -> handleAddDoctorScheduleSubmit());

        TextArea formOutputArea = new TextArea();
        formOutputArea.setEditable(false);
        formOutputArea.getStyleClass().add("text-area");
        formOutputArea.setPrefHeight(100);
        currentOutputArea = formOutputArea;

        formView.getChildren().addAll(title, formGrid, submitButton, formOutputArea);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(formView);
    }

    private void handleAddDoctorScheduleSubmit() {
        int doctorId = -1;
        try {
            doctorId = Integer.parseInt(addScheduleDoctorIdField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Input Tidak Valid", null, "ID Dokter harus berupa angka.");
            return;
        }

        Doctor doctor = doctorManager.findDoctorById(doctorId);
        if (doctor == null) {
            showAlert(AlertType.ERROR, "Gagal", null, "Dokter dengan ID " + doctorId + " tidak ditemukan.");
            return;
        }
        if (currentLoggedInDoctor != null && currentLoggedInDoctor.getId() != doctorId) {
            showAlert(AlertType.ERROR, "Akses Ditolak", null, "Anda hanya dapat menambahkan jadwal untuk dokter yang sedang login.");
            return;
        } else if (currentLoggedInDoctor == null) {
            showAlert(AlertType.WARNING, "Akses Ditolak", null, "Hanya dokter yang sedang login yang dapat menambahkan jadwal.");
            return;
        }


        String startDateString = addScheduleStartDateField.getText().trim();
        String startTimeString = addScheduleStartTimeField.getText().trim();
        String endDateString = addScheduleEndDateField.getText().trim();
        String endTimeString = addScheduleEndTimeField.getText().trim();

        if (startDateString.isEmpty() || startTimeString.isEmpty() || endDateString.isEmpty() || endTimeString.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Tidak Lengkap", null, "Semua bidang tanggal dan waktu harus diisi.");
            return;
        }

        try {
            LocalDateTime startDateTime = LocalDateTime.parse(startDateString + " " + startTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            LocalDateTime endDateTime = LocalDateTime.parse(endDateString + " " + endTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            if (startDateTime.isAfter(endDateTime) || startDateTime.isEqual(endDateTime)) {
                showAlert(AlertType.ERROR, "Input Tidak Valid", null, "Waktu mulai harus sebelum waktu berakhir.");
                return;
            }

            ScheduleSlot newSlot = new ScheduleSlot(startDateTime, endDateTime);
            if (doctorManager.addDoctorSchedule(doctorId, newSlot)) {
                showAlert(AlertType.INFORMATION, "Sukses", null, "Jadwal berhasil ditambahkan untuk Dokter " + doctor.getName() + ".");
                appendOutput("Jadwal ditambahkan: " + newSlot.toString());
            } else {
                showAlert(AlertType.ERROR, "Gagal", null, "Gagal menambahkan jadwal. Mungkin ada tumpang tindih waktu.");
                appendOutput("Gagal menambahkan jadwal.");
            }

            addScheduleDoctorIdField.setText(String.valueOf(currentLoggedInDoctor.getId()));
            addScheduleStartDateField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            addScheduleStartTimeField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
            addScheduleEndDateField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            addScheduleEndTimeField.setText(LocalDateTime.now().plusHours(1).format(DateTimeFormatter.ofPattern("HH:mm")));

        } catch (DateTimeParseException e) {
            showAlert(AlertType.ERROR, "Format Tanggal/Waktu Tidak Valid", null, "Format tanggal atau waktu tidak valid. Harap gunakan McDermott-MM-DD dan HH:MM.");
        }
    }

    @FXML
    private void showScheduleAppointmentView() {
        VBox formView = new VBox(15);
        formView.setPadding(new Insets(20));
        formView.setAlignment(Pos.TOP_CENTER);
        formView.getStyleClass().add("form-container");
        formView.setMaxWidth(Double.MAX_VALUE);

        Label title = new Label("Schedule New Appointment");
        title.getStyleClass().add("content-title");

        GridPane formGrid = new GridPane();
        formGrid.getStyleClass().add("form-grid");
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setMaxWidth(Double.MAX_VALUE);

        // Appointment ID
        formGrid.add(new Label("Appointment ID:"), 0, 0);
        schAppointmentIdField = new TextField();
        schAppointmentIdField.setPromptText("Auto-generated");
        schAppointmentIdField.setEditable(false);
        schAppointmentIdField.setText(String.valueOf(appointmentManager.getNextId()));
        schAppointmentIdField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(schAppointmentIdField, Priority.ALWAYS);
        formGrid.add(schAppointmentIdField, 1, 0);

        // Patient ID
        formGrid.add(new Label("Patient ID:"), 0, 1);
        schPatientIdField = new TextField();
        schPatientIdField.setPromptText("Patient ID");
        schPatientIdField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(schPatientIdField, Priority.ALWAYS);
        formGrid.add(schPatientIdField, 1, 1);

        // Patient Illness
        formGrid.add(new Label("Patient Illness:"), 0, 2);
        schIllnessField = new TextField();
        schIllnessField.setPromptText("Patient Illness (e.g., Heart, General, Dental)");
        schIllnessField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(schIllnessField, Priority.ALWAYS);
        formGrid.add(schIllnessField, 1, 2);

        // Doctor ID Input
        formGrid.add(new Label("Doctor ID (Input):"), 0, 3);
        initialDoctorIdField = new TextField();
        initialDoctorIdField.setPromptText("Doctor ID to view schedule");
        initialDoctorIdField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(initialDoctorIdField, Priority.ALWAYS);
        formGrid.add(initialDoctorIdField, 1, 3);

        // View Doctor Schedule Button
        Button viewScheduleButton = new Button("View & Select Doctor Schedule");
        viewScheduleButton.getStyleClass().add("form-button");
        viewScheduleButton.setOnAction(event -> handleViewAndSelectDoctorSchedule());
        formGrid.add(viewScheduleButton, 0, 4, 2, 1);

        // Doctor Schedule Display Area
        doctorScheduleDisplayBox = new VBox(5);
        doctorScheduleDisplayBox.setPadding(new Insets(5));
        doctorScheduleDisplayBox.getStyleClass().add("schedule-display-vbox");

        ScrollPane scheduleScrollPane = new ScrollPane(doctorScheduleDisplayBox);
        scheduleScrollPane.setFitToWidth(true);
        scheduleScrollPane.setPrefHeight(180);
        scheduleScrollPane.setMaxWidth(Double.MAX_VALUE);
        scheduleScrollPane.getStyleClass().add("schedule-scroll-pane");
        formGrid.add(scheduleScrollPane, 0, 5, 2, 1);

        // Select Slot Number
        formGrid.add(new Label("Select Slot Number:"), 0, 6);
        selectedScheduleSlotField = new TextField();
        selectedScheduleSlotField.setPromptText("Enter slot number (e.g., 1)");
        selectedScheduleSlotField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(selectedScheduleSlotField, Priority.ALWAYS);
        formGrid.add(selectedScheduleSlotField, 1, 6);

        // Select Slot Button
        Button selectSlotButton = new Button("Select Slot");
        selectSlotButton.getStyleClass().add("form-button");
        selectSlotButton.setOnAction(event -> handleSelectScheduleSlot());
        formGrid.add(selectSlotButton, 0, 7, 2, 1);

        // Selected Doctor ID
        formGrid.add(new Label("Selected Doctor ID:"), 0, 8);
        schDoctorIdField = new TextField();
        schDoctorIdField.setPromptText("Doctor ID (Selected)");
        schDoctorIdField.setEditable(false);
        schDoctorIdField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(schDoctorIdField, Priority.ALWAYS);
        formGrid.add(schDoctorIdField, 1, 8);

        // Selected Date
        formGrid.add(new Label("Selected Date:"), 0, 9);
        schDateField = new TextField();
        schDateField.setPromptText("YYYY-MM-DD (dari slot)");
        schDateField.setEditable(false);
        schDateField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(schDateField, Priority.ALWAYS);
        formGrid.add(schDateField, 1, 9);

        // Selected Time
        formGrid.add(new Label("Selected Time:"), 0, 10);
        schTimeField = new TextField();
        schTimeField.setPromptText("HH:MM (dari slot)");
        schTimeField.setEditable(false);
        schTimeField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(schTimeField, Priority.ALWAYS);
        formGrid.add(schTimeField, 1, 10);

        // Recommend Doctor Button
        Button recommendDoctorButton = new Button("Recommend Doctor by Illness");
        recommendDoctorButton.getStyleClass().add("form-button");
        recommendDoctorButton.setOnAction(event -> handleRecommendDoctor());
        formGrid.add(recommendDoctorButton, 0, 11, 2, 1);

        // Schedule Appointment Button
        Button submitButton = new Button("Schedule Appointment");
        submitButton.getStyleClass().add("form-button");
        submitButton.setOnAction(event -> handleScheduleAppointmentSubmit());
        formGrid.add(submitButton, 0, 12, 2, 1);

        // Output TextArea
        TextArea formOutputArea = new TextArea();
        formOutputArea.setEditable(false);
        formOutputArea.setPrefHeight(100);
        formOutputArea.setMaxWidth(Double.MAX_VALUE);
        formOutputArea.getStyleClass().add("text-area");
        currentOutputArea = formOutputArea;

        formView.getChildren().addAll(title, formGrid, formOutputArea);

        ScrollPane outerScrollPane = new ScrollPane(formView);
        outerScrollPane.setFitToWidth(true);
        outerScrollPane.setPadding(new Insets(10));

        contentPane.getChildren().clear();
        contentPane.getChildren().add(outerScrollPane);
    }

    @FXML
    private void handleViewAndSelectDoctorSchedule() {
        if (initialDoctorIdField == null || doctorScheduleDisplayBox == null || schDoctorIdField == null) {
            showAlert(AlertType.ERROR, "Internal Error", null, "Komponen form tidak diinisialisasi dengan benar.");
            System.err.println("DEBUG: handleViewAndSelectDoctorSchedule - Salah satu field penting adalah NULL.");
            return;
        }

        System.out.println("DEBUG: handleViewAndSelectDoctorSchedule called.");
        System.out.println("DEBUG: initialDoctorIdField text: " + initialDoctorIdField.getText());

        String doctorIdStr = initialDoctorIdField.getText().trim();
        if (doctorIdStr.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Kosong", null, "Masukkan ID Dokter untuk melihat jadwal.");
            return;
        }

        try {
            int doctorId = Integer.parseInt(doctorIdStr);
            Doctor selectedDoctor = doctorManager.findDoctorById(doctorId);
            if (selectedDoctor == null) {
                showAlert(AlertType.ERROR, "Dokter Tidak Ditemukan", null, "Dokter dengan ID " + doctorId + " tidak ditemukan.");
                doctorScheduleDisplayBox.getChildren().clear();
                doctorScheduleDisplayBox.getChildren().add(new Label("Dokter tidak ditemukan."));
                schDoctorIdField.setText("");
                return;
            }

            schDoctorIdField.setText(String.valueOf(selectedDoctor.getId()));

            doctorScheduleDisplayBox.getChildren().clear();
            doctorScheduleDisplayBox.getChildren().add(new Label("Jadwal untuk Dokter " + selectedDoctor.getName() + " (ID: " + selectedDoctor.getId() + "):"));
            doctorScheduleDisplayBox.getChildren().add(new Label("-------------------------------------------"));

            if (selectedDoctor.getSchedule().isEmpty()) {
                doctorScheduleDisplayBox.getChildren().add(new Label("Tidak ada jadwal yang terdaftar untuk dokter ini."));
            } else {
                int slotIndex = 1;
                for (ScheduleSlot slot : selectedDoctor.getSchedule()) {
                    Label slotLabel = new Label(slotIndex++ + ". " + slot.toString());
                    slotLabel.getStyleClass().add("schedule-slot-item");
                    if (slot.isAvailable()) {
                        slotLabel.getStyleClass().add("schedule-slot-available");
                    } else {
                        slotLabel.getStyleClass().add("schedule-slot-booked");
                    }
                    doctorScheduleDisplayBox.getChildren().add(slotLabel);
                }
            }
            showAlert(AlertType.INFORMATION, "Jadwal Dokter", null, "Jadwal dokter ditampilkan. Silakan pilih nomor slot.");

        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Input Tidak Valid", null, "ID Dokter harus berupa angka.");
            initialDoctorIdField.clear();
        }
    }

    // Metode handleRecommendDoctor
    @FXML
    private void handleRecommendDoctor() {
        if (schIllnessField == null) {
            showAlert(AlertType.ERROR, "Internal Error", null, "Form fields not initialized.");
            return;
        }
        String illness = schIllnessField.getText().trim();
        if (illness.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Kosong", null, "Masukkan jenis penyakit untuk rekomendasi dokter.");
            return;
        }

        MyLinkedList<Doctor> suitableDoctors = doctorManager.findDoctorsBySpecialization(illness);
        StringBuilder sb = new StringBuilder();
        if (suitableDoctors.isEmpty()) {
            sb.append("Tidak ada dokter dengan spesialisasi '").append(illness).append("' yang ditemukan.");
            showAlert(AlertType.INFORMATION, "Tidak Ada Dokter", null, sb.toString());
            appendOutput(sb.toString());
        } else {
            sb.append("Dokter yang cocok untuk spesialisasi '").append(illness).append("':\n");
            for (Doctor doc : suitableDoctors) {
                sb.append("ID: ").append(doc.getId())
                        .append(" | Nama: ").append(doc.getName())
                        .append(" | Spesialisasi: ").append(doc.getSpecialization()).append("\n");
            }
            appendOutput(sb.toString());
            showAlert(AlertType.INFORMATION, "Rekomendasi Dokter", null, "Daftar dokter yang direkomendasikan ditampilkan di area output.");
        }
    }

    // Metode handleSelectScheduleSlot
    @FXML
    private void handleSelectScheduleSlot() {
        if (schDoctorIdField == null || selectedScheduleSlotField == null || doctorScheduleDisplayBox == null || schDateField == null || schTimeField == null) {
            showAlert(AlertType.ERROR, "Internal Error", null, "Komponen form tidak diinisialisasi dengan benar.");
            return;
        }

        if (schDoctorIdField.getText().isEmpty()) {
            showAlert(AlertType.WARNING, "Pilih Dokter", null, "Silakan lihat jadwal dokter terlebih dahulu.");
            return;
        }
        int doctorId = Integer.parseInt(schDoctorIdField.getText());
        Doctor selectedDoctor = doctorManager.findDoctorById(doctorId);
        if (selectedDoctor == null) {
            showAlert(AlertType.ERROR, "Dokter Tidak Ditemukan", null, "Dokter dengan ID " + doctorId + " tidak ditemukan. Pilih dokter yang valid.");
            return;
        }

        String slotNumberStr = selectedScheduleSlotField.getText().trim();
        if (slotNumberStr.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Kosong", null, "Masukkan nomor slot jadwal yang ingin dipilih.");
            return;
        }
        try {
            int slotIndex = Integer.parseInt(slotNumberStr) - 1;
            if (slotIndex < 0 || slotIndex >= selectedDoctor.getSchedule().size()) {
                showAlert(AlertType.ERROR, "Slot Tidak Valid", null, "Nomor slot tidak valid. Masukkan nomor yang ada di daftar jadwal.");
                return;
            }

            ScheduleSlot chosenSlot = selectedDoctor.getSchedule().get(slotIndex);
            if (!chosenSlot.isAvailable()) {
                showAlert(AlertType.WARNING, "Slot Tidak Tersedia", null, "Slot waktu yang dipilih sudah dipesan.");
                return;
            }

            schDateField.setText(chosenSlot.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            schTimeField.setText(chosenSlot.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            appendOutput("Slot jadwal " + chosenSlot.toString() + " dipilih.");
            showAlert(AlertType.INFORMATION, "Slot Dipilih", null, "Slot jadwal berhasil dipilih. Tanggal dan waktu janji temu sudah diisi.");

        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Input Tidak Valid", null, "Nomor slot harus berupa angka.");
        }
    }

    // Metode handleScheduleAppointmentSubmit
    @FXML
    private void handleScheduleAppointmentSubmit() {
        if (schAppointmentIdField == null || schPatientIdField == null || schDoctorIdField == null || schDateField == null || schTimeField == null) {
            showAlert(AlertType.ERROR, "Internal Error", null, "Komponen form tidak diinisialisasi dengan benar.");
            return;
        }

        int appointmentId = Integer.parseInt(schAppointmentIdField.getText());

        int patientId = -1;
        try {
            patientId = Integer.parseInt(schPatientIdField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Input Tidak Valid", null, "ID Pasien harus berupa angka.");
            return;
        }
        Patient patient = patientManager.findPatientById(patientId);
        if (patient == null) {
            showAlert(AlertType.ERROR, "Gagal", null, "Pasien dengan ID " + patientId + " tidak ditemukan.");
            return;
        }

        int doctorId = -1;
        try {
            doctorId = Integer.parseInt(schDoctorIdField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Input Tidak Valid", null, "ID Dokter harus berupa angka.");
            return;
        }
        Doctor doctor = doctorManager.findDoctorById(doctorId);
        if (doctor == null) {
            showAlert(AlertType.ERROR, "Gagal", null, "Dokter dengan ID " + doctorId + " tidak ditemukan.");
            return;
        }

        String dateString = schDateField.getText().trim();
        String timeString = schTimeField.getText().trim();

        if (dateString.isEmpty() || timeString.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Tidak Lengkap", null, "Tanggal dan waktu janji temu harus diisi (pilih slot jadwal).");
            return;
        }

        try {
            LocalDateTime appointmentTime = LocalDateTime.parse(dateString + " " + timeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            boolean isInSchedule = false;
            ScheduleSlot bookedSlot = null;
            for (ScheduleSlot slot : doctor.getSchedule()) {
                if (appointmentTime.isEqual(slot.getStartTime()) && slot.isAvailable()) {
                    isInSchedule = true;
                    bookedSlot = slot;
                    break;
                }
            }

            if (!isInSchedule) {
                showAlert(AlertType.ERROR, "Validasi Jadwal", null, "Waktu janji temu tidak valid atau slot tidak tersedia. Pilih waktu yang tepat dari jadwal dokter.");
                return;
            }

            Appointment newAppointment = new Appointment(appointmentId, patientId, doctorId, appointmentTime);

            appointmentManager.scheduleAppointment(newAppointment);

            if (appointmentManager.isAppointmentIdExists(newAppointment.getAppointmentId())) {
                if (bookedSlot != null) {
                    bookedSlot.setAvailable(false);
                    doctorManager.saveDoctorsToFile();
                }
                showAlert(AlertType.INFORMATION, "Sukses", null, "Janji temu berhasil dijadwalkan.");
                appendOutput("Janji temu berhasil dijadwalkan: " + newAppointment.toString());
            } else {
                showAlert(AlertType.ERROR, "Gagal", null, "Penjadwalan janji temu gagal. Cek output log untuk detail.");
                appendOutput("Penjadwalan janji temu gagal.");
            }

            schAppointmentIdField.setText(String.valueOf(appointmentManager.getNextId()));
            schPatientIdField.clear();
            schIllnessField.clear();
            initialDoctorIdField.clear();
            schDoctorIdField.clear();
            schDateField.clear();
            schTimeField.clear();
            doctorScheduleDisplayBox.getChildren().clear();
            selectedScheduleSlotField.clear();

        } catch (DateTimeParseException e) {
            showAlert(AlertType.ERROR, "Format Tanggal/Waktu Tidak Valid", null, "Format tanggal atau waktu tidak valid. Harap gunakan McDermott-MM-DD dan HH:MM.");
        }
    }


    @FXML
    private void showProcessNextAppointmentView() {
        VBox view = new VBox(15);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.TOP_CENTER);
        view.getStyleClass().add("form-container");

        Label title = new Label("Process Next Appointment");
        title.getStyleClass().add("content-title");

        Appointment nextAppointment = appointmentManager.peekNextAppointment();

        TextArea processInfoArea = new TextArea();
        processInfoArea.setEditable(false);
        processInfoArea.getStyleClass().add("text-area");
        processInfoArea.setPrefHeight(300);
        currentOutputArea = processInfoArea;

        // Tampilkan info janji temu berikutnya
        if (nextAppointment != null) {
            StringBuilder infoSb = new StringBuilder();
            infoSb.append("Janji Temu Berikutnya di Antrean:\n");
            infoSb.append(nextAppointment.toString()).append("\n");

            Doctor associatedDoctor = doctorManager.findDoctorById(nextAppointment.getDoctorId());
            infoSb.append("Dokter Terkait: ");
            if (associatedDoctor != null) {
                infoSb.append(associatedDoctor.getName()).append(" (ID: ").append(associatedDoctor.getId()).append(", Spesialisasi: ").append(associatedDoctor.getSpecialization()).append(")\n");
            } else {
                infoSb.append("Detail dokter tidak ditemukan (ID: ").append(nextAppointment.getDoctorId()).append(")\n");
            }
            processInfoArea.setText(infoSb.toString());

            // Tampilkan daftar dokter yang sedang login
            processInfoArea.appendText("\n--- Dokter yang Sedang Login ---\n");
            MyLinkedList<Doctor> loggedInDoctors = doctorManager.getLoggedInDoctorsList();
            if (loggedInDoctors.isEmpty()) {
                processInfoArea.appendText("Tidak ada dokter yang sedang login.");
            } else {
                int count = 1;
                for (Doctor d : loggedInDoctors) {
                    processInfoArea.appendText(count++ + ". ID: " + d.getId() + " | Nama: " + d.getName() + "\n");
                }
            }
            processInfoArea.appendText("--------------------------------\n");

        } else {
            processInfoArea.setText("Tidak ada janji temu dalam antrean untuk diproses.");
        }

        // Field dan Tombol untuk memilih dokter pemroses
        Label chooseDoctorLabel = new Label("Masukkan ID dokter yang akan memproses janji temu ini:");
        TextField doctorIdToProcessField = new TextField();
        doctorIdToProcessField.setPromptText("Doctor ID");

        Button processButton = new Button("Process Appointment");
        processButton.getStyleClass().add("form-button");
        processButton.setOnAction(event -> {
            if (currentLoggedInDoctor == null) {
                showAlert(AlertType.WARNING, "Akses Ditolak", null, "Hanya dokter yang sedang login yang dapat memproses janji temu.");
                appendOutput("Proses janji temu gagal: Dokter tidak login.");
                return;
            }

            String idStr = doctorIdToProcessField.getText().trim();
            if (idStr.isEmpty()) {
                showAlert(AlertType.WARNING, "Input Kosong", null, "Masukkan ID dokter yang akan memproses.");
                return;
            }

            try {
                int processingDoctorId = Integer.parseInt(idStr);

                // Validasi: Dokter yang dimasukkan harus sama dengan dokter yang login saat ini
                if (currentLoggedInDoctor.getId() != processingDoctorId) {
                    showAlert(AlertType.WARNING, "Akses Ditolak", null, "Anda harus memproses sebagai Dokter ID " + currentLoggedInDoctor.getId() + ".");
                    appendOutput("Proses janji temu gagal: ID dokter tidak cocok dengan yang login.");
                    return;
                }

                Appointment appointmentToProcess = appointmentManager.peekNextAppointment();
                if (appointmentToProcess == null) {
                    showAlert(AlertType.INFORMATION, "Antrean Kosong", null, "Tidak ada janji temu dalam antrean untuk diproses.");
                    appendOutput("Tidak ada janji temu yang dapat diproses.");
                    return;
                }

                // Validasi: ID dokter yang login harus sesuai dengan ID dokter di janji temu
                if (processingDoctorId != appointmentToProcess.getDoctorId()) {
                    showAlert(AlertType.WARNING, "Akses Ditolak", null, "Janji temu ini adalah untuk Dokter ID " + appointmentToProcess.getDoctorId() + ". Anda (Dokter ID " + processingDoctorId + ") tidak berwenang memprosesnya.");
                    appendOutput("Proses janji temu gagal: Dokter tidak terkait dengan janji temu ini.");
                    return;
                }

                // Jika semua validasi lolos, lanjutkan proses janji temu
                Appointment processedAppointment = appointmentManager.processNextAppointment(); // Ini akan dequeue
                if (processedAppointment != null) {
                    showAlert(AlertType.INFORMATION, "Sukses", null, "Janji temu berhasil diproses: " + processedAppointment.getAppointmentId());
                    appendOutput("Janji temu diproses: " + processedAppointment.toString());
                } else {
                    showAlert(AlertType.ERROR, "Gagal", null, "Terjadi kesalahan saat memproses janji temu.");
                    appendOutput("Terjadi kesalahan yang tidak terduga saat memproses janji temu.");
                }
                showProcessNextAppointmentView(); // Muat ulang tampilan untuk memperbarui antrean
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Input Tidak Valid", null, "ID Dokter harus berupa angka.");
            }
            doctorIdToProcessField.clear();
        });

        view.getChildren().addAll(title, processInfoArea, chooseDoctorLabel, doctorIdToProcessField, processButton);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(view);
    }

    @FXML
    private void showViewUpcomingAppointmentsView() {
        VBox displayView = new VBox(10);
        displayView.setPadding(new Insets(20));
        displayView.setAlignment(Pos.TOP_CENTER);
        displayView.getStyleClass().add("form-container");

        Label title = new Label("Upcoming Appointments");
        title.getStyleClass().add("content-title");

        TextArea appointmentListArea = new TextArea();
        appointmentListArea.setEditable(false);
        appointmentListArea.getStyleClass().add("text-area");
        appointmentListArea.setPrefHeight(400);

        StringBuilder sb = new StringBuilder();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        appointmentManager.viewUpcomingAppointments();

        System.out.flush();
        System.setOut(old);
        sb.append(baos.toString());

        if (sb.length() == 0 || sb.toString().trim().equals("Tidak ada janji temu mendatang.")) {
            sb.setLength(0);
            sb.append("Tidak ada janji temu mendatang.");
        }
        appointmentListArea.setText(sb.toString());


        currentOutputArea = appointmentListArea;

        displayView.getChildren().addAll(title, appointmentListArea);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(displayView);
    }

    @FXML
    private void showSearchPatientByIdBSTView() {
        VBox formView = new VBox(15);
        formView.setPadding(new Insets(20));
        formView.setAlignment(Pos.TOP_CENTER);
        formView.getStyleClass().add("form-container");

        Label title = new Label("Search Patient by ID (BST)");
        title.getStyleClass().add("content-title");

        GridPane formGrid = new GridPane();
        formGrid.getStyleClass().add("form-grid");
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        searchPatientByIdBSTField = new TextField();
        searchPatientByIdBSTField.setPromptText("Patient ID");
        formGrid.add(new Label("Patient ID:"), 0, 0);
        formGrid.add(searchPatientByIdBSTField, 1, 0);

        Button submitButton = new Button("Search Patient");
        submitButton.getStyleClass().add("form-button");
        submitButton.setOnAction(event -> {
            try {
                int patientIdToSearch = Integer.parseInt(searchPatientByIdBSTField.getText().trim());
                Patient foundPatient = patientTreeManager.searchPatientById(patientIdToSearch);
                if (foundPatient != null) {
                    appendOutput("Pasien ditemukan di BST: " + foundPatient.toString());
                    showAlert(AlertType.INFORMATION, "Sukses", null, "Pasien ditemukan: " + foundPatient.getName());
                } else {
                    appendOutput("Pasien dengan ID " + patientIdToSearch + " tidak ditemukan di BST.");
                    showAlert(AlertType.INFORMATION, "Tidak Ditemukan", null, "Pasien dengan ID " + patientIdToSearch + " tidak ditemukan.");
                }
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Input Tidak Valid", null, "ID harus berupa angka.");
            }
            searchPatientByIdBSTField.clear();
        });

        TextArea formOutputArea = new TextArea();
        formOutputArea.setEditable(false);
        formOutputArea.getStyleClass().add("text-area");
        formOutputArea.setPrefHeight(100);
        currentOutputArea = formOutputArea;

        formView.getChildren().addAll(title, formGrid, submitButton, formOutputArea);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(formView);
    }

    @FXML
    private void showDisplayAllPatientsBSTView() {
        VBox displayView = new VBox(10);
        displayView.setPadding(new Insets(20));
        displayView.setAlignment(Pos.TOP_CENTER);
        displayView.getStyleClass().add("form-container");

        Label title = new Label("All Patients (BST Inorder)");
        title.getStyleClass().add("content-title");

        TextArea bstOutputArea = new TextArea();
        bstOutputArea.setEditable(false);
        bstOutputArea.getStyleClass().add("text-area");
        bstOutputArea.setPrefHeight(400);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        patientTreeManager.inOrderDisplay();

        System.out.flush();
        System.setOut(old);

        bstOutputArea.setText(baos.toString());

        currentOutputArea = bstOutputArea;

        displayView.getChildren().addAll(title, bstOutputArea);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(displayView);
    }

    @FXML
    private void handleExit() {
        showAlert(AlertType.INFORMATION, "Keluar", null, "Terima kasih telah menggunakan sistem Daisuke Clinic. Sampai jumpa!");
        saveAllManagerDataOnExit();
        javafx.application.Platform.exit();
    }


    // --- Catatan Medis (Medical Records) ---
    @FXML
    private void showAddMedicalRecordView() {
        VBox formView = new VBox(15);
        formView.setPadding(new Insets(20));
        formView.setAlignment(Pos.TOP_CENTER);
        formView.getStyleClass().add("form-container");

        Label title = new Label("Add New Medical Record");
        title.getStyleClass().add("content-title");

        GridPane formGrid = new GridPane();
        formGrid.getStyleClass().add("form-grid");
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        mrRecordIdField = new TextField();
        mrRecordIdField.setPromptText("Auto-generated");
        mrRecordIdField.setEditable(false);
        mrRecordIdField.setText(String.valueOf(medicalRecordManager.getNextRecordId()));

        mrPatientIdField = new TextField();
        mrPatientIdField.setPromptText("Patient ID");

        mrDoctorIdField = new TextField();
        mrDoctorIdField.setPromptText("Doctor ID (Current: " + (currentLoggedInDoctor != null ? currentLoggedInDoctor.getId() : "N/A") + ")");
        if (currentLoggedInDoctor != null) {
            mrDoctorIdField.setText(String.valueOf(currentLoggedInDoctor.getId()));
            mrDoctorIdField.setEditable(false);
        } else {
            mrDoctorIdField.setEditable(true);
        }

        mrComplaintField = new TextField();
        mrComplaintField.setPromptText("Patient Complaint");

        mrDiagnosisField = new TextField();
        mrDiagnosisField.setPromptText("Diagnosis");

        mrMedicationField = new TextField();
        mrMedicationField.setPromptText("Medication Prescribed");

        formGrid.add(new Label("Record ID:"), 0, 0);
        formGrid.add(mrRecordIdField, 1, 0);
        formGrid.add(new Label("Patient ID:"), 0, 1);
        formGrid.add(mrPatientIdField, 1, 1);
        formGrid.add(new Label("Doctor ID:"), 0, 2);
        formGrid.add(mrDoctorIdField, 1, 2);
        formGrid.add(new Label("Complaint:"), 0, 5);
        formGrid.add(mrComplaintField, 1, 5);
        formGrid.add(new Label("Diagnosis:"), 0, 6);
        formGrid.add(mrDiagnosisField, 1, 6);
        formGrid.add(new Label("Medication:"), 0, 7);
        formGrid.add(mrMedicationField, 1, 7);

        Button submitButton = new Button("Add Record");
        submitButton.getStyleClass().add("form-button");
        submitButton.setOnAction(event -> handleAddMedicalRecordSubmit());

        TextArea formOutputArea = new TextArea();
        formOutputArea.setEditable(false);
        formOutputArea.getStyleClass().add("text-area");
        formOutputArea.setPrefHeight(100);
        currentOutputArea = formOutputArea;

        formView.getChildren().addAll(title, formGrid, submitButton, formOutputArea);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(formView);
    }

    private void handleAddMedicalRecordSubmit() {
        int recordId = Integer.parseInt(mrRecordIdField.getText());
        int patientId = -1;
        try {
            patientId = Integer.parseInt(mrPatientIdField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Input Tidak Valid", null, "ID Pasien harus berupa angka.");
            return;
        }
        Patient patient = patientManager.findPatientById(patientId);
        if (patient == null) {
            showAlert(AlertType.ERROR, "Gagal", null, "Pasien dengan ID " + patientId + " tidak ditemukan.");
            return;
        }

        int doctorId = -1;
        try {
            doctorId = Integer.parseInt(mrDoctorIdField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Input Tidak Valid", null, "ID Dokter harus berupa angka.");
            return;
        }
        Doctor doctor = doctorManager.findDoctorById(doctorId);
        if (doctor == null) {
            showAlert(AlertType.ERROR, "Gagal", null, "Dokter dengan ID " + doctorId + " tidak ditemukan dalam catatan dokter.");
            return;
        }
        if (currentLoggedInDoctor != null && currentLoggedInDoctor.getId() != doctorId) {
            showAlert(AlertType.ERROR, "Invalid Doctor ID", null, "ID Dokter harus sesuai dengan dokter yang sedang login.");
            return;
        } else if (currentLoggedInDoctor == null) {
            showAlert(AlertType.WARNING, "Akses Ditolak", null, "Hanya dokter yang sedang login yang dapat menambahkan catatan medis.");
            return;
        }

        // PERBAIKAN: Ambil waktu saat ini
        LocalDateTime recordTime = LocalDateTime.now();

        String complaint = mrComplaintField.getText().trim();
        String diagnosis = mrDiagnosisField.getText().trim();
        String medication = mrMedicationField.getText().trim();

        if (complaint.isEmpty() || diagnosis.isEmpty() || medication.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Tidak Lengkap", null, "Keluhan, Diagnosa, dan Obat harus diisi.");
            return;
        }

        MedicalRecord newRecord = new MedicalRecord(recordId, patientId, doctorId, recordTime, complaint, diagnosis, medication);

        medicalRecordManager.addMedicalRecord(newRecord);
        if (medicalRecordManager.isRecordIdExists(newRecord.getRecordId())) {
            showAlert(AlertType.INFORMATION, "Sukses", null, "Catatan medis berhasil ditambahkan.");
            appendOutput("Catatan medis ditambahkan: " + newRecord.toString());
        } else {
            showAlert(AlertType.ERROR, "Gagal", null, "Penambahan catatan medis gagal. ID mungkin sudah ada.");
            appendOutput("Penambahan catatan medis gagal.");
        }

        // Bersihkan form dan update ID berikutnya
        mrRecordIdField.setText(String.valueOf(medicalRecordManager.getNextRecordId()));
        mrPatientIdField.clear();
        mrComplaintField.clear();
        mrDiagnosisField.clear();
        mrMedicationField.clear();
    }

    @FXML
    private void showViewPatientMedicalHistoryView() {
        VBox formView = new VBox(15);
        formView.setPadding(new Insets(20));
        formView.setAlignment(Pos.TOP_CENTER);
        formView.getStyleClass().add("form-container");

        Label title = new Label("View Patient Medical History");
        title.getStyleClass().add("content-title");

        GridPane formGrid = new GridPane();
        formGrid.getStyleClass().add("form-grid");
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        viewMrPatientIdField = new TextField();
        viewMrPatientIdField.setPromptText("Patient ID");
        formGrid.add(new Label("Patient ID:"), 0, 0);
        formGrid.add(viewMrPatientIdField, 1, 0);

        Button submitButton = new Button("View History");
        submitButton.getStyleClass().add("form-button");
        submitButton.setOnAction(event -> {
            try {
                int patientId = Integer.parseInt(viewMrPatientIdField.getText().trim());
                MyLinkedList<MedicalRecord> patientRecords = medicalRecordManager.getRecordsByPatientId(patientId);
                StringBuilder sb = new StringBuilder();
                if (patientRecords.isEmpty()) {
                    sb.append("Tidak ada riwayat medis untuk pasien ID ").append(patientId).append(".");
                } else {
                    sb.append("--- Riwayat Medis Pasien ID ").append(patientId).append(" ---\n");
                    for (MedicalRecord record : patientRecords) {
                        sb.append(record.toString()).append("\n");
                    }
                    sb.append("--------------------------------------\n");
                }
                appendOutput(sb.toString());
                showAlert(AlertType.INFORMATION, "Riwayat Medis", null, "Riwayat medis ditampilkan di area output.");
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Input Tidak Valid", null, "ID Pasien harus berupa angka.");
            }
            viewMrPatientIdField.clear();
        });

        TextArea formOutputArea = new TextArea();
        formOutputArea.setEditable(false);
        formOutputArea.getStyleClass().add("text-area");
        formOutputArea.setPrefHeight(300);
        currentOutputArea = formOutputArea;

        formView.getChildren().addAll(title, formGrid, submitButton, formOutputArea);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(formView);
    }

    @FXML
    private void showDisplayAllMedicalRecordsView() {
        VBox displayView = new VBox(10);
        displayView.setPadding(new Insets(20));
        displayView.setAlignment(Pos.TOP_CENTER);
        displayView.getStyleClass().add("form-container");

        Label title = new Label("All Medical Records");
        title.getStyleClass().add("content-title");

        TextArea recordListArea = new TextArea();
        recordListArea.setEditable(false);
        recordListArea.getStyleClass().add("text-area");
        recordListArea.setPrefHeight(400);

        MyLinkedList<MedicalRecord> allRecords = medicalRecordManager.getAllMedicalRecords();
        StringBuilder sb = new StringBuilder();
        if (allRecords.isEmpty()) {
            sb.append("Tidak ada catatan medis dalam sistem.");
        } else {
            sb.append("--- Daftar Semua Catatan Medis ---\n");
            for (MedicalRecord record : allRecords) {
                sb.append(record.toString()).append("\n");
            }
            sb.append("------------------------------------\n");
        }
        recordListArea.setText(sb.toString());

        currentOutputArea = recordListArea;

        displayView.getChildren().addAll(title, recordListArea);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(displayView);
    }
}