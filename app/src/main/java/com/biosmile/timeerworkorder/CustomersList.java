package com.biosmile.timeerworkorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.biosmile.timeerworkorder.Login.LoginActivity;
import com.biosmile.timeerworkorder.Model.Users;
import com.biosmile.timeerworkorder.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

public class CustomersList extends AppCompatActivity {

    Spinner customerNamesSpinner;
    DatabaseReference customerSpinnerRef;

    ArrayList<String> customerList = new ArrayList<>();
    TextView todayDateTimePicker, workingHours;
    String currentDate, currentTime, date_time, dateTimerFormat;
    
    Button submitButton;
    ImageView backIcon;
    TextInputLayout customerJobDetailTxt, customerHardwareDetailTxt, workHourTxt;
    RadioGroup travelRadioBox;
    RadioButton  radioButton;
    String customerJobDetail, customerHardwareDetail, workDate, workTime, customerName, travel;
    ProgressDialog loadingDialog;

    DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_list);

        customerSpinnerRef = FirebaseDatabase.getInstance().getReference().child("Customers");
        rootRef = FirebaseDatabase.getInstance().getReference();

        todayDateTimePicker = findViewById(R.id.today_date_text);
        workHourTxt = findViewById(R.id.work_hour_txt);
        submitButton = findViewById(R.id.customer_detail_submit_btn);
        backIcon = findViewById(R.id.back_icon);
        
        customerJobDetailTxt = findViewById(R.id.jobs_details_txt);
        customerHardwareDetailTxt = findViewById(R.id.hardware_list_txt);
        travelRadioBox = findViewById(R.id.radio_group);

        customerHardwareDetailTxt.getEditText().setText("None");
//        workingHours.getEd.setText("00:00");

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat getDate = new SimpleDateFormat("MM dd, yyyy");
        currentDate = getDate.format(calendar.getTime());

        SimpleDateFormat getTIme = new SimpleDateFormat("HH:mm:ss a");
        currentTime = getTIme.format(calendar.getTime());

        loadingDialog = new ProgressDialog(this);

        SimpleDateFormat getDateFormat = new SimpleDateFormat("MM-dd-yyyy, HH:mm:ss a");
        dateTimerFormat = getDateFormat.format(calendar.getTime());

        date_time = dateTimerFormat;
        todayDateTimePicker.setText(date_time);

        todayDateTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(todayDateTimePicker);
            }
        });

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomersList.super.onBackPressed();
            }
        });
        
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkInternetConnection();
//                validateFormData();
            }
        });

        customerNamesSpinner = findViewById(R.id.choose_customer_spinner);
        showSpinnerData();

    }

    private void checkInternetConnection() {
        if (!isConnected(this)) {
            showCustomDialog();
        } else {
            validateFormData();
        }
    }


    private boolean isConnected(CustomersList customersList) {
        ConnectivityManager connectivityManager = (ConnectivityManager) customersList.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }

    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomersList.this);
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
                        CustomersList.super.onBackPressed();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void validateFormData() {

        int selectedId = travelRadioBox.getCheckedRadioButtonId();
        radioButton =  findViewById(selectedId);
        
        customerJobDetail = customerJobDetailTxt.getEditText().getText().toString().trim();
        customerHardwareDetail = customerHardwareDetailTxt.getEditText().getText().toString();
        workDate = todayDateTimePicker.getText().toString();
        customerName = customerNamesSpinner.getSelectedItem().toString();
        travel = radioButton.getText().toString();
        workTime = workHourTxt.getEditText().getText().toString();


        if(TextUtils.isEmpty(customerJobDetail)){
            Toast.makeText(getApplicationContext(), "Job Details is Empty", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(customerHardwareDetail)){
            Toast.makeText(getApplicationContext(), "Hardware Details is Empty", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(workTime)){
            Toast.makeText(getApplicationContext(), "Work Time is Empty", Toast.LENGTH_SHORT).show();
        } else{

            saveDataToDataBase(customerJobDetail, customerHardwareDetail, workDate, workTime, customerName, travel);
        }
        
    }

    private void saveDataToDataBase(String customerJobDetail, String customerHardwareDetail, String workDate, String workTime, String customerName, String travel) {
        loadingDialog.setTitle("Please Wait...");
        loadingDialog.setMessage("While we are uploading Record....");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

        HashMap<String, Object> workMap = new HashMap<>();
        workMap.put("customerName", customerName);
        workMap.put("customerJobDetail", customerJobDetail);
        workMap.put("customerHardwareDetail", customerHardwareDetail);
        workMap.put("customerWorkDateDetail", workDate);
        workMap.put("customerWorkTimeDetail", workTime);
        workMap.put("customerTravelDetail", travel);
        workMap.put("time", ServerValue.TIMESTAMP);

//        rootRef.child("Work").child(workDate).updateChildren(workMap).addOnCompleteListener(new OnCompleteListener<Void>() {
        rootRef.child("Work").child(customerName).child(workDate).updateChildren(workMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingDialog.dismiss();
                CustomersList.super.onBackPressed();
                finish();

//                HashMap<String, Object> workMapTwo = new HashMap<>();
//                workMapTwo.put("customerWorkDateDetailTwo", workDate);
//                rootRef.child("Work").child(workDate).child("WorkTime").updateChildren(workMapTwo).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        loadingDialog.dismiss();
//                        CustomersList.super.onBackPressed();
//                        finish();
//                    }
//                });

            }
        });

    }


    private void showDateTimeDialog(final TextView todayDateTimePicker) {

        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM-dd-yyyy, HH:mm:ss a");

                        todayDateTimePicker.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(CustomersList.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(CustomersList.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

//        final Calendar calendar=Calendar.getInstance();
//        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                calendar.set(Calendar.YEAR,year);
//                calendar.set(Calendar.MONTH,month);
//                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
//                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MMM-dd-yyyy");
//                todayDateTimePicker.setText(simpleDateFormat.format(calendar.getTime()));
//
//            }
//        };
//
//        new DatePickerDialog(CustomersList.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();


    }

    private void showSpinnerData() {

        customerSpinnerRef.orderByChild("companyName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                customerList.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    customerList.add(items.child("companyName").getValue(String.class));
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, customerList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                customerNamesSpinner.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        CustomersList.super.onBackPressed();
    }
}