package edu.unitec.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

//SYNCSTATE 1 = INSERT
//SYNCSTATE 2 = UPDATE
//SYNCSTATE 3 = DELETE
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Participation";

    // Course Table
    private static final String TABLE_COURSE = "course";
    // Table Course - Table Fields
    private static final String COURSE_UUID = "TeacherUUID";
    private static final String COURSE_ID = "CourseId";
    private static final String COURSE_CODE = "CourseCode";
    private static final String COURSE_NAME = "CourseName";
    private static final String COURSE_DESC = "CourseDescription";

    // Section Table
    private static final String TABLE_SECTION = "section";
    // Table Section - Table Fields
    private static final String SECT_UUID = "TeacherUUID";
    private static final String SECT_ID = "SectionId";
    private static final String SECT_COURSE = "CourseId";
    private static final String SECT_QTR = "SectionQuarter";
    private static final String SECT_SEM = "SectionSemester";
    private static final String SECT_YEA = "SectionYear";
    private static final String SECT_CODE = "SectionCode";

    // Students Table
    private static final String TABLE_STUDENT = "student";
    // Table Students Fie- Table Fieldslds
    private static final String STU_EMAIL = "StudentEmail";
    private static final String STU_PASSWORD = "StudentPassword";
    private static final String STU_ID = "StudentId";
    private static final String STU_NAME = "StudentName";
    private static final String STU_MAJOR = "StudentMajor";

    // Students Per Section Table
    private static final String TABLE_STUDENTSECTION = "studentSection";
    // Table Students Per Section - Table Fields
    private static final String STUSEC_UUID = "TeacherUUID";
    private static final String STUSEC_ID = "StudentSectionId";
    private static final String STUSEC_SECT = "SectionId";
    private static final String STUSEC_STUD = "StudentId";
    private static final String STUSEC_FINAL = "StudentSectionFinal";

    // Partipations Per Student Table
    private static final String TABLE_PARTICIPATION = "participationStudent";
    // Table Participations Per Student - Fields
    private static final String PART_UUID = "TeacherUUID";
    private static final String PART_ID = "ParticipationId";
    private static final String PART_STUSECT = "StudentSectionId";
    private static final String PART_GRADE = "ParticipationGrade";
    private static final String PART_DATE = "ParticipationDate";
    private static final String PART_COMMENT = "ParticipationComment";

    // Homework Table
    private static final String TABLE_HOMEWORK = "homework";
    // Table Homework - Table Fields
    private static final String HOMEWORK_UUID = "TeacherUUID";
    private static final String HOMEWORK_ID = "HomeworkId";
    private static final String HOMEWORK_NAME = "HomeworkName";
    private static final String HOMEWORK_SECID = "SectionId";

    // Critera Per Homework Table
    private static final String TABLE_CRITERIA = "criteria";
    // Table Homework - Table Fields
    private static final String CRITERIA_UUID = "TeacherUUID";
    private static final String CRITERIA_ID = "CriteriaId";
    private static final String CRITERIA_NAME = "CriteriaName";
    private static final String CRITERIA_HOMEWORK = "HomeworkId";
    private static final String CRITERIA_WEIGHT = "CriteriaWeight";

    // Homework Per Student Table
    private static final String TABLE_HOMESTU = "homeworkStudent";
    // Table Homework - Table Fields
    private static final String HOMESTU_UUID = "TeacherUUID";
    private static final String HOMESTU_HomeworkStudentDate = "HomeworkStudentDate";
    private static final String HOMESTU_ID = "HomeworkStudentId";
    private static final String HOMESTU_CriteriaId = "CriteriaId";
    private static final String HOMESTU_StudentId = "StudentId";
    private static final String HOMESTU_Grade = "HomeworkStudentGrade";

    // Teacher Table
    private static final String TABLE_TEACHER = "Teacher";
    // Table Teacher - Table Fields
    private static final String TEACHER_UUID = "TeacherUUID";
    private static final String TEACHER_NAME = "TeacherName";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TEACHER_TABLE = "CREATE TABLE " + TABLE_TEACHER + " (" +
                TEACHER_UUID + " TEXT PRIMARY KEY, " +
                TEACHER_NAME + " TEXT," +
                " SyncState INTEGER )";


        String CREATE_COURSE_TABLE =
                "CREATE TABLE " + TABLE_COURSE + " (" +
                        COURSE_ID + " INTEGER, " +
                        COURSE_UUID + " TEXT, " +
                        COURSE_CODE + " TEXT," +
                        COURSE_NAME + " TEXT," +
                        COURSE_DESC + " TEXT, " +
                        " SyncState INTEGER, "+
                        "PRIMARY KEY (" + COURSE_UUID + "," + COURSE_ID + "), " +
                        "FOREIGN KEY(" + COURSE_UUID + ") REFERENCES " + TABLE_TEACHER + "(" + TEACHER_UUID + ")" +
                        ")";
        String CREATE_SECTION_TABLE =
                "CREATE TABLE " + TABLE_SECTION + " (" +
                        SECT_UUID + " TEXT , " +
                        SECT_ID + " INTEGER , " +
                        SECT_COURSE + " INTEGER," +
                        SECT_QTR + " INTEGER," +
                        SECT_SEM + " INTEGER," +
                        SECT_YEA + " INTEGER, " +
                        SECT_CODE + " TEXT," +
                        " SyncState INTEGER, "+
                        "PRIMARY KEY (" + SECT_UUID + "," + SECT_ID + "), " +
                        "FOREIGN KEY(" + SECT_COURSE + ") REFERENCES " + TABLE_COURSE + "(" + COURSE_ID + "), " +
                        "FOREIGN KEY(" + SECT_UUID + ") REFERENCES " + TABLE_TEACHER + "(" + TEACHER_UUID + ")" +
                        ")";
        String CREATE_STUDENT_TABLE =
                "CREATE TABLE " + TABLE_STUDENT + " (" +
                        STU_ID + " INTEGER ," +
                        STU_NAME + " TEXT," +
                        STU_MAJOR + " TEXT," +
                        STU_EMAIL + " TEXT PRIMARY KEY," +
                        STU_PASSWORD + " TEXT, " +
                        " SyncState INTEGER "+
                        ")";

        String CREATE_STUDENTSECTION_TABLE =
                "CREATE TABLE " + TABLE_STUDENTSECTION + " (" +
                        STUSEC_UUID + " TEXT, " +
                        STUSEC_ID + " INTEGER," +
                        STUSEC_SECT + " INTEGER," +
                        STUSEC_STUD + " INTEGER," +
                        STUSEC_FINAL + " REAL," +
                        " SyncState INTEGER, "+
                        "PRIMARY KEY (" + STUSEC_UUID + "," + STUSEC_ID + "), " +
                        "FOREIGN KEY(" + STUSEC_SECT + ") REFERENCES " + TABLE_SECTION + "(" + SECT_ID + "), " +
                        "FOREIGN KEY(" + STUSEC_STUD + ") REFERENCES " + TABLE_STUDENT + "(" + STU_ID + ")," +
                        "FOREIGN KEY(" + STUSEC_UUID + ") REFERENCES " + TABLE_TEACHER + "(" + TEACHER_UUID + ")" +
                        ")";
        String CREATE_PARTICIPATION_TABLE =
                "CREATE TABLE " + TABLE_PARTICIPATION + " (" +
                        PART_UUID + " TEXT, " +
                        PART_ID + " INTEGER," +
                        PART_STUSECT + " INTEGER," +
                        PART_GRADE + " REAL," +
                        PART_DATE + " TEXT," +
                        PART_COMMENT + " TEXT," +
                        " SyncState INTEGER, "+
                        "PRIMARY KEY (" + PART_UUID + "," + PART_ID + "), " +
                        "FOREIGN KEY(" + PART_STUSECT + ") REFERENCES " + TABLE_STUDENTSECTION + "(" + STUSEC_ID + ")," +
                        "FOREIGN KEY(" + PART_UUID + ") REFERENCES " + TABLE_TEACHER + "(" + TEACHER_UUID + ")" +
                        ")";
        String CREATE_HOMEWORK_TABLE =
                "CREATE TABLE " + TABLE_HOMEWORK + " (" +
                        HOMEWORK_UUID + " TEXT, " +
                        HOMEWORK_ID + " INTEGER, " +
                        HOMEWORK_NAME + " TEXT," +
                        HOMEWORK_SECID + " INTEGER," +
                        " SyncState INTEGER, "+
                        "PRIMARY KEY (" + HOMEWORK_UUID + "," + HOMEWORK_ID + "), " +
                        "FOREIGN KEY(" + HOMEWORK_SECID + ") REFERENCES " + TABLE_SECTION + "(" + SECT_ID + ")," +
                        "FOREIGN KEY(" + HOMEWORK_UUID + ") REFERENCES " + TABLE_TEACHER + "(" + TEACHER_UUID + ")" +
                        ")";
        String CREATE_CRITERIA_TABLE =
                "CREATE TABLE " + TABLE_CRITERIA + " (" +
                        CRITERIA_UUID + " TEXT, " +
                        CRITERIA_ID + " INTEGER," +
                        CRITERIA_NAME + " TEXT," +
                        CRITERIA_WEIGHT + " REAL," +
                        CRITERIA_HOMEWORK + " INTEGER," +
                        " SyncState INTEGER, "+
                        "PRIMARY KEY (" + CRITERIA_UUID + "," + CRITERIA_ID + "), " +
                        "FOREIGN KEY(" + CRITERIA_HOMEWORK + ") REFERENCES " + TABLE_HOMEWORK + "(" + HOMEWORK_ID + ")," +
                        "FOREIGN KEY(" + CRITERIA_UUID + ") REFERENCES " + TABLE_TEACHER + "(" + TEACHER_UUID + ")" +
                        ")";
        String CREATE_HOMESTU_TABLE =
                "CREATE TABLE " + TABLE_HOMESTU + " (" +
                        HOMESTU_UUID + " TEXT, " +
                        HOMESTU_ID + " INTEGER," +
                        HOMESTU_Grade + " REAL," +
                        HOMESTU_CriteriaId + " INTEGER," +
                        HOMESTU_StudentId + " INTEGER," +
                        HOMESTU_HomeworkStudentDate + " TEXT, " +
                        " SyncState INTEGER, "+
                        "PRIMARY KEY (" + HOMESTU_UUID + "," + HOMESTU_ID + "), " +
                        "FOREIGN KEY(" + HOMESTU_CriteriaId + ") REFERENCES " + TABLE_CRITERIA + "(" + CRITERIA_ID + "), " +
                        "FOREIGN KEY(" + HOMESTU_StudentId + ") REFERENCES " + TABLE_STUDENT + "(" + STU_ID + ")," +
                        "FOREIGN KEY(" + HOMESTU_UUID + ") REFERENCES " + TABLE_TEACHER + "(" + TEACHER_UUID + ")" +
                        ")";

        db.execSQL(CREATE_TEACHER_TABLE);
        db.execSQL(CREATE_COURSE_TABLE);
        db.execSQL(CREATE_SECTION_TABLE);
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_STUDENTSECTION_TABLE);
        db.execSQL(CREATE_PARTICIPATION_TABLE);
        db.execSQL(CREATE_HOMEWORK_TABLE);
        db.execSQL(CREATE_CRITERIA_TABLE);
        db.execSQL(CREATE_HOMESTU_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTSECTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOMEWORK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRITERIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOMESTU);
        onCreate(db);
    }

    void addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COURSE_ID, getLastCourseID(course.getCourseUUID()));
        values.put(COURSE_UUID, course.getCourseUUID());
        values.put(COURSE_CODE, course.getCourseCode());
        values.put(COURSE_NAME, course.getCourseName());
        values.put(COURSE_DESC, course.getCourseDescription());
        values.put("SyncState",1);
        db.insert(TABLE_COURSE, null, values);
        db.close();
    }

    int getLastCourseID(String UUID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT CourseId FROM course WHERE TeacherUUID  = '" + UUID + "' ORDER BY CourseId DESC LIMIT 1";


        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int retval = cursor.getInt(0) + 1;
            cursor.close();
            return retval;
        } else {
            cursor.close();
            return 1;
        }

    }

    void addStudentTable(Student student, Section section) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STUSEC_ID, getStudentSectionID(section.get_SectionUUID()));
        values.put(STUSEC_UUID, section.get_SectionUUID());
        values.put(STUSEC_SECT, section.get_SectionId());
        values.put(STUSEC_STUD, student.get_StudentId());
        values.put(STUSEC_FINAL, 0);
        values.put("SyncState",1);
        db.insert(TABLE_STUDENTSECTION, null, values);
        db.close();
    }

    int getStudentSectionID(String UUID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT StudentSectionId FROM studentSection WHERE TeacherUUID  = '" + UUID + "' ORDER BY StudentSectionId DESC LIMIT 1";


        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int retval = cursor.getInt(0) + 1;
            cursor.close();
            return retval;
        } else {
            cursor.close();
            return 1;
        }

    }

    List<Integer> getStudentIdListBySectionId(int sectionId, String UUID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + STUSEC_STUD +
                " FROM " + TABLE_STUDENTSECTION + " WHERE " + STUSEC_SECT + " = " + sectionId + " AND " + STUSEC_UUID + " = '" + UUID + "' ";
        Cursor cursor = db.rawQuery(query, null);
        List<Integer> studentIdList = new ArrayList<Integer>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                studentIdList.add(cursor.getInt(0));
                cursor.moveToNext();
            }

        }
        cursor.close();
        return studentIdList;
    }

    void addParticipation(Participation participation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PART_ID, getParticipationID(participation.get_ParcicipationUUID()));
        values.put(PART_UUID, participation.get_ParcicipationUUID());
        values.put(PART_STUSECT, participation.get_StudentSectionId());
        values.put(PART_GRADE, participation.get_ParticipationGrade());
        values.put(PART_DATE, participation.get_ParticipationDate());
        values.put(PART_COMMENT, participation.get_ParticipationComment());
        values.put("SyncState",1);
        db.insert(TABLE_PARTICIPATION, null, values);
        db.close();
    }

    int getParticipationID(String UUID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT ParticipationId FROM participationStudent WHERE TeacherUUID  = '" + UUID + "' ORDER BY ParticipationId DESC LIMIT 1";


        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int retval = cursor.getInt(0) + 1;
            cursor.close();
            return retval;
        } else {
            cursor.close();
            return 1;
        }

    }

    public List<Participation> getParticipationList(int sectionId, String UUID) {
        List<Participation> participationList = new ArrayList<Participation>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + STUSEC_ID + " FROM " + TABLE_STUDENTSECTION + " WHERE " + STUSEC_SECT + " = " + sectionId + " AND " + STUSEC_UUID + " = '" + UUID + "' ";
        Cursor studentsSections = db.rawQuery(query, null);
        studentsSections.moveToFirst();
        for (int i = 0; i < studentsSections.getCount(); i++) {
            query = "SELECT " + PART_ID + ", " + PART_GRADE + ", " + PART_DATE + ", " + PART_COMMENT + ", " + PART_UUID +
                    " FROM " + TABLE_PARTICIPATION + " WHERE " + PART_STUSECT + " = " + studentsSections.getInt(0) + " AND " + PART_UUID + " = '" + UUID + "'";
            Cursor participations = db.rawQuery(query, null);
            if (participations.getCount() > 0) {
                participations.moveToFirst();
                do {
                    participationList.add(new Participation(
                            participations.getInt(0),
                            participations.getDouble(1),
                            participations.getString(2),
                            participations.getString(3),
                            participations.getString(4)));
                } while (participations.moveToNext());
            }
            participations.close();
            studentsSections.moveToNext();
        }
        studentsSections.close();
        return participationList;
    }

    public String getMaxAverageStudentParticipation(int sectionId, String UUID) {
        //internal class to get the Name of the student and the average
        class StudentAndAverage {
            String name;
            Double average;

            StudentAndAverage(String name, Double avg) {
                this.name = name;
                this.average = avg;
            }

            StudentAndAverage() {
                name = "";
                average = 0.0;
            }
        }
        List<StudentAndAverage> averagesList = new ArrayList<StudentAndAverage>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + STUSEC_ID + ", " + STUSEC_STUD + " FROM " + TABLE_STUDENTSECTION + " WHERE " + STUSEC_SECT + " = " + sectionId + " AND " + STUSEC_UUID + " = '" + UUID + "'";
        Cursor studentsSections = db.rawQuery(query, null);
        studentsSections.moveToFirst();

        for (int i = 0; i < studentsSections.getCount(); i++) {
            double acumGrades = 0, average = 0;
            int quantityOfGrades = 0;
            query = "SELECT " + PART_GRADE +
                    " FROM " + TABLE_PARTICIPATION + " WHERE " + PART_STUSECT + " = " + studentsSections.getInt(0) + " AND " + PART_UUID + " = '" + UUID + "'";
            Cursor participations = db.rawQuery(query, null);
            if (participations.getCount() > 0) {
                participations.moveToFirst();
                do {
                    acumGrades += participations.getDouble(0);
                    quantityOfGrades++;
                } while (participations.moveToNext());
                average = acumGrades / quantityOfGrades;
                String name = this.getStudentName(studentsSections.getInt(1));
                averagesList.add(new StudentAndAverage(name, average));
            }
            participations.close();
            studentsSections.moveToNext();
        }
        studentsSections.close();
        //find the Highest Average
        if (averagesList.size() > 0) {
            double max = 0;
            String maxName = "";
            for (int i = 0; i < averagesList.size(); i++) {
                if (averagesList.get(i).average > max) {
                    max = averagesList.get(i).average;
                    maxName = averagesList.get(i).name;
                }
            }
            return maxName + "\t" + Math.round(max) + "%";
        }
        return "N/A";
    }

    public String getMinAverageStudentParticipation(int sectionId, String UUID) {
        try {
            //internal class to get the Name of the student and the average
            class StudentAndAverage {
                String name;
                Double average;

                StudentAndAverage(String name, Double avg) {
                    this.name = name;
                    this.average = avg;
                }

                StudentAndAverage() {
                    name = "";
                    average = 0.0;
                }
            }

            List<StudentAndAverage> averagesList = new ArrayList<StudentAndAverage>();
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT " + STUSEC_ID + ", " + STUSEC_STUD + " FROM " + TABLE_STUDENTSECTION + " " +
                    "WHERE " + STUSEC_SECT + " = " + sectionId + " AND " + STUSEC_UUID + " = '" + UUID + "'";
            Cursor studentsSections = db.rawQuery(query, null);
            studentsSections.moveToFirst();

            for (int i = 0; i < studentsSections.getCount(); i++) {
                double acumGrades = 0, average = 0;
                int quantityOfGrades = 0;
                query = "SELECT " + PART_GRADE +
                        " FROM " + TABLE_PARTICIPATION + "" +
                        " WHERE " + PART_STUSECT + " = " + studentsSections.getInt(0) + " AND " + PART_UUID + " = '" + UUID + "' ";
                Cursor participations = db.rawQuery(query, null);
                if (participations.getCount() > 0) {
                    participations.moveToFirst();
                    do {
                        acumGrades += participations.getDouble(0);
                        quantityOfGrades++;
                    } while (participations.moveToNext());
                    average = acumGrades / quantityOfGrades;
                    String name = this.getStudentName(studentsSections.getInt(1));
                    averagesList.add(new StudentAndAverage(name, average));
                }
                participations.close();
                studentsSections.moveToNext();
            }
            studentsSections.close();
            //find the Lowest Average
            if (averagesList.size() > 0) {
                double min = averagesList.get(0).average;
                String minName = averagesList.get(0).name;
                for (int i = 0; i < averagesList.size(); i++) {
                    if (averagesList.get(i).average < min) {
                        min = averagesList.get(i).average;
                        minName = averagesList.get(i).name;
                    }
                }
                return minName + "\t" + Math.round(min) + "%";
            } else {
                return "N/A";
            }
        } catch (Exception e) {
            return "N/A";
        }
    }

    public String getMaxAverageStudentHomework(int sectionId, String UUID) {
        //internal class to get the Name of the student and the average
        class StudentAndAverage {
            String name;
            Double average;

            StudentAndAverage(String name, Double avg) {
                this.name = name;
                this.average = avg;
            }

            StudentAndAverage() {
                name = "";
                average = 0.0;
            }
        }
        List<StudentAndAverage> averagesList = new ArrayList<StudentAndAverage>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + STUSEC_ID + ", " + STUSEC_STUD + " FROM " + TABLE_STUDENTSECTION + " " +
                "WHERE " + STUSEC_SECT + " = " + sectionId + " AND " + STUSEC_UUID + " = '" + UUID + "' ";
        Cursor studentsSections = db.rawQuery(query, null);
        studentsSections.moveToFirst();

        for (int i = 0; i < studentsSections.getCount(); i++) {
            double acumGrades = 0, average = 0;
            int quantityOfGrades = 0;
            query = "SELECT " + PART_GRADE +
                    " FROM " + TABLE_PARTICIPATION + " WHERE " + PART_STUSECT + " = " + studentsSections.getInt(0)
                    + " AND " + PART_UUID + " = '" + UUID + "' "
            ;
            Cursor participations = db.rawQuery(query, null);
            if (participations.getCount() > 0) {
                participations.moveToFirst();
                do {
                    acumGrades += participations.getDouble(0);
                    quantityOfGrades++;
                } while (participations.moveToNext());
                average = acumGrades / quantityOfGrades;
                String name = this.getStudentName(studentsSections.getInt(1));
                averagesList.add(new StudentAndAverage(name, average));
            }
            participations.close();
            studentsSections.moveToNext();
        }
        studentsSections.close();
        //find the Highest Average
        double max = 0;
        String maxName = "";
        for (int i = 0; i < averagesList.size(); i++) {
            if (averagesList.get(i).average > max) {
                max = averagesList.get(i).average;
                maxName = averagesList.get(i).name;
            }
        }
        return maxName + "\t" + Math.round(max) + "%";
    }

    String getStudentName(int studentId) {
        String name = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + STU_NAME + " FROM " + TABLE_STUDENT + " WHERE " + STU_ID + " = " + studentId;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            name = cursor.getString(0);
        }
        cursor.close();
        return name;
    }

    List<Participation> getStudentParticipationList(int studentSectionId, String UUID) {
        List<Participation> currentStudentParticipationList = new ArrayList<Participation>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM participationStudent WHERE StudentSectionId=" + studentSectionId + " AND TeacherUUID = '" + UUID + "'";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                currentStudentParticipationList.add(new Participation(cursor.getInt(1), cursor.getInt(2),
                        cursor.getDouble(3), cursor.getString(4),
                        cursor.getString(5), cursor.getString(0)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return currentStudentParticipationList;
    }

    double getFinalGrade(int studentSectionId, String UUID) {
        String selectQuery = "SELECT StudentSectionFinal FROM studentSection WHERE StudentSectionId = " +
                studentSectionId + " AND TeacherUUID = '" + UUID + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        double studentSectionFinal = 0;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            studentSectionFinal = cursor.getDouble(0);
        }
        cursor.close();
        return studentSectionFinal;
    }
