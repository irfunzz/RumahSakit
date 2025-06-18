# <Daisuke Klinik>  
_Aplikasi Manajemen <Rumah Sakit> Berbasis JavaFX_

---

## 👥 Anggota Kelompok
| Nama Lengkap            | NIM      |
| ----------------------- | -------- |
| Muhammad Irfan          | L0124063 |
| Muhammad Rafi Al-Farrel | L0124110 |
| Queen Nika Prahara M.P. | L0124115 |
| Rafif Adyatma Setyawan  | L0124117 |

---

## 📌 Deskripsi Singkat

Proyek ini merupakan aplikasi berbasis **JavaFX** yang dikembangkan sebagai tugas mata kuliah **Struktur Data & Algoritma**. Aplikasi ini bertujuan untuk membantu pasien & dokter dalam mengelola fitur utama seperti data, jadwal, dll secara lebih efisien dengan tampilan antarmuka yang interaktif.

---

## ⚙️ Fitur Utama

- ✅ Menambahkan dan mengelola data (contoh: dokter, pasien)
- ✅ Menjadwalkan janji temu atau kegiatan
- ✅ Menampilkan daftar data secara dinamis
- ✅ Navigasi antar menu menggunakan Scene JavaFX
- ✅ Tampilan UI disusun dengan FXML + CSS

---

## 💻 Cara Menjalankan Aplikasi

1. Buka aplikasi **IntelliJ IDEA**
2. Pilih `Open` dan arahkan ke folder proyek ini (`RumahS`)
3. Pastikan Java SDK **versi 17 atau 21** telah terpasang
4. Tambahkan **JavaFX SDK versi 21** (download dari https://openjfx.io)
5. Tambahkan konfigurasi VM Options berikut ke Run/Debug Configuration: (--module-path "<path ke javafx-sdk-21>/lib" --add-modules javafx.controls,javafx.fxml)
6. Jalankan file `Main.java` dari package `com.irfan.DaisukeClinic`

---

## 🧱 Struktur Proyek (Singkat)
```
RUMAHS/
├── .idea/                               # Konfigurasi IntelliJ IDEA
├── .mvn/wrapper/                        # Maven Wrapper
│   ├── maven-wrapper.jar
│   └── maven-wrapper.properties
├── src/
│   └── main/
│       ├── java/
│       │   └── com/irfan/DaisukeClinic/
│       │       ├── controller/          # Kontrol utama aplikasi (MainController)
│       │       ├── model/
│       │       │   ├── core/            # Manajemen data utama (akun, dokter, pasien, dll.)
│       │       │   └── structures/      # Struktur data khusus (Map, BST, LinkedList, dsb.)
│       │       ├── utils/               # Utilitas utama aplikasi (Main.java)
│       │       └── module-info.java     # Modul Java (JPMS)
│       └── resources/
│           ├── assets/                  # Berkas-berkas data dan media
│           │   ├── *.txt                # Data akun, pasien, dokter, rekam medis, dll.
│           │   ├── *.png                # Ikon aplikasi
│           ├── com/irfan/DaisukeClinic/
│           │   └── MainView.fxml        # Tampilan utama berbasis JavaFX FXML
│           ├── messsges.properties      # File properties bahasa
│           └── style.css                # Styling untuk FXML (JavaFX)
├── target/                              # Output build Maven
│   └── classes/                         # Kode yang sudah dikompilasi
│       ├── assets/                      # Salinan dari `resources/assets`
│       └── com/irfan/DaisukeClinic/
│           ├── controller/              # File class hasil kompilasi
│           ├── model/core/
│           └── model/structures/
├── pom.xml                              # File konfigurasi Maven
├── mvnw, mvnw.cmd                       # Skrip untuk menjalankan Maven wrapper
├── carajalan.txt                        # Petunjuk manual penggunaan (opsional)
├── patients.dat                         # Data biner pasien
└── README.md                            # Dokumentasi proyek ini
```
---

## 🛠️ Tools dan Teknologi

- Java SDK 17 / 21
- JavaFX SDK 21
- IntelliJ IDEA
- Maven (untuk manajemen dependensi)
- SceneBuilder (jika digunakan)

---

## 🖥️ Fitur Aplikasi Saat Dijalankan
Ketika aplikasi Daisuke Clinic dijalankan, pengguna akan disuguhkan antarmuka grafis (GUI) yang bersih dan interaktif. Berikut adalah beberapa fitur utama yang tersedia:
 1. 🏠 Tampilan Beranda (Home View)
  - Menampilkan nama aplikasi dan identitas klinik.
  - Bisa menampilkan informasi umum, seperti jam operasional atau sambutan.
  - Terdapat tombol navigasi ke halaman lain, seperti jadwal dokter.
 2. 📅 Jadwal Dokter (Schedule View)
  - Menampilkan jadwal praktik dokter dalam bentuk tabel atau daftar.
  - Pengguna dapat melihat:
    - Nama dokter
    - Spesialisasi
    - Hari dan jam praktik
  - Tampilan ini diambil dari file .fxml (schedule.fxml) yang dirancang secara visual
 3. 🎨 Desain Antarmuka yang Konsisten
  - Aplikasi menggunakan style.css untuk menyamakan warna, ukuran huruf, dan gaya komponen seperti tombol dan label.
  - Hal ini memberi kesan profesional dan nyaman bagi pengguna.
 4. 🔄 Navigasi Antar Halaman
  - Navigasi antar FXML didukung oleh controller yang memungkinkan perpindahan dari halaman home ke jadwal, atau sebaliknya.
  - Biasanya ini ditangani oleh tombol dalam file FXML yang dihubungkan ke event handler di dalam package controller.

---

## 📎 Catatan Tambahan

- Pastikan JavaFX sudah dikonfigurasi dengan benar sebelum menjalankan aplikasi
- Jika menggunakan SceneBuilder, pastikan path FXML sesuai dengan struktur `resources/view`
- Jalankan aplikasi melalui `Main.java` agar semua fungsi berjalan sebagaimana mestinya

---
