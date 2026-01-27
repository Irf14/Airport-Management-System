import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RunwayManagement {

    private static final String RUNWAY_FILE = "runways.txt";
    private List<Runway> runwayList;

    // 1️⃣ Constructor – load runway data ONCE
    public RunwayManagement() {
        runwayList = new ArrayList<>();
        loadRunwaysFromFile();
    }

    // 2️⃣ Read runways.txt and create Runway objects
    private void loadRunwaysFromFile() {
        runwayList.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(RUNWAY_FILE))) {
            String line;

            while ((line = br.readLine()) != null) {
                // Format: runwayId,isFree,assignedflightInstanceId
                String[] data = line.split(",");

                String runwayId = data[0];
                boolean isFree = Boolean.parseBoolean(data[1]);
                String flightInstanceId = data[2];

                Runway runway = new Runway(runwayId, isFree, flightInstanceId);
                runwayList.add(runway);
            }

        } catch (IOException e) {
            System.out.println("Error loading runway data.");
        }
    }

    // 3️⃣ Save ALL runway objects back to file
    private void saveRunwaysToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUNWAY_FILE))) {
            for (Runway runway : runwayList) {
                bw.write(runway.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving runway data.");
        }
    }

    // 4️⃣ Assign a free runway to a flight
    public String assignRunway(String flightInstanceId) {
        for (Runway runway : runwayList) {
            if (runway.isFree()) {
                runway.assignFlight(flightInstanceId);
                saveRunwaysToFile();
                return runway.getRunwayId();
            }
        }

        return null; // No free runway available
    }

    // 5️⃣ Free a runway assigned to a flight
    public boolean freeRunway(String flightInstanceId) {
        for (Runway runway : runwayList) {
            if (!runway.isFree() && runway.getAssignedflightInstanceId().equals(flightInstanceId)) {
                runway.freeRunway();
                saveRunwaysToFile();
                return true;
            }
        }

        return false; // No runway matched this flight
    }

    // 6️⃣ Display all runways (for admin/debug)
    public void displayRunways() {
        for (Runway runway : runwayList) {
            System.out.println(runway.getRunwayId() + " " + runway.isFree() + " " + runway.getAssignedflightInstanceId());
        }
    }
}
