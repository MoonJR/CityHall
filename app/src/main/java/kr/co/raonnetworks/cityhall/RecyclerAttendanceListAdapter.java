package kr.co.raonnetworks.cityhall;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import kr.co.raonnetworks.cityhall.libs.DBManager;
import kr.co.raonnetworks.cityhall.model.AttendanceModel;
import kr.co.raonnetworks.cityhall.model.EducationModel;

/**
 * Created by MoonJR on 2015. 8. 27..
 */
public class RecyclerAttendanceListAdapter extends RecyclerView.Adapter<RecyclerAttendanceListAdapter.ViewHolder> {

    private EducationModel mEducationModel;
    private ArrayList<AttendanceModel> datas;
    private SimpleDateFormat format;


    public RecyclerAttendanceListAdapter(EducationModel mEducationModel) {
        this.mEducationModel = mEducationModel;
        this.datas = DBManager.getAttendance(mEducationModel);
        this.format = new SimpleDateFormat("HH:mm");
    }

    @Override
    public RecyclerAttendanceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_attendance_list, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        AttendanceModel data = datas.get(position);
        if (data.getWorkerName() == null) {
            viewHolder.mTextViewName.setText("미등록");
        } else {
            viewHolder.mTextViewName.setText(data.getWorkerName());
        }

        viewHolder.mTextViewStart.setText(format.format(mEducationModel.getEduStart()));
        viewHolder.mTextViewEnd.setText(format.format(mEducationModel.getEduEnd()));
        viewHolder.mTextViewTime.setText(mEducationModel.getEduTime());
        viewHolder.mTextViewTag.setText(Long.toHexString(data.getWorkerCard()));

    }

    @Override
    public int getItemCount() {
        if (datas != null) {
            return datas.size();
        } else {
            return 0;
        }
    }

    public void notifyUpdate() {
        this.datas = DBManager.getAttendance(mEducationModel);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextViewName;
        public TextView mTextViewStart;
        public TextView mTextViewEnd;
        public TextView mTextViewTime;
        public TextView mTextViewTag;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            mTextViewName = (TextView) itemLayoutView.findViewById(R.id.textViewName);
            mTextViewStart = (TextView) itemLayoutView.findViewById(R.id.textViewStart);
            mTextViewEnd = (TextView) itemLayoutView.findViewById(R.id.textViewEnd);
            mTextViewTime = (TextView) itemLayoutView.findViewById(R.id.textViewTime);
            mTextViewTag = (TextView) itemLayoutView.findViewById(R.id.textViewTag);
        }
    }

}
