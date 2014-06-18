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
        return studentSectionFinal;
    }

    public void UpdateparticipationStudent(int studentSectionId, double studentSectionFinal ){
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE studentSection SET StudentSectionFinal =  " + studentSectionFinal + " WHERE StudentSectionId = " +
                studentSectionId;
        db.execSQL(strSQL);
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
            //Log.d("toDelete: ", toDelete.get(i));

            //Eliminar referencia del estudiante de tabla de participaciones por estudiante
            List<Participation> currentStudentParticipationList = new ArrayList<Participation>();
            String query = "SELECT StudentSectionId FROM studentSection WHERE StudentId=" + toDelete.get(i);
            Cursor cursor = db.rawQuery(query, null);
            int cursor_position=0;
            if ( cursor.moveToFirst() ){
                do{
                    //Log.d("cursor: ", cursor.getInt(cursor_position));
                    strSQL = "DELETE FROM participationStudent WHERE StudentSectionId=" + cursor.getInt(cursor_position);
                    db.execSQL(strSQL);
                    cursor_position++;
                }while ( cursor.moveToNext() );
            }

            //Eliminar referencia del estudiante de tabla de estudiantes por sección
            strSQL = "DELETE FROM studentSection WHERE StudentId=" + toDelete.get(i);
            db.execSQL(strSQL);

            //Eliminar referencia del estudiante de tabla de estudiantes
            strSQL = "DELETE FROM student WHERE StudentId=" + toDelete.get(i);
            db.execSQL(strSQL);
        }
        db.close();
    }

    boolean studentExist(int studentId)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + STU_ID + " FROM " + TABLE_STUDENT + " WHERE " + STU_ID + " = " + studentId, null);

        if ( cursor.getCount() > 0 )
        {
            return true;
        }

        return false;
    }
    boolean tableStudentIsEmpty()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + STU_ID + " FROM " + TABLE_STUDENT, null);

        if ( cursor.getCount() > 0 )
        {
            return false;
        }

        return true;
    }

    boolean studentSectionExist(Section section, int studentId)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + STUSEC_STUD + " FROM " + TABLE_STUDENTSECTION +
                " WHERE " + STUSEC_SECT + " = " + section.get_SectionId(), null);

        if ( cursor.moveToFirst() )
        {
            do
            {
                if ( cursor.getInt(0) == studentId )
                {
                    return true;
                }

            } while ( cursor.moveToNext() );
        }

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
        return course;
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
            return true;
        }

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
            return true;
        }

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
    }
    public boolean homeworkStudentExist(int criteriaId,int studentId){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + HOMESTU_ID +
                " FROM " + TABLE_HOMESTU +
                " WHERE " + HOMESTU_CriteriaId + " = '" + criteriaId + "'"+
                " AND " + HOMESTU_StudentId + " = " + studentId, null);
        if ( cursor.getCount() > 0 )
        {
            return true;
        }

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
            return cursor.getInt(0);
        }
        return 0;
    }
    public void updateHomeworkStudent(int homeworkStudentId,double grade){
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE "+TABLE_HOMESTU+" SET "+HOMESTU_Grade+" = " + grade +
                        " WHERE "+HOMESTU_ID+" = " +homeworkStudentId;
        db.execSQL(strSQL);
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

            for(int j=0; j<criteria_homework.getCount(); j++){
                int criteria_id_homework=criteria_homework.getInt(0);
                double homework_weigth=criteria_homework.getDouble(1);
                //get the grades of the criterias
                Cursor grade_criteria = db.rawQuery("SELECT " + HOMESTU_Grade + " FROM " + TABLE_HOMESTU + " WHERE " + HOMESTU_CriteriaId + "=" + criteria_id_homework + " AND " + HOMESTU_StudentId + "=" + StudentID,null);
                if(grade_criteria.getCount()>0) {
                    grade_criteria.moveToFirst();
                    total_criteria_percentage += (grade_criteria.getDouble(0)*homework_weigth)/100;
                    total_points_criteria += homework_weigth;
                    criteria_homework.moveToNext();
                }
            }homeworks_return.add(name_homework + "HOLAHELLO" + Double.toString((total_criteria_percentage / total_points_criteria)*100));
            homeworks.moveToNext();
        }
        db.close();
        return homeworks_return;
    }
    public Double getCriteriaGrade(int studentId, int criteriaId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor grade_criteria = db.rawQuery("SELECT " + HOMESTU_Grade +
                                            " FROM " + TABLE_HOMESTU +
                                            " WHERE " + HOMESTU_CriteriaId + "=" + criteriaId +
                                            " AND " + HOMESTU_StudentId + "=" + studentId,null);
        //if the criteria has been checked
        if(grade_criteria.getCount()>0){
            grade_criteria.moveToFirst();
            return grade_criteria.getDouble(0);
        }
        //if the criteria hasn't been checked
        return 0.0;

    }

}