import java.util.*;
import java.text.SimpleDateFormat;

// Class to represent a user account
class User {
    private String username;
    private String password;
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}

// Class to represent a train
class Train {
    private String trainNumber;
    private String trainName;
    private String source;
    private String destination;
    private int totalSeats;
    private int availableSeats;
    
    public Train(String trainNumber, String trainName, String source, String destination, int totalSeats) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
    }
    
    // Getters
    public String getTrainNumber() { return trainNumber; }
    public String getTrainName() { return trainName; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public int getAvailableSeats() { return availableSeats; }
    
    public boolean reserveSeats(int seats) {
        if (availableSeats >= seats) {
            availableSeats -= seats;
            return true;
        }
        return false;
    }
    
    public void cancelSeats(int seats) {
        availableSeats += seats;
    }
    
    @Override
    public String toString() {
        return trainNumber + " - " + trainName + " (" + source + " to " + destination + ") - Seats: " + availableSeats;
    }
}

// Class to represent a reservation
class Reservation {
    private static int pnrCounter = 1000;
    private String pnr;
    private String passengerName;
    private int age;
    private String trainNumber;
    private String trainName;
    private String classType;
    private Date dateOfJourney;
    private String fromPlace;
    private String toDestination;
    private int numberOfSeats;
    private String status;
    
    public Reservation(String passengerName, int age, String trainNumber, 
                      String trainName, String classType, Date dateOfJourney, 
                      String fromPlace, String toDestination, int numberOfSeats) {
        this.pnr = "PNR" + (pnrCounter++);
        this.passengerName = passengerName;
        this.age = age;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.classType = classType;
        this.dateOfJourney = dateOfJourney;
        this.fromPlace = fromPlace;
        this.toDestination = toDestination;
        this.numberOfSeats = numberOfSeats;
        this.status = "Confirmed";
    }
    
    // Getters
    public String getPnr() { return pnr; }
    public String getPassengerName() { return passengerName; }
    public String getTrainNumber() { return trainNumber; }
    public String getTrainName() { return trainName; }
    public String getClassType() { return classType; }
    public Date getDateOfJourney() { return dateOfJourney; }
    public String getFromPlace() { return fromPlace; }
    public String getToDestination() { return toDestination; }
    public int getNumberOfSeats() { return numberOfSeats; }
    public String getStatus() { return status; }
    
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return "PNR: " + pnr +
               "\nPassenger: " + passengerName + " (Age: " + age + ")" +
               "\nTrain: " + trainNumber + " - " + trainName +
               "\nClass: " + classType +
               "\nDate: " + dateFormat.format(dateOfJourney) +
               "\nRoute: " + fromPlace + " to " + toDestination +
               "\nSeats: " + numberOfSeats +
               "\nStatus: " + status;
    }
}

// Main class for the reservation system
public class OnlineReservationSystem {
    private static Map<String, User> users = new HashMap<>();
    private static List<Train> trains = new ArrayList<>();
    private static List<Reservation> reservations = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static boolean loggedIn = false;
    private static String currentUser = "";
    
