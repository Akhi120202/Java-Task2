import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        DataExchangeSimulation simulation = new DataExchangeSimulation();
        simulation.simulateDataExchange();

        User externalUser = new User("ExternalUser2", false);
        User admin = new User("Admin1", true);

        // Create charging station
        ChargingStation chargingStation = new ChargingStation("Station1");
        chargingStation.loadStateFromFile();

        // Book timeslot and prioritize queue
        chargingStation.showAvailableTimeslots();
        Scanner scanner = new Scanner(System.in);
        int choice = Integer.parseInt(scanner.nextLine());
        //scanner.close();
        int[] choices = chargingStation.bookTimeslot(externalUser, choice);
        int energyChoice = choices[1];
        int chosenTimeslot = choices[0];
        List<String> stationLogs = new ArrayList<>();
        if (chosenTimeslot != -1) {
            System.out.println(externalUser.username + " booked timeslot " + chargingStation.getTimeRange(chosenTimeslot) + " at " + chargingStation.getCurrentTime());
            stationLogs.add(externalUser.username + " booked timeslot " + chargingStation.getTimeRange(chosenTimeslot) + " at " + chargingStation.getCurrentTime());

            chargingStation.displayAvailableEnergySources();
            //Scanner scan = new Scanner(System.in);
            //energyChoice = scanner.nextInt();
            //scan.close();
        }
        chargingStation.prioritizeQueue(admin);

        // Create and manage log files
        LogManager.createLog(LogManager.systemLog, "System updated successfully");
        LogManager.createLog(LogManager.stationLog, stationLogs.get(0));
        LogManager.createLog(LogManager.energyLog, "User made a selection "+chargingStation.getSource(energyChoice) +" in the energy management system.");



        // Save station logs to file
        //LogManager.saveStationLogToFile(LogManager.stationLog, stationLogs);

        // Load station logs from file
        //List<String> loadedStationLogs = LogManager.loadStationLogFromFile(LogManager.stationLog);

    }
       
}
