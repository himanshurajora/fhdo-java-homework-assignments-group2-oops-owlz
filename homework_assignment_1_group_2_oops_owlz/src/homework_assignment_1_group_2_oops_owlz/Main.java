package homework_assignment_1_group_2_oops_owlz;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		System.out.println("=== Storage and Task Management System ===");
		System.out.println("Group 2 - OOPS OWLZ");
		System.out.println("==========================================");

		demonstrateSystemFeatures();

		System.out.println("\nStarting interactive log file management interface...");
		LogFileInterface logInterface = new LogFileInterface();
		logInterface.start();
	}

	private static void demonstrateSystemFeatures() {
		System.out.println("\n=== System Features Demonstration ===");

		LogManager logManager = new LogManager();
		TaskManager taskManager = new TaskManager();
		DataExchangeSimulator dataSimulator = new DataExchangeSimulator();

		System.out.println("\n1. Creating Storage Vehicles:");
		StorageVehicle vehicle1 = new StorageVehicle("VH001", "Cargo Truck Alpha", 1000.0);
		StorageVehicle vehicle2 = new StorageVehicle("VH002", "Delivery Van Beta", 500.0);
		StorageVehicle vehicle3 = new StorageVehicle("VH003", "Heavy Loader Gamma", 2000.0);

		System.out.println(vehicle1);
		System.out.println(vehicle2);
		System.out.println(vehicle3);

		System.out.println("\n2. Creating Charging Stations:");
		ChargingStation station1 = new ChargingStation("CS001", "Main Charging Hub", "Warehouse A", 10, 50.0);
		ChargingStation station2 = new ChargingStation("CS002", "Express Charging Point", "Dock B", 5, 30.0);
		ChargingStation station3 = new ChargingStation("CS003", "Emergency Charging Station", "Garage C", 3, 75.0);

		System.out.println(station1);
		System.out.println(station2);
		System.out.println(station3);

		System.out.println("\n3. Vehicle Operations with Logging:");
		vehicle1.loadCargo(300.0);
		logManager.logVehicleActivity("VH001", "Loaded 300kg cargo");

		vehicle1.loadCargo(200.0);
		logManager.logVehicleActivity("VH001", "Loaded additional 200kg cargo");

		vehicle1.setStatus("In Transit");
		logManager.logVehicleActivity("VH001", "Status changed to In Transit");

		vehicle2.loadCargo(400.0);
		logManager.logVehicleActivity("VH002", "Loaded 400kg cargo");

		vehicle3.performMaintenance();
		logManager.logVehicleActivity("VH003", "Maintenance completed");

		System.out.println("\n4. Charging Station Operations with Logging:");
		station1.startCharging("VH001");
		logManager.logStationActivity("CS001", "Started charging VH001");

		station1.startCharging("VH002");
		logManager.logStationActivity("CS001", "Started charging VH002");

		station2.startCharging("VH003");
		logManager.logStationActivity("CS002", "Started charging VH003");

		station1.stopCharging("VH001");
		logManager.logStationActivity("CS001", "Stopped charging VH001");

		System.out.println("\n5. Task Management with Logging:");
		Task task1 = taskManager.createTask("Load Cargo", "Load 500kg of cargo into VH001",
				Task.TaskPriority.HIGH, "Operator1");
		Task task2 = taskManager.createTask("Maintenance Check", "Perform routine maintenance on CS001",
				Task.TaskPriority.MEDIUM, "Technician1");
		Task task3 = taskManager.createTask("System Backup", "Create system backup",
				Task.TaskPriority.LOW, "Admin1");
		logManager.logSystemActivity("Task created: System Backup assigned to Admin1");

		logManager.logSystemActivity("Task created: Load Cargo assigned to Operator1");

		taskManager.startTask(task1.getTaskId());
		logManager.logSystemActivity("Task started: Load Cargo");

		taskManager.completeTask(task1.getTaskId());
		logManager.logSystemActivity("Task completed: Load Cargo");

		taskManager.startTask(task2.getTaskId());
		logManager.logSystemActivity("Task started: Maintenance Check");

		System.out.println("\n6. Data Exchange Simulation (Byte & Character Streams):");
		dataSimulator.simulateVehicleStationDataExchange(vehicle1, station1);

		List<StorageVehicle> vehicles = Arrays.asList(vehicle1, vehicle2, vehicle3);
		List<ChargingStation> stations = Arrays.asList(station1, station2, station3);
		dataSimulator.simulateConfigurationDataExchange(vehicles, stations);

		List<String> sampleLogs = Arrays.asList(
				"[2024-01-15 10:30:00] VEHICLE_VH001: Loaded 300.0 units of cargo",
				"[2024-01-15 10:35:00] STATION_CS001: Vehicle VH001 started charging",
				"[2024-01-15 11:00:00] SYSTEM: Daily maintenance completed",
				"[2024-01-15 11:15:00] VEHICLE_VH002: Status changed to In Transit",
				"[2024-01-15 11:30:00] STATION_CS002: Vehicle VH003 stopped charging");
		dataSimulator.simulateLogDataStreaming(sampleLogs);

		System.out.println("\n7. Regular Expressions for Log Parsing:");
		for (String logEntry : sampleLogs) {
			List<String> parsed = logManager.parseLogEntry(logEntry);
			if (!parsed.isEmpty()) {
				System.out.println("Original: " + logEntry);
				System.out.println("Parsed: [Timestamp: " + parsed.get(0) +
						", Action: " + parsed.get(1) +
						", Description: " + parsed.get(2) + "]");
				System.out.println();
			}
		}

		System.out.println("\n8. Log File Metadata Management:");
		List<LogManager.LogMetadata> allMetadata = logManager.getAllLogMetadata();
		System.out.println("Total log files created: " + allMetadata.size());

		for (LogManager.LogMetadata metadata : allMetadata) {
			System.out.println("File: " + metadata.getFileName() +
					", Type: " + metadata.getType() +
					", Date: " + metadata.getDate() +
					", Size: " + metadata.getSize() + " bytes");
		}

		System.out.println("\n9. Log File Validation:");
		String[] testFileNames = {
				"vehicle_2024-01-15.log",
				"station_2024-01-15.log",
				"system_2024-01-15.log",
				"invalid_file.txt",
				"vehicle_2024-13-45.log"
		};

		for (String fileName : testFileNames) {
			boolean isValid = logManager.isValidLogFileName(fileName);
			System.out.println("File: " + fileName + " - Valid: " + isValid);
		}

		System.out.println("\n10. Stream Types Demonstration:");
		dataSimulator.demonstrateStreamTypes();

		System.out.println("\n=== System Features Demonstration Complete ===");
		System.out.println("\nEvidence of Functionality:");
		System.out.println("✓ Storage Vehicle Management with capacity tracking");
		System.out.println("✓ Charging Station Management with slot monitoring");
		System.out.println("✓ Task Management with priority and status tracking");
		System.out.println("✓ Daily log files for vehicles, stations, and system");
		System.out.println("✓ Metadata management (create, move, delete, archive)");
		System.out.println("✓ Byte and character streams for data exchange");
		System.out.println("✓ Regular expressions for log parsing and validation");
		System.out.println("✓ User interface for log file access by equipment/date");
		System.out.println("✓ Comprehensive logging system with timestamps");
		System.out.println("✓ File I/O operations with proper error handling");
	}
}