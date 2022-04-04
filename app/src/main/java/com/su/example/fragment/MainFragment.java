package com.su.example.fragment;

import android.os.Bundle;
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
import com.su.example.data.MainFragmentDataManager1;
import com.su.example.data.MainFragmentDataManager2;
import com.su.example.dialog.DialogHelper;
import com.su.example.model.list.ListSolidItem;
import com.su.solid._abstract.SolidBaseView;
import com.su.solid.annotation.SolidView;
import com.su.solid.callback.SolidCallback;
import com.su.solid.solid.Solid;
import com.su.solid.thread_type.ThreadType;

import java.util.List;

public class MainFragment extends Fragment implements SolidBaseView {

    private RecyclerView recycler;
    private ListAdapter adapter;
    private int clickPosition = -1;

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView = null;
        try {

            Solid.getInstance().register(solidId(), this,
                    new MainFragmentDataManager1(), new MainFragmentDataManager2());//可以进行多对多绑定
            Solid.getInstance().call(solidId(), Config.BIND_ID_LIST, Solid.CallType.CALL_TYPE_DATA_TO_VIEW);

            contentView = inflater.inflate(R.layout.fragment_main, container, false);
            recycler = contentView.findViewById(R.id.recycler);
            recycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            adapter = new ListAdapter((List<ListSolidItem>) Solid.getInstance()
                    .queryProviderData(solidId(), Config.PROVIDER_ID_LIST));
            adapter.setOnItemSelectListener(position -> {
                DialogHelper.showSimpleDialog(getContext(), "提示", "是否删除item",
                        (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            clickPosition = position;
                            Solid.getInstance().call(solidId(), Config.BIND_ID_CLICK,
                                    Solid.CallType.CALL_TYPE_VIEW_TO_DATA);
                        });
            });
            recycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentView;
    }

    @SolidView(bindId = Config.BIND_ID_LIST,threadType = ThreadType.MAIN)
    void onGetListItems(Object data, String msg) {
        adapter.notifyDataSetChanged();
    }

    @SolidView(bindId = Config.BIND_ID_CLICK)
    void itemSelect(SolidCallback callback) {
        callback.onDataGet(clickPosition);
    }

    @SolidView(bindId = Config.BIND_ID_REFRESH,threadType = ThreadType.MAIN)
    void onRefreshList(Object data,String msg){
        adapter.notifyDataSetChanged();
    }

    @Override
    public int solidId() {
        return Config.SOLID_ID_FRAGMENT_MAIN;
    }
}
