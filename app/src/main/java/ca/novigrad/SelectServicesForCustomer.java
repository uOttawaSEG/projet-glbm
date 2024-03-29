package ca.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SelectServicesForCustomer extends AppCompatActivity {
    private DatabaseReference databaseReference ;
    private ListView listViewServices;
    private ArrayList<Service> servicesInfo;
    private ArrayList<String> servicesKey;
    private ArrayList<String> services;
    private ArrayList<String> schedule;
    private ArrayAdapter<String> serviceAdapter;
    private String branchID;
    private String userID;
    private TextView mondayTime;
    private TextView tuesdayTime;
    private TextView wednesdayTime;
    private TextView thursdayTime;
    private TextView fridayTime;
    private TextView saturdayTime;
    private TextView sundayTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_services_for_customer);

        Bundle bundle = getIntent().getExtras();


        branchID = bundle.getString("branchID");
        userID = bundle.getString("userUID");


        schedule = new ArrayList<>();
        servicesInfo = new ArrayList<>();
        services = new ArrayList<>();
        servicesKey = new ArrayList<>();
        listViewServices = findViewById(R.id.ListViewServiceToSelectCustomer);

        mondayTime = findViewById(R.id.textViewMondayTime2);
        tuesdayTime = findViewById(R.id.textViewTuesdayTime2);
        wednesdayTime = findViewById(R.id.textViewWednesdayTime2);
        thursdayTime = findViewById(R.id.textViewThursdayTime2);
        fridayTime = findViewById(R.id.textViewFridayTime2);
        saturdayTime = findViewById(R.id.textViewSaturdayTime2);
        sundayTime = findViewById(R.id.textViewSundayTime2);

        // adapter for listView
        serviceAdapter = new ArrayAdapter<>(SelectServicesForCustomer.this, android.R.layout.simple_list_item_1,services);
        databaseReference = FirebaseDatabase.getInstance().getReference("Branches");

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.child(branchID).child("servicesOffered").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                services.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String serviceOffered = dataSnapshot.getValue(String.class);
                    services.add(serviceOffered);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Services");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                servicesInfo.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Service service = dataSnapshot.getValue(Service.class);
                    for(String serviceName:services){
                        if (service.getServiceName().compareTo(serviceName)==0){
                            servicesInfo.add(service);
                            servicesKey.add(dataSnapshot.getKey());
                        }
                    }

                }
                services.clear();
                for(Service service : servicesInfo){
                    services.add(service.getServiceName());
                }
                listViewServices.setAdapter(serviceAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listViewServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SelectServicesForCustomer.this, FillForm.class);
                intent.putExtra("userUID", userID);
                intent.putExtra("branchID", branchID);
                intent.putExtra("serviceSelectedKey", servicesKey.get(position));
                intent.putExtra("serviceSelectedName", servicesInfo.get(position).getServiceName());
                startActivity(intent);
            }
        });
        databaseReference.child(branchID).child("schedule").addValueEventListener(new ValueEventListener() {
            // setting the schedule display
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DaySchedule daySchedule = dataSnapshot.getValue(DaySchedule.class);
                    BranchActivity.setDaySchedule(daySchedule,schedule);

                }
                // the schedule array gathers all the schedules of each day sorted in alphabetical order
                mondayTime.setText(schedule.get(1));
                tuesdayTime.setText(schedule.get(5));
                wednesdayTime.setText(schedule.get(6));
                thursdayTime.setText(schedule.get(4));
                fridayTime.setText(schedule.get(0));
                saturdayTime.setText(schedule.get(2));
                sundayTime.setText(schedule.get(3));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}