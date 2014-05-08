package edu.unitec.app;

/**
 * Created by Henry on 11-29-13.
 */
public class Course
{

	// Private data variables
	private int _CourseId;
	private String _CourseCode;
	private String _CourseName;
	private String _CourseDescription;

    public Course(){}
	public Course(int id, String code, String name, String description)
    {
		_CourseId = id;
		_CourseCode = code;
		_CourseName = name;
		_CourseDescription = description;
	}
	public Course(String _CourseCode, String _CourseName,
			String _CourseDescription)
    {
		this._CourseCode = _CourseCode;
		this._CourseName = _CourseName;
		this._CourseDescription = _CourseDescription;
	}
	public int getCourseId()
    {
		return _CourseId;
	}
	public void setCourseId(int _CourseId)
    {
		this._CourseId = _CourseId;
	}
	public String getCourseCode()
    {
		return _CourseCode;
	}
	public void setCourseCode(String _CourseCode)
    {
		this._CourseCode = _CourseCode;
	}
	public String getCourseName()
    {
		return _CourseName;
	}
	public void setCourseName(String _CourseName)
    {
		this._CourseName = _CourseName;
	}
	public String getCourseDescription()
    {
		return _CourseDescription;
	}
	public void setCourseDescription(String _CourseDescription)
    {
		this._CourseDescription = _CourseDescription;
	}
	public String toString(){
		return _CourseCode + " " + _CourseName + " " + _CourseDescription;
	}
}