package com.example.chetanmuliya.studentpanel.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.chetanmuliya.studentpanel.R;
import com.example.chetanmuliya.studentpanel.adapter.AttendanceMonthAdapter;
import com.example.chetanmuliya.studentpanel.app.ApiClient;
import com.example.chetanmuliya.studentpanel.app.ApiInterface;
import com.example.chetanmuliya.studentpanel.helper.CustomOnCLickListener;
import com.example.chetanmuliya.studentpanel.helper.CustomProgressDialog;
import com.example.chetanmuliya.studentpanel.helper.SQLiteLoginHandler;
import com.example.chetanmuliya.studentpanel.model.AttendanceMonth;
import com.example.chetanmuliya.studentpanel.model.Student;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceActivity extends AppCompatActivity {

     public CustomProgressDialog customProgressDialog;
     private CustomOnCLickListener customOnCLickListener;
    Context ctx;
    RecyclerView attendanceRVList;
    String username,batch,my;
    SQLiteLoginHandler db;
    List<AttendanceMonth> attendanceMonthList;
    private AttendanceMonthAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Attendance");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

        db=new SQLiteLoginHandler(getApplicationContext());
        List<Student> studentList=db.getStudentdetails();
        if(getIntent()!=null){
            batch=getIntent().getExtras().getString("selectedBatch");
        }

        username=studentList.get(0).getUsername();
        //progress dialog
        customProgressDialog=new CustomProgressDialog(this);
        customProgressDialog.showProgressDialog();
        customProgressDialog.addMessage("Please wait... loading attendance");
        //adapter
        attendanceMonthList=new ArrayList<>();
        attendanceRVList=(RecyclerView)findViewById(R.id.attendanceRV);
        attendanceRVList.setHasFixedSize(true);
        attendanceRVList.setLayoutManager(new LinearLayoutManager(this));
        fetchAttendance();
    }

    private void fetchAttendance() {
        ApiInterface services = ApiClient.getClient().create(ApiInterface.class);
        Log.e("(*****", "batch: "+batch +" username" +username );
       Call<List<AttendanceMonth>> call = services.getAttendanceMonth(batch,username);
       call.enqueue(new Callback<List<AttendanceMonth>>() {
           @Override
           public void onResponse(Call<List<AttendanceMonth>> call, Response<List<AttendanceMonth>> response) {
               customProgressDialog.hideProgressDialog();
               if(response == null){
                   Toast.makeText(getApplicationContext(),"No Attendance Record",Toast.LENGTH_LONG).show();
                   finish();
                   return;
               }else {
                   attendanceMonthList = response.body();
                   adapter = new AttendanceMonthAdapter(attendanceMonthList, ctx, new CustomOnCLickListener() {
                       @Override
                       public void onCLick(View v, int position) {
                           Intent intent = new Intent(v.getContext(), DetailAttendanceActivity.class);
                           intent.putExtra("my", attendanceMonthList.get(position).getMonth());
                           intent.putExtra("username", username);
                           intent.putExtra("batch", batch);
                           startActivity(intent);
                       }
                   });
                   attendanceRVList.setAdapter(adapter);
                   Log.e("****", "onResponse: " + new Gson().toJson(response));
               }
           }

           @Override
           public void onFailure(Call<List<AttendanceMonth>> call, Throwable t) {
               CustomProgressDialog.hideProgressDialog();
               Toast.makeText(getApplicationContext(),"FAILED ! to Load attendance",Toast.LENGTH_LONG).show();
               t.printStackTrace();
           }
       });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
