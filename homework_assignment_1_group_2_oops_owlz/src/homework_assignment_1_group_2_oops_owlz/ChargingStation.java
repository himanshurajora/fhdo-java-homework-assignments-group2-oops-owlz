package homework_assignment_1_group_2_oops_owlz;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChargingStation {
    private String stationId;
    private String stationName;
    private String location;
    private int totalSlots;
    private int occupiedSlots;
    private double powerOutput;
    private String status;
    private List<String> logEntries;

    public ChargingStation(String stationId, String stationName, String location, int totalSlots, double powerOutput) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.location = location;
        this.totalSlots = totalSlots;
        this.occupiedSlots = 0;
        this.powerOutput = powerOutput;
        this.status = "Operational";
        this.logEntries = new ArrayList<>();
    }

    public boolean startCharging(String vehicleId) {
        if (occupiedSlots < totalSlots && "Operational".equals(status)) {
            occupiedSlots++;
            logActivity("CHARGING_START", "Vehicle " + vehicleId + " started charging");
            return true;
        } else {
            logActivity("ERROR", "Cannot start charging for vehicle " + vehicleId + " - no available slots");
            return false;
        }
    }

    public void stopCharging(String vehicleId) {
        if (occupiedSlots > 0) {
            occupiedSlots--;
            logActivity("CHARGING_STOP", "Vehicle " + vehicleId + " stopped charging");
        } else {
            logActivity("ERROR", "No vehicles currently charging");
        }
    }

    public void setStatus(String status) {
        this.status = status;
        logActivity("STATUS_CHANGE", "Station status changed to: " + status);
    }

    public void performMaintenance() {
        logActivity("MAINTENANCE", "Station maintenance performed");
    }

    private void logActivity(String action, String description) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logEntry = String.format("[%s] %s: %s - Slots: %d/%d",
                timestamp, action, description, occupiedSlots, totalSlots);
        logEntries.add(logEntry);
    }

    public String getStationId() {
        return stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public String getLocation() {
        return location;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public int getOccupiedSlots() {
        return occupiedSlots;
    }

    public double getPowerOutput() {
        return powerOutput;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getLogEntries() {
        return new ArrayList<>(logEntries);
    }

    @Override
    public String toString() {
        return String.format("ChargingStation[ID: %s, Name: %s, Location: %s, Slots: %d/%d, Status: %s]",
                stationId, stationName, location, occupiedSlots, totalSlots, status);
    }
}