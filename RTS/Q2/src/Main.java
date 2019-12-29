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
 *     It also contains method for taking input.
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
     * The <code>main</code> method.
     *
     * @param args arguments.
     */
    public static void main(String args[]) {
        Vector<Process> system = inputData();
        Processor processor = new Processor();
        processor.run(system);
    }
}
