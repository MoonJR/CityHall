package kr.co.raonnetworks.cityhall;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.raonnetworks.cityhall.libs.DBManager;
import kr.co.raonnetworks.cityhall.model.AttendanceModel;
import kr.co.raonnetworks.cityhall.model.EducationModel;
import kr.co.raonnetworks.cityhall.model.WorkerModel;

/**
 * Created by MoonJR on 2015. 8. 27..
 */
public class RecyclerSelfAttendanceListAdapter extends RecyclerView.Adapter<RecyclerSelfAttendanceListAdapter.ViewHolder> {

    private ArrayList<AttendanceModel> datas;
    private Context mContext;
    private EducationModel mEducationModel;
    private String workerName;


    public RecyclerSelfAttendanceListAdapter(Context mContext, EducationModel mEducationModel, String workerName) {
        this.mContext = mContext;
        this.mEducationModel = mEducationModel;
        this.workerName = workerName;
        this.datas = DBManager.getAttendanceStatus(mEducationModel, workerName);
    }

    @Override
    public RecyclerSelfAttendanceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                           int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_education_self_attendance_list, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        AttendanceModel data = datas.get(position);
        viewHolder.mTextViewName.setText(data.getWorkerName());
        viewHolder.mTextViewPart.setText(data.getWorkerPart());

        String workerDivision = data.getWorkerDivision();
        if (workerName != null) {
            viewHolder.mTextViewDivision.setText(workerDivision);
        }


        final WorkerModel mWorkerModel = new WorkerModel();
        final String workerName = data.getWorkerName();
        mWorkerModel.setWorkerId(data.getWorkerId());
        mWorkerModel.setWorkerCard(data.getWorkerCard());


        //출석하지 않은 데이터
        if (data.getAttendanceId() == 0) {
            viewHolder.mButtonSelfAttendance.setText("출석");
            viewHolder.mButtonSelfAttendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(mContext)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle("알림")
                            .setMessage("'" + workerName + "' 님을 수기로 참석처리 하시겠습니까?")
                            .setNegativeButton("아니오", null)
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DBManager.addAttendance(mEducationModel, mWorkerModel);
                                    notifyUpdate(RecyclerSelfAttendanceListAdapter.this.workerName);
                                }
                            }).show();


                }
            });
        } else {
            //출석한 데이터
            viewHolder.mButtonSelfAttendance.setText("취소");
            viewHolder.mButtonSelfAttendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mContext)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle("알림")
                            .setMessage("'" + workerName + "' 님은 이미 참석처리가 되어 있습니다. 참석내역을 취소하시겠습니까?")
                            .setNegativeButton("아니오", null)
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DBManager.deleteAttendance(mEducationModel, mWorkerModel);
                                    notifyUpdate(RecyclerSelfAttendanceListAdapter.this.workerName);
                                }
                            }).show();
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        if (datas != null) {
            return datas.size();
        } else {
            return 0;
        }
    }

    public void notifyUpdate(String workerName) {
        this.datas = DBManager.getAttendanceStatus(this.mEducationModel, workerName);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewName;
        private TextView mTextViewPart;
        private TextView mTextViewDivision;
        private Button mButtonSelfAttendance;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            mTextViewName = (TextView) itemLayoutView.findViewById(R.id.textViewName);
            mTextViewPart = (TextView) itemLayoutView.findViewById(R.id.textViewPart);
            mTextViewDivision = (TextView) itemLayoutView.findViewById(R.id.textViewDivision);
            mButtonSelfAttendance = (Button) itemLayoutView.findViewById(R.id.buttonSelfAttendance);
        }
    }

}
