package com.example.chetanmuliya.studentpanel.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chetanmuliya.studentpanel.R;
import com.example.chetanmuliya.studentpanel.activity.AttendanceActivity;
import com.example.chetanmuliya.studentpanel.activity.TestRecordActivity;
import com.example.chetanmuliya.studentpanel.activity.TimeTableActivity;

import java.util.List;

/**
 * Created by domaine on 03/05/18.
 */

public class SelectCourseAdapter extends RecyclerView.Adapter<SelectCourseAdapter.MyViewHolder> {

    String[] batchList ;
    String[] coursenameList ;
    Context ctx;
    String status;

    public SelectCourseAdapter(String[] batchList, String status, String[] coursenameList, Context ctx ) {
        this.batchList = batchList;
        this.coursenameList = coursenameList;
        this.status = status;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.custom_select_course,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
          holder.selectCourseView.setText(coursenameList[position]);
          if(status.equals("reportcard")) {
              holder.selectCourseView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Intent intent = new Intent(v.getContext(), TestRecordActivity.class);
                      intent.putExtra("selectedBatch", batchList[position]);
                      v.getContext().startActivity(intent);
                  }
              });
          }else  if(status.equals("attendance")) {
              holder.selectCourseView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Intent intent = new Intent(v.getContext(), AttendanceActivity.class);
                      intent.putExtra("selectedBatch",batchList[position]);
                      v.getContext().startActivity(intent);
                  }
              });
          }else  if(status.equals("timetable")) {
              holder.selectCourseView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Intent intent = new Intent(v.getContext(), TimeTableActivity.class);
                      intent.putExtra("selectedBatch",batchList[position]);
                      intent.putExtra("onSelect","timetable");
                      v.getContext().startActivity(intent);
                  }
              });
          }else  if(status.equals("testschedule")) {
              holder.selectCourseView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Intent intent = new Intent(v.getContext(), TimeTableActivity.class);
                      intent.putExtra("selectedBatch",batchList[position]);
                      intent.putExtra("onSelect","testschedule");
                      v.getContext().startActivity(intent);
                  }
              });
          }
    }

    @Override
    public int getItemCount() {
        return coursenameList.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView selectCourseView;
        public MyViewHolder(View itemView) {
            super(itemView);
            selectCourseView = (TextView)itemView.findViewById(R.id.selectCourseName);
        }

    }
}
