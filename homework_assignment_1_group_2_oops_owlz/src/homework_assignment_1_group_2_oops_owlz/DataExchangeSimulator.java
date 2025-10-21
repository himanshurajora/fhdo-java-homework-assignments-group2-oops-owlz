package homework_assignment_1_group_2_oops_owlz;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DataExchangeSimulator {

    private static final String DATA_DIRECTORY = "data_exchange";
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void simulateVehicleStationDataExchange(StorageVehicle vehicle, ChargingStation station) {
        try {
            File dataDir = new File(DATA_DIRECTORY);
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

            String exchangeData = generateExchangeData(vehicle, station);

            String fileName = "exchange_" + vehicle.getVehicleId() + "_" + station.getStationId() + ".dat";
            writeDataWithByteStream(fileName, exchangeData);

            String readData = readDataWithByteStream(fileName);

            System.out.println("Data Exchange Simulation:");
            System.out.println("Original: " + exchangeData);
            System.out.println("Read back: " + readData);
            System.out.println("Data integrity: " + (exchangeData.equals(readData) ? "PASS" : "FAIL"));

        } catch (IOException e) {
            System.err.println("Error in data exchange simulation: " + e.getMessage());
        }
    }

    public void simulateConfigurationDataExchange(List<StorageVehicle> vehicles, List<ChargingStation> stations) {
        try {
            String configData = generateConfigurationData(vehicles, stations);

            String fileName = "config_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                    + ".txt";
            writeDataWithCharacterStream(fileName, configData);

            String readData = readDataWithCharacterStream(fileName);

            System.out.println("\nConfiguration Data Exchange Simulation:");
            System.out.println("Original length: " + configData.length());
            System.out.println("Read back length: " + readData.length());
            System.out.println("Data integrity: " + (configData.equals(readData) ? "PASS" : "FAIL"));

        } catch (IOException e) {
            System.err.println("Error in configuration data exchange simulation: " + e.getMessage());
        }
    }

    public void simulateLogDataStreaming(List<String> logEntries) {
        try {
            String fileName = "streaming_logs_"
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".log";

            writeLogsWithBufferedStream(fileName, logEntries);

            List<String> readLogs = readLogsWithBufferedStream(fileName);

            System.out.println("\nLog Data Streaming Simulation:");
            System.out.println("Original entries: " + logEntries.size());
            System.out.println("Read back entries: " + readLogs.size());
            System.out.println("Data integrity: " + (logEntries.equals(readLogs) ? "PASS" : "FAIL"));

        } catch (IOException e) {
            System.err.println("Error in log data streaming simulation: " + e.getMessage());
        }
    }

    private String generateExchangeData(StorageVehicle vehicle, ChargingStation station) {
        StringBuilder data = new StringBuilder();
        data.append("EXCHANGE_DATA\n");
        data.append("TIMESTAMP:").append(LocalDateTime.now().format(TIMESTAMP_FORMATTER)).append("\n");
        data.append("VEHICLE_ID:").append(vehicle.getVehicleId()).append("\n");
        data.append("VEHICLE_NAME:").append(vehicle.getVehicleName()).append("\n");
        data.append("VEHICLE_LOAD:").append(vehicle.getCurrentLoad()).append("/").append(vehicle.getCapacity())
                .append("\n");
        data.append("STATION_ID:").append(station.getStationId()).append("\n");
        data.append("STATION_NAME:").append(station.getStationName()).append("\n");
        data.append("STATION_SLOTS:").append(station.getOccupiedSlots()).append("/").append(station.getTotalSlots())
                .append("\n");
        data.append("POWER_OUTPUT:").append(station.getPowerOutput()).append("\n");
        data.append("END_EXCHANGE_DATA");
        return data.toString();
    }

    private String generateConfigurationData(List<StorageVehicle> vehicles, List<ChargingStation> stations) {
        StringBuilder config = new StringBuilder();
        config.append("SYSTEM_CONFIGURATION\n");
        config.append("TIMESTAMP:").append(LocalDateTime.now().format(TIMESTAMP_FORMATTER)).append("\n");
        config.append("VEHICLES_COUNT:").append(vehicles.size()).append("\n");

        for (StorageVehicle vehicle : vehicles) {
            config.append("VEHICLE:").append(vehicle.getVehicleId())
                    .append(",").append(vehicle.getVehicleName())
                    .append(",").append(vehicle.getCapacity())
                    .append(",").append(vehicle.getStatus()).append("\n");
        }

        config.append("STATIONS_COUNT:").append(stations.size()).append("\n");
        for (ChargingStation station : stations) {
            config.append("STATION:").append(station.getStationId())
                    .append(",").append(station.getStationName())
                    .append(",").append(station.getLocation())
                    .append(",").append(station.getTotalSlots())
                    .append(",").append(station.getPowerOutput()).append("\n");
        }

        config.append("END_CONFIGURATION");
        return config.toString();
    }

    private void writeDataWithByteStream(String fileName, String data) throws IOException {
        File file = new File(DATA_DIRECTORY, fileName);
        try (FileOutputStream fos = new FileOutputStream(file); BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            bos.write(dataBytes);
            bos.flush();
        }
    }

    private String readDataWithByteStream(String fileName) throws IOException {
        File file = new File(DATA_DIRECTORY, fileName);
        try (FileInputStream fis = new FileInputStream(file); BufferedInputStream bis = new BufferedInputStream(fis)) {

            byte[] buffer = new byte[1024];
            StringBuilder result = new StringBuilder();
            int bytesRead;

            while ((bytesRead = bis.read(buffer)) != -1) {
                result.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            }

            return result.toString();
        }
    }

    private void writeDataWithCharacterStream(String fileName, String data) throws IOException {
        File file = new File(DATA_DIRECTORY, fileName);
        try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8); BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(data);
            bw.flush();
        }
    }

    private String readDataWithCharacterStream(String fileName) throws IOException {
        File file = new File(DATA_DIRECTORY, fileName);
        try (FileReader fr = new FileReader(file, StandardCharsets.UTF_8); BufferedReader br = new BufferedReader(fr)) {

            StringBuilder result = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                result.append(line).append("\n");
            }

            return result.toString();
        }
    }

    private void writeLogsWithBufferedStream(String fileName, List<String> logEntries) throws IOException {
        File file = new File(DATA_DIRECTORY, fileName);
        try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8); BufferedWriter bw = new BufferedWriter(fw)) {

            for (String entry : logEntries) {
                bw.write(entry);
                bw.newLine();
            }
            bw.flush();
        }
    }

    private List<String> readLogsWithBufferedStream(String fileName) throws IOException {
        File file = new File(DATA_DIRECTORY, fileName);
        List<String> logs = new ArrayList<>();

        try (FileReader fr = new FileReader(file, StandardCharsets.UTF_8); BufferedReader br = new BufferedReader(fr)) {

            String line;
            while ((line = br.readLine()) != null) {
                logs.add(line);
            }
        }

        return logs;
    }

    public void demonstrateStreamTypes() {
        System.out.println("\n=== Stream Types Demonstration ===");

        String testData = "This is a test data for stream demonstration.\nIt contains multiple lines.\nAnd special characters: @#$%^&*()";

        try {
            System.out.println("\n1. Byte Stream Processing:");
            byte[] bytes = testData.getBytes(StandardCharsets.UTF_8);
            System.out.println("Original data length: " + testData.length() + " characters");
            System.out.println("Byte array length: " + bytes.length + " bytes");

            System.out.println("\n2. Character Stream Processing:");
            char[] chars = testData.toCharArray();
            System.out.println("Character array length: " + chars.length + " characters");

            System.out.println("\n3. String Stream Processing:");
            String[] lines = testData.split("\n");
            System.out.println("Number of lines: " + lines.length);
            for (int i = 0; i < lines.length; i++) {
                System.out.println("Line " + (i + 1) + ": " + lines[i]);
            }

        } catch (Exception e) {
            System.err.println("Error in stream demonstration: " + e.getMessage());
        }
    }
}
