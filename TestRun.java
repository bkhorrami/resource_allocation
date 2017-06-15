package resources;

import javax.crypto.Mac;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Random;
import java.lang.*;

/**
 *
 * Created by babak_khorrami on 7/21/16.
 */
public class TestRun {

    /** */
    public static ArrayList<Process> readProcess(String processFile){
        //Map<Process,Integer> jobList= new HashMap<>();
        ArrayList<Process> jobQ = new ArrayList<>();

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

    /** */
    public static List<Machine> readMachine(String machineFile){
        //Map<Process,Integer> jobList= new HashMap<>();
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

    /** Initialize */
    public static List<Machine> initialize(ArrayList<Process> pq , List<Machine> mc){
        int count = 0;
        //*** While the job queue is not empty:
        List<Machine> reserveHardware = new ArrayList<>(); //List of Reserved Machines and their load
        while(!pq.isEmpty()){
            Random randomGenerator = new Random();
            int index = randomGenerator.nextInt(mc.size());
            Machine currMachine = Machine.copy(mc.get(index));
            currMachine.setId(++count);
            boolean hasSpace = true;
            while(hasSpace){
                if(!pq.isEmpty()){
                    Random rd = new Random();
                    int idx = rd.nextInt(pq.size());
                    Process currJob = pq.get(idx);
                    hasSpace = currMachine.assignJob(currJob);
                    if(hasSpace) pq.remove(idx);
                }
                else break;
            }
            if(currMachine.getCoreUsage() > 0 | currMachine.getMemoryUsage() > 0){
                reserveHardware.add(currMachine);
            }
        }
        return reserveHardware;
    }

    /*************************************/
//    public static List<Machine> xor2xtoLarge(List<Machine> hardware , Machine currMachine) {
//            // if any of x or 2x machines have less than core=2/mem=8 loads, assign all to a large machine:
//            if (currMachine.getCoreUsage() <= 2 & currMachine.getMemoryUsage() <= 8) {
//                if (currMachine.getType().equals("zoom.2xlarge") | currMachine.getType().equals("zoom.xlarge")) {
//                    Machine lm = new Machine("zoom.large");
//                    lm.setId(currMachine.getId());
//                    boolean space;
//                    for (Process p : currMachine.getJobs()) {
//                        space = lm.assignJob(p); //assign the job to the smaller machine
//                        if (!space) {
//                            System.out.println("Job can't be assigned!");
//                            break;
//                        }
//                    }
//                    //hardware.remove(i); //Remove the machine ...
//                    hardware.remove(currMachine);
//                    hardware.add(lm); //Add new large machine
//                }
//            }
//    }



    /**  */
    public static List<Machine> reduceMachine(List<Machine> hardware) {
        for(int i=0; i<hardware.size();i++){
            Machine currMachine = hardware.get(i);
            //** if any of x or 2x machines have less than core=2/mem=8 loads, assign all to a large machine:
            if (currMachine.getCoreUsage() <= 2 & currMachine.getMemoryUsage() <= 8) {
                if (currMachine.getType().equals("zoom.2xlarge") | currMachine.getType().equals("zoom.xlarge")) {
                    Machine lm = new Machine("zoom.large");
                    lm.setId(currMachine.getId());
                    boolean space;
                    for (Process p : currMachine.getJobs()) {
                        space = lm.assignJob(p); //assign the job to the smaller machine
                        if(!space){
                            System.out.println("Job can't be assigned!");
                            break;
                        }
                    }
                    hardware.remove(i); //Remove the machine ...
                    hardware.add(lm); //Add new large machine
                }
            } else if (currMachine.getCore() <= 4 & currMachine.getCore() > 2 &
                       currMachine.getMemory() > 8 & currMachine.getMemory() <= 16) {
                if (currMachine.getType().equals("zoom.2xlarge")) {
                    Machine xlm = new Machine("zoom.xlarge");
                    xlm.setId(currMachine.getId());
                    boolean space;
                    for (Process p : currMachine.getJobs()) {
                        space = xlm.assignJob(p); //assign the job to the smaller machine
                        if(!space){
                            System.out.println("Job can't be assigned!");
                            break;
                        }
                    }
                    hardware.add(xlm);
                    hardware.remove(currMachine);
                }
            }
            else if(currMachine.getCore() <= 6 & currMachine.getCore() > 4 &
                    currMachine.getMemory() > 16 & currMachine.getMemory() <= 24){
                if (currMachine.getType().equals("zoom.2xlarge")){
                    //**** Create a Large and an xLarge machine and try to assign the jobs of 2xLarge to them:
                    Machine lm = new Machine("zoom.large");
                    Machine xlm = new Machine("zoom.xlarge");
                    boolean space1;
                    boolean space2;





                }


            }
        }
        return hardware;
    }

    /** Take the Machine Assignment and makes improvements */
    public static List<Machine> improveAssignment(List<Machine> hardware){
//        Collections.sort(hardware, new Comparator<Machine>()
//        {
//            public int compare(Machine m1, Machine m2)
//            {
//                    if (m1.availableCore() == m2.availableCore())
//                        return 0;
//                    else if (m1.availableCore() > m2.availableCore())
//                        return -1;
//                    else
//                        return 1;
//            }
//
//        });

        Collections.sort(hardware, new Comparator<Machine>()
        {
            public int compare(Machine m1, Machine m2)
            {
                if (m1.availableMemory() == m2.availableMemory())
                    return 0;
                else if (m1.availableMemory() > m2.availableMemory())
                    return -1;
                else
                    return 1;
            }

        });
        //*** Try to find places on other machines to assign jobs from the least allocated machines:
        boolean hasSpace;
        for(int i = 0 ; i < hardware.size() ; i++){
            for(int k = 0 ; k < hardware.get(i).getJobs().size() ; k++){
                Process job = hardware.get(i).getJobs().get(k);
                for(int j = i+1 ; j < hardware.size() ; j++){
                    if(job.getCore() < hardware.get(j).availableCore() &
                            job.getMemory() < hardware.get(j).availableMemory()){
                        hardware.get(i).removeJob(job);
                        hasSpace = hardware.get(j).assignJob(job);
                        if(!hasSpace) continue;
                    }
                }
            }
            //** If all the jobs on this machine are re-assigned, removed the machine from the fleet:
            if(hardware.get(i).getCoreUsage()==0 & hardware.get(i).getMemoryUsage()==0)
                hardware.remove(i);
        }


        return hardware;
    }



    /** Return Total Cost of Hardware */
    public static double totalCost(List<Machine> hardware){
        double cost = 0.0;
        for(Machine m : hardware)
            cost += m.getDailyCost();

        return cost;
    }

    /** */
    public static void main(String[] args) {
        String file = "./data/process.csv";
        ArrayList<Process> process = TestRun.readProcess(file);


        String machineFile = "./data/machine.csv";
        List<Machine> machines = TestRun.readMachine(machineFile);

        System.out.println("*******------------ Initial Solutions --------******");

        List<Machine> reserve = TestRun.initialize(process,machines);
        System.out.println(TestRun.totalCost(reserve)+"; "+reserve.size());



//        for(Machine m : reserve) {
//            m.printMachine();
//            System.out.println("-------------------------------");
//        }
//        System.out.println(reserve.size());
//        System.out.println("Total Cost = "+TestRun.totalCost(reserve));

        System.out.println("***********************************************");
        System.out.println("&&&&&&&&&&&&&&&  Reduce Machines &&&&&&&&&&&&&&");
        System.out.println("***********************************************");
        List<Machine> reserve2 = TestRun.reduceMachine(reserve);

        //reserve2.forEach(Machine::printMachine);

        //System.out.println(reserve2.size()+","+reserve.size());
        System.out.println("Total Cost = "+TestRun.totalCost(reserve2)+"; "+reserve2.size());

        List<Machine> reserve3 = TestRun.improveAssignment(reserve2);
        System.out.println("Total Cost = "+TestRun.totalCost(reserve3)+"; "+reserve3.size());
//        for(Machine m : reserve3)
//            System.out.printf(m.availableMemory()+",");

        //reserve3.forEach(m-> System.out.println(m.availableMemory()));
        List<Machine> reserve4 = TestRun.improveAssignment(reserve3);
        reserve4.forEach(Machine::printMachine);
        System.out.println("Total Cost = "+TestRun.totalCost(reserve4)+"; "+reserve4.size());



    }


}










//    /**  */
//    public static List<Machine> reduceMachine(List<Machine> hardware) {
//        for (Machine m : hardware) {
//            // if any of x or 2x machines have less than 2/8 loads assign all to a large machine:
//            if (m.getCoreUsage() <= 2 & m.getMemoryUsage() <= 8) {
//                if (m.getType().equals("zoom.2xlarge") | m.getType().equals("zoom.xlarge")) {
//                    Machine lm = new Machine("zoom.large");
//                    lm.setId(m.getId());
//                    boolean space;
//                    for (Process p : m.getJobs()) {
//                        space = lm.assignJob(p); //assign the job to the smaller machine
//                        if(!space){
//                            System.out.println("Job can't be assigned!");
//                            break;
//                        }
//                    }
//                    hardware.add(lm);
//                    System.out.println("here.......");
//                    hardware.remove(m);
//                }
//            } else if (m.getCore() <= 4 & m.getCore() > 2 & m.getMemory() > 8 & m.getMemory() <= 16) {
//                if (m.getType().equals("zoom.2xlarge")) {
//                    Machine xlm = new Machine("zoom.xlarge");
//                    xlm.setId(m.getId());
//                    boolean space;
//                    for (Process p : m.getJobs()) {
//                        space = xlm.assignJob(p); //assign the job to the smaller machine
//                        if(!space){
//                            System.out.println("Job can't be assigned!");
//                            break;
//                        }
//                    }
//                    hardware.add(xlm);
//                    hardware.remove(m);
//                }
//            }
//        }
//
//        return hardware;
//    }



//        Collections.sort(hardware, new Comparator<Machine>()
//        {
//            public int compare(Machine m1, Machine m2) {
//                    if (m1.availableCore() == m2.availableCore()
//                            & m1.availableMemory() == m2.availableMemory())
//                        return 0;
//                    else if (m1.availableCore() > m2.availableCore()
//                            & m1.availableMemory() > m2.availableMemory())
//                        return -11;
//                    else if(m1.availableCore() < m2.availableCore()
//                            & m1.availableMemory() < m2.availableMemory())
//                        return 1;
//                    else{
//                        if(m1.getCoreUtilization()+m1.getMemoryUtilization()
//                                ==m2.getCoreUtilization()+m2.getMemoryUtilization())
//                            return 0;
//                        else if(m1.getCoreUtilization()+m1.getMemoryUtilization()
//                                > m2.getCoreUtilization()+m2.getMemoryUtilization())
//                            return -1;
//                        else
//                            return 1;
//                    }
//
//                }
//            }
//        );
