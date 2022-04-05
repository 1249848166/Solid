package com.su.example.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.su.example.R;
import com.su.example.adapter.ListAdapter;
import com.su.example.config.Config;
import com.su.example.data.ListDataManager;
import com.su.example.dialog.DialogHelper;
import com.su.example.model.list.ListSolidItem;
import com.su.solid._abstract.SolidBaseView;
import com.su.solid.annotation.SolidView;
import com.su.solid.callback.SolidCallback;
import com.su.solid.solid.Solid;

import java.util.List;

public class ListSampleActivity extends AppCompatActivity implements SolidBaseView {

    private RecyclerView list;
    private ListAdapter adapter;
    private int clickItemPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sample);
  
        Solid.getInstance()
                .addDataManager(new ListDataManager())
                .register(this);
        Solid.getInstance().call(solidId(),Config.BIND_ID_LIST,
                Solid.CallType.CALL_TYPE_DATA_TO_VIEW);//根据绑定id手动选择显示哪个数据试图
    }

    private void dialog1(int position) {
        DialogHelper.showSimpleDialog(this, "提示", "是否删除item",
                (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    clickItemPosition = position;
                    Solid.getInstance().call(solidId(),Config.BIND_ID_CLICK,
                            Solid.CallType.CALL_TYPE_VIEW_TO_DATA);//view向data传递数据
                    adapter.notifyDataSetChanged();
                });
    }

    @SuppressWarnings("unchecked")
    @SolidView(bindId = Config.BIND_ID_LIST)
    void showList(Object data, String msg) {
        runOnUiThread(() -> {
            adapter = new ListAdapter((List<ListSolidItem>) Solid.getInstance()
                    .queryProviderData(solidId(), Config.PROVIDER_ID_LIST));
            list = findViewById(R.id.list1);
            list.setAdapter(adapter);
            list.setLayoutManager(new LinearLayoutManager(ListSampleActivity.this,
                    RecyclerView.VERTICAL, false));
            adapter.setOnItemSelectListener(this::dialog1);
            adapter.notifyDataSetChanged();
        });
    }

    @SolidView(bindId = Config.BIND_ID_CLICK)
    void onItemClick(SolidCallback callback) {
        callback.onDataGet(clickItemPosition);
    }

    @SolidView(bindId = Config.BIND_ID_REFRESH)
    void refreshList(Object data, String msg) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public int solidId() {
        return Config.SOLID_ID_LIST;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Solid.getInstance().unRegister(solidId());
    }
}