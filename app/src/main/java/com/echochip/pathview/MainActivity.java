package com.echochip.pathview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.echochip.pathview.view.PathView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_start, btn_fail, btn_success;
    PathView pathView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_fail = (Button) findViewById(R.id.btn_fail);
        btn_success = (Button) findViewById(R.id.btn_success);
        pathView = (PathView) findViewById(R.id.pathView);

        btn_start.setOnClickListener(this);
        btn_fail.setOnClickListener(this);
        btn_success.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                pathView.setState(PathView.State.Process);
                break;
            case R.id.btn_fail:
                pathView.setState(PathView.State.Fail);
                break;
            case R.id.btn_success:
                pathView.setState(PathView.State.Success);
                break;
        }
    }
}
