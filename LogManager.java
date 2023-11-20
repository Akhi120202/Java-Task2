/**
 * LogManager class manages logging operations, including creating, moving, deleting, and archiving log files.
 * It also handles saving and loading station and energy logs using byte streams, and logs energy source selections.
 * 
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

class LogManager {
    
    static String systemLog = "system_log.txt";

    // Functionality of the charging station log file
    static String stationLog = "station_log.txt";

    // Functionality of the energy management system log file
    static String energyLog = "energy_log.txt";

    
    public static void createLog(String logFileName, String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFileName, true))) {
            writer.println(getCurrentTime() + " : " + message);
            System.out.println("Log created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error creating log: " + e.getMessage());
        }
    } 

   public static void moveLog(String source, String destination) {
        File sourceFile = new File(source);
        File destFile = new File(destination);

        try {
            // Check if the source file exists
            if (!sourceFile.exists()) {
                System.out.println("Source file does not exist.");
                return;
            }

            // Check if the destination directory exists; if not, create it
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }

            // Move the file using java.nio.file
            Files.move(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Log file moved successfully.");
        } catch (IOException e) {
            handleMoveLogError(e, source, destination);
        }
    }

    private static void handleMoveLogError(IOException e, String source, String destination) {
        System.err.println("Error moving log file from " + source + " to " + destination + ": " + e.getMessage());

        // Specific handling for FileAlreadyExistsException
        if (e instanceof FileAlreadyExistsException) {
            System.out.println("A file with the same name already exists at the destination.");
        }
    }

   public static void deleteLog(String logFileName) {
        File logFile = new File(logFileName);

        // Check if the file exists before attempting to delete
        if (!logFile.exists()) {
            System.out.println("Log file does not exist.");
            return;
        }

        // Archive the log file before deletion
        archiveLog(logFileName);

        try {
            // Attempt to delete the file
            if (logFile.delete()) {
                System.out.println("Log file deleted successfully.");
            } else {
                System.out.println("Failed to delete log file.");
            }
        } catch (SecurityException e) {
            handleDeleteLogError(e, logFileName);
        }
    }

    private static void archiveLog(String logFileName) {
        File logFile = new File(logFileName);
        File archiveDirectory = new File("archive");

        // Check if the archive directory exists; if not, create it
        if (!archiveDirectory.exists()) {
            archiveDirectory.mkdirs();
        }

        // Create the destination path for archiving
        Path destinationPath = archiveDirectory.toPath().resolve(logFile.toPath().getFileName());

        try {
            // Move the log file to the archive directory
            Files.move(logFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Log file archived successfully.");
        } catch (IOException e) {
            handleArchiveLogError(e, logFileName);
        }
    }

    private static void handleDeleteLogError(SecurityException e, String logFileName) {
        System.err.println("Error deleting log file " + logFileName + ": " + e.getMessage());
    }

    private static void handleArchiveLogError(IOException e, String logFileName) {
        System.err.println("Error archiving log file " + logFileName + ": " + e.getMessage());
    }
public static void saveStationLogToFile(String stationLog, List<String> stationLogs) {
        // Save the station logs to a file using byte streams
        try (OutputStream outputStream = new FileOutputStream(stationLog)) {
            for (String log : stationLogs) {
                System.out.println(log);
                byte[] data = log.getBytes();
                outputStream.write(data);
            }
            System.out.println("Station logs saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving station logs: " + e.getMessage());
        }
    }

    public static List<String> loadStationLogFromFile(String stationLog) {
        // Load the station logs from a file using byte streams
        List<String> stationLogs = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(stationLog)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            String logData = byteArrayOutputStream.toString();
            String[] logs = logData.split("\n");
            for (String log : logs) {
                stationLogs.add(log);
            }
            System.out.println("Station logs loaded from file.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading station logs: " + e.getMessage());
        }
        return stationLogs;
    }
    public static void logEnergySources(int timeslot, List<String> energySources) {
        // Log the selected energy sources for a specific timeslot
        try (PrintWriter writer = new PrintWriter(new FileWriter(energyLog, true))) {
            writer.println(getCurrentTime() + " : Timeslot " + timeslot + " - Selected Energy Sources: " + String.join(",", energySources));
            System.out.println("Energy sources logged successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error logging energy sources: " + e.getMessage());
        }
    }
    public static List<String> loadEnergyLogFromFile(String energyLog) {
        // Load the energy logs from a file using byte streams
        List<String> energyLogs = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(energyLog)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            String logData = byteArrayOutputStream.toString();
            String[] logs = logData.split("\n");
            for (String log : logs) {
                energyLogs.add(log);
            }
            System.out.println("Energy logs loaded from file.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading energy logs: " + e.getMessage());
        }
        return energyLogs;
    }

    private static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}

