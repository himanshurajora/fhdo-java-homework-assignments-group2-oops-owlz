package homework_assignment_1_group_2_oops_owlz;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StorageVehicle {
    private String vehicleId;
    private String vehicleName;
    private double capacity;
    private double currentLoad;
    private String status;
    private LocalDateTime lastMaintenance;
    private List<String> logEntries;

    public StorageVehicle(String vehicleId, String vehicleName, double capacity) {
        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
        this.capacity = capacity;
        this.currentLoad = 0.0;
        this.status = "Available";
        this.lastMaintenance = LocalDateTime.now();
        this.logEntries = new ArrayList<>();
    }

    public void loadCargo(double amount) {
        if (currentLoad + amount <= capacity) {
            currentLoad += amount;
            logActivity("LOAD", "Loaded " + amount + " units of cargo");
        } else {
            logActivity("ERROR", "Cannot load " + amount + " units - exceeds capacity");
        }
    }

    public void unloadCargo(double amount) {
        if (currentLoad >= amount) {
            currentLoad -= amount;
            logActivity("UNLOAD", "Unloaded " + amount + " units of cargo");
        } else {
            logActivity("ERROR", "Cannot unload " + amount + " units - insufficient cargo");
        }
    }

    public void setStatus(String status) {
        this.status = status;
        logActivity("STATUS_CHANGE", "Status changed to: " + status);
    }

    public void performMaintenance() {
        this.lastMaintenance = LocalDateTime.now();
        logActivity("MAINTENANCE", "Maintenance performed");
    }

    private void logActivity(String action, String description) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logEntry = String.format("[%s] %s: %s - Load: %.2f/%.2f",
                timestamp, action, description, currentLoad, capacity);
        logEntries.add(logEntry);
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public double getCapacity() {
        return capacity;
    }

    public double getCurrentLoad() {
        return currentLoad;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getLastMaintenance() {
        return lastMaintenance;
    }

    public List<String> getLogEntries() {
        return new ArrayList<>(logEntries);
    }

    @Override
    public String toString() {
        return String.format("Vehicle[ID: %s, Name: %s, Load: %.2f/%.2f, Status: %s]",
                vehicleId, vehicleName, currentLoad, capacity, status);
    }
}