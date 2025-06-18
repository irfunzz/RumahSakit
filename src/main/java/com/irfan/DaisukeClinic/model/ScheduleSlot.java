package com.irfan.DaisukeClinic.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScheduleSlot implements Serializable {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isAvailable;

    public ScheduleSlot(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = true;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    // Metode helper untuk mengecek apakah waktu tertentu berada dalam slot ini
    public boolean contains(LocalDateTime time) {
        return (time.isAfter(startTime) || time.isEqual(startTime)) && time.isBefore(endTime);
    }

    // Metode helper untuk mengecek apakah dua slot tumpang tindih
    public boolean overlapsWith(ScheduleSlot other) {
        return (this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime));
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return startTime.format(formatter) + " - " + endTime.format(formatter) + (isAvailable ? " (Available)" : " (Booked)");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleSlot that = (ScheduleSlot) o;
        return startTime.isEqual(that.startTime) && endTime.isEqual(that.endTime);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(startTime, endTime);
    }
}