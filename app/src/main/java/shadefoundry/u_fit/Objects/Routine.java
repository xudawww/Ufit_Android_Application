package shadefoundry.u_fit.Objects;

import java.io.Serializable;
import java.util.ArrayList;

import shadefoundry.u_fit.Objects.Exercise;

public class Routine implements Serializable {
    private String id;



    private long starttime;



    private long endtime;



    private boolean isprimary,oncalendar;


    private ArrayList<Exercise> execiseList = new ArrayList<Exercise>();



    private String title;
    public Routine(){}
    public Routine(String routineId, long startTime, long endTime, boolean isPrimary, boolean onCalendar, ArrayList<Exercise> exerciseList,String title) {
        this.id= routineId;
        this.starttime = startTime;
        this.endtime = endTime;
        this.isprimary = isPrimary;
        this.oncalendar = onCalendar;
        this.title = title;

    }
    public void setExerciseList(ArrayList<Exercise>  e){
        this.execiseList=e;
    }
    public void addExerciseList(Exercise e){
        this.execiseList.add(e);
    }
    public ArrayList<Exercise> getExeciseList() {
        return execiseList;
    }



    public Routine(String id, long starttime, long endtime, boolean isprimary, boolean oncalendar,String title) {
        this.id = id;
        this.starttime = starttime;
        this.endtime = endtime;
        this.isprimary = isprimary;
        this.oncalendar = oncalendar;
        this.title = title;

    }

    //functions to be move to api


    //Getters and Setters
    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getid() {
        return id;
    }

    public void setRoutineId(String routineId) {
        this.id = routineId;
    }
    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public boolean isIsprimary() {
        return isprimary;
    }

    public void setIsprimary(boolean isprimary) {
        this.isprimary = isprimary;
    }
    public boolean isOnCalendar() {
        return oncalendar;
    }

    public void setOnCalendar(boolean onCalendar) {
        this.oncalendar = onCalendar;
    }



}
