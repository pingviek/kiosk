package com.example.emptytest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button b_enable, b_lock;

    static final int RESULT_ENABLE = 1;
    DevicePolicyManager devicePolicyManager;
    ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b_enable = (Button)findViewById(R.id.b_enable);
        b_lock = (Button)findViewById(R.id.b_lock);

        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(MainActivity.this, Controller.class);

        boolean active = devicePolicyManager.isAdminActive(componentName);


        if (active) {
            b_enable.setText("DISABLE");
            b_lock.setVisibility((View.VISIBLE));
        } else {
            b_enable.setText("ENABLE");
            b_lock.setVisibility((View.GONE));
        }

        b_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean active = devicePolicyManager.isAdminActive(componentName);
                if (active) {
                    devicePolicyManager.removeActiveAdmin(componentName);
                    b_enable.setText("ENABLE");
                    b_lock.setVisibility((View.GONE));
                } else {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable the app!");
                    startActivityForResult(intent, RESULT_ENABLE);
                }
            }
        });

        b_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                devicePolicyManager.lockNow();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    b_enable.setText("DISABLE");
                    b_lock.setVisibility((View.VISIBLE));
                } else {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
