package edu.unitec.app;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by Nivx on 12/6/14.
 */
public class RetainDataFragment extends Fragment {
    SemesterQuarter RData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public SemesterQuarter getData() {
        return RData;
    }

    public void setData(SemesterQuarter data, boolean Previous) {

        if (data.getQuarter() == SemesterQuarter.CURRENT_QUARTER && data.getSemester() == SemesterQuarter.CURRENT_SEMESTER && data.getYear() == SemesterQuarter.CURRENT_YEAR){
            Previous = false;
        }else{
            Previous = true;
        }

        this.RData = data;
    }
}
