package edu.unitec.app;

import java.util.Calendar;

/**
 * Created by Nivx on 12/6/14.
 */
public class SemesterQuarter {

    private int year;
    private int semester;
    private int quarter;

    public final static int CURRENT_YEAR = getCurrentYear();
    public final static int CURRENT_SEMESTER = getCurrentSemester();
    public final static int CURRENT_QUARTER = getCurrentQuarter();

    public SemesterQuarter(int year, int semester, int quarter) {
        this.year = year;
        this.semester = semester;
        this.quarter = quarter;
    }

    public SemesterQuarter() {
        this.year = getCurrentYear();
        this.semester = getCurrentSemester();
        this.quarter = getCurrentQuarter();
    }

    public int getYear() {

        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    private static int getCurrentYear(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    private static int getCurrentSemester(){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        if ( month <= 5 ){
            return 1;
        }
        return 2;
    }

    private static int getCurrentQuarter(){
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH); //January == 0
        int currentSemester = getCurrentSemester();
        int currentQuarter = 0;

        if ( currentSemester == 1 ){
            if ( currentMonth <= 2 ){
                currentQuarter = 1;
            }else{
                currentQuarter = 2;
            }
        }else if ( currentSemester == 2 ){
            if ( currentMonth <= 8 ){
                currentQuarter = 3;
            }else{
                currentQuarter = 4;
            }
        }
        return currentQuarter;
    }

    public boolean checkPrevious(){
        if (year == CURRENT_YEAR && semester == CURRENT_SEMESTER && quarter == CURRENT_QUARTER){
            return false;
        }else{
            return true;
        }
    }

}
