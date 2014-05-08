package edu.unitec.app;

import java.io.Serializable;

/**
 * Created by Henry on 11-29-13.
 */
public class Section implements Serializable
{

    private int _SectionId;
	private int _CourseId;
	private int _SectionQuarter;
	private int _SectionSemester;
    private int _SectionYear;

    public Section(){}
    public Section(int _SectionId, int _CourseId, int _SectionQuarter,
                   int _SectionSemester, int _SectionYear)
    {
		super();
		this._SectionId = _SectionId;
		this._CourseId = _CourseId;
		this._SectionQuarter = _SectionQuarter;
		this._SectionSemester = _SectionSemester;
		this._SectionYear = _SectionYear;
	}
	public int get_SectionId()
    {
		return _SectionId;
	}
	public void set_SectionId(int _SectionId)
    {
		this._SectionId = _SectionId;
	}
	public int get_CourseId()
    {
		return _CourseId;
	}
	public void set_CourseId(int _CourseId)
    {
		this._CourseId = _CourseId;
	}
	public int get_SectionQuarter()
    {
		return _SectionQuarter;
	}
	public void set_SectionQuarter(int _SectionQuarter)
    {
		this._SectionQuarter = _SectionQuarter;
	}
	public int get_SectionSemester()
    {
		return _SectionSemester;
	}
	public void set_SectionSemester(int _SectionSemester)
    {
		this._SectionSemester = _SectionSemester;
	}
	public int get_SectionYear()
    {
		return _SectionYear;
	}
	public void set_SectionYear(int _SectionYear)
    {
		this._SectionYear = _SectionYear;
	}

    @Override
    public String toString(){
        return _SectionId+","+_CourseId+","+_SectionQuarter+","+_SectionSemester+","+_SectionYear;
    }
}