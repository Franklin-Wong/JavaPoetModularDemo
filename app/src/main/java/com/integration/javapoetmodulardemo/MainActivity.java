package com.integration.javapoetmodulardemo;

import android.os.Bundle;

import com.integration.arouter_annotation.ARouter;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 依赖注解
 */
@ARouter(path = "/app/MainActivity")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }
}
