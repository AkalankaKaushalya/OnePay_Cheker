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
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecureRandom;
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

    TextView tv;

    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static String generateRandomString() {
        SecureRandom random = new SecureRandom();
        int length = random.nextInt(11) + 10;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            sb.append(ALLOWED_CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tv = findViewById(R.id.tv);

        String randomString = generateRandomString();

        double doubleValue = 100.00;
        float floatValue = (float) doubleValue;


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
                        "d6ab21065d748866d05aae2cb35bb0e4e118fce830c6db3b7b13eb8454609ae9e328fd8ca4b3a7b2.ZCLG118C243ED0A26BD1B",
                        "FCVW118C243ED0A26BCD3",
                        "72Z6118C243ED0A26BD04"
                );

                // Set product details (Replace placeholders with actual data)
                Product product = new Product(floatValue,  OnepayIPGInit.CurrencyTypes.LKR,randomString,
                        new ArrayList<>(Collections.singletonList(
                                new Product.TransactionItem("abc", "001", 1, floatValue)
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
                        assert result.getData() != null;
                        //Toast.makeText(MainActivity.this, ""+data, Toast.LENGTH_SHORT).show();
                        if (data != null) {
                            String status = (String) data.getSerializable(OnepayIPG.IPG_RESULT);
                            Toast.makeText(MainActivity.this, ""+data.getSerializable(OnepayIPG.IPG_RESULT), Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, ""+data.getSerializable(OnepayIPG.IPG_DATA), Toast.LENGTH_SHORT).show();
                            tv.setText(OnepayIPG.IPG_DATA);
                            data.getSerializable(OnepayIPG.IPG_RESULT);
                            if (status == "SUCCESS") {
                                OnepayIPGSuccess resultData = (OnepayIPGSuccess) data.getSerializable(OnepayIPG.IPG_RESULT_DATA);
                                if (resultData != null) {
                                    Toast.makeText(MainActivity.this, resultData.getDescription(), Toast.LENGTH_LONG).show();
                                }
                            } else if (status == "FAILED") {
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




}