    public static void main(String[] args) {
        initializeData();
        
        System.out.println("=============================================");
        System.out.println("      WELCOME TO ONLINE RESERVATION SYSTEM");
        System.out.println("=============================================");
        
        while (true) {
            if (!loggedIn) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }
    
    // Initialize with sample data
    private static void initializeData() {
        // Add users
        users.put("admin", new User("admin", "admin123"));
        users.put("user1", new User("user1", "pass1"));
        users.put("user2", new User("user2", "pass2"));
        
        // Add trains
        trains.add(new Train("12341", "Rajdhani Express", "Mumbai", "Delhi", 100));
        trains.add(new Train("12342", "Shatabdi Express", "Delhi", "Bhopal", 80));
        trains.add(new Train("12343", "Duronto Express", "Chennai", "Kolkata", 120));
        trains.add(new Train("12344", "Garib Rath", "Bangalore", "Hyderabad", 150));
        trains.add(new Train("12345", "Tejas Express", "Mumbai", "Goa", 90));
    }
    
    private static void showLoginMenu() {
        System.out.println("\n=== LOGIN FORM ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        if (authenticateUser(username, password)) {
            loggedIn = true;
            currentUser = username;
            System.out.println("\nLogin successful! Welcome, " + username);
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }
    
    private static boolean authenticateUser(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }
    
    private static void showMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Make Reservation");
        System.out.println("2. Cancel Reservation");
        System.out.println("3. View All Trains");
        System.out.println("4. View My Reservations");
        System.out.println("5. Logout");
        System.out.println("6. Exit");
        System.out.print("Please choose an option (1-6): ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                makeReservation();
                break;
            case 2:
                cancelReservation();
                break;
            case 3:
                viewAllTrains();
                break;
            case 4:
                viewMyReservations();
                break;
            case 5:
                logout();
                break;
            case 6:
                System.out.println("Thank you for using Online Reservation System. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
    
    private static void makeReservation() {
        System.out.println("\n=== RESERVATION SYSTEM ===");
        
        // Show available trains
        System.out.println("Available Trains:");
        for (int i = 0; i < trains.size(); i++) {
            System.out.println((i+1) + ". " + trains.get(i));
        }
        
        System.out.print("Select Train (enter number): ");
        int trainChoice = getIntInput();
        if (trainChoice < 1 || trainChoice > trains.size()) {
            System.out.println("Invalid train selection.");
            return;
        }
        
        Train selectedTrain = trains.get(trainChoice - 1);
        
        // Get passenger details
        System.out.print("Passenger Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Age: ");
        int age = getIntInput();
        
        // Class type
        System.out.print("Class Type (Sleeper/AC/General): ");
        String classType = scanner.nextLine();
        
        // Number of seats
        System.out.print("Number of Seats: ");
        int seats = getIntInput();
        
        if (seats > selectedTrain.getAvailableSeats()) {
            System.out.println("Sorry, only " + selectedTrain.getAvailableSeats() + " seats available.");
            return;
        }
        
        // Date of journey
        System.out.print("Date of Journey (DD-MM-YYYY): ");
        String dateStr = scanner.nextLine();
        Date date;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(dateStr);
        } catch (Exception e) {
            System.out.println("Invalid date format. Using current date.");
            date = new Date();
        }
        
        // From and To places
        System.out.print("From Place: ");
        String fromPlace = scanner.nextLine();
        
        System.out.print("To Destination: ");
        String toDestination = scanner.nextLine();
        
        // Confirm reservation
        System.out.println("\nPlease review your details:");
        System.out.println("Train: " + selectedTrain.getTrainNumber() + " - " + selectedTrain.getTrainName());
        System.out.println("Passenger: " + name + " (Age: " + age + ")");
        System.out.println("Class: " + classType + ", Seats: " + seats);
        System.out.println("Date: " + new SimpleDateFormat("dd-MM-yyyy").format(date));
        System.out.println("Route: " + fromPlace + " to " + toDestination);
        
        System.out.print("\nConfirm reservation? (yes/no): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("yes")) {
            // Create reservation
            Reservation reservation = new Reservation(name, age, selectedTrain.getTrainNumber(), 
                    selectedTrain.getTrainName(), classType, date, fromPlace, toDestination, seats);
            
            // Reserve seats
            if (selectedTrain.reserveSeats(seats)) {
                reservations.add(reservation);
                System.out.println("\nReservation successful!");
                System.out.println(reservation);
                System.out.println("Please note your PNR number for future reference: " + reservation.getPnr());
            } else {
                System.out.println("Reservation failed. Not enough seats available.");
            }
        } else {
            System.out.println("Reservation cancelled.");
        }
    }
    
    private static void cancelReservation() {
        System.out.println("\n=== CANCELLATION FORM ===");
        System.out.print("Enter PNR Number: ");
        String pnr = scanner.nextLine();
        
        Reservation reservation = findReservationByPnr(pnr);
        
        if (reservation == null) {
            System.out.println("No reservation found with PNR: " + pnr);
            return;
        }
        
        System.out.println("\nReservation Details:");
        System.out.println(reservation);
        
        System.out.print("\nDo you want to cancel this reservation? (yes/no): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("yes")) {
            // Find the train and free up the seats
            Train train = findTrainByNumber(reservation.getTrainNumber());
            if (train != null) {
                train.cancelSeats(reservation.getNumberOfSeats());
            }
            
            reservation.setStatus("Cancelled");
            System.out.println("Reservation cancelled successfully.");
        } else {
            System.out.println("Cancellation aborted.");
        }
    }
    
    private static void viewAllTrains() {
        System.out.println("\n=== ALL TRAINS ===");
        for (Train train : trains) {
            System.out.println(train);
        }
    }
    
    private static void viewMyReservations() {
        System.out.println("\n=== MY RESERVATIONS ===");
        boolean found = false;
        
        for (Reservation reservation : reservations) {
            System.out.println(reservation);
            System.out.println("-----------------------");
            found = true;
        }
        
        if (!found) {
            System.out.println("No reservations found.");
        }
    }
    
    private static Reservation findReservationByPnr(String pnr) {
        for (Reservation reservation : reservations) {
            if (reservation.getPnr().equalsIgnoreCase(pnr)) {
                return reservation;
            }
        }
        return null;
    }
    
    private static Train findTrainByNumber(String trainNumber) {
        for (Train train : trains) {
            if (train.getTrainNumber().equals(trainNumber)) {
                return train;
            }
        }
        return null;
    }
    
    private static void logout() {
        loggedIn = false;
        currentUser = "";
        System.out.println("Logged out successfully.");
    }
    
    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}