package edu.unitec.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Participation";

    // Course Table
    private static final String TABLE_COURSE = "course";
    // Table Course - Table Fields
    private static final String COURSE_ID = "CourseId";
    private static final String COURSE_CODE = "CourseCode";
    private static final String COURSE_NAME = "CourseName";
    private static final String COURSE_DESC = "CourseDescription";

    // Section Table
    private static final String TABLE_SECTION = "section";
    // Table Section - Table Fields
    private static final String SECT_ID = "SectionId";
    private static final String SECT_COURSE = "CourseId";
    private static final String SECT_QTR = "SectionQuarter";
    private static final String SECT_SEM = "SectionSemester";
    private static final String SECT_YEA = "SectionYear";
    private static final String SECT_CODE = "SectionCode";

    // Students Table
    private static final String TABLE_STUDENT="student";
    // Table Students Fie- Table Fieldslds
    private static final String STU_ID = "StudentId";
    private static final String STU_NAME = "StudentName";
    private static final String STU_MAJOR = "StudentMajor";

    // Students Per Section Table
    private static final String TABLE_STUDENTSECTION = "studentSection";
    // Table Students Per Section - Table Fields
    private static final String STUSEC_ID = "StudentSectionId";
    private static final String STUSEC_SECT = "SectionId";
    private static final String STUSEC_STUD = "StudentId";
    private static final String STUSEC_FINAL = "StudentSectionFinal";

    // Partipations Per Student Table
    private static final String TABLE_PARTICIPATION = "participationStudent";
    // Table Participations Per Student - Fields
    private static final String PART_ID = "ParticipationId";
    private static final String PART_STUSECT = "StudentSectionId";
    private static final String PART_GRADE = "ParticipationGrade";
    private static final String PART_DATE = "ParticipationDate";
    private static final String PART_COMMENT = "ParticipationComment";

    // Homework Table
    private static final String TABLE_HOMEWORK = "homework";
    // Table Homework - Table Fields
    private static final String HOMEWORK_ID = "HomeworkId";
    private static final String HOMEWORK_NAME = "HomeworkName";
    private static final String HOMEWORK_SECID = "SectionId";

    // Critera Per Homework Table
    private static final String TABLE_CRITERIA = "criteria";
    // Table Homework - Table Fields
    private static final String CRITERIA_ID = "CriteriaId";
    private static final String CRITERIA_NAME = "CriteriaName";
    private static final String CRITERIA_HOMEWORK = "HomeworkId";
    private static final String CRITERIA_WEIGHT = "CriteriaWeight";

    // Homework Per Student Table
    private static final String TABLE_HOMESTU = "homeworkStudent";
    // Table Homework - Table Fields
    private static final String HOMESTU_ID = "HomeworkStudentId";
    private static final String HOMESTU_CriteriaId = "CriteriaId";
    private static final String HOMESTU_StudentId = "StudentId";
    private static final String HOMESTU_Grade = "HomeworkStudentGrade";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COURSE_TABLE =
                "CREATE TABLE " + TABLE_COURSE + " (" +
                        COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COURSE_CODE + " TEXT," +
                        COURSE_NAME + " TEXT," +
                        COURSE_DESC + " TEXT" +
                        ")";
        String CREATE_SECTION_TABLE =
                "CREATE TABLE " + TABLE_SECTION + " (" +
                        SECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SECT_COURSE + " INTEGER," +
                        SECT_QTR + " INTEGER," +
                        SECT_SEM + " INTEGER," +
                        SECT_YEA + " INTEGER, " +
                        SECT_CODE + " TEXT," +
                        "FOREIGN KEY(" + SECT_COURSE + ") REFERENCES " + TABLE_COURSE + "(" + COURSE_ID + ")" +
                        ")";
        String CREATE_STUDENT_TABLE =
                "CREATE TABLE " + TABLE_STUDENT + " (" +
                        STU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        STU_NAME + " TEXT," +
                        STU_MAJOR + " TEXT" + ")";
        String CREATE_STUDENTSECTION_TABLE =
                "CREATE TABLE " + TABLE_STUDENTSECTION + " (" +
                        STUSEC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        STUSEC_SECT + " INTEGER," +
                        STUSEC_STUD + " INTEGER," +
                        STUSEC_FINAL + " REAL," +
                        "FOREIGN KEY(" + STUSEC_SECT + ") REFERENCES " + TABLE_SECTION + "(" + SECT_ID + "), " +
                        "FOREIGN KEY(" + STUSEC_STUD + ") REFERENCES " + TABLE_STUDENT + "(" + STU_ID + ")" +
                        ")";
        String CREATE_PARTICIPATION_TABLE =
                "CREATE TABLE " + TABLE_PARTICIPATION + " (" +
                        PART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        PART_STUSECT + " INTEGER," +
                        PART_GRADE + " REAL," +
                        PART_DATE + " TEXT," +
                        PART_COMMENT + " TEXT," +
                        "FOREIGN KEY(" + PART_STUSECT + ") REFERENCES " + TABLE_STUDENTSECTION + "(" + STUSEC_ID + ")" +
                        ")";
        String CREATE_HOMEWORK_TABLE =
                "CREATE TABLE " + TABLE_HOMEWORK + " (" +
                        HOMEWORK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        HOMEWORK_NAME + " TEXT," +
                        HOMEWORK_SECID + " INTEGER," +
                        "FOREIGN KEY(" + HOMEWORK_SECID + ") REFERENCES " + TABLE_SECTION + "(" + SECT_ID + ")" +
                        ")";
        String CREATE_CRITERIA_TABLE =
                "CREATE TABLE " + TABLE_CRITERIA + " (" +
                        CRITERIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        CRITERIA_NAME + " TEXT," +
                        CRITERIA_WEIGHT + " REAL," +
                        CRITERIA_HOMEWORK + " INTEGER," +
                        "FOREIGN KEY(" + CRITERIA_HOMEWORK + ") REFERENCES " + TABLE_HOMEWORK + "(" + HOMEWORK_ID + ")" +
                        ")";
        String CREATE_HOMESTU_TABLE =
                "CREATE TABLE " + TABLE_HOMESTU + " (" +
                        HOMESTU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        HOMESTU_Grade + " REAL," +
                        HOMESTU_CriteriaId + " INTEGER," +
                        HOMESTU_StudentId + " INTEGER," +
                        "FOREIGN KEY(" + HOMESTU_CriteriaId + ") REFERENCES " + TABLE_CRITERIA + "(" + CRITERIA_ID + "), " +
                        "FOREIGN KEY(" + HOMESTU_StudentId + ") REFERENCES " + TABLE_STUDENT + "(" + STU_ID + ")" +
                        ")";

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
    void addCourse(Course course){
        SQLiteDatabase	db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(COURSE_ID, course.getCourseId());
        values.put(COURSE_CODE, course.getCourseCode());
        values.put(COURSE_NAME, course.getCourseName());
        values.put(COURSE_DESC, course.getCourseDescription());
        db.insert(TABLE_COURSE, null, values);
        db.close();
    }

    void addStudentTable(Student student, Section section){
        SQLiteDatabase	db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STUSEC_SECT, section.get_SectionId());
        values.put(STUSEC_STUD, student.get_StudentId());
        values.put(STUSEC_FINAL, 0);
        db.insert(TABLE_STUDENTSECTION, null, values);
        db.close();
    }

    List<Integer> getStudentIdListBySectionId(int sectionId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+STUSEC_STUD+
                " FROM "+ TABLE_STUDENTSECTION+" WHERE "+STUSEC_SECT+" = "+sectionId;
        Cursor cursor = db.rawQuery(query, null);
        List<Integer> studentIdList=new ArrayList<Integer>();
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            for(int i=0;i<cursor.getCount();i++){
                studentIdList.add(cursor.getInt(0));
                cursor.moveToNext();
            }

        }
        cursor.close();
        return  studentIdList;
    }

    void addParticipation(Participation participation){
        SQLiteDatabase	db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PART_STUSECT, participation.get_StudentSectionId());
        values.put(PART_GRADE, participation.get_ParticipationGrade());
        values.put(PART_DATE, participation.get_ParticipationDate());
        values.put(PART_COMMENT, participation.get_ParticipationComment());
        db.insert(TABLE_PARTICIPATION, null, values);
        db.close();
    }

    public List<Participation> getParticipationList(int sectionId){
        List<Participation> participationList=new ArrayList<Participation>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+STUSEC_ID+" FROM "+ TABLE_STUDENTSECTION+" WHERE "+STUSEC_SECT+" = "+sectionId;
        Cursor studentsSections = db.rawQuery(query, null);
        studentsSections.moveToFirst();
        for(int i=0;i<studentsSections.getCount();i++){
            query = "SELECT "+PART_ID+", "+PART_GRADE+ ", "+PART_DATE+", "+PART_COMMENT+
                    " FROM "+ TABLE_PARTICIPATION+" WHERE "+PART_STUSECT+" = "+studentsSections.getInt(0);
            Cursor participations = db.rawQuery(query, null);
            if(participations.getCount()>0) {
                participations.moveToFirst();
                do {
                    participationList.add(new Participation(
                            participations.getInt(0),
                            participations.getDouble(1),
                            participations.getString(2),
                            participations.getString(3)));
                } while (participations.moveToNext());
            }
            participations.close();
            studentsSections.moveToNext();
        }
        studentsSections.close();
        return participationList;
    }
    public String getMaxAverageStudentParticipation(int sectionId){
        //internal class to get the Name of the student and the average
        class StudentAndAverage{
            String name;
            Double average;
            StudentAndAverage(String name,Double avg){
                this.name=name;
                this.average=avg;
            }
            StudentAndAverage(){
                name="";
                average=0.0;
            }
        }
        List<StudentAndAverage> averagesList=new ArrayList<StudentAndAverage>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+STUSEC_ID+", "+STUSEC_STUD+" FROM "+ TABLE_STUDENTSECTION+" WHERE "+STUSEC_SECT+" = "+sectionId;
        Cursor studentsSections = db.rawQuery(query, null);
        studentsSections.moveToFirst();

        for(int i=0;i<studentsSections.getCount();i++){
            double acumGrades=0,average=0;
            int quantityOfGrades=0;
            query = "SELECT "+PART_GRADE+
                    " FROM "+ TABLE_PARTICIPATION+" WHERE "+PART_STUSECT+" = "+studentsSections.getInt(0);
            Cursor participations = db.rawQuery(query, null);
            if(participations.getCount()>0) {
                participations.moveToFirst();
                do {
                    acumGrades+=participations.getDouble(0);
                    quantityOfGrades++;
                } while (participations.moveToNext());
                average=acumGrades/quantityOfGrades;
                String name=this.getStudentName(studentsSections.getInt(1));
                averagesList.add(new StudentAndAverage(name,average));
            }
            participations.close();
            studentsSections.moveToNext();
        }
        studentsSections.close();
        //find the Highest Average
        if(averagesList.size()>0) {
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

    public String getMinAverageStudentParticipation(int sectionId){
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
            String query = "SELECT " + STUSEC_ID + ", " + STUSEC_STUD + " FROM " + TABLE_STUDENTSECTION + " WHERE " + STUSEC_SECT + " = " + sectionId;
            Cursor studentsSections = db.rawQuery(query, null);
            studentsSections.moveToFirst();

            for (int i = 0; i < studentsSections.getCount(); i++) {
                double acumGrades = 0, average = 0;
                int quantityOfGrades = 0;
                query = "SELECT " + PART_GRADE +
                        " FROM " + TABLE_PARTICIPATION + " WHERE " + PART_STUSECT + " = " + studentsSections.getInt(0);
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
            if(averagesList.size()>0) {
                double min = averagesList.get(0).average;
                String minName = averagesList.get(0).name;
                for (int i = 0; i < averagesList.size(); i++) {
                    if (averagesList.get(i).average < min) {
                        min = averagesList.get(i).average;
                        minName = averagesList.get(i).name;
                    }
                }
                return minName + "\t" + Math.round(min) + "%";
            }else{
                return "N/A";
            }
        }catch(Exception e){
            return "N/A";
        }
    }
    public String getMaxAverageStudentHomework(int sectionId){
        //internal class to get the Name of the student and the average
        class StudentAndAverage{
            String name;
            Double average;
            StudentAndAverage(String name,Double avg){
                this.name=name;
                this.average=avg;
            }
            StudentAndAverage(){
                name="";
                average=0.0;
            }
        }
        List<StudentAndAverage> averagesList=new ArrayList<StudentAndAverage>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+STUSEC_ID+", "+STUSEC_STUD+" FROM "+ TABLE_STUDENTSECTION+" WHERE "+STUSEC_SECT+" = "+sectionId;
        Cursor studentsSections = db.rawQuery(query, null);
        studentsSections.moveToFirst();

        for(int i=0;i<studentsSections.getCount();i++){
            double acumGrades=0,average=0;
            int quantityOfGrades=0;
            query = "SELECT "+PART_GRADE+
                    " FROM "+ TABLE_PARTICIPATION+" WHERE "+PART_STUSECT+" = "+studentsSections.getInt(0);
            Cursor participations = db.rawQuery(query, null);
            if(participations.getCount()>0) {
                participations.moveToFirst();
                do {
                    acumGrades+=participations.getDouble(0);
                    quantityOfGrades++;
                } while (participations.moveToNext());
                average=acumGrades/quantityOfGrades;
                String name=this.getStudentName(studentsSections.getInt(1));
                averagesList.add(new StudentAndAverage(name,average));
            }
            participations.close();
            studentsSections.moveToNext();
        }
        studentsSections.close();
        //find the Highest Average
        double max=0;
        String maxName="";
        for(int i=0;i<averagesList.size();i++){
            if(averagesList.get(i).average>max){
                max=averagesList.get(i).average;
                maxName=averagesList.get(i).name;
            }
        }
        return maxName+"\t"+Math.round(max)+"%";
    }
    String getStudentName(int studentId){
        String name="";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+STU_NAME+" FROM "+TABLE_STUDENT+" WHERE "+STU_ID+" = "+studentId;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            name=cursor.getString(0);
        }
        cursor.close();
        return name;
    }
    List<Participation> getStudentParticipationList(int studentSectionId)
    {
        List<Participation> currentStudentParticipationList = new ArrayList<Participation>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM participationStudent WHERE StudentSectionId="+studentSectionId;

        Cursor cursor = db.rawQuery(query, null);

        if ( cursor.moveToFirst() )
        {
            do
            {
                currentStudentParticipationList.add(new Participation(cursor.getInt(0), cursor.getInt(1),
                        cursor.getDouble(2), cursor.getString(3),
                        cursor.getString(4)));

            } while ( cursor.moveToNext() );
        }

        cursor.close();
        db.close();

        return currentStudentParticipationList;
    }

    double getFinalGrade( int studentSectionId){
        String selectQuery  = "SELECT StudentSectionFinal FROM studentSection WHERE StudentSectionId = " +
                studentSectionId;
        SQLiteDatabase db = this.getWritableDatabase();
        double studentSectionFinal = 0;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if ( cursor.moveToFirst() )
        {
            studentSectionFinal = cursor.getDouble(0);
        }
        cursor.close();
        return studentSectionFinal;
    }

    public void UpdateparticipationStudent(int studentSectionId, double studentSectionFinal ){
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE studentSection SET StudentSectionFinal =  " + studentSectionFinal + " WHERE StudentSectionId = " +
                studentSectionId;
        db.execSQL(strSQL);
        db.close();
    }

    void addStudent(Student student){
        SQLiteDatabase	db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STU_ID, student.get_StudentId());
        values.put(STU_NAME, student.get_StudentName());
        values.put(STU_MAJOR, student.get_StudentMajor());
        db.insert(TABLE_STUDENT, null, values);
        db.close();
    }

    void deleteStudent(ArrayList<Integer> toDelete){
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL;
        for(int i=0; i<toDelete.size(); i++){
            //Eliminar referencia del estudiante de tabla de participaciones por estudiante
            List<Participation> currentStudentParticipationList = new ArrayList<Participation>();
            String query = "SELECT StudentSectionId FROM studentSection WHERE StudentId=" + toDelete.get(i);
            Cursor cursor = db.rawQuery(query, null);
            if ( cursor.moveToFirst() ){
                do{
                    strSQL = "DELETE FROM participationStudent WHERE StudentSectionId=" + cursor.getInt(0);
                    db.execSQL(strSQL);
                }while ( cursor.moveToNext() );
            }
            cursor.close();
            //Eliminar referencia del estudiante de tabla de estudiantes por sección
            strSQL = "DELETE FROM studentSection WHERE StudentId=" + toDelete.get(i);
            db.execSQL(strSQL);

            /*//Eliminar referencia del estudiante de tabla de criterios
            String query2 = "SELECT CriteriaId FROM homeworkStudent WHERE StudentId=" + toDelete.get(i);
            Cursor cursor2 = db.rawQuery(query2, null);
            if ( cursor2.moveToFirst() ){
                do{
                    strSQL = "DELETE FROM criteria WHERE CriteriaId=" + Integer.toString(cursor2.getInt(0));
                    db.execSQL(strSQL);
                }while ( cursor2.moveToNext() );
            }*/

            //Eliminar referencia del estudiante de tabla de criterios
            strSQL = "DELETE FROM homeworkStudent WHERE StudentId=" + toDelete.get(i);
            db.execSQL(strSQL);


            //Eliminar referencia del estudiante de tabla de estudiantes
            strSQL = "DELETE FROM student WHERE StudentId=" + toDelete.get(i);
            db.execSQL(strSQL);
        }
        db.close();
    }

    boolean studentExist(int studentId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + STU_ID + " FROM " + TABLE_STUDENT + " WHERE " + STU_ID + " = " + studentId, null);
        if ( cursor.getCount() > 0 ){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
    boolean tableStudentIsEmpty(int sectionId){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + STUSEC_STUD + " FROM " + TABLE_STUDENTSECTION +
                                    " WHERE " + STUSEC_SECT + " = " + sectionId, null);
        if ( cursor.getCount() > 0 ){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    boolean studentSectionExist(Section section, int studentId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + STUSEC_STUD + " FROM " + TABLE_STUDENTSECTION +
                " WHERE " + STUSEC_SECT + " = " + section.get_SectionId(), null);
        if ( cursor.moveToFirst() )        {
            do{
                if ( cursor.getInt(0) == studentId ){
                    cursor.close();
                    return true;
                }

            }while ( cursor.moveToNext() );
        }
        cursor.close();
        return false;
    }

    Course getCourse(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_COURSE,
                new String[]{COURSE_ID, COURSE_CODE, COURSE_NAME, COURSE_DESC},
                COURSE_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Course course = new Course(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3));
        cursor.close();
        return course;
    }
    public String getCourseName(int courseId){
        List<Course> courseList = new ArrayList<Course>();
        String selectQuery  = "SELECT "+COURSE_NAME+" FROM " + TABLE_COURSE + " WHERE " + COURSE_ID + " = "+courseId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if(cursor.getCount()>0){
            String retVal = cursor.getString(0);
            cursor.close();
            return retVal;
        }
        String retVal = cursor.getString(0);
        cursor.close();
        return retVal;
    }
    public List<Course> getAllCourses(){
        List<Course> courseList = new ArrayList<Course>();
        String selectQuery  = "SELECT * FROM " + TABLE_COURSE + " ORDER BY " + COURSE_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do{
                Course course = new Course();
                course.setCourseId(Integer.parseInt(cursor.getString(0)));
                course.setCourseCode(cursor.getString(1));
                course.setCourseName(cursor.getString(2));
                course.setCourseDescription(cursor.getString(3));
                courseList.add(course);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return courseList;
    }

    public List<Section> getAllSections(){
        List<Section> sectionList = new ArrayList<Section>();
        String selectQuery  = "SELECT * FROM " + TABLE_SECTION + " ORDER BY " + SECT_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do{
                Section section = new Section();
                section.set_SectionId(Integer.parseInt(cursor.getString(0)));
                section.set_CourseId(Integer.parseInt(cursor.getString(1)));
                section.set_SectionQuarter(Integer.parseInt(cursor.getString(2)));
                section.set_SectionSemester(Integer.parseInt(cursor.getString(3)));
                section.set_SectionYear(Integer.parseInt(cursor.getString(4)));
                sectionList.add(section);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return sectionList;
    }

    public List<String> getAllName_Courses(){
        List<String> courseList = new ArrayList<String>();
        String selectQuery  = "SELECT * FROM " + TABLE_COURSE + " ORDER BY " + COURSE_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do{
                courseList.add(cursor.getString(2));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return courseList;
    }
    public int getCoursesCount(){
        String countQuery = "SELECT * FROM " + TABLE_COURSE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }
    public int updateCourse(Course course){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COURSE_CODE, course.getCourseCode());
        values.put(COURSE_NAME, course.getCourseName());
        values.put(COURSE_DESC, course.getCourseDescription());
        return db.update(
                TABLE_COURSE,
                values,
                COURSE_ID + "=?",
                new String[]{String.valueOf(course.getCourseId())});
    }
    public void deleteCourse(Course course){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(
                TABLE_COURSE,
                COURSE_ID + "=?",
                new String[]{String.valueOf(course.getCourseId())});
        db.close();
    }
    public void addHomework(Homework homework){
        SQLiteDatabase	db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HOMEWORK_NAME, homework.getHomeworkName());
        values.put(HOMEWORK_SECID, homework.getSectionId());
        db.insert(TABLE_HOMEWORK, null, values);
        db.close();
    }
    public Homework getLastHomework(){
        Homework homework=null;
        String selectQuery  = "SELECT * FROM " + TABLE_HOMEWORK +
                " ORDER BY " + HOMEWORK_ID + " DESC LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do{
                homework=new Homework(cursor.getInt(0),cursor.getString(1),cursor.getInt(2));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return homework;
    }
    public List<Homework> getAll_Homework(int sectionId){
        List<Homework> homeworkList = new ArrayList<Homework>();
        String selectQuery  = "SELECT * FROM " + TABLE_HOMEWORK +
                              " WHERE "+ HOMEWORK_SECID + " = " + sectionId +
                              " ORDER BY " + HOMEWORK_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do{
                homeworkList.add(new Homework(cursor.getInt(0),cursor.getString(1),cursor.getInt(2)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return homeworkList;
    }

    boolean homeworkExist(Homework homework,int SectionId)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + HOMEWORK_ID +
                                    " FROM " + TABLE_HOMEWORK +
                                    " WHERE " + HOMEWORK_NAME + " = '" + homework.getHomeworkName() + "'"+
                                    " AND " + HOMEWORK_SECID + " = " + SectionId, null);

        if ( cursor.getCount() > 0 )
        {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
    boolean criteriaExist(String criteriaName,int homeworkId)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + CRITERIA_ID +
                " FROM " + TABLE_CRITERIA +
                " WHERE " + CRITERIA_NAME + " = '" + criteriaName + "'"+
                " AND " + CRITERIA_HOMEWORK + " = " + homeworkId, null);

        if ( cursor.getCount() > 0 )
        {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
    public void addCriteria(Criteria criteria){
        SQLiteDatabase	db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CRITERIA_NAME, criteria.getCriteriaName());
        values.put(CRITERIA_WEIGHT,criteria.getCriteriaWeight());
        values.put(CRITERIA_HOMEWORK,criteria.getCriteriaHomeworkId());
        db.insert(TABLE_CRITERIA, null, values);
        db.close();
    }
    public double getCriteriaWeight(String criteriaName,int homeworkId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + CRITERIA_WEIGHT +
                " FROM " + TABLE_CRITERIA +
                " WHERE " + CRITERIA_NAME + " = '" + criteriaName + "'"+
                " AND " + CRITERIA_HOMEWORK + " = " + homeworkId;
        Cursor cursor = db.rawQuery(query, null);
        Double weight=0.0;
        if ( cursor.moveToFirst() )
        {
            weight = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return weight;
    }
    public List<Double> getAllCriteria_Weights(int homeworkId){
        List<Double> weightList = new ArrayList<Double>();
        String selectQuery  = "SELECT " + CRITERIA_WEIGHT +
                " FROM " + TABLE_CRITERIA +
                " WHERE " + CRITERIA_HOMEWORK + " = " + homeworkId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do{
                weightList.add(cursor.getDouble(0));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return weightList;
    }
    public List<Criteria> getAllCriteriaByHomework(int homeworkId){
        List<Criteria> criteriaList = new ArrayList<Criteria>();
        String selectQuery  = "SELECT * FROM " + TABLE_CRITERIA +
                " WHERE " + CRITERIA_HOMEWORK + " = " + homeworkId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do{
                criteriaList.add(new Criteria(cursor.getInt(0),cursor.getString(1),cursor.getDouble(2),cursor.getInt(3)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return criteriaList;
    }
    public void printHomeworkTable(){
        String selectQuery  = "SELECT * FROM " + TABLE_HOMEWORK +
                " ORDER BY " + HOMEWORK_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do{
                Homework homework=new Homework(cursor.getInt(0),cursor.getString(1),cursor.getInt(2));
                Log.e(DatabaseHandler.class.toString(),homework.toString()+"\n");
            }while (cursor.moveToNext());
        }
        cursor.close();
    }
    public void printCriteriaTable(){
        String selectQuery  = "SELECT * FROM " + TABLE_CRITERIA
                            +" ORDER BY " + CRITERIA_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do{
                Criteria criteria= new Criteria(cursor.getInt(0),cursor.getString(1),cursor.getDouble(2),cursor.getInt(3));
                Log.e(DatabaseHandler.class.toString(),criteria.toString()+"\n");
            }while (cursor.moveToNext());
        }
        cursor.close();
    }
    public void addHomeworkStudent(Double grade,int criteriaId,int studentId){
        SQLiteDatabase	db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HOMESTU_Grade, grade);
        values.put(HOMESTU_CriteriaId,criteriaId);
        values.put(HOMESTU_StudentId,studentId);
        db.insert(TABLE_HOMESTU, null, values);
        db.close();
    }
    public void printHomeworkStudentTable(){
        String selectQuery  = "SELECT * FROM " + TABLE_HOMESTU
                +" ORDER BY " + HOMESTU_ID+ " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do{
                Log.e(DatabaseHandler.class.toString(),"Homestu_Id= "+cursor.getInt(0)+
                                                      " Homestu_Grade= "+cursor.getDouble(1)+
                                                      " Homestu_CriteriaId= "+cursor.getInt(2)+
                                                      " Homestu_StudentId= "+cursor.getInt(3)+"\n");
            }while (cursor.moveToNext());
        }
        cursor.close();
    }
    public boolean homeworkStudentExist(int criteriaId,int studentId){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + HOMESTU_ID +
                " FROM " + TABLE_HOMESTU +
                " WHERE " + HOMESTU_CriteriaId + " = '" + criteriaId + "'"+
                " AND " + HOMESTU_StudentId + " = " + studentId, null);
        if ( cursor.getCount() > 0 )
        {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
    public int getHomeworkStudentId(int criteriaId,int studentId){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + HOMESTU_ID +
                " FROM " + TABLE_HOMESTU +
                " WHERE " + HOMESTU_CriteriaId + " = '" + criteriaId + "'"+
                " AND " + HOMESTU_StudentId + " = " + studentId, null);
        if ( cursor.moveToFirst() )
        {
            int retVal = cursor.getInt(0);
            cursor.close();
            return retVal;
        }
        cursor.close();
        return 0;
    }
    public void updateHomeworkStudent(int homeworkStudentId,double grade){
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE "+TABLE_HOMESTU+" SET "+HOMESTU_Grade+" = " + grade +
                        " WHERE "+HOMESTU_ID+" = " +homeworkStudentId;
        db.execSQL(strSQL);
        db.close();
    }

    public List<String> getHomeworkNameAndGrade(int StudentID, int SectionID){
        SQLiteDatabase db = this.getReadableDatabase();
        //query to get the Homeworks of the section
        Cursor homeworks = db.rawQuery("SELECT " + HOMEWORK_ID + ", " + HOMEWORK_NAME + " FROM " + TABLE_HOMEWORK + " WHERE " + HOMEWORK_SECID + "=" + SectionID, null);
        homeworks.moveToFirst();
        double total_criteria_percentage=0;//Sum of all percentages obtained in all criteria of a homework
        double total_points_criteria = 0;//Total points possible per homework / Sum of all criteria weight per homework
        List<String> homeworks_return=new ArrayList<String>();

        for(int i=0; i<homeworks.getCount(); i++){
            int id_homework = homeworks.getInt(0);
            String name_homework = homeworks.getString(1);
            //query to get all the criteria "id and weight" from a homework
            Cursor criteria_homework = db.rawQuery("SELECT " + CRITERIA_ID + ", " + CRITERIA_WEIGHT + " FROM " + TABLE_CRITERIA + " WHERE " + CRITERIA_HOMEWORK + "=" + id_homework,null);
            criteria_homework.moveToFirst();
            total_criteria_percentage = 0;
            total_points_criteria=0;

            for(int j=0; j<criteria_homework.getCount(); j++){
                int criteria_id_homework=criteria_homework.getInt(0);
                double criteria_weight=criteria_homework.getDouble(1);
                //get the grades of the criterias
                Cursor grade_criteria = db.rawQuery("SELECT " + HOMESTU_Grade + " FROM " + TABLE_HOMESTU + " WHERE " + HOMESTU_CriteriaId + "=" + criteria_id_homework + " AND " + HOMESTU_StudentId + "=" + StudentID,null);
                if(grade_criteria.getCount()>0) {
                    grade_criteria.moveToFirst();
                    total_criteria_percentage += (grade_criteria.getDouble(0)*criteria_weight)/100;
                    criteria_homework.moveToNext();
                }
                grade_criteria.close();
                total_points_criteria += criteria_weight;
            }
            criteria_homework.close();
            homeworks_return.add(name_homework + "HOLAHELLO" + Double.toString((total_criteria_percentage / total_points_criteria)*100));
            homeworks.moveToNext();
        }
        homeworks.close();
        db.close();
        return homeworks_return;
    }
    public double getCriteriaGrade(int studentId, int criteriaId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor grade_criteria = db.rawQuery("SELECT " + HOMESTU_Grade +
                                            " FROM " + TABLE_HOMESTU +
                                            " WHERE " + HOMESTU_CriteriaId + "=" + criteriaId +
                                            " AND " + HOMESTU_StudentId + "=" + studentId,null);
        //if the criteria has been checked
        if(grade_criteria.getCount()>0){
            grade_criteria.moveToFirst();
            double retVal = grade_criteria.getDouble(0);
            grade_criteria.close();
            return retVal;
        }
        //if the criteria hasn't been checked
        grade_criteria.close();
        return 0;
    }

    public List<String> exportStudentGrades(){
        String to_write;
        List<String> all_grades = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor courseid_cursor = db.rawQuery("SELECT " + COURSE_ID +","+ COURSE_CODE +","+ COURSE_NAME + " FROM " + TABLE_COURSE, null);
        courseid_cursor.moveToFirst();
        do{
            to_write="";
            int courseid = courseid_cursor.getInt(0);
            to_write="Course CODE:" + courseid_cursor.getString(1) + "\n";
            to_write+="Course NAME:" + courseid_cursor.getString(2) + "\n" + "\n";
            Cursor sectionid_cursor = db.rawQuery("SELECT " + SECT_ID +","+ SECT_CODE + " FROM " + TABLE_SECTION + " WHERE " + SECT_COURSE + "=" + courseid, null);
            sectionid_cursor.moveToFirst();
            do{
                int sectionid = sectionid_cursor.getInt(0);
                to_write+=("Section CODE:" + sectionid_cursor.getString(1) + "\n");
                Cursor studentsectionid_cursor = db.rawQuery("SELECT " + STUSEC_ID +","+ STUSEC_STUD + " FROM " + TABLE_STUDENTSECTION + " WHERE " + STUSEC_SECT + "=" + sectionid, null);
                studentsectionid_cursor.moveToFirst();
                do{
                    Cursor student = db.rawQuery("SELECT " + STU_ID +","+ STU_NAME + " FROM " + TABLE_STUDENT + " WHERE " + STU_ID + "=" + studentsectionid_cursor.getInt(1), null);
                    student.moveToFirst();
                    to_write+=("  + Student ID:" + Integer.toString(student.getInt(0)) + "\n" + "    Name:" + student.getString(1) + "\n");

                    int studentsectionid = studentsectionid_cursor.getInt(0);
                    int studentid = studentsectionid_cursor.getInt(1);

                    List<String> tareas_estudiante = getHomeworkNameAndGrade(studentid, sectionid);
                    to_write+="     >Homeworks\n";
                    for(String s: tareas_estudiante){
                        String[] homework_parts = (s.split("HOLAHELLO"));
                        to_write+=("        * " + homework_parts[0] + " " + homework_parts[1] + "\n");
                        //Log.d("TAREA", s);
                    }
                    db = this.getReadableDatabase();
                    List<Participation> participaciones_estudiantes = getStudentParticipationList(studentsectionid);
                    to_write+="     >Paticipations\n";
                    for(Participation p: participaciones_estudiantes){
                        to_write+=("        * " + Double.toString(p.get_ParticipationGrade()) + " - " + p.get_ParticipationDate() + " - " + p.get_ParticipationComment() + "\n");
                    }
                    db = this.getReadableDatabase();
                    student.close();
                }while(studentsectionid_cursor.moveToNext());
                studentsectionid_cursor.close();
            }while(sectionid_cursor.moveToNext());
            sectionid_cursor.close();
            all_grades.add(to_write);
        }while(courseid_cursor.moveToNext());
        courseid_cursor.close();
        db.close();
        return all_grades;
    }
}