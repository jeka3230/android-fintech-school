package ru.tinkoff.cake.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.tinkoff.cake.R;

public class LicenceIsOverActivity extends AppCompatActivity {
    public static void start(Context context) {
        Intent intent = new Intent(context, LicenceIsOverActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licence);
    }
}
