package kr.co.raonnetworks.cityhall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.raonnetworks.cityhall.R;
import kr.co.raonnetworks.cityhall.activity.EducationDetailActivity;
import kr.co.raonnetworks.cityhall.libs.DBManager;
import kr.co.raonnetworks.cityhall.model.EducationModel;

/**
 * Created by MoonJR on 2015. 8. 27..
 */
public class RecyclerEducationListAdapter extends RecyclerView.Adapter<RecyclerEducationListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<EducationModel> datas;


    public RecyclerEducationListAdapter(Context mContext) {
        this.mContext = mContext;
        this.datas = DBManager.getEdu();

    }

    @Override
    public RecyclerEducationListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_education_list, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final EducationModel data = this.datas.get(position);
        viewHolder.mTextViewName.setText(data.getEduName());
        viewHolder.mTextViewType.setText(data.getEduType());
        viewHolder.mTextViewTarget.setText(data.getEduTargetString());
        viewHolder.mTextViewStart.setText(data.getEduStartString());
        viewHolder.mTextViewEnd.setText(data.getEduEndString());
        viewHolder.mTextViewAttendanceCount.setText(Integer.toString(data.getEduAttendanceCount()));
        viewHolder.mLayoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntentEducationDetail = new Intent(mContext, EducationDetailActivity.class);
                mIntentEducationDetail.putExtra("eduId", data.getEduId());
                mContext.startActivity(mIntentEducationDetail);
            }
        });
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
        this.datas = DBManager.getEdu();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewGroup mLayoutMain;
        public TextView mTextViewName;
        public TextView mTextViewType;
        public TextView mTextViewTarget;
        public TextView mTextViewStart;
        public TextView mTextViewEnd;
        public TextView mTextViewAttendanceCount;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            mTextViewName = (TextView) itemLayoutView.findViewById(R.id.textViewName);
            mTextViewType = (TextView) itemLayoutView.findViewById(R.id.textViewType);
            mTextViewTarget = (TextView) itemLayoutView.findViewById(R.id.textViewTarget);
            mTextViewStart = (TextView) itemLayoutView.findViewById(R.id.textViewStart);
            mTextViewEnd = (TextView) itemLayoutView.findViewById(R.id.textViewEnd);
            mTextViewAttendanceCount = (TextView) itemLayoutView.findViewById(R.id.textViewAttendanceCount);
            mLayoutMain = (ViewGroup) itemLayoutView.findViewById(R.id.layoutMain);
        }
    }

}
