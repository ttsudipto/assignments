import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Vector;

/**
 * Class containing the {@link #main(String[])} method.
 *
 * <p>
 *     It also contain methods for taking input and check the
 *     schedulability of the system of processes given as input.
 * </p>
 */
public class Main {

    /**
     * Takes input.
     *
     * <p>
     *     This method reads the file <code>input.txt</code>
     *     which is supposed to contain the input. Each line
     *     of the input file has the following format :
     *     <pre>
     *         <Process name> <Phase> <Period> <Execution time> <Deadline>
     *     </pre>
     *     It reads the file line by line and create
     *     {@link Process}s with the given parameters.
     * </p>
     *
     * @return a <code>Vector</code> containing the {@link Process}s.
     */
    private static Vector<Process> inputData() {
        Vector<Process> system = new Vector<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
//            FileReader reader = new FileReader("input.txt");
            while (reader.ready()) {
                String line = reader.readLine();
                Scanner sc = new Scanner(line);
                String name = sc.next();
                int phase = sc.nextInt();
                int period = sc.nextInt();
                int eTime = sc.nextInt();
                int deadline = sc.nextInt();
                system.addElement(new Process(name, phase, period, eTime, deadline));
                sc.close();
            }

        } catch (FileNotFoundException e) {
            System.out.println("Input file not found");
        } catch (IOException e) {
            System.out.println("Cannot read input file");
        } catch (InputMismatchException e) {
            System.out.println("Improper input file");
        }

        return system;
    }

    /**
     * Checks if the system of processes is schedulable.
     *
     * <p>
     *     The system of processes is schedulable if the
     *     total utilization is less than or equal to
     *     <code>1</code>.
     * </p>
     *
     * @param system <code>Vector<Process></code> representing
     *               the system of processes.
     * @return <code>true</code>, if schedulable, <br/>
     *          <code>false</code>, otherwise.
     */
    private static boolean isSchedulable(Vector<Process> system) {
        double utilization = 0;
        for (int i=0; i<system.size(); ++i) {
            Process p = system.get(i);
            utilization += ((double) p.getExecutionTime()) / ((double) p.getPeriod());
        }

        return utilization <= 1;
    }

    /**
     * The <code>main</code> method.
     *
     * @param args arguments.
     */
    public static void main(String args[]) {
        Vector<Process> system = inputData();
        if (isSchedulable(system)) {
            Processor processor = new Processor();
            processor.run(system);
        } else
            System.out.println("System of processes not schedulable");
    }
}
