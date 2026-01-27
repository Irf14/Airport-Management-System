import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Main {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // === Managers ===
        GateManagement gateManager = new GateManagement();
        RunwayManagement runwayManager = new RunwayManagement();
        FlightManagement flightManager = new FlightManagement(gateManager, runwayManager);
        PassengerManagement passengerManager = new PassengerManagement(flightManager);
        DepartureFlightManager departureManager = new DepartureFlightManager(flightManager, passengerManager);
        ArrivalFlightManager arrivalManager = new ArrivalFlightManager(flightManager);
        departureManager.setArrivalManager(arrivalManager);
        arrivalManager.setDepartureManager(departureManager);
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Flight Management");
            System.out.println("2. Gate Management");
            System.out.println("3. Runway Management");
            System.out.println("4. Passenger Management");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            String input = sc.nextLine();
            switch (input) {
                case "1":
                    flightManagementMenu(sc, flightManager, departureManager, arrivalManager);
                    break;
                case "2":
                    gateManagementMenu(sc, gateManager);
                    break;
                case "3":
                    runwayManagementMenu(sc, runwayManager);
                    break;
                case "4":
                    passengerManagementMenu(sc, passengerManager);
                    break;
                case "5":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        sc.close();
        System.out.println("Exiting system...");
    }

    // ===== Flight Management Menu =====
    private static void flightManagementMenu(Scanner sc, FlightManagement fm, DepartureFlightManager dm, ArrivalFlightManager am) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Flight Management ---");
            System.out.println("1. Display all flights");
            System.out.println("2. Display flight by Status");
            System.out.println("3. Display flight by Flight Instance ID");
            System.out.println("4. Departure Flights Menu");
            System.out.println("5. Arrival Flights Menu");
            System.out.println("6. Back");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    fm.displayFlights();
                    break;
                case "2":
                    System.out.print("Enter status (DEPART-SCHEDULED, ARRIVE-SCHEDULED, BOARDING, DEPARTED, ARRIVED): ");
                    String status = sc.nextLine();
                    System.out.println();
                    fm.displayFlightsByStatus(status);
                    break;
                case "3":
                    System.out.print("Enter Flight Instance ID: ");
                    String fid = sc.nextLine();
                    System.out.println();
                    fm.displayFlightByID(fid);
                    break;
                case "4":
                    departureMenu(sc, dm);
                    break;
                case "5":
                    arrivalMenu(sc, am);
                    break;
                case "6":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }


    // ===== Departure Menu =====
    private static void departureMenu(Scanner sc, DepartureFlightManager dm) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Departure Flights ---");
            System.out.println("1. Prepare Boarding");
            System.out.println("2. Complete Departure (enter current time)");
            System.out.println("3. Show Departed List");
            System.out.println("4. Clear Departed Flights File");  // new option
            System.out.println("5. Back");                        // back moved to 5
            System.out.print("Enter choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter Flight Instance ID(FlightNum-DepartDateTime): ");
                    String flightInstanceId = sc.nextLine();
                    System.out.print("Enter Flight's Boarding date and time (yyyy-MM-ddTHH:mm): ");
                    LocalDateTime depTime;
                    String input = sc.nextLine();
                    try {
                        depTime = LocalDateTime.parse(input, formatter);
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid format. Example: 2026-01-26T14:30");
                        break;
                    }
                    dm.prepareBoarding(flightInstanceId, depTime);
                    break;
                case "2":
                    depTime = getUserSimulationTime(sc);
                    if (depTime != null) dm.checkAndDepartFlights(depTime);
                    break;
                case "3":
                    dm.displayDepartedFlights();
                    break;
                case "4":  // new option
                    dm.clearDepartedFlightsFile();
                    break;
                case "5":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }


    // ===== Arrival Menu =====
    private static void arrivalMenu(Scanner sc, ArrivalFlightManager am) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Arrival Flights ---");
            System.out.println("1. Process Arrival");
            System.out.println("2. Complete Arrival (Enter current time)");
            System.out.println("3. Show Arrived Flights");
            System.out.println("4. Clear Arrived Flights File");  // new option
            System.out.println("5. Back");                        // back moved to 5
            System.out.print("Enter choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter Flight Instance ID(FlightNum-DepartDateTime): ");
                    String flightInstanceId = sc.nextLine();
                    System.out.print("Enter Flight's arriving date and time (yyyy-MM-ddTHH:mm): ");
                    LocalDateTime arrTime;
                    String input = sc.nextLine();
                    try {
                        arrTime = LocalDateTime.parse(input, formatter);
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid format. Example: 2026-01-26T14:30");
                        break;
                    }
                    am.processArrival(flightInstanceId, arrTime);
                    break;
                case "2":
                    arrTime = getUserSimulationTime(sc);
                    if (arrTime != null) am.checkAndCompleteArrivals(arrTime);
                    break;
                case "3":
                    am.displayArrivedFlights();
                    break;
                case "4":  // new option
                    am.clearArrivedFlightsFile();
                    break;
                case "5":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }


    // ===== Gate Management Menu =====
    private static void gateManagementMenu(Scanner sc, GateManagement gm) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Gate Management ---");
            System.out.println("1. Display all gates");
            System.out.println("2. Assign gate manually");
            System.out.println("3. Free a gate manually");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    gm.displayGates();
                    break;
                case "2":
                    System.out.print("Enter Flight Instance ID(FlightNum-DepartDateTime): ");
                    String fid = sc.nextLine();
                    String assigned = gm.assignGate(fid);
                    System.out.println(assigned != null ? "Gate assigned: " + assigned : "No free gate available");
                    break;
                case "3":
                    System.out.print("Enter Flight ID to free gate: ");
                    boolean freed = gm.freeGate(sc.nextLine());
                    System.out.println(freed ? "Gate freed." : "No gate matched this flight.");
                    break;
                case "4":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // ===== Runway Management Menu =====
    private static void runwayManagementMenu(Scanner sc, RunwayManagement rm) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Runway Management ---");
            System.out.println("1. Display all runways");
            System.out.println("2. Assign runway manually");
            System.out.println("3. Free a runway manually");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    rm.displayRunways();
                    break;
                case "2":
                    System.out.print("Enter Flight Instance ID(FlightNum-DepartDateTime): ");
                    String fid = sc.nextLine();
                    String assigned = rm.assignRunway(fid);
                    System.out.println(assigned != null ? "Runway assigned: " + assigned : "No free runway available");
                    break;
                case "3":
                    System.out.print("Enter Flight ID to free runway: ");
                    boolean freed = rm.freeRunway(sc.nextLine());
                    System.out.println(freed ? "Runway freed." : "No runway matched this flight.");
                    break;
                case "4":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // ===== Passenger Management Menu =====
    private static void passengerManagementMenu(Scanner sc, PassengerManagement pm) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Passenger Management ---");
            System.out.println("1. Check-in passenger (with simulation time)");
            System.out.println("2. Display all passengers");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    System.out.print("Enter Ticket ID: ");
                    String tid = sc.nextLine();
                    LocalDateTime simTime = getUserSimulationTime(sc);
                    if (simTime != null) pm.checkIn(tid, simTime);
                    break;
                case "2":
                    pm.displayPassengers();
                    break;
                case "3":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // ===== Helper: Get user-specified simulation time =====
    private static LocalDateTime getUserSimulationTime(Scanner sc) {
        System.out.print("Enter simulation date and time (yyyy-MM-ddTHH:mm): ");
        String input = sc.nextLine();
        try {
            return LocalDateTime.parse(input, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid format. Example: 2026-01-26T14:30");
            return null;
        }
    }
}
