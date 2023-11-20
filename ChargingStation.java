/**
 * ChargingStation class represents a charging station for vehicles, 
 * managing timeslot bookings, queue prioritization, and state persistence. 
 * It includes methods for booking timeslots, displaying information, and handling energy sources.
 * 
 */
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

class ChargingStation {
    String stationId;
    Queue<User> queue;
    List<Integer> availableTimeslots;
    List<Integer> bookedSlots;
    List<User> bookedUsers;
    static String stationLog = "src/station_state.text";
    List<String> availableEnergySources;

    public ChargingStation(String stationId) {
        this.stationId = stationId;
        this.availableTimeslots = new ArrayList<>();
        this.bookedUsers = new ArrayList<>();
        this.bookedSlots = new ArrayList<>();
        this.queue = new LinkedList<>();
        this.timeslots();
        this.availableEnergySources=new ArrayList<>();
        availableEnergySources.add("Solar");
        availableEnergySources.add("Wind");
        availableEnergySources.add("Hydro");
    }
    

    public void saveStateToFile() {
        // Save the state of the charging station to a file using character streams
        try (Writer writer = new FileWriter(stationLog)) {
            // Write stationId
            writer.write("stationId:" + stationId + "\n");

            // Write availableTimeslots
            writer.write("availableTimeslots:");
            for (int timeslot : bookedSlots) {
                writer.write(timeslot + ",");
            }
            writer.write("\n");

            // Write bookedUsers
            writer.write("bookedUsers:");
            for (User user : bookedUsers) {
                writer.write(user.username + "-" + user.isAdmin + ",");
            }
            writer.write("\n");

            System.out.println("Charging station state saved to file.");
        } 
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving charging station state: " + e.getMessage());
        }
    }

    public void loadStateFromFile() {
        // Load the state of the charging station from a file using character streams
        try (Scanner scanner = new Scanner(new FileReader(stationLog))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                String key = parts[0];
                String value = parts[1];

                switch (key) {
                    case "stationId":
                        stationId = value;
                        break;
                    case "availableTimeslots":
                        String[] timeslots = value.split(",");
                        bookedSlots.clear();
                        for (String timeslot : timeslots) {
                            availableTimeslots.remove(Integer.valueOf(Integer.parseInt(timeslot)));
                            bookedSlots.add(Integer.parseInt(timeslot));
                        }
                        break;
                    case "bookedUsers":
                        String[] users = value.split(",");
                        bookedUsers.clear();
                        for (String userInfo : users) {
                            String[] userParts = userInfo.split("-");
                            String username = userParts[0];
                            boolean isAdmin = Boolean.parseBoolean(userParts[1]);
                            bookedUsers.add(new User(username, isAdmin));
                            queue.add(new User(username, isAdmin));
                        }
                        break;
                }
            }
            System.out.println("Charging station state loaded from file.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading charging station state: " + e.getMessage());
        }
    }


    public int[] bookTimeslot(User user, int choice) {
        // Using try-with-resources to automatically close the Scanner
        int[] choices = {-1,-1} ;
        choice--;
 
        try (Scanner scanner = new Scanner(System.in)) {
            
            // Check if there is an empty timeslot
            if (!availableTimeslots.isEmpty() && choice < availableTimeslots.size() && choice>0) {
                System.out.println("Do you want to confirm this booking? (yes/no)");
    
                String confirmation = scanner.nextLine().toLowerCase();
    
                if (confirmation.equals("yes")) {
                    int slot = availableTimeslots.get(choice);
                    //System.out.println(user.username + " booked timeslot " + getTimeRange(slot) + " at " + getCurrentTime());

                    bookedUsers.add(user);
                    bookedSlots.add(slot);
                    availableTimeslots.remove(Integer.valueOf(slot));
                    choices[0] = slot;
                    saveStateToFile();
                    displayAvailableEnergySources();
                    int energyChoice = scanner.nextInt();
                    choices[1] = energyChoice;

                } else if (confirmation.equals("no")) {
                   System.out.println("Booking canceled.");
    
                } else {
                    System.out.println("Invalid input. Booking canceled.");
                }
            } 
        } 
        return choices;
    }

    public void displayAvailableEnergySources() {
        System.out.println("Available energy sources:");
        for (int i = 0; i < availableEnergySources.size(); i++) {
            System.out.println((i + 1) + ". " + availableEnergySources.get(i));
        }
    }
    
    
    public String getTimeRange(int timeslot) {
        // Assuming business hours from 9:00 AM to 5:00 PM with 30-minute slots
        int startTime = timeslot; // 9:00 AM represented as 900 in 24-hour format
    
        int startHour = startTime / 100;
        int startMinute = startTime % 100;
        timeslot +=30;
        int selectedHour = timeslot / 100;
        int selectedMinute = timeslot % 100;
    
        return String.format("%02d:%02d AM - %02d:%02d AM", startHour, startMinute, selectedHour, selectedMinute);
    }
    

    public void showAvailableTimeslots() {
        // Display selected available timeslots
        int i = 1;
        System.out.println("Available timeslots:");
        for(int time: availableTimeslots){
            System.out.println(i+". " + this.getTimeRange(time));
            i++;
        }    

    }

    public void prioritizeQueue(User admin) {
        // Implement queue prioritization logic
        if (queue.contains(admin)) {
            // Admin is already in the queue; move them to the front
            queue.remove(admin);
            ((LinkedList<User>) queue).addFirst(admin);
            System.out.println("Queue prioritized. " + admin.username + " moved to the front at " + getCurrentTime());
        } else {
            System.out.println(admin.username + " is not in the queue.");
        }
    }

    public String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public String getSource(int choice){
        
        if(choice <= availableEnergySources.size() && choice>0){
            return availableEnergySources.get(choice-1);
        }

        return "no";
    }

    private void timeslots(){

        int start = 900;

        while(start <= 1700){
            availableTimeslots.add(start);
            if(start%100 == 0)
                start+=30;
            else
                start+=70;
        }
    }

}

