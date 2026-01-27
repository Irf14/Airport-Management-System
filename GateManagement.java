import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GateManagement {

    private static final String GATE_FILE = "gates.txt";
    private List<Gate> gateList;

    // 1️⃣ Constructor – loads gate data ONCE
    public GateManagement() {
        gateList = new ArrayList<>();
        loadGatesFromFile();
    }

    // 2️⃣ Read gates.txt and create Gate objects
    private void loadGatesFromFile() {
        //gateList.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(GATE_FILE))) {
            String line;

            while ((line = br.readLine()) != null) {
                // Format: gateId,isFree,assignedFlightId
                String[] data = line.split(",");

                String gateId = data[0];
                boolean isFree = Boolean.parseBoolean(data[1]);
                String flightId = data[2];

                Gate gate = new Gate(gateId, isFree, flightId);
                gateList.add(gate);
            }

        } catch (IOException e) {
            System.out.println("Error loading gate data.");
        }
    }

    // 3️⃣ Save ALL gate objects back to file
    private void saveGatesToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(GATE_FILE))) {

            for (Gate gate : gateList) {
                bw.write(gate.toFileString());
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving gate data.");
        }
    }

    // 4️⃣ Assign a free gate to a flight
    public String assignGate(String flightInstanceId) {

        for (Gate gate : gateList) {
            if (gate.isFree()) {
                gate.assignFlight(flightInstanceId);
                saveGatesToFile();
                return gate.getGateId();
            }
        }

        return null; // No free gate available
    }

    // 5️⃣ Free gate assigned to a flight
    public boolean freeGate(String flightInstanceId) {

        for (Gate gate : gateList) {
            if (!gate.isFree() && gate.getAssignedFlightInstanceId().equals(flightInstanceId)) {
                gate.freeGate();
                saveGatesToFile();
                return true;
            }
        }

        return false; // No gate matched this flight
    }

    // 6️⃣ View all gates (for admin/debug)
    public void displayGates() {
        for (Gate gate : gateList) {
            System.out.println(gate.getGateId() + " " + gate.isFree() + " " + gate.getAssignedFlightInstanceId());
        }
    }

}
