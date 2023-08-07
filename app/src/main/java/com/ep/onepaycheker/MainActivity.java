package com.ep.onepaycheker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import lk.onepay.android.ipg.model.Configurations;
import lk.onepay.android.ipg.model.Customer;
import lk.onepay.android.ipg.model.OnepayIPGError;
import lk.onepay.android.ipg.model.OnepayIPGInit;
import lk.onepay.android.ipg.model.OnepayIPGSuccess;
import lk.onepay.android.ipg.model.Product;
import lk.onepay.android.ipg.ui.OnepayIPGActivity;
import lk.onepay.android.ipg.utilities.OnepayIPG;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OnepayIPGActivity.class);

                // Set customer information (Replace placeholders with actual data)
                Customer customer = new Customer(
                        "Akalanka",
                        "Kaushalya",
                        "0779805664",
                        "kaushalyaakalanka343@gmail.com"
                );

                // Set payment configurations (Replace placeholders with actual data)
                Configurations configurations = new Configurations(
                        "f558a2b6c839f73a3eefa1d94d7a4ff27f8331ea0d8efc85529a9a7a337a641fdd87273b3ba4294d.06ZI118C241165368821D",
                        "7LSM118C24116536881B1",
                        "UOWQ118C24116536881FF"
                );

                // Set product details (Replace placeholders with actual data)
                Product product = new Product(100.0F,  OnepayIPGInit.CurrencyTypes.LKR,"xdfrtfrrdse",
                        new ArrayList<>(Collections.singletonList(
                                new Product.TransactionItem("abc", "001", 1, 2.00f)
                        ))
                );

                // Build OnepayIPGInit object
                OnepayIPGInit onepayIPGInit = new OnepayIPGInit.Builder()
                        .setUser(customer)
                        .setConfigurations(configurations)
                        .setProduct(product)
                        .build();

                // Put the OnepayIPGInit object into intent extras
                intent.putExtra(OnepayIPG.IPG_DATA, onepayIPGInit);

                // Start the OnepayIPGActivity and handle the result in getIpgContent
                getIpgContent.launch(intent);
            }
        });
    }


    private final ActivityResultLauncher<Intent> getIpgContent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bundle data = result.getData() != null ? result.getData().getExtras() : null;
                        if (data != null) {
                            IPGStatus status = (IPGStatus) data.getSerializable(OnepayIPG.IPG_RESULT);
                            if (status == IPGStatus.SUCCESS) {
                                OnepayIPGSuccess resultData = (OnepayIPGSuccess) data.getSerializable(OnepayIPG.IPG_RESULT_DATA);
                                if (resultData != null) {
                                    Toast.makeText(MainActivity.this, resultData.getDescription(), Toast.LENGTH_LONG).show();
                                }
                            } else if (status == IPGStatus.FAILED) {
                                OnepayIPGError resultData = (OnepayIPGError) data.getSerializable(OnepayIPG.IPG_RESULT_DATA);
                                if (resultData != null) {
                                    Toast.makeText(MainActivity.this, resultData.getDescription(), Toast.LENGTH_LONG).show();
                                }
                            }
                            // Handle other cases if needed
                        }
                    }
                }
            }
    );

    public enum IPGStatus {
        SUCCESS,
        FAILED,
        PENDING,
        CANCELLED
    }
}