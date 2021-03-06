package com.su.example.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.su.example.R;
import com.su.example.adapter.ListAdapter;
import com.su.example.config.Config;
import com.su.example.dialog.DialogHelper;
import com.su.example.model.list.ListSolidItem;
import com.su.example.model.list.ListSolidItem1;
import com.su.solid.solid.Solid;

import java.util.ArrayList;
import java.util.List;

public class MeFragment extends Fragment {

    private RecyclerView recycler;
    private ListAdapter adapter;

    private final List<ListSolidItem> items=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView=null;
        try {
            contentView = inflater.inflate(R.layout.fragment_me, container,false);
            recycler=contentView.findViewById(R.id.recycler);
            items.clear();
            adapter=new ListAdapter(items);
            adapter.setOnItemSelectListener(position -> {
                DialogHelper.showSimpleDialog(getContext(), "提示", "是否删除item",
                        (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            items.remove(position);
                            adapter.notifyDataSetChanged();
                        });
            });
            recycler.setAdapter(adapter);
            recycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
            getData(() -> requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return contentView;
    }

    private void getData(OnDataCallback callback){
        items.clear();
        new Thread(() -> {
//            SystemClock.sleep(1000);
            items.add(new ListSolidItem1("测试","这个页面显示不使用solid，会怎么用"));
            items.add(new ListSolidItem1("测试","模拟从网络获取数据"));
            callback.onGet();
        }).start();
    }

    private interface OnDataCallback{
        void onGet();
    }

}
