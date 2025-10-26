package homework_assignment_1_group_2_oops_owlz;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class LogFileInterface {
    private LogManager logManager;
    private Scanner scanner;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
    private static final Pattern EQUIPMENT_ID_PATTERN = Pattern.compile("[A-Z0-9_]+");
    private static final Pattern LOG_TYPE_PATTERN = Pattern.compile("(vehicle|station|system|all)");

    public LogFileInterface() {
        this.logManager = new LogManager();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Storage and Task Management System ===");
        System.out.println("Log File Management Interface");
        System.out.println("==========================================");

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getMenuChoice();

            switch (choice) {
                case 1:
                    openLogByEquipment();
                    break;
                case 2:
                    openLogByDate();
                    break;
                case 3:
                    listAllLogFiles();
                    break;
                case 4:
                    manageLogFiles();
                    break;
                case 5:
                    demonstrateSystemFunctionality();
                    break;
                case 6:
                    running = false;
                    System.out.println("Exiting system...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Open log file by equipment name/ID");
        System.out.println("2. Open log file by date");
        System.out.println("3. List all log files");
        System.out.println("4. Manage log files (move, delete, archive)");
        System.out.println("5. Demonstrate system functionality");
        System.out.println("6. Exit");
        System.out.print("Enter your choice (1-6): ");
    }

    private int getMenuChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void openLogByEquipment() {
        System.out.println("\n=== Open Log by Equipment ===");
        System.out.print("Enter equipment name/ID: ");
        String equipmentName = scanner.nextLine().trim();

        if (!isValidEquipmentId(equipmentName)) {
            System.out.println("Invalid equipment ID format. Use only uppercase letters, numbers, and underscores.");
            return;
        }

        List<String> logEntries = logManager.readLogByEquipment(equipmentName);

        if (logEntries.isEmpty()) {
            System.out.println("No log entries found for equipment: " + equipmentName);
        } else {
            System.out.println("\nLog entries for equipment '" + equipmentName + "':");
            System.out.println("==========================================");
            for (String entry : logEntries) {
                System.out.println(entry);
            }
            System.out.println("Total entries found: " + logEntries.size());
        }
    }

    private void openLogByDate() {
        System.out.println("\n=== Open Log by Date ===");
        System.out.print("Enter date (YYYY-MM-DD): ");
        String dateInput = scanner.nextLine().trim();

        if (!isValidDate(dateInput)) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            return;
        }

        List<String> logEntries = logManager.readLogByDate(dateInput);

        if (logEntries.isEmpty()) {
            System.out.println("No log entries found for date: " + dateInput);
        } else {
            System.out.println("\nLog entries for date '" + dateInput + "':");
            System.out.println("==========================================");
            for (String entry : logEntries) {
                System.out.println(entry);
            }
            System.out.println("Total entries found: " + logEntries.size());
        }
    }

    private void listAllLogFiles() {
        System.out.println("\n=== All Log Files ===");
        List<LogManager.LogMetadata> allMetadata = logManager.getAllLogMetadata();

        if (allMetadata.isEmpty()) {
            System.out.println("No log files found.");
            return;
        }

        System.out.println("Log Files:");
        System.out.println("==========================================");
        for (LogManager.LogMetadata metadata : allMetadata) {
            System.out.println(metadata);
        }

        System.out.println("\nLog Files by Type:");
        System.out.println("==========================================");
        String[] types = { "vehicle", "station", "system" };
        for (String type : types) {
            List<LogManager.LogMetadata> typeMetadata = logManager.getLogMetadataByType(type);
            System.out.println(type.toUpperCase() + " logs (" + typeMetadata.size() + " files):");
            for (LogManager.LogMetadata metadata : typeMetadata) {
                System.out.println("  - " + metadata.getFileName() + " (" + metadata.getSize() + " bytes)");
            }
        }
    }

    private void manageLogFiles() {
        System.out.println("\n=== Log File Management ===");
        System.out.println("1. Move log file");
        System.out.println("2. Delete log file");
        System.out.println("3. Archive log file");
        System.out.println("4. Back to main menu");
        System.out.print("Enter your choice (1-4): ");

        int choice = getMenuChoice();

        switch (choice) {
            case 1:
                moveLogFile();
                break;
            case 2:
                deleteLogFile();
                break;
            case 3:
                archiveLogFile();
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void moveLogFile() {
        System.out.print("Enter log file name to move: ");
        String fileName = scanner.nextLine().trim();

        if (!logManager.isValidLogFileName(fileName)) {
            System.out.println("Invalid log file name format.");
            return;
        }

        System.out.print("Enter destination directory: ");
        String destination = scanner.nextLine().trim();

        boolean success = logManager.moveLogFile(fileName, destination);
        if (success) {
            System.out.println("Log file moved successfully.");
        } else {
            System.out.println("Failed to move log file.");
        }
    }

    private void deleteLogFile() {
        System.out.print("Enter log file name to delete: ");
        String fileName = scanner.nextLine().trim();

        if (!logManager.isValidLogFileName(fileName)) {
            System.out.println("Invalid log file name format.");
            return;
        }

        System.out.print("Are you sure you want to delete " + fileName + "? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if ("yes".equals(confirmation)) {
            boolean success = logManager.deleteLogFile(fileName);
            if (success) {
                System.out.println("Log file deleted successfully.");
            } else {
                System.out.println("Failed to delete log file.");
            }
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }

    private void archiveLogFile() {
        System.out.print("Enter log file name to archive: ");
        String fileName = scanner.nextLine().trim();

        if (!logManager.isValidLogFileName(fileName)) {
            System.out.println("Invalid log file name format.");
            return;
        }

        boolean success = logManager.archiveLogFile(fileName);
        if (success) {
            System.out.println("Log file archived successfully.");
        } else {
            System.out.println("Failed to archive log file.");
        }
    }

    private void demonstrateSystemFunctionality() {
        System.out.println("\n=== System Functionality Demonstration ===");

        StorageVehicle vehicle1 = new StorageVehicle("VH001", "Cargo Truck Alpha", 1000.0);
        StorageVehicle vehicle2 = new StorageVehicle("VH002", "Delivery Van Beta", 500.0);

        ChargingStation station1 = new ChargingStation("CS001", "Main Charging Hub", "Warehouse A", 10, 50.0);
        ChargingStation station2 = new ChargingStation("CS002", "Express Charging Point", "Dock B", 5, 30.0);

        TaskManager taskManager = new TaskManager();
        DataExchangeSimulator dataSimulator = new DataExchangeSimulator();

        System.out.println("\n1. Vehicle Operations:");
        vehicle1.loadCargo(300.0);
        vehicle1.loadCargo(200.0);
        vehicle1.setStatus("In Transit");
        vehicle1.unloadCargo(150.0);

        vehicle2.loadCargo(400.0);
        vehicle2.performMaintenance();

        System.out.println("\n1. Charging Station Operations:");
        station1.startCharging("VH001");
        station1.startCharging("VH002");
        station2.startCharging("VH003");
        station1.stopCharging("VH001");

        System.out.println("\n2. Task Management:");
        Task task1 = taskManager.createTask("Load Cargo", "Load 500kg of cargo into VH001",
                Task.TaskPriority.HIGH, "Operator1");
        Task task2 = taskManager.createTask("Maintenance Check", "Perform routine maintenance on CS001",
                Task.TaskPriority.MEDIUM, "Technician1");

        taskManager.startTask(task1.getTaskId());
        taskManager.completeTask(task1.getTaskId());
        taskManager.startTask(task2.getTaskId());

        System.out.println("\n3. Logging System:");
        logManager.logVehicleActivity("VH001", "Loaded 500kg cargo");
        logManager.logVehicleActivity("VH002", "Completed maintenance");
        logManager.logStationActivity("CS001", "Started charging VH001");
        logManager.logSystemActivity("System maintenance completed");

        System.out.println("\n4. Data Exchange Simulation:");
        dataSimulator.simulateVehicleStationDataExchange(vehicle1, station1);

        List<StorageVehicle> vehicles = new ArrayList<>();
        vehicles.add(vehicle1);
        vehicles.add(vehicle2);

        List<ChargingStation> stations = new ArrayList<>();
        stations.add(station1);
        stations.add(station2);
        dataSimulator.simulateConfigurationDataExchange(vehicles, stations);

        dataSimulator.demonstrateStreamTypes();

        List<String> logData = new ArrayList<>();
        logData.add("[2024-01-15 10:30:00] VEHICLE_VH001: Loaded 300.0 units of cargo");
        logData.add("[2024-01-15 10:35:00] STATION_CS001: Vehicle VH001 started charging");
        logData.add("[2024-01-15 11:00:00] SYSTEM: Daily maintenance completed");
        dataSimulator.simulateLogDataStreaming(logData);

        System.out.println("\n=== Demonstration Complete ===");
    }

    private boolean isValidDate(String date) {
        if (!DATE_PATTERN.matcher(date).matches()) {
            return false;
        }

        try {
            LocalDate.parse(date, DATE_FORMATTER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidEquipmentId(String equipmentId) {
        return EQUIPMENT_ID_PATTERN.matcher(equipmentId).matches();
    }

    @SuppressWarnings("unused")
    private boolean isValidLogType(String logType) {
        return LOG_TYPE_PATTERN.matcher(logType.toLowerCase()).matches();
    }
}