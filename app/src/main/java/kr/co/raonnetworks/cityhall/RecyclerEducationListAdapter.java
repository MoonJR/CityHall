package kr.co.raonnetworks.cityhall;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.raonnetworks.cityhall.model.EducationModel;

/**
 * Created by MoonJR on 2015. 8. 27..
 */
public class RecyclerEducationListAdapter extends RecyclerView.Adapter<RecyclerEducationListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<EducationModel> datas;

    public RecyclerEducationListAdapter(Context mContext, ArrayList<EducationModel> datas) {
        this.mContext = mContext;
        this.datas = datas;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerEducationListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        // create a new view
//        View itemLayoutView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.adapter_bus_route_list, parent, false);

        // create ViewHolder

        return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        EducationModel data = this.datas.get(position);

    }

    @Override
    public int getItemCount() {
        if (datas != null) {
            return datas.size();
        } else {
            return 0;
        }
    }

    public ArrayList<EducationModel> getData() {
        return datas;
    }


    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

//        public ViewGroup mLayoutAdapterMain;
//        public ViewGroup mLayoutBus;
//
//        public TextView mTextViewPlateNo;
//        public TextView mTextViewStationNm;
//        public TextView mTextViewRemainSeatCount;
//        public TextView mTextViewDistance;

        public TextView mTextView;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
//            mLayoutAdapterMain = (ViewGroup) itemLayoutView.findViewById(R.id.layoutAdapterMain);
//            mLayoutBus = (ViewGroup) itemLayoutView.findViewById(R.id.layoutBus);
//            mTextViewStationNm = (TextView) itemLayoutView.findViewById(R.id.textViewStationNm);
//            mTextViewPlateNo = (TextView) itemLayoutView.findViewById(R.id.textViewPlateNo);
//            mTextViewRemainSeatCount = (TextView) itemLayoutView.findViewById(R.id.textViewRemainSeatCount);
//            mTextViewDistance = (TextView) itemLayoutView.findViewById(R.id.textViewDistance);
        }
    }

}
