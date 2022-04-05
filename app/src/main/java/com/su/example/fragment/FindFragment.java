package com.su.example.fragment;

import android.app.slice.Slice;
import android.os.Bundle;
import android.util.Log;
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
import com.su.example.data.FindFragmentDataManager;
import com.su.example.dialog.DialogHelper;
import com.su.example.model.list.ListSolidItem;
import com.su.example.model.list.ListSolidItem1;
import com.su.example.service.ListItemObserver;
import com.su.example.service.ListItemRemoveObservable;
import com.su.solid._abstract.SolidBaseView;
import com.su.solid.solid.Solid;

import java.util.ArrayList;
import java.util.List;

public class FindFragment extends Fragment implements SolidBaseView {

    private RecyclerView recycler;
    private ListAdapter adapter;

    private final List<ListSolidItem> items=new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView=null;
        try {
            contentView = inflater.inflate(R.layout.fragment_find, container,false);
            recycler=contentView.findViewById(R.id.recycler);
            adapter=new ListAdapter(items);

            //使用数据监听情况下可以不需要register
            Solid.getInstance().addDataManager(new FindFragmentDataManager());
            //设置observer的消费者
            final ListItemObserver listItemObserver=new ListItemObserver();
            listItemObserver.setConsumer(this);
            Solid.getInstance().addObserver(Config.SERVICE_ID_LIST,listItemObserver);

            adapter.setOnItemSelectListener(position -> {
                DialogHelper.showSimpleDialog(getContext(), "提示", "是否删除item",
                        (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            items.remove(position);
                            adapter.notifyDataSetChanged();
                            //创建删除的observable，并且消费
                            try {
                                final ListItemRemoveObservable observable = new ListItemRemoveObservable();
                                observable.setDataSource(position);
                                Solid.getInstance().service(Config.SERVICE_ID_REMOVE, observable);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        });
            });
            recycler.setAdapter(adapter);
            recycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        }catch (Exception e){
            e.printStackTrace();
        }
        return contentView;
    }

    public void refreshList(List<ListSolidItem> items){
        this.items.clear();
        this.items.addAll(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public int solidId() {
        return Config.SOLID_ID_FRAGMENT_FIND;
    }
}
