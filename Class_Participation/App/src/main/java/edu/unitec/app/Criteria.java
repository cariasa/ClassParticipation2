package edu.unitec.app;

/**
 * Created by Alberto on 12/06/2014.
 */
public class Criteria {
    private int CriteriaId;
    private String CriteriaName;
    private double CriteriaWeight;
    private int CriteriaHomeworkId;

    public Criteria(String criteriaName, double criteriaWeight, int criteriaHomeworkId) {
        CriteriaName = criteriaName;
        CriteriaWeight = criteriaWeight;
        CriteriaHomeworkId = criteriaHomeworkId;
    }

    public Criteria(int criteriaId, String criteriaName, double criteriaWeight, int criteriaHomeworkId) {
        CriteriaId = criteriaId;
        CriteriaName = criteriaName;
        CriteriaWeight = criteriaWeight;
        CriteriaHomeworkId = criteriaHomeworkId;
    }

    public int getCriteriaId() {
        return CriteriaId;
    }

    public void setCriteriaId(int criteriaId) {
        CriteriaId = criteriaId;
    }

    public String getCriteriaName() {
        return CriteriaName;
    }

    public void setCriteriaName(String criteriaName) {
        CriteriaName = criteriaName;
    }

    public double getCriteriaWeight() {
        return CriteriaWeight;
    }

    public void setCriteriaWeight(double criteriaWeight) {
        CriteriaWeight = criteriaWeight;
    }

    public int getCriteriaHomeworkId() {
        return CriteriaHomeworkId;
    }

    public void setCriteriaHomeworkId(int criteriaHomeworkId) {
        CriteriaHomeworkId = criteriaHomeworkId;
    }

    @Override
    public String toString() {
        return "Criteria{" +
                "CriteriaHomeworkId=" + CriteriaHomeworkId +
                ", CriteriaWeight=" + CriteriaWeight +
                ", CriteriaName='" + CriteriaName + '\'' +
                ", CriteriaId=" + CriteriaId +
                '}';
    }
}
