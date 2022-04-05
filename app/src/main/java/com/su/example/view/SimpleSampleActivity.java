package com.su.example.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.su.example.R;
import com.su.example.data.SimpleDataManager;
import com.su.example.model.simple.SimpleTextSolidData;
import com.su.solid._abstract.SolidBaseView;
import com.su.solid.annotation.SolidView;
import com.su.solid.solid.Solid;

import java.util.List;

public class SimpleSampleActivity extends AppCompatActivity implements SolidBaseView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //1.注册view和data，可以分开注册，也可以一起注册
        Solid.getInstance()
                .addDataManager(new SimpleDataManager())
                .register(this);
        //2.调用绑定方法，便可以将数据和试图绑定，将数据和试图解耦
        Solid.getInstance().call(solidId(),1, Solid.CallType.CALL_TYPE_DATA_TO_VIEW);
    }

    @SuppressLint("NonConstantResourceId")
    @SolidView(bindId = 1)
    void showSimpleText(List<SimpleTextSolidData> data, String msg){
        ((TextView)findViewById(R.id.text)).setText(data.get(0).getText());
    }

    //需要实现返回分离id，用来区分试图和数据组，
    // 比如这里的MainActivity和SimpleData共用一个solidId，
    // 说明他们之间对应数据和试图
    @Override
    public int solidId() {
        return 0;
    }

    //在试图销毁的时候，根据分离id销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Solid.getInstance().unRegister(solidId());
    }
}