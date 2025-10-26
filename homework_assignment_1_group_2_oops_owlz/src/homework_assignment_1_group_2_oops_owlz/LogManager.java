package homework_assignment_1_group_2_oops_owlz;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class LogManager {
    private static final String LOG_DIRECTORY = "logs";
    private static final String ARCHIVE_DIRECTORY = "logs/archive";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final Pattern LOG_FILE_PATTERN = Pattern
            .compile("(vehicle|station|system)_(\\d{4}-\\d{2}-\\d{2})\\.log");
    private static final Pattern LOG_ENTRY_PATTERN = Pattern
            .compile("\\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\] ([A-Z_]+): (.+)");

    private Map<String, LogMetadata> logMetadata;

    public LogManager() {
        this.logMetadata = new HashMap<>();
        initializeDirectories();
        loadExistingMetadata();
    }

    private void initializeDirectories() {
        try {
            Files.createDirectories(Paths.get(LOG_DIRECTORY));
            Files.createDirectories(Paths.get(ARCHIVE_DIRECTORY));
        } catch (IOException e) {
            System.err.println("Error creating log directories: " + e.getMessage());
        }
    }

    private void loadExistingMetadata() {
        try {
            Path logDir = Paths.get(LOG_DIRECTORY);
            if (Files.exists(logDir)) {
                Files.list(logDir)
                        .filter(path -> path.toString().endsWith(".log"))
                        .forEach(this::loadLogMetadata);
            }
        } catch (IOException e) {
            System.err.println("Error loading existing metadata: " + e.getMessage());
        }
    }

    private void loadLogMetadata(Path logFile) {
        try {
            String fileName = logFile.getFileName().toString();
            Matcher matcher = LOG_FILE_PATTERN.matcher(fileName);
            if (matcher.matches()) {
                String type = matcher.group(1);
                String date = matcher.group(2);
                long size = Files.size(logFile);
                LocalDateTime lastModified = LocalDateTime.ofInstant(
                        Files.getLastModifiedTime(logFile).toInstant(),
                        TimeZone.getDefault().toZoneId());

                String key = type + "_" + date;
                logMetadata.put(key, new LogMetadata(fileName, type, date, size, lastModified));
            }
        } catch (IOException e) {
            System.err.println("Error loading metadata for " + logFile + ": " + e.getMessage());
        }
    }

    public void logVehicleActivity(String vehicleId, String activity) {
        String date = LocalDate.now().format(DATE_FORMATTER);
        String fileName = "vehicle_" + date + ".log";
        String logEntry = String.format("[%s] VEHICLE_%s: %s",
                LocalDateTime.now().format(DATETIME_FORMATTER), vehicleId, activity);
        writeToLogFile(fileName, logEntry);
        updateMetadata(fileName, "vehicle", date);
    }

    public void logStationActivity(String stationId, String activity) {
        String date = LocalDate.now().format(DATE_FORMATTER);
        String fileName = "station_" + date + ".log";
        String logEntry = String.format("[%s] STATION_%s: %s",
                LocalDateTime.now().format(DATETIME_FORMATTER), stationId, activity);
        writeToLogFile(fileName, logEntry);
        updateMetadata(fileName, "station", date);
    }

    public void logSystemActivity(String activity) {
        String date = LocalDate.now().format(DATE_FORMATTER);
        String fileName = "system_" + date + ".log";
        String logEntry = String.format("[%s] SYSTEM: %s",
                LocalDateTime.now().format(DATETIME_FORMATTER), activity);
        writeToLogFile(fileName, logEntry);
        updateMetadata(fileName, "system", date);
    }

    private void writeToLogFile(String fileName, String logEntry) {
        try {
            Path logFile = Paths.get(LOG_DIRECTORY, fileName);
            Files.write(logFile, (logEntry + System.lineSeparator()).getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error writing to log file " + fileName + ": " + e.getMessage());
        }
    }

    private void updateMetadata(String fileName, String type, String date) {
        try {
            Path logFile = Paths.get(LOG_DIRECTORY, fileName);
            long size = Files.size(logFile);
            LocalDateTime lastModified = LocalDateTime.ofInstant(
                    Files.getLastModifiedTime(logFile).toInstant(),
                    TimeZone.getDefault().toZoneId());

            String key = type + "_" + date;
            logMetadata.put(key, new LogMetadata(fileName, type, date, size, lastModified));
        } catch (IOException e) {
            System.err.println("Error updating metadata for " + fileName + ": " + e.getMessage());
        }
    }

    public List<String> readLogFile(String fileName) {
        List<String> logEntries = new ArrayList<>();
        try {
            Path logFile = Paths.get(LOG_DIRECTORY, fileName);
            if (Files.exists(logFile)) {
                logEntries = Files.readAllLines(logFile);
            }
        } catch (IOException e) {
            System.err.println("Error reading log file " + fileName + ": " + e.getMessage());
        }
        return logEntries;
    }

    public List<String> readLogByEquipment(String equipmentName) {
        List<String> allEntries = new ArrayList<>();
        for (LogMetadata metadata : logMetadata.values()) {
            List<String> entries = readLogFile(metadata.getFileName());
            for (String entry : entries) {
                if (entry.contains(equipmentName)) {
                    allEntries.add(entry);
                }
            }
        }
        return allEntries;
    }

    public List<String> readLogByDate(String date) {
        List<String> allEntries = new ArrayList<>();
        for (LogMetadata metadata : logMetadata.values()) {
            if (metadata.getDate().equals(date)) {
                allEntries.addAll(readLogFile(metadata.getFileName()));
            }
        }
        return allEntries;
    }

    public boolean moveLogFile(String fileName, String destination) {
        try {
            Path source = Paths.get(LOG_DIRECTORY, fileName);
            Path dest = Paths.get(destination, fileName);
            Files.move(source, dest, StandardCopyOption.REPLACE_EXISTING);

            String key = fileName.replace(".log", "");
            logMetadata.remove(key);
            return true;
        } catch (IOException e) {
            System.err.println("Error moving log file " + fileName + ": " + e.getMessage());
            return false;
        }
    }

    public boolean deleteLogFile(String fileName) {
        try {
            Path logFile = Paths.get(LOG_DIRECTORY, fileName);
            Files.deleteIfExists(logFile);

            String key = fileName.replace(".log", "");
            logMetadata.remove(key);
            return true;
        } catch (IOException e) {
            System.err.println("Error deleting log file " + fileName + ": " + e.getMessage());
            return false;
        }
    }

    public boolean archiveLogFile(String fileName) {
        try {
            Path sourceFile = Paths.get(LOG_DIRECTORY, fileName);
            if (!Files.exists(sourceFile)) {
                return false;
            }

            String archiveName = fileName.replace(".log", "_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".zip");
            Path archiveFile = Paths.get(ARCHIVE_DIRECTORY, archiveName);

            try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(archiveFile))) {
                ZipEntry entry = new ZipEntry(fileName);
                zos.putNextEntry(entry);
                Files.copy(sourceFile, zos);
                zos.closeEntry();
            }

            Files.delete(sourceFile);

            String key = fileName.replace(".log", "");
            logMetadata.remove(key);
            return true;
        } catch (IOException e) {
            System.err.println("Error archiving log file " + fileName + ": " + e.getMessage());
            return false;
        }
    }

    public List<LogMetadata> getAllLogMetadata() {
        return new ArrayList<>(logMetadata.values());
    }

    public List<LogMetadata> getLogMetadataByType(String type) {
        return logMetadata.values().stream()
                .filter(metadata -> metadata.getType().equals(type))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public List<LogMetadata> getLogMetadataByDate(String date) {
        return logMetadata.values().stream()
                .filter(metadata -> metadata.getDate().equals(date))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public boolean isValidLogFileName(String fileName) {
        return LOG_FILE_PATTERN.matcher(fileName).matches();
    }

    public List<String> parseLogEntry(String logEntry) {
        Matcher matcher = LOG_ENTRY_PATTERN.matcher(logEntry);
        if (matcher.matches()) {
            List<String> parts = new ArrayList<>();
            parts.add(matcher.group(1));
            parts.add(matcher.group(2));
            parts.add(matcher.group(3));
            return parts;
        }
        return new ArrayList<>();
    }

    public static class LogMetadata {
        private String fileName;
        private String type;
        private String date;
        private long size;
        private LocalDateTime lastModified;

        public LogMetadata(String fileName, String type, String date, long size, LocalDateTime lastModified) {
            this.fileName = fileName;
            this.type = type;
            this.date = date;
            this.size = size;
            this.lastModified = lastModified;
        }

        public String getFileName() {
            return fileName;
        }

        public String getType() {
            return type;
        }

        public String getDate() {
            return date;
        }

        public long getSize() {
            return size;
        }

        public LocalDateTime getLastModified() {
            return lastModified;
        }

        @Override
        public String toString() {
            return String.format("LogMetadata[File: %s, Type: %s, Date: %s, Size: %d bytes, Modified: %s]",
                    fileName, type, date, size, lastModified.format(DATETIME_FORMATTER));
        }
    }
}