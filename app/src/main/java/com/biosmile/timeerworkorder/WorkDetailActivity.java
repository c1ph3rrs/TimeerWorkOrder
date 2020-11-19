package com.biosmile.timeerworkorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.biosmile.timeerworkorder.Clients.ClientsDetailActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class WorkDetailActivity extends AppCompatActivity {

    TextInputLayout workDetailJobTxt, workDetailHardwareTxt, workDetailHourTxt;
    String workJobDetail, workDetailHardware, workDetailHour, workTodayDate, workTravel, workSpinner;
    ImageView workDetailBackIcon, workDetailDeleteIcon;
    TextView workTodayTimeTV, workerCompanyName;
    Button workUpdateBtn;
    RadioGroup travelRadioBox;
    RadioButton yesRadioButton, noRadioButton, radioButton;
    ProgressDialog loadingDialog;

    ArrayList<String> customerList = new ArrayList<>();
    DatabaseReference customerSpinnerRef, rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_detail);

        customerSpinnerRef = FirebaseDatabase.getInstance().getReference().child("Customers");


        workJobDetail = getIntent().getStringExtra("workJobDetail");
        workDetailHardware = getIntent().getStringExtra("workHardware");
        workDetailHour = getIntent().getStringExtra("workTimeDetail");
        workTodayDate = getIntent().getStringExtra("workDateDetail");
        workTravel = getIntent().getStringExtra("workTravelDetail");
        workSpinner = getIntent().getStringExtra("workName");

        rootRef = FirebaseDatabase.getInstance().getReference().child("Work").child(workSpinner);

        customerList.add(0, workSpinner);
        workDetailJobTxt = findViewById(R.id.work_jobs_details_txt);
        workDetailHardwareTxt = findViewById(R.id.work_hardware_detail_list_txt);
        workDetailHourTxt = findViewById(R.id.work_hour_detail_txt);
        workTodayTimeTV = findViewById(R.id.work_detail_date_text);
        travelRadioBox = findViewById(R.id.radio_group);
        workerCompanyName = findViewById(R.id.work_detail_customer_spinner);
        workUpdateBtn = findViewById(R.id.customer_work_detail_submit_btn);
        workDetailBackIcon = findViewById(R.id.work_detail_back_icon);
        workDetailDeleteIcon = findViewById(R.id.work_detail_delete_btn);
        yesRadioButton = findViewById(R.id.radio_yes_label);
        noRadioButton = findViewById(R.id.radio_no_label);
        loadingDialog = new ProgressDialog(this);

        workDetailJobTxt.getEditText().setText(workJobDetail);
        workDetailHardwareTxt.getEditText().setText(workDetailHardware);
        workDetailHourTxt.getEditText().setText(workDetailHour);
        workTodayTimeTV.setText(workTodayDate);
        workerCompanyName.setText(workSpinner);

        if(workTravel.equals("Yes")){
            yesRadioButton.setChecked(true);
        }else{
            noRadioButton.setChecked(true);
        }

        workDetailBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkDetailActivity.super.onBackPressed();
            }
        });

        workUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence options[] = new CharSequence[]{
                        "Yes",
                        "No"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(WorkDetailActivity.this);
                builder.setTitle("Are you sure you want to Update this Record.?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            checkInternetConnection();
                        } else {
                            finish();
                        }
                    }
                });
                builder.show();
            }
        });

        workDetailDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{
                        "Yes",
                        "No"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(WorkDetailActivity.this);
                builder.setTitle("Are you sure you want to Delete this Work Record.?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            checkInternet();
                        } else {
                            finish();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    private void checkInternetConnection() {
        if (!isConnected(this)) {
            showCustomDialog();
        } else {
            validateUpdatedData();
        }
    }

    private void checkInternet() {
        if (!isConnected(this)) {
            showCustomDialog();
        } else {
            deleteClientData();
        }
    }

    private void deleteClientData() {
        rootRef.child(workTodayDate).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                WorkDetailActivity.super.onBackPressed();
                Toast.makeText(getApplicationContext(), "Work Record Deleted", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isConnected(WorkDetailActivity workDetail) {
        ConnectivityManager connectivityManager = (ConnectivityManager) workDetail.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }

    }

    private void showCustomDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(WorkDetailActivity.this);
        builder.setMessage("Please connect to the internet to proceed further").setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        WorkDetailActivity.super.onBackPressed();
                    }
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void validateUpdatedData() {

        String job, hardware, workTime, travel;

        int selectedId = travelRadioBox.getCheckedRadioButtonId();
        radioButton =  findViewById(selectedId);

        job = workDetailJobTxt.getEditText().getText().toString().trim();
        hardware = workDetailHardwareTxt.getEditText().getText().toString().trim();
        workTime = workDetailHourTxt.getEditText().getText().toString().trim();
        travel = radioButton.getText().toString();

        if(TextUtils.isEmpty(job)){
            Toast.makeText(getApplicationContext(), "Please Enter the Job Name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(hardware)){
            Toast.makeText(getApplicationContext(), "Please Enter the Hardware Detail", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(workTime)){
            Toast.makeText(getApplicationContext(), "Please Enter the Work Time", Toast.LENGTH_SHORT).show();
        }else{
            updateDataToFireBase(job, hardware, workTime, travel);
        }
    }

    private void updateDataToFireBase(String job, String hardware, String workTime, String travel) {

        loadingDialog.setTitle("Apply Changing");
        loadingDialog.setMessage("Please wait while we update");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

        HashMap<String, Object> workMap = new HashMap<>();
        workMap.put("customerName", workerCompanyName.getText().toString());
        workMap.put("customerJobDetail", job);
        workMap.put("customerHardwareDetail", hardware);
        workMap.put("customerWorkDateDetail", workTodayTimeTV.getText().toString());
        workMap.put("customerWorkTimeDetail", workTime);
        workMap.put("customerTravelDetail", travel);
        rootRef.child(workTodayDate).updateChildren(workMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    loadingDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Record Updated Successfully", Toast.LENGTH_SHORT).show();
                    WorkDetailActivity.super.onBackPressed();
                    finish();
                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Make Sure you have an active INTERNET. OR Please Try Again ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        WorkDetailActivity.super.onBackPressed();
    }
}