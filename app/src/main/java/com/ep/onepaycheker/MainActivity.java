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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);

        String randomString = generateRandomString();

        double doubleValue = 100.00;
        float floatValue = (float) doubleValue;
        Toast.makeText(this, ""+floatValue, Toast.LENGTH_SHORT).show();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OnepayIPGActivity.class);

                // Set customer information (Replace placeholders with actual data)
                Customer customer = new Customer(
                        "Akalanka",
                        "Kaushalya",
                        "+94779805664",
                        "kaushalyaakalanka343@gmail.com"
                );

                // Set payment configurations (Replace placeholders with actual data)
                Configurations configurations = new Configurations(
                        "8945a60756f2ee7c4a9ea7962659a59c12d50b40dbcb889fcd76f9b16616d924fe657cd8517b8d20.OWKA118C243F01B2DB54F",
                        "RRNJ118C243F01B2DB50B",
                        "B60R118C243F01B2DB536"
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
                        tv.setText(result.getData().toString());
                        Toast.makeText(MainActivity.this, ""+data, Toast.LENGTH_SHORT).show();
                        if (data != null) {
                            //IPGStatus status = (IPGStatus) data.getSerializable(OnepayIPG.IPG_RESULT);
                            Toast.makeText(MainActivity.this, ""+data.getSerializable(OnepayIPG.IPG_RESULT), Toast.LENGTH_SHORT).show();
                            //data.getSerializable(OnepayIPG.IPG_RESULT);
//                            if (status == IPGStatus.SUCCESS) {
//                                OnepayIPGSuccess resultData = (OnepayIPGSuccess) data.getSerializable(OnepayIPG.IPG_RESULT_DATA);
//                                if (resultData != null) {
//                                    Toast.makeText(MainActivity.this, resultData.getDescription(), Toast.LENGTH_LONG).show();
//                                }
//                            } else if (status == IPGStatus.FAILED) {
//                                OnepayIPGError resultData = (OnepayIPGError) data.getSerializable(OnepayIPG.IPG_RESULT_DATA);
//                                if (resultData != null) {
//                                    Toast.makeText(MainActivity.this, resultData.getDescription(), Toast.LENGTH_LONG).show();
//                                }
//                            }
                            // Handle other cases if needed
                        }
                    }
                }
            }
    );

    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static String generateRandomString() {
        SecureRandom random = new SecureRandom();
        // Generate a random length between 10 and 20
        int length = random.nextInt(11) + 10;
        // Build the random string
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            sb.append(ALLOWED_CHARACTERS.charAt(randomIndex));
        }

        return sb.toString();
    }


}