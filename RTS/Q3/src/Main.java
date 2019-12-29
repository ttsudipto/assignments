import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
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
     *     of the input file is either a resource definition,
     *     or a process definition or resource requirement
     *     definition. A resource definition has the following
     *     format :
     *     <pre>
     *         r <Resource number>
     *     </pre>
     *     A process definition has the following format :
     *     <pre>
     *         p <Process name> <Phase> <Period> <Execution time> <Deadline> <Priority>
     *     </pre>
     *     A resource requirement has the following format :
     *     <pre>
     *         rr <Process name> <Resource number> <offset> <duration>
     *     </pre>
     * </p>
     *
     * @return a <code>Vector</code> containing the {@link Process}s.
     */
    private static Vector<Process> inputData() {
        Vector<Process> system = new Vector<>();
        HashMap<Integer, Resource> resourceMap = new HashMap<>();
        HashMap<String, Process> processMap = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            while (reader.ready()) {
                String line = reader.readLine();
//                System.out.println(line);
                Scanner sc = new Scanner(line);
                String type = sc.next();
                if (type.equals("r")) {
                    int no = sc.nextInt();
                    resourceMap.put(no, new Resource(no));
//                    System.out.println("Resource OK");
                } else if (type.equals("p")) {
                    String name = sc.next();
                    int phase = sc.nextInt();
                    int period = sc.nextInt();
                    int eTime = sc.nextInt();
                    int deadline = sc.nextInt();
                    int priority = sc.nextInt();
                    processMap.put(name, new Process(name, phase, period, eTime, deadline, priority));
//                    System.out.println("Process OK");
                } else if (type.equals("rr")) {
                    String pName = sc.next();
                    int resNo = sc.nextInt();
                    int offset = sc.nextInt();
                    int duration = sc.nextInt();
                    Process p = processMap.get(pName);
                    Resource r = resourceMap.get(resNo);
                    p.addResourceRequirement(r, offset, duration);
//                    System.out.println("RR OK");
                }
                sc.close();
            }
            for (Process p : processMap.values())
                system.addElement(p);

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
