package edu.unitec.app;

/**
 * Created by Henry on 11-29-13.
 */
public class  Participation
{

    private int _ParticipationId;
	private int _StudentSectionId;
	private double _ParticipationGrade;
	private String _ParticipationDate;
	private String _ParticipationComment;
    private String _ParcicipationUUID;

    public Participation(){}
    public Participation(int _ParticipationId, int _StudentSectionId,
			double _ParticipationGrade, String _ParticipationDate,
			String _ParticipationComment,String UUID)
    {
		super();
		this._ParticipationId = _ParticipationId;
		this._StudentSectionId = _StudentSectionId;
		this._ParticipationGrade = _ParticipationGrade;
		this._ParticipationDate = _ParticipationDate;
		this._ParticipationComment = _ParticipationComment;
        this._ParcicipationUUID = UUID;
	}
	public Participation(int _ParticipationId, int _StudentSectionId,
			double _ParticipationGrade, String _ParticipationDate,String UUID)
    {
		super();
		this._ParticipationId = _ParticipationId;
		this._StudentSectionId = _StudentSectionId;
		this._ParticipationGrade = _ParticipationGrade;
		this._ParticipationDate = _ParticipationDate;
        this._ParcicipationUUID = UUID;
	}
	public Participation(int _ParticipationId, int _StudentSectionId,
			double _ParticipationGrade,String UUID)
    {
		super();
		this._ParticipationId = _ParticipationId;
		this._StudentSectionId = _StudentSectionId;
		this._ParticipationGrade = _ParticipationGrade;
		this._ParticipationDate = new java.sql.Timestamp(new java.util.Date().getTime()).toString();
        this._ParcicipationUUID = UUID;
	}
	public Participation(int _ParticipationId, int _StudentSectionId,String UUID)
    {
		super();
		this._ParticipationId = _ParticipationId;
		this._StudentSectionId = _StudentSectionId;
		this._ParticipationGrade = 0.0;
		this._ParticipationDate = new java.sql.Timestamp(new java.util.Date().getTime()).toString();
        this._ParcicipationUUID = UUID;
	}
    public Participation(int _StudentSectionId, double _ParticipationGrade, String _ParticipationDate,
                         String _ParticipationComment, String UUID)
    {
        super();
        this._StudentSectionId = _StudentSectionId;
        this._ParticipationGrade = _ParticipationGrade;
        this._ParticipationDate = _ParticipationDate;
        this._ParticipationComment = _ParticipationComment;
        this._ParcicipationUUID = UUID;
    }

	public int get_ParticipationId()
    {
		return _ParticipationId;
	}
	public void set_ParticipationId(int _ParticipationId)
    {
		this._ParticipationId = _ParticipationId;
	}
	public int get_StudentSectionId()
    {
		return _StudentSectionId;
	}
	public void set_StudentSectionId(int _StudentSectionId)
    {
		this._StudentSectionId = _StudentSectionId;
	}
	public double get_ParticipationGrade()
    {
		return _ParticipationGrade;
	}
	public void set_ParticipationGrade(double _ParticipationGrade)
    {
		this._ParticipationGrade = _ParticipationGrade;
	}
	public String get_ParticipationDate()
    {
		return _ParticipationDate;
	}
	public void set_ParticipationDate(String _ParticipationDate)
    {
		this._ParticipationDate = _ParticipationDate;
	}
	public String get_ParticipationComment()
    {
		return _ParticipationComment;
	}
	public void set_ParticipationComment(String _ParticipationComment)
    {
		this._ParticipationComment = _ParticipationComment;
	}

    public String get_ParcicipationUUID() {
        return _ParcicipationUUID;
    }

    public void set_ParcicipationUUID(String _ParcicipationUUID) {
        this._ParcicipationUUID = _ParcicipationUUID;
    }
}