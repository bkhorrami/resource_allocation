package resources;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


/**
 * This Class Represents Machine Data
 * Created by babak_khorrami on 7/21/16.
 */
public class Machine implements Comparable<Machine> , Comparator<Machine>{
    private int id;
    private String type;
    private double core;
    private double memory;
    private double dailyCost;
    private List<Process> jobs; //List of processes Assigned to this machine
    private double coreUsage; //How much core is used by assigned processes
    private double memoryUsage; //How much memory is used by assigned processes


    /** Default Constructor */
    public Machine() {
        this.id = 0;
        this.type = null;
        this.core = 0;
        this.memory = 0;
        this.dailyCost = 0;
        this.jobs = new ArrayList<Process>();
        this.coreUsage = 0;
        this.memoryUsage = 0;
    }

    /**
     * Constructor
     */
    public Machine(String t, double c, double m, double d) {
        this.id = 0;
        this.type = t;
        this.core = c;
        this.memory = m;
        this.dailyCost = d;
        this.jobs = new ArrayList<Process>();
        this.coreUsage = 0;
        this.memoryUsage = 0;
    }

    /** Copy Constructor */
    public static Machine copy(Machine m){
        Machine newMachine = new Machine();
        newMachine.id = 0;
        newMachine.type = m.getType();
        newMachine.core = m.getCore();
        newMachine.memory = m.getMemory();
        newMachine.dailyCost = m.getDailyCost();
        newMachine.jobs = new ArrayList<Process>();
        newMachine.coreUsage = m.getCoreUsage();
        newMachine.memoryUsage = m.getMemoryUsage();
        return newMachine;
    }

    /** Helper Constructor to create new Machine based on hardware name */
    public Machine(String str){
        if(str.equals("zoom.large")){
            this.type = str;
            this.core = 2.0;
            this.memory = 8.0;
            this.dailyCost = 2.0;
        }
        else if(str.equals("zoom.xlarge")){
            this.type = str;
            this.core = 4.0;
            this.memory = 16.0;
            this.dailyCost = 3.95;
        }
        else if(str.equals("zoom.2xlarge")){
            this.type = str;
            this.core = 8.0;
            this.memory = 32.0;
            this.dailyCost = 7.90;
        }
        else{
            System.out.println("No Such Machine Exists!");
            return;
        }
        this.id = 0;
        this.jobs = new ArrayList<Process>();
        this.coreUsage = 0;
        this.memoryUsage = 0;
    }

    /** Return true if machine can process job p */
    public boolean checkLoad(Process p){
        if(p.getCore() <= (this.core - this.coreUsage) & p.getMemory() <= (this.memory - this.memoryUsage))
            return true;
        else
            return false;
    }

    /** If possible, Assign Process p to the Machine and Update Memory and Core Usages */
    public boolean assignJob(Process p){
        boolean possible = false;
        if(this.checkLoad(p)){
            possible = true;
            //** Add the process to the list of assigned processes and update memory and core usages:
            this.jobs.add(p);
            this.coreUsage += p.getCore();
            this.memoryUsage += p.getMemory();
        }
        return possible;
    }

    /** Remove process p from the machine and free up memory and core resources */
//    public boolean removeJob(Process pr){
//        //** Find the process on the list of jobs assigned to the machine and remove it
//        boolean found = false;
//        for(Process e : this.jobs){
//            if(e.equals(pr)==0){
//                found = true;
//                this.jobs.remove(e);
//                this.coreUsage -= e.getCore();  //*** Free-up core
//                this.memoryUsage -= e.getMemory(); //*** Free-up memory
//            }
//        }
//        return found;
//    }

    public boolean removeJob(Process pr){
        //** Find the process on the list of jobs assigned to the machine and remove it
        boolean found = false;
        Iterator<Process> pIter = this.jobs.iterator();
        while(pIter.hasNext()){
            Process currJob = pIter.next();
            if(currJob.equals(pr)==0){
                found = true;
                this.coreUsage -= currJob.getCore();  //*** Free-up core
                this.memoryUsage -= currJob.getMemory(); //*** Free-up memory
                pIter.remove();
            }
        }
        return found;
    }

    /** Removes the Process/Job in Jobs Array at index i */
    public void removeJob(int i){
        if(i < 0 | i > this.jobs.size()){
            System.out.println("The index doesn't exist!");
            return;
        }
        this.jobs.remove(i);
        this.coreUsage = this.coreUsage - this.jobs.get(i).getCore();
        this.memoryUsage = this.memoryUsage - this.jobs.get(i).getMemory();
    }

    /** Transfer Jobs from machine to another one */
//    public boolean transferJobs(ArrayList<Machine> mcList){
//        Iterator<Process> pIter = this.jobs.iterator();
//        boolean hasSpace;
//        for(Machine mc : mcList){
//            while(pIter.hasNext()){
//                Process currJob = pIter.next();
//                hasSpace = mc.assignJob(currJob);
//                if(!hasSpace) break; //If the current machine has no space, go to the next one.
//            }
//        }
//        if(this.jobs.isEmpty()){
//            return true;
//        }
//
//
//
//
//    }

    /** Return Core Usage by Assigned Processes */
    public double getCoreUsage(){
        return this.coreUsage;
    }

    /** Return Memory Usage by Assigned Processes */
    public double getMemoryUsage(){
        return this.memoryUsage;
    }

    /**
     * Return Machine Type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Return Machine Cores
     */
    public double getCore() {
        return this.core;
    }

    /**
     * Return Machine Memory
     */
    public double getMemory() {
        return this.memory;
    }

    /**
     * Return Machine's Daily Cost
     */
    public double getDailyCost() {
        return this.dailyCost;
    }


    /**
     * Return Available Core
     */
    public double availableCore(){
        return (this.core - this.coreUsage);
    }

    /** Return Available Memory */
    public double availableMemory(){
        return (this.memory - this.memoryUsage);
    }

    /** Return Job List */
    public List<Process> getJobs(){
        return this.jobs;
    }

    /** Return Percentage of core used */
    public double getCoreUtilization(){
        return this.coreUsage/this.core;
    }

    /** Return Percentage of Memory used */
    public double getMemoryUtilization(){
        return this.memoryUsage/this.memory;
    }

    /** Print Machine's Data */
    public void printMachine(){
        System.out.println("("+this.id+")"+" Machine Type: "+this.getType()+", "+this.jobs.size()+" jobs are assigned: ");
//        for(Process j : this.jobs)
//            j.printJob();
        this.jobs.forEach(Process::printJob);
        System.out.println("Core Usage: "+this.coreUsage+", Memory Usage: "+this.memoryUsage);
        System.out.println("Core Available: "+(this.availableCore())+", Memory Available: "+(this.availableMemory()));
        System.out.println("----------------------------------------");
    }

    /** Return Machine's ID */
    public int getId(){
        return this.id;
    }

    /** Set the Machine's ID */
    public void setId(int i){
        this.id = i;
    }

    /**
     * Compare Two Machines based on Daily Cost
     */
    @Override
    public int compareTo(Machine m) {
        if (this.dailyCost == m.getDailyCost())
            return 0;
        else if (this.dailyCost > m.getDailyCost())
            return 1;
        else
            return -1;
    }

    public int compare(Machine m1, Machine m2){
        return 0;
    }

    /** Are two machines equal **/
    public boolean equals(Machine mc){
        if(this.type.equals(mc.getType()) & this.coreUsage == mc.getCoreUsage()
                & this.memoryUsage == mc.getMemoryUsage() & this.jobs.containsAll(mc.getJobs())){
            return true;
        }
        else
            return false;
    }

}
