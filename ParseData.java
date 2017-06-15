package resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 *
 * Created by babak_khorrami on 7/22/16.
 */
public class ParseData {

    /** Static Method to Parse Processes Data */
    public static Queue<Process> readProcess(String processFile){
        Queue<Process> jobQ = new ArrayDeque<>(); //A queue to hold individual jobs

        try {
            File f = new File(processFile);
            Scanner sc = new Scanner(f);

            sc.nextLine();//** Skip the first line (column titles)
            Process pr;

            //Process Type,Number,Cores,Memory
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] details = line.split(",");
                String prType = details[0];
                Integer num = Integer.parseInt(details[1]);
                Double core = Double.parseDouble(details[2]);
                Double mem = Double.parseDouble(details[3]);
                pr = new Process(prType,core,mem);//Create a process
                for(int i = 0 ; i < num ; i++)
                    jobQ.add(pr);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return jobQ;

    }

    /** Static Method to Parse Machines Data */
    public static List<Machine> readMachine(String machineFile){
        List<Machine> machineList = new ArrayList<>();

        try {
            File f = new File(machineFile);
            Scanner sc = new Scanner(f);


            sc.nextLine();//** Skip the first line (column titles)
            Machine mc;

            //Instance Type,Cores,Memory,Daily Cost
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] details = line.split(",");
                String type = details[0];
                Double core = Double.parseDouble(details[1]);
                Double mem = Double.parseDouble(details[2]);
                Double cost = Double.parseDouble(details[3]);
                mc = new Machine(type,core,mem,cost);//Create a process
                machineList.add(mc);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return machineList;

    }
}
