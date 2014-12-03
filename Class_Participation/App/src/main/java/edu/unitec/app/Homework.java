package edu.unitec.app;

import java.io.Serializable;

/**
 * Created by Alberto on 11/06/2014.
 */
public class Homework implements Serializable {
    private int HomeworkId;
    private String HomeworkName;
    private int SectionId;
    private String HomeworkUUID;

    public Homework(int homeworkId, String homeworkName, int sectionId , String UUID) {
        HomeworkId = homeworkId;
        HomeworkName = homeworkName;
        SectionId = sectionId;
        HomeworkUUID = UUID;
    }

    public Homework(String homeworkName, int sectionId , String UUID) {
        HomeworkName = homeworkName;
        SectionId = sectionId;
        HomeworkUUID = UUID;
    }

    public int getHomeworkId() {
        return HomeworkId;
    }

    public void setHomeworkId(int homeworkId) {
        HomeworkId = homeworkId;
    }

    public String getHomeworkName() {
        return HomeworkName;
    }

    public void setHomeworkName(String homeworkName) {
        HomeworkName = homeworkName;
    }

    public int getSectionId() {
        return SectionId;
    }

    public void setSectionId(int sectionId) {
        SectionId = sectionId;
    }

    public String getHomeworkUUID() {
        return HomeworkUUID;
    }

    public void setHomeworkUUID(String homeworkUUID) {
        HomeworkUUID = homeworkUUID;
    }

    @Override
    public String toString() {
        return "Homework{" +
                "HomeworkId=" + HomeworkId +
                ", HomeworkName='" + HomeworkName + '\'' +
                ", SectionId=" + SectionId +
                '}';
    }
}