//*//
    public void UpdateparticipationStudent(int studentSectionId, double studentSectionFinal, String UUID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE studentSection SET StudentSectionFinal =  " + studentSectionFinal + " " +
                "WHERE StudentSectionId = " +
                studentSectionId + " AND TeacherUUID = '" + UUID + "'";
        db.execSQL(strSQL);
        db.close();
    }

    void addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STU_ID, student.get_StudentId());
        values.put(STU_NAME, student.get_StudentName());
        values.put(STU_MAJOR, student.get_StudentMajor());
        values.put(STU_EMAIL, student.get_StudentEmail());
        values.put(STU_PASSWORD, student.get_StudentId());
        values.put("SyncState",1);
        db.insert(TABLE_STUDENT, null, values);
        db.close();
    }

    void deleteStudent(ArrayList<Integer> toDelete , String UUID , int SectionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL;
        for (int i = 0; i < toDelete.size(); i++) {
            //Eliminar referencia del estudiante de tabla de participaciones por estudiante
            String query = "SELECT StudentSectionId FROM studentSection WHERE StudentId=" + toDelete.get(i) +" AND TeacherUUID = '"+UUID+"' AND SectionId = '" +SectionId+ "'";
            int StudentSectionId = 0;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                StudentSectionId = cursor.getInt(0);
            }
            cursor.close();
            //Eliminar referencia del estudiante de tabla de estudiantes por sección
            strSQL = "DELETE FROM studentSection WHERE StudentId=" + toDelete.get(i) +" AND TeacherUUID = '"+UUID+"' AND StudentSectionId ='"+StudentSectionId+"' ";
            db.execSQL(strSQL);

            strSQL = "DELETE FROM participationStudent WHERE StudentId=" + toDelete.get(i) +" AND TeacherUUID = '"+UUID+"' AND StudentSectionId ='"+StudentSectionId+"' ";
            db.execSQL(strSQL);

            /*
            strSQL = "SELECT HomeworkId FROM homework WHERE SectionId = '"+SectionId+"'";
            cursor = db.rawQuery(query, null);

            if (cursor.getCount() > 0){
                cursor.moveToFirst();

            }
            */
        }
        db.close();
    }

    boolean studentExist(String studentEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + STU_ID + " FROM " + TABLE_STUDENT + " WHERE " + STU_EMAIL + " = '" + studentEmail + "'", null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    boolean tableStudentIsEmpty(int sectionId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + STUSEC_STUD + " FROM " + TABLE_STUDENTSECTION +
                " WHERE " + STUSEC_SECT + " = " + sectionId, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    boolean studentSectionExist(Section section, int studentId, String UUID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + STUSEC_STUD + " FROM " + TABLE_STUDENTSECTION +
                " WHERE " + STUSEC_SECT + " = " + section.get_SectionId() + " AND " + STUSEC_UUID + " = '" + UUID + "'"
                , null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(0) == studentId) {
                    cursor.close();
                    return true;
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return false;
    }

    Course getCourse(int id, String UUID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_COURSE,
                new String[]{COURSE_ID, COURSE_UUID, COURSE_CODE, COURSE_NAME, COURSE_DESC},
                COURSE_ID + "=? and " + COURSE_UUID + " =?",
                new String[]{String.valueOf(id), UUID},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Course course = new Course(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(1));
        cursor.close();
        return course;
    }

    public String getCourseName(int courseId, String UUID) {
        List<Course> courseList = new ArrayList<Course>();
        String selectQuery = "SELECT " + COURSE_NAME + " FROM " + TABLE_COURSE + "" +
                " WHERE " + COURSE_ID + " = " + courseId + " AND " + COURSE_UUID + " = '" + UUID + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            String retVal = cursor.getString(0);
            cursor.close();
            return retVal;
        }
        String retVal = cursor.getString(0);
        cursor.close();
        return retVal;
    }

    public List<Course> getAllCourses(String UUID) {
        List<Course> courseList = new ArrayList<Course>();
        String selectQuery = "SELECT * FROM " + TABLE_COURSE + " " +
                "WHERE " + COURSE_UUID + " = '" + UUID + "' ORDER BY " + COURSE_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Course course = new Course();
                course.setCourseId(Integer.parseInt(cursor.getString(0)));
                course.setCourseCode(cursor.getString(2));
                course.setCourseName(cursor.getString(3));
                course.setCourseDescription(cursor.getString(4));
                course.setCourseUUID(cursor.getString(1));
                courseList.add(course);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return courseList;
    }

    public List<Section> getAllSections(String UUID) {
        List<Section> sectionList = new ArrayList<Section>();
        String selectQuery = "SELECT * FROM " + TABLE_SECTION +
                " WHERE " + SECT_UUID + " = '" + UUID + "' ORDER BY " + SECT_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Section section = new Section();
                section.set_SectionUUID(cursor.getString(0));
                section.set_SectionId(Integer.parseInt(cursor.getString(1)));
                section.set_CourseId(Integer.parseInt(cursor.getString(2)));
                section.set_SectionQuarter(Integer.parseInt(cursor.getString(3)));
                section.set_SectionSemester(Integer.parseInt(cursor.getString(4)));
                section.set_SectionYear(Integer.parseInt(cursor.getString(5)));
                sectionList.add(section);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sectionList;
    }

    public List<String> getAllName_Courses(String UUID) {
        List<String> courseList = new ArrayList<String>();
        String selectQuery = "SELECT * FROM " + TABLE_COURSE + "" +
                " WHERE " + COURSE_UUID + " = '" + UUID + "' ORDER BY " + COURSE_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                courseList.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return courseList;
    }

    public int getCoursesCount(String UUID) {
        String countQuery = "SELECT * FROM " + TABLE_COURSE + " WHERE " + COURSE_UUID + " = '" + UUID + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }

//*//
    public int updateCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COURSE_UUID, course.getCourseUUID());
        values.put(COURSE_CODE, course.getCourseCode());
        values.put(COURSE_NAME, course.getCourseName());
        values.put(COURSE_DESC, course.getCourseDescription());
        return db.update(
                TABLE_COURSE,
                values,
                COURSE_ID + "=? and " + COURSE_UUID + "=?",
                new String[]{String.valueOf(course.getCourseId()), course.getCourseUUID()});
    }

    public void deleteCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(
                TABLE_COURSE,
                COURSE_ID + "=? and " + COURSE_UUID + "=?",
                new String[]{String.valueOf(course.getCourseId()), course.getCourseUUID()});
        db.close();
    }

    public void addHomework(Homework homework) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(HOMEWORK_ID, getHomeworkID(homework.getHomeworkUUID()));
        values.put(HOMEWORK_UUID, homework.getHomeworkUUID());
        values.put(HOMEWORK_NAME, homework.getHomeworkName());
        values.put(HOMEWORK_SECID, homework.getSectionId());
        values.put("SyncState",1);
        db.insert(TABLE_HOMEWORK, null, values);
        db.close();
    }

    int getHomeworkID(String UUID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT HomeworkId FROM homework WHERE TeacherUUID  = '" + UUID + "' ORDER BY HomeworkId DESC LIMIT 1";


        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int retval = cursor.getInt(0) + 1;
            cursor.close();
            return retval;
        } else {
            cursor.close();
            return 1;
        }

    }

    public Homework getLastHomework(String UUID) {
        Homework homework = null;
        String selectQuery = "SELECT * FROM " + TABLE_HOMEWORK +
                " WHERE " + HOMEWORK_UUID + " = '" + UUID + "'" +
                " ORDER BY " + HOMEWORK_ID + " DESC LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                homework = new Homework(cursor.getInt(1), cursor.getString(2), cursor.getInt(3), UUID);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return homework;
    }

    public List<Homework> getAll_Homework(int sectionId, String UUID) {
        List<Homework> homeworkList = new ArrayList<Homework>();
        String selectQuery = "SELECT * FROM " + TABLE_HOMEWORK +
                " WHERE " + HOMEWORK_SECID + " = " + sectionId +
                " AND " + HOMEWORK_UUID + " = '" + UUID + "'" +
                " ORDER BY " + HOMEWORK_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                homeworkList.add(new Homework(cursor.getInt(1), cursor.getString(2), cursor.getInt(3), UUID));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return homeworkList;
    }

    boolean homeworkExist(Homework homework, int SectionId, String UUID) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + HOMEWORK_ID +
                " FROM " + TABLE_HOMEWORK +
                " WHERE " + HOMEWORK_NAME + " = '" + homework.getHomeworkName() + "'" +
                " AND " + HOMEWORK_SECID + " = " + SectionId +
                " AND " + HOMEWORK_UUID + " = '" + UUID + "'", null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    boolean criteriaExist(String criteriaName, int homeworkId, String UUID) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + CRITERIA_ID +
                " FROM " + TABLE_CRITERIA +
                " WHERE " + CRITERIA_NAME + " = '" + criteriaName + "'" +
                " AND " + CRITERIA_HOMEWORK + " = " + homeworkId +
                " AND " + CRITERIA_UUID + " = '" + UUID + "'", null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public void addCriteria(Criteria criteria) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CRITERIA_ID, getCriteriaID(criteria.getCriteriaUUID()));
        values.put(CRITERIA_UUID, criteria.getCriteriaUUID());
        values.put(CRITERIA_NAME, criteria.getCriteriaName());
        values.put(CRITERIA_WEIGHT, criteria.getCriteriaWeight());
        values.put(CRITERIA_HOMEWORK, criteria.getCriteriaHomeworkId());
        values.put("SyncState",1);
        db.insert(TABLE_CRITERIA, null, values);
        db.close();
    }

    int getCriteriaID(String UUID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT CriteriaId FROM criteria WHERE TeacherUUID  = '" + UUID + "' ORDER BY CriteriaId DESC LIMIT 1";


        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int retval = cursor.getInt(0) + 1;
            cursor.close();
            return retval;
        } else {
            cursor.close();
            return 1;
        }

    }

    public double getCriteriaWeight(String criteriaName, int homeworkId, String UUID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + CRITERIA_WEIGHT +
                " FROM " + TABLE_CRITERIA +
                " WHERE " + CRITERIA_NAME + " = '" + criteriaName + "'" +
                " AND " + CRITERIA_HOMEWORK + " = " + homeworkId +
                " AND " + CRITERIA_UUID + " = '" + UUID + "'";
        Cursor cursor = db.rawQuery(query, null);
        Double weight = 0.0;
        if (cursor.moveToFirst()) {
            weight = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return weight;
    }

    public List<Double> getAllCriteria_Weights(int homeworkId, String UUID) {
        List<Double> weightList = new ArrayList<Double>();
        String selectQuery = "SELECT " + CRITERIA_WEIGHT +
                " FROM " + TABLE_CRITERIA +
                " WHERE " + CRITERIA_HOMEWORK + " = " + homeworkId +
                " AND " + CRITERIA_UUID + " = '" + UUID + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                weightList.add(cursor.getDouble(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return weightList;
    }

    public List<Criteria> getAllCriteriaByHomework(int homeworkId, String UUID) {
        List<Criteria> criteriaList = new ArrayList<Criteria>();
        String selectQuery = "SELECT * FROM " + TABLE_CRITERIA +
                " WHERE " + CRITERIA_HOMEWORK + " = " + homeworkId +
                " AND " + CRITERIA_UUID + " = '" + UUID + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                criteriaList.add(new Criteria(cursor.getInt(1), cursor.getString(2), cursor.getDouble(3), cursor.getInt(4), UUID));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return criteriaList;
    }

    public void printHomeworkTable(String UUID) {
        String selectQuery = "SELECT * FROM " + TABLE_HOMEWORK +
                " WHERE " + HOMEWORK_UUID + " = '" + UUID + "'" +
                " ORDER BY " + HOMEWORK_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Homework homework = new Homework(cursor.getInt(1), cursor.getString(2), cursor.getInt(3), UUID);
                Log.e(DatabaseHandler.class.toString(), homework.toString() + "\n");
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void printCriteriaTable(String UUID) {
        String selectQuery = "SELECT * FROM " + TABLE_CRITERIA +
                " WHERE " + CRITERIA_UUID + " = '" + UUID + "'"
                + " ORDER BY " + CRITERIA_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Criteria criteria = new Criteria(cursor.getInt(1), cursor.getString(2), cursor.getDouble(3), cursor.getInt(4), UUID);
                Log.e(DatabaseHandler.class.toString(), criteria.toString() + "\n");
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void addHomeworkStudent(Double grade, int criteriaId, int studentId, String UUID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(HOMESTU_ID, getHomeworkStudentID(UUID));
        values.put(HOMESTU_UUID, UUID);
        values.put(HOMESTU_Grade, grade);
        values.put(HOMESTU_CriteriaId, criteriaId);
        values.put(HOMESTU_StudentId, studentId);
        values.put("SyncState",1);
        db.insert(TABLE_HOMESTU, null, values);
        db.close();
    }

    int getHomeworkStudentID(String UUID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT HomeworkStudentId FROM homeworkStudent WHERE TeacherUUID  = '" + UUID + "' ORDER BY HomeworkStudentId DESC LIMIT 1";


        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int retval = cursor.getInt(0) + 1;
            cursor.close();
            return retval;
        } else {
            cursor.close();
            return 1;
        }

    }

    public void printHomeworkStudentTable(String UUID) {
        String selectQuery = "SELECT * FROM " + TABLE_HOMESTU
                + " WHERE " + HOMESTU_UUID + " = '" + UUID + "'"
                + " ORDER BY " + HOMESTU_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Log.e(DatabaseHandler.class.toString(), "Homestu_Id= " + cursor.getInt(0) +
                        " Homestu_Grade= " + cursor.getDouble(1) +
                        " Homestu_CriteriaId= " + cursor.getInt(2) +
                        " Homestu_StudentId= " + cursor.getInt(3) + "\n");
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public boolean homeworkStudentExist(int criteriaId, int studentId, String UUID) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + HOMESTU_ID +
                " FROM " + TABLE_HOMESTU +
                " WHERE " + HOMESTU_CriteriaId + " = '" + criteriaId + "'" +
                " AND " + HOMESTU_StudentId + " = " + studentId +
                " AND " + HOMESTU_UUID + " = '" + UUID + "'", null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public int getHomeworkStudentId(int criteriaId, int studentId, String UUID) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + HOMESTU_ID +
                " FROM " + TABLE_HOMESTU +
                " WHERE " + HOMESTU_CriteriaId + " = '" + criteriaId + "'" +
                " AND " + HOMESTU_StudentId + " = " + studentId +
                " AND " + HOMEWORK_UUID + " = '" + UUID + "'", null);
        if (cursor.moveToFirst()) {
            int retVal = cursor.getInt(0);
            cursor.close();
            return retVal;
        }
        cursor.close();
        return 0;
    }

    public void updateHomeworkStudent(int homeworkStudentId, double grade, String UUID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE " + TABLE_HOMESTU + " SET " + HOMESTU_Grade + " = " + grade +
                " WHERE " + HOMESTU_ID + " = " + homeworkStudentId +
                " AND " + HOMESTU_UUID + " = '" + UUID + "'";
        db.execSQL(strSQL);
        db.close();
    }

    public List<String> getHomeworkNameAndGrade(int StudentID, int SectionID, String UUID) {
        SQLiteDatabase db = this.getReadableDatabase();
        //query to get the Homeworks of the section

        String QUERY = "SELECT H.HomeworkName, SUM(coalesce(HS.HomeworkStudentGrade ,0) / (SELECT COUNT(CriteriaId) FROM criteria WHERE HomeworkId = H.HomeworkId  )) AS Grade " +
                "FROM student S " +
                "JOIN studentSection SS ON S.StudentId = SS.StudentId " +
                "JOIN homework H ON H.SectionId = SS.SectionId " +
                "JOIN criteria C ON H.HomeworkId = C.HomeworkId " +
                "LEFT JOIN homeworkStudent HS ON HS.StudentId  = S.StudentId AND HS.CriteriaId = C.CriteriaId " +
                "WHERE SS.SectionId = '" + SectionID + "' AND SS.TeacherUUID = '" + UUID + "' AND S.StudentId = '" + StudentID + "' " +
                "GROUP BY S.StudentName , H.HomeworkName " +
                "ORDER BY S.StudentId";

        Cursor homeworks = db.rawQuery(QUERY, null);
        homeworks.moveToFirst();

        List<String> homeworks_return = new ArrayList<String>();

        do {

            String AddVal = homeworks.getString(0) + "HOLAHELLO" + homeworks.getString(1);
            homeworks_return.add(AddVal);

        } while (homeworks.moveToNext());


        return homeworks_return;
        //NAMEHOLAHELLO52
    }

    public double getCriteriaGrade(int studentId, int criteriaId, String UUID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor grade_criteria = db.rawQuery("SELECT " + HOMESTU_Grade +
                " FROM " + TABLE_HOMESTU +
                " WHERE " + HOMESTU_CriteriaId + "=" + criteriaId +
                " AND " + HOMESTU_StudentId + "=" + studentId +
                " AND " + HOMESTU_UUID + " = '" + UUID + "'", null);
        //if the criteria has been checked
        if (grade_criteria.getCount() > 0) {
            grade_criteria.moveToFirst();
            double retVal = grade_criteria.getDouble(0);
            grade_criteria.close();
            return retVal;
        }
        //if the criteria hasn't been checked
        grade_criteria.close();
        return 0;
    }

    public List<String> exportStudentGrades(String UUID) {
        String to_write;
        List<String> all_grades = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor courseid_cursor = db.rawQuery("SELECT " + COURSE_ID + "," + COURSE_CODE + "," + COURSE_NAME + " FROM " + TABLE_COURSE +
                " WHERE " + COURSE_UUID + " = '" + UUID + "'", null);
        courseid_cursor.moveToFirst();
        do {
            to_write = "";
            int courseid = courseid_cursor.getInt(0);
            to_write = "Course CODE:" + courseid_cursor.getString(1) + "\n";
            to_write += "Course NAME:" + courseid_cursor.getString(2) + "\n" + "\n";
            Cursor sectionid_cursor = db.rawQuery("SELECT " + SECT_ID + "," + SECT_CODE + " FROM " + TABLE_SECTION +
                    " WHERE " + SECT_COURSE + "=" + courseid +
                    " AND " + SECT_UUID + " = '" + UUID + "'", null);
            sectionid_cursor.moveToFirst();
            do {
                int sectionid = sectionid_cursor.getInt(0);
                to_write += ("Section CODE:" + sectionid_cursor.getString(1) + "\n");
                Cursor studentsectionid_cursor = db.rawQuery("SELECT " + STUSEC_ID + "," + STUSEC_STUD + " FROM " + TABLE_STUDENTSECTION +
                        " WHERE " + STUSEC_SECT + "=" + sectionid +
                        " AND " + STUSEC_UUID + " = '" + UUID + "'", null);
                studentsectionid_cursor.moveToFirst();
                do {
                    Cursor student = db.rawQuery("SELECT " + STU_ID + "," + STU_NAME + " FROM " + TABLE_STUDENT + " " +
                            "WHERE " + STU_ID + "=" + studentsectionid_cursor.getInt(1), null);
                    student.moveToFirst();
                    to_write += ("  + Student ID:" + Integer.toString(student.getInt(0)) + "\n" + "    Name:" + student.getString(1) + "\n");

                    int studentsectionid = studentsectionid_cursor.getInt(0);
                    int studentid = studentsectionid_cursor.getInt(1);

                    List<String> tareas_estudiante = getHomeworkNameAndGrade(studentid, sectionid, UUID);
                    to_write += "     >Homeworks\n";
                    for (String s : tareas_estudiante) {
                        String[] homework_parts = (s.split("HOLAHELLO"));
                        to_write += ("        * " + homework_parts[0] + " " + homework_parts[1] + "\n");
                        //Log.d("TAREA", s);
                    }
                    db = this.getReadableDatabase();
                    List<Participation> participaciones_estudiantes = getStudentParticipationList(studentsectionid, UUID);
                    to_write += "     >Paticipations\n";
                    for (Participation p : participaciones_estudiantes) {
                        to_write += ("        * " + Double.toString(p.get_ParticipationGrade()) + " - " + p.get_ParticipationDate() + " - " + p.get_ParticipationComment() + "\n");
                    }
                    db = this.getReadableDatabase();
                    student.close();
                } while (studentsectionid_cursor.moveToNext());
                studentsectionid_cursor.close();
            } while (sectionid_cursor.moveToNext());
            sectionid_cursor.close();
            all_grades.add(to_write);
        } while (courseid_cursor.moveToNext());
        courseid_cursor.close();
        db.close();
        return all_grades;
    }

    public List<String> getAvailableSemester() {
        List<String> retVal = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT DISTINCT SectionQuarter,SectionSemester,SectionYear FROM section";
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {

                String addVal = "Quarter: " + cursor.getInt(0) + " Semester: " + cursor.getInt(1) + " Year: " + cursor.getInt(2);
                retVal.add(addVal);

                cursor.moveToNext();
            }

        } else {
            retVal = null;
        }

        cursor.close();

        //0        1 2         3 4     5
        //Quarter: 4 Semester: 5 Year: 2014
        return retVal;
    }

	public void forceAddTeacher(String UUID, String Name){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "INSERT INTO Teacher VALUES('"+UUID+"', '"+Name+"',1)";
        db.execSQL(query);
        db.close();
    }


    public String composeJSONfromSQLiteTeacher(int SyncState){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM Teacher WHERE SyncState = "+SyncState;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("teacherId", cursor.getString(0));
                map.put("teacherName", cursor.getString(1));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    public ArrayList<HashMap<String, String>> getAllTeachers(int SyncState) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM Teacher WHERE SyncState = "+SyncState;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("teacherId", cursor.getString(0));
                map.put("teacherName", cursor.getString(1));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return wordList;
    }

    public String composeJSONfromSQLiteCourse(int SyncState){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM course WHERE SyncState = "+SyncState;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Id", String.valueOf(cursor.getInt(0)));
                map.put("UUID", cursor.getString(1));
                map.put("Code", cursor.getString(2));
                map.put("Name", cursor.getString(3));
                map.put("Desc", cursor.getString(4));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    public ArrayList<HashMap<String, String>> getAllCourse(int SyncState) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM course WHERE SyncState = "+SyncState;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Id", String.valueOf(cursor.getString(0)));
                map.put("UUID", cursor.getString(1));
                map.put("Code", cursor.getString(2));
                map.put("Name", cursor.getString(3));
                map.put("Desc", cursor.getString(4));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return wordList;
    }

    public String composeJSONfromSQLiteSection(int SyncState){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM section WHERE SyncState = "+SyncState;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("UUID", cursor.getString(0));
                map.put("Id", String.valueOf(cursor.getInt(1)));
                map.put("Course", String.valueOf(cursor.getInt(2)));
                map.put("Qtr", String.valueOf(cursor.getInt(3)));
                map.put("Sem", String.valueOf(cursor.getInt(4)));
                map.put("Yea", String.valueOf(cursor.getInt(5)));
                map.put("Code", cursor.getString(6));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    public ArrayList<HashMap<String, String>> getAllSection(int SyncState) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM section WHERE SyncState = "+SyncState;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("UUID", cursor.getString(0));
                map.put("Id", String.valueOf(cursor.getInt(1)));
                map.put("Course", String.valueOf(cursor.getInt(2)));
                map.put("Qtr", String.valueOf(cursor.getInt(3)));
                map.put("Sem", String.valueOf(cursor.getInt(4)));
                map.put("Yea", String.valueOf(cursor.getInt(5)));
                map.put("Code", cursor.getString(6));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return wordList;
    }

    public String composeJSONfromSQLiteStudent(int SyncState){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM student WHERE SyncState = "+SyncState;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Id", cursor.getString(0));
                map.put("Name", cursor.getString(1));
                map.put("Major", cursor.getString(2));
                map.put("Email", cursor.getString(3));
                map.put("Password", cursor.getString(4));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    public ArrayList<HashMap<String, String>> getAllStudents(int SyncState) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM student WHERE SyncState = "+SyncState;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Id", cursor.getString(0));
                map.put("Name", cursor.getString(1));
                map.put("Major", cursor.getString(2));
                map.put("Email", cursor.getString(3));
                map.put("Password", cursor.getString(4));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return wordList;
    }

    public String composeJSONfromSQLiteStudentSection(int SyncState){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM studentSection WHERE SyncState = "+SyncState;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("UUID", cursor.getString(0));
                map.put("Id", String.valueOf(cursor.getInt(1)));
                map.put("Sect", String.valueOf(cursor.getInt(2)));
                map.put("Stud", String.valueOf(cursor.getInt(3)));
                map.put("Final", String.valueOf(cursor.getDouble(4)));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    public ArrayList<HashMap<String, String>> getAllStudentSection(int SyncState) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM studentSection WHERE SyncState = "+SyncState;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("UUID", cursor.getString(0));
                map.put("Id", String.valueOf(cursor.getInt(1)));
                map.put("Sect", String.valueOf(cursor.getInt(2)));
                map.put("Stud", String.valueOf(cursor.getInt(3)));
                map.put("Final", String.valueOf(cursor.getDouble(4)));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return wordList;
    }

    public String composeJSONfromSQLiteStudentParticipation(int SyncState){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM participationStudent WHERE SyncState = "+SyncState;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("UUID", cursor.getString(0));
                map.put("Id", String.valueOf(cursor.getInt(1)));
                map.put("Stusect", String.valueOf(cursor.getInt(2)));
                map.put("Grade", String.valueOf(cursor.getDouble(3)));
                map.put("Date", cursor.getString(4));
                map.put("Comment", cursor.getString(5));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    public ArrayList<HashMap<String, String>> getAllStudentParticipation(int SyncState) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM participationStudent WHERE SyncState = "+SyncState;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("UUID", cursor.getString(0));
                map.put("Id", String.valueOf(cursor.getInt(1)));
                map.put("Stusect", String.valueOf(cursor.getInt(2)));
                map.put("Grade", String.valueOf(cursor.getDouble(3)));
                map.put("Date", cursor.getString(4));
                map.put("Comment", cursor.getString(5));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return wordList;
    }


    public String composeJSONfromSQLiteHomework(int SyncState){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM homework WHERE SyncState = "+SyncState;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("UUID", cursor.getString(0));
                map.put("Id", String.valueOf(cursor.getInt(1)));
                map.put("Name", cursor.getString(2));
                map.put("Secid", String.valueOf(cursor.getInt(3)));
               /*
                System.out.println(cursor.getString(0));
                System.out.println(cursor.getInt(1));
                System.out.println(cursor.getString(2));
                System.out.println(cursor.getInt(3));
                */
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    public ArrayList<HashMap<String, String>> getAllHomework(int SyncState) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM homework WHERE SyncState = "+SyncState;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("UUID", cursor.getString(0));
                map.put("Id", String.valueOf(cursor.getInt(1)));
                map.put("Name", cursor.getString(2));
                map.put("Secid", String.valueOf(cursor.getInt(3)));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return wordList;
    }


    public String composeJSONfromSQLiteCriteria(int SyncState){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM criteria WHERE SyncState = "+SyncState;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("UUID", cursor.getString(0));
                map.put("Id", String.valueOf(cursor.getInt(1)));
                map.put("Name", cursor.getString(2));
                map.put("Weight", String.valueOf(cursor.getDouble(3)));
                map.put("Homework", String.valueOf(cursor.getInt(4)));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    public ArrayList<HashMap<String, String>> getAllCriteria(int SyncState) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM criteria WHERE SyncState = "+SyncState;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("UUID", cursor.getString(0));
                map.put("Id", String.valueOf(cursor.getInt(1)));
                map.put("Name", cursor.getString(2));
                map.put("Weight", String.valueOf(cursor.getDouble(3)));
                map.put("Homework", String.valueOf(cursor.getInt(4)));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return wordList;
    }

    public String composeJSONfromSQLiteHomeWorkStudent(int SyncState){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM homeworkStudent WHERE SyncState = "+SyncState;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("UUID", cursor.getString(0));
                map.put("Id", String.valueOf(cursor.getInt(1)));
                map.put("Grade", String.valueOf(cursor.getDouble(2)));
                map.put("Criteriaid", String.valueOf(cursor.getInt(3)));
                map.put("Studentid", String.valueOf(cursor.getInt(4)));
                map.put("hsd", String.valueOf(cursor.getString(5)));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    public ArrayList<HashMap<String, String>> getAllHomeWorkStudent(int SyncState) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM homeworkStudent WHERE SyncState = "+SyncState;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("UUID", cursor.getString(0));
                map.put("Id", String.valueOf(cursor.getInt(1)));
                map.put("Grade", String.valueOf(cursor.getDouble(2)));
                map.put("Criteriaid", String.valueOf(cursor.getInt(3)));
                map.put("Studentid", String.valueOf(cursor.getInt(4)));
                map.put("hsd", String.valueOf(cursor.getString(5)));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return wordList;
    }
    

	public List<String> getTotalHomeworkGrades(String UUID, int SectionId) {
        List<String> retVal = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT S.StudentId, S.StudentName , ROUND(SUM(coalesce(HS.HomeworkStudentGrade ,0) / (SELECT COUNT(CriteriaId) FROM criteria WHERE HomeworkId = H.HomeworkId  ) / (SELECT count(HomeworkId)*100 FROM homework WHERE SectionId = SS.SectionId)) *100) AS Grade " +
                "FROM student S JOIN studentSection SS ON S.StudentId = SS.StudentId " +
                "JOIN homework H ON H.SectionId = SS.SectionId JOIN criteria C ON H.HomeworkId = C.HomeworkId " +
                "LEFT JOIN homeworkStudent HS ON HS.StudentId  = S.StudentId AND HS.CriteriaId = C.CriteriaId " +
                "WHERE SS.SectionId = '" + SectionId + "' AND SS.TeacherUUID = '" + UUID + "' " +
                "GROUP BY S.StudentName " +
                "ORDER BY S.StudentId";
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String AddVal = cursor.getString(0) + "SEPARATOR" + cursor.getString(1) + "SEPARATOR" + cursor.getString(2);
                retVal.add(AddVal);

                cursor.moveToNext();
            }
        } else {
            retVal = null;
        }

        cursor.close();
        db.close();
        return retVal;
        //IDSEPARATORNAMESEPARATOR100
    }

    public List<String> getTotalParticipationGrades(String UUID, int SectionId) {
        List<String> retVal = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT S.StudentId, S.StudentName , PS.ParticipationDate,PS.ParticipationComment,ROUND( SUM(PS.ParticipationGrade) / (SELECT COUNT(ParticipationId) FROM participationStudent WHERE StudentSectionId = SS.StudentSectionId ) ) as Grade " +
                "FROM student S " +
                "JOIN studentSection SS ON S.StudentId = SS.StudentId " +
                "JOIN participationStudent PS ON SS.StudentSectionId = PS.StudentSectionId " +
                "JOIN section SEC ON SS.SectionId = SEC.SectionId " +
                "WHERE SEC.SectionId = '" + SectionId + "' AND SEC.TeacherUUID = '" + UUID + "' " +
                "GROUP BY S.StudentName " +
                "ORDER BY S.StudentId";
        Cursor cursor = db.rawQuery(QUERY, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String AddVal = cursor.getString(0) + "SEPARATOR" +
                        cursor.getString(1) + "SEPARATOR" +
                        cursor.getString(2) + "SEPARATOR" +
                        cursor.getString(3) + "SEPARATOR" +
                        cursor.getString(4);
                retVal.add(AddVal);

                cursor.moveToNext();
            }
        } else {
            retVal = null;
        }
        cursor.close();
        db.close();
        return retVal;
        //IDSEPARATORNAMEDATESEPARATORCOMMENTSEPARATOR100
    }

    public List<String> getTotalHomeworkGrades(String UUID, int SectionId, String StudentId) {
        List<String> retVal = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT S.StudentId, S.StudentName, ROUND(SUM(coalesce(HStu.HomeworkStudentGrade ,0) / (SELECT COUNT(CriteriaId) FROM criteria WHERE HomeworkId = H.HomeworkId  ) / (SELECT count(HomeworkId)*100 FROM homework WHERE SectionId = '" + SectionId + "')) *100) AS Grade  " +
                "FROM homework H " +
                "JOIN criteria C ON H.HomeworkId = C.HomeworkId " +
                "JOIN homeworkStudent HStu ON C.CriteriaId = HStu.CriteriaId AND HStu.StudentId = S.StudentId " +
                "JOIN student S ON HStu.StudentId = S.StudentId " +
                "WHERE H.SectionId = '" + SectionId + "' AND H.TeacherUUID = '" + UUID + "' AND S.StudentId = '" + StudentId + "'" +
                "GROUP BY S.StudentName " +
                "ORDER BY S.StudentId";
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String AddVal = cursor.getString(0) + "SEPARATOR" + cursor.getString(1) + "SEPARATOR" + cursor.getString(2);
                retVal.add(AddVal);

                cursor.moveToNext();
            }
        } else {
            retVal = null;
        }

        cursor.close();
        db.close();
        return retVal;
        //NAMESEPARATOR100
    }

    public List<String> getTotalParticipationGrades(String UUID, int SectionId, String StudentId) {
        List<String> retVal = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT S.StudentId, S.StudentName , PS.ParticipationDate,PS.ParticipationComment,ROUND( SUM(PS.ParticipationGrade) / (SELECT COUNT(ParticipationId) FROM participationStudent WHERE StudentSectionId = SS.StudentSectionId ) ) as Grade " +
                "FROM student S " +
                "JOIN studentSection SS ON S.StudentId = SS.StudentId " +
                "JOIN participationStudent PS ON SS.StudentSectionId = PS.StudentSectionId " +
                "JOIN section SEC ON SS.SectionId = SEC.SectionId " +
                "WHERE SEC.SectionId = '" + SectionId + "' AND SEC.TeacherUUID = '" + UUID + "' AND S.StudentId = '" + StudentId + "'" +
                "GROUP BY S.StudentName " +
                "ORDER BY S.StudentId";
        Cursor cursor = db.rawQuery(QUERY, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String AddVal = cursor.getString(0) + "SEPARATOR" + cursor.getString(1) + "SEPARATOR" + cursor.getString(2) + "SEPARATOR" + cursor.getString(3) + "SEPARATOR" + cursor.getString(4);
                retVal.add(AddVal);

                cursor.moveToNext();
            }
        } else {
            retVal = null;
        }
        cursor.close();
        db.close();
        return retVal;
        //NAMESEPARATOR100
    }

    public List<Participation> getParticipationStudent(String UUID, String studentId, String sectionId) {
        List<Participation> retVal = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT PS.StudentSectionId , PS.ParticipationDate,PS.ParticipationComment , PS.ParticipationGrade FROM student S JOIN studentSection SS ON S.StudentId = SS.StudentId JOIN participationStudent PS ON SS.StudentSectionId = PS.StudentSectionId WHERE SS.StudentId = '" + studentId + "' AND SS.SectionId = '" + sectionId + "' AND SS.TeacherUUID = '" + UUID + "' ORDER BY S.StudentId";
        Cursor cursor = db.rawQuery(QUERY, null);
        /*
        int _StudentSectionId, double _ParticipationGrade, String _ParticipationDate,
                         String _ParticipationComment, String UUID
         */
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                retVal.add(new Participation(cursor.getInt(0), cursor.getDouble(3), cursor.getString(1), cursor.getString(2), UUID));
            } while (cursor.moveToNext());
        } else {
            retVal = null;
        }


        return retVal;
    }

    public ArrayList<String> getRandomStudent(int SectionId, String UUID) {
        ArrayList<String> retVal = null;
        SQLiteDatabase db = this.getReadableDatabase();
        int total = 0;
        String QUERY = "SELECT S.StudentId , S.StudentName , SS.StudentSectionId FROM student S JOIN studentSection SS ON S.StudentId = SS.StudentId WHERE SS.SectionId = '" + SectionId + "' AND SS.TeacherUUID = '" + UUID + "'";
        Cursor cursor = db.rawQuery(QUERY, null);


        //STUDENT ID
        //STUDENT NAME
        //SECTIONID

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
        } else {
            return null;
        }

        do {
            Random R = new Random();
            int RandomVal = R.nextInt(cursor.getCount());
            cursor.moveToPosition(RandomVal);

            String StudentId = cursor.getString(0);
            String QUERYPS = "SELECT coalesce (MAX(DATE(substr(PS.ParticipationDate,7,4) || '-' || substr(PS.ParticipationDate,4,2) || '-' || substr(PS.ParticipationDate,1,2))),' ') as ParticipationDate,  coalesce(ParticipationComment,'  ') as ParticipationComment FROM participationStudent PS JOIN studentSection SQ ON PS.StudentSectionId = SQ.StudentSectionId JOIN student S ON SQ.StudentId = S.StudentId WHERE SQ.SectionId = '" + SectionId + "' AND SQ.StudentId = '" + StudentId + "' AND SQ.TeacherUUID = '" + UUID + "'";
            Cursor CheckDate = db.rawQuery(QUERYPS, null);

            if (CheckDate != null) {
                if (CheckDate.getCount() > 0) {
                    CheckDate.moveToFirst();
                    String CommentState = CheckDate.getString(CheckDate.getColumnIndex("ParticipationComment"));
                    if (!(CommentState.equals("Absent"))) {
                        retVal = new ArrayList();
                        retVal.add(StudentId);
                        retVal.add(cursor.getString(1));
                        retVal.add(cursor.getString(2));
                        cursor.close();
                        CheckDate.close();
                        return retVal;

                    } else {

                        String DATE = CheckDate.getString(0);
                        DateFormat FORMATO = new SimpleDateFormat("yyyy-MM-dd");
                        Date ActualDate = new Date();
                        String ACTUDATE = FORMATO.format(ActualDate);

                        if (!DATE.equals(ACTUDATE)) {
                            retVal = new ArrayList();
                            retVal.add(StudentId);
                            retVal.add(cursor.getString(1));
                            retVal.add(cursor.getString(2));
                            cursor.close();
                            CheckDate.close();
                            return retVal;
                        }
                    }
                } else {
                    retVal = new ArrayList();
                    retVal.add(StudentId);
                    retVal.add(cursor.getString(1));
                    retVal.add(cursor.getString(2));
                    cursor.close();
                    return retVal;
                }
            }
            CheckDate.close();
            total++;
        } while (total < cursor.getCount());
        cursor.close();
        return retVal;
    }

    public List<String> getSectionGrades(int SectionId, String UUID) {
        List<String> retVal = null;
        double ParticipationPercentage = 0;

        List<String> Homework = getTotalHomeworkGrades(UUID, SectionId);
        retVal = new ArrayList();
        if (Homework == null){
            Homework = new ArrayList();
        }

        for (int i = 0; i < Homework.size(); i++) {
            String[] HomeworkS = Homework.get(i).split("SEPARATOR");
            List<String> FinalParticipations = getTotalParticipationGrades(UUID, SectionId, HomeworkS[0]);
            if (FinalParticipations != null) {
                ParticipationPercentage = Double.parseDouble(FinalParticipations.get(0).split("SEPARATOR")[4]);
            } else {
                ParticipationPercentage = 0;
            }

            String AddVal = HomeworkS[0] + "SEPARATOR" + HomeworkS[1] + "SEPARATOR" + ParticipationPercentage + "SEPARATOR" + HomeworkS[2] + "SEPARATOR";


            retVal.add(AddVal);
        }

        return retVal;
    }

    
    public void clearSyncState(){
        SQLiteDatabase db = this.getReadableDatabase();
        String QueryTeacher = "UPDATE Teacher SET SyncState = 0 WHERE SyncState = 1";
        String QueryCourse = "UPDATE course SET SyncState = 0 WHERE SyncState = 1";
        String QueryCriteria = "UPDATE criteria SET SyncState = 0 WHERE SyncState = 1";
        String QueryHomework = "UPDATE homework SET SyncState = 0 WHERE SyncState = 1";
        String QueryHomeworkStudent = "UPDATE homeworkStudent SET SyncState = 0 WHERE SyncState = 1";
        String QueryParticipationStudent = "UPDATE participationStudent SET SyncState = 0 WHERE SyncState = 1";
        String QuerySection = "UPDATE section SET SyncState = 0 WHERE SyncState = 1";
        String QueryStudent = "UPDATE student SET SyncState = 0 WHERE SyncState = 1";
        String QueryStudentSection = "UPDATE studentSection SET SyncState = 0 WHERE SyncState = 1";

        db.rawQuery(QueryTeacher,null);
        db.rawQuery(QueryCourse,null);
        db.rawQuery(QueryCriteria,null);
        db.rawQuery(QueryHomework,null);
        db.rawQuery(QueryHomeworkStudent,null);
        db.rawQuery(QueryParticipationStudent,null);
        db.rawQuery(QuerySection,null);
        db.rawQuery(QueryStudent,null);
        db.rawQuery(QueryStudentSection,null);
    }

}