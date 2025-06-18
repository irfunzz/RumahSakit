package com.irfan.DaisukeClinic.model.structures;

import com.irfan.DaisukeClinic.model.Patient;

public interface Identifiable {
    int getId();
    String getName();

    int compareTo(Patient other);
}
