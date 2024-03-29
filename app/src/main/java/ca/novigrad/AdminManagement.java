package ca.novigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
        only the administrator can accept or delete the other users. That is why we create a mangeAccount activity
        Also, he can create, delete, or update some services that a branch want to offer to their clients. that is why we have the manageService activity
 */

public class AdminManagement extends AppCompatActivity {
    private Button manageServices ;
    private Button manageAccounts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_management);

        manageAccounts = findViewById(R.id.buttonManageAccounnts);
        manageServices = findViewById(R.id.buttonManageServices);

        manageAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ManageAccount.class));
            }
        });
        manageServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ManageService.class));
            }
        });
    }
}