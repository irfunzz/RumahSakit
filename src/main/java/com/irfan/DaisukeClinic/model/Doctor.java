package com.irfan.DaisukeClinic.model;

import com.irfan.DaisukeClinic.model.structures.MyLinkedList;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Doctor implements Serializable {
    private int id;
    private String name;
    private String specialization;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private MyLinkedList<ScheduleSlot> schedule;

    public Doctor(int id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.loginTime = null;
        this.logoutTime = null;
        this.schedule = new MyLinkedList<>();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public LocalDateTime getLoginTime() { return loginTime; }
    public LocalDateTime getLogoutTime() { return logoutTime; }
    public MyLinkedList<ScheduleSlot> getSchedule() { return schedule; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public void setLoginTime(LocalDateTime loginTime) { this.loginTime = loginTime; }
    public void setLogoutTime(LocalDateTime logoutTime) { this.logoutTime = logoutTime; }
    public void setSchedule(MyLinkedList<ScheduleSlot> schedule) { this.schedule = schedule; }


    public void addScheduleSlot(ScheduleSlot slot) {
        this.schedule.add(slot);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String loginTimeStr = (loginTime != null) ? loginTime.format(formatter) : "N/A";
        String logoutTimeStr = (logoutTime != null) ? logoutTime.format(formatter) : "N/A";

        StringBuilder sb = new StringBuilder();
        sb.append("id = ").append(id)
                .append("\n name = '").append(name).append('\'')
                .append("\n specialization = '").append(specialization).append('\'')
                .append("\n loginTime = ").append(loginTimeStr)
                .append("\n logoutTime = ").append(logoutTimeStr);

        if (!schedule.isEmpty()) {
            sb.append(", Schedule: [");
            boolean first = true;
            for (ScheduleSlot slot : schedule) {
                if (!first) sb.append(", ");
                sb.append(slot.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"))).append("-").append(slot.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                first = false;
            }
            sb.append("]");
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Doctor doctor = (Doctor) obj;
        return id == doctor.id;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }
}