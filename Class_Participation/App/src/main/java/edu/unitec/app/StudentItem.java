package edu.unitec.app;

import android.widget.Button;

/**
 * Created by Alberto on 08/06/2014.
 */
public class StudentItem {
    protected String studentName;
    public Button buttonParticipation;
    public Button buttonHomework;
    public Button buttonStatistics;
    protected long StudentItemId;

    public StudentItem(String studentName) {
        this.studentName = studentName;
    }

    public StudentItem(String studentName, Button buttonParticipation, Button buttonHomework, Button buttonStatistics, long studentItemId) {
        this.studentName = studentName;
        this.buttonParticipation = buttonParticipation;
        this.buttonHomework = buttonHomework;
        this.buttonStatistics = buttonStatistics;
        StudentItemId = studentItemId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Button getButtonParticipation() {
        return buttonParticipation;
    }

    public void setButtonParticipation(Button buttonParticipation) {
        this.buttonParticipation = buttonParticipation;
    }

    public Button getButtonHomework() {
        return buttonHomework;
    }

    public void setButtonHomework(Button buttonHomework) {
        this.buttonHomework = buttonHomework;
    }

    public Button getButtonStatistics() {
        return buttonStatistics;
    }

    public void setButtonStatistics(Button buttonStatistics) {
        this.buttonStatistics = buttonStatistics;
    }

    public long getStudentItemId() {
        return StudentItemId;
    }

    public void setStudentItemId(long studentItemId) {
        StudentItemId = studentItemId;
    }
}
