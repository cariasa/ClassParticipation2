package edu.unitec.app;

/**
 * Created by Henry on 11-29-13.
 */
public class StudentSection
{

    private int _StudentSectionId;
	private int _SectionId;
	private int _StudentId;
	private double _StudentSectionFinal;

    public StudentSection(){}
	public StudentSection(int _StudentSectionId, int _SectionId,
			int _StudentId, double _StudentSectionFinal)
    {
		super();
		this._StudentSectionId = _StudentSectionId;
		this._SectionId = _SectionId;
		this._StudentId = _StudentId;
		this._StudentSectionFinal = _StudentSectionFinal;
	}

	public StudentSection(int _StudentSectionId, int _SectionId,
			int _StudentId)
    {
		super();
		this._StudentSectionId = _StudentSectionId;
		this._SectionId = _SectionId;
		this._StudentId = _StudentId;
		this._StudentSectionFinal = 0.0;
	}
	public int get_StudentSectionId()
    {
		return _StudentSectionId;
	}
	public void set_StudentSectionId(int _StudentSectionId)
    {
		this._StudentSectionId = _StudentSectionId;
	}
	public int get_SectionId()
    {
		return _SectionId;
	}
	public void set_SectionId(int _SectionId)
    {
		this._SectionId = _SectionId;
	}
	public int get_StudentId()
    {
		return _StudentId;
	}
	public void set_StudentId(int _StudentId)
    {
		this._StudentId = _StudentId;
	}
	public double get_StudentSectionFinal()
    {
		return _StudentSectionFinal;
	}
	public void set_StudentSectionFinal(double _StudentSectionFinal)
    {
		this._StudentSectionFinal = _StudentSectionFinal;
	}
}