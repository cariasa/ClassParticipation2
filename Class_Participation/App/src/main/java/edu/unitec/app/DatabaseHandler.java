package edu.unitec.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Henry on 12-02-13.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Participation";

    // Table Course
    private static final String TABLE_COURSE = "course";
    // Table Course Fields
    private static final String COURSE_ID = "CourseId";
    private static final String COURSE_CODE = "CourseCode";
    private static final String COURSE_NAME = "CourseName";
    private static final String COURSE_DESC = "CourseDescription";

    // Table Section
    private static final String TABLE_SECTION = "section";
    // Table Section Fields
    private static final String SECT_ID = "SectionId";
    private static final String SECT_COURSE = "CourseId";
    private static final String SECT_QTR = "SectionQuarter";
    private static final String SECT_SEM = "SectionSemester";
    private static final String SECT_YEA = "SectionYear";

    // Table Students
    private static final String TABLE_STUDENT="student";
    // Table Students Fields
    private static final String STU_ID = "StudentId";
    private static final String STU_NAME = "StudentName";
    private static final String STU_MAJOR = "StudentMajor";

    // Table Students Per Section
    private static final String TABLE_STUDENTSECTION = "studentSection";
    // Table Students Per Section Fields
    private static final String STUSEC_ID = "StudentSectionId";
    private static final String STUSEC_SECT = "SectionId";
    private static final String STUSEC_STUD = "StudentId";
    private static final String STUSEC_FINAL = "StudentSectionFinal";

    // Table Partipations Per Student
    private static final String TABLE_PARTICIPATION = "participationStudent";
    // Table Participations Per Student Fields
    private static final String PART_ID = "ParticipationID";
    private static final String PART_STUSECT = "StudentSectionId";
    private static final String PART_GRADE = "ParticipationGrade";
    private static final String PART_DATE = "ParticipationDate";
    private static final String PART_COMMENT = "ParticipationComment";

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
                        COURSE_DESC + " TEXT" + ")";
        String CREATE_SECTION_TABLE =
                "CREATE TABLE " + TABLE_SECTION + " (" +
                        SECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SECT_COURSE + " INTEGER," +
                        SECT_QTR + " INTEGER," +
                        SECT_SEM + " INTEGER," +
                        SECT_YEA + " INTEGER, " +
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

        db.execSQL(CREATE_COURSE_TABLE);
        db.execSQL(CREATE_SECTION_TABLE);
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_STUDENTSECTION_TABLE);
        db.execSQL(CREATE_PARTICIPATION_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTSECTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPATION);
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
}
