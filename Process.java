package resources;

import java.util.Comparator;
import java.util.Iterator;

/**
 * This class represents the process type
 * Created by babak_khorrami on 7/21/16.
 */

public class Process implements Comparator<Process>{
    private String type;
    private double core;
    private double memory;

    /** Constructor */
    public Process(String type , double core , double mem){
        this.type = type;
        this.core = core;
        this.memory = mem;
    }

    /** Return process type */
    public String getType(){
        return this.type;
    }

    /** Return process core requirements */
    public double getCore(){
        return this.core;
    }

    /** Return process memory requirements */
    public double getMemory(){
        return this.memory;
    }


    @Override
    public int compare(Process p1 , Process p2){
        return 0;
    }

    public int equals(Process p){
        if(this.type.compareToIgnoreCase(p.getType())==0 & this.core==p.getCore() & this.memory==p.getMemory())
            return 0;
        else
            return -1;

    }

    /** Print the Process */
    public void printJob(){
        System.out.println("Process: "+this.type+", Core: "+this.core+", Memory: "+this.memory);
    }
}
