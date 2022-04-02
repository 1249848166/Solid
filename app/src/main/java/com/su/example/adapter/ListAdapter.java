package com.su.example.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.su.example.R;
import com.su.example.model.list.ListSolidItem;
import com.su.example.model.list.ListSolidItem1;
import com.su.example.model.list.ListSolidItem2;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.VH> implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        if(onItemSelectListener!=null)
            onItemSelectListener.select((Integer) view.getTag());
    }

    public interface OnItemSelectListener{
        void select(int position);
    }

    private OnItemSelectListener onItemSelectListener;

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    private final List<ListSolidItem> listSolidItems;

    public ListAdapter(List<ListSolidItem> listSolidItems) {
        this.listSolidItems = listSolidItems;
    }

    @Override
    public int getItemViewType(int position) {
        return ((ListSolidItem)listSolidItems.get(position)).getType();
    }

    @NonNull
    @Override
    public ListAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return viewType==1?new VH(View.inflate(parent.getContext(), R.layout.item_list1,null)):
                new VH(View.inflate(parent.getContext(),R.layout.item_list2,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.VH holder, int position) {
        if(getItemViewType(position)==1){
            final ListSolidItem1 item1= (ListSolidItem1) listSolidItems.get(position);
            final TextView title=holder.itemView.findViewById(R.id.title);
            final TextView content=holder.itemView.findViewById(R.id.content);
            title.setText(item1.getTitle());
            content.setText(item1.getContent());
        }else{
            final ListSolidItem2 item2= (ListSolidItem2) listSolidItems.get(position);
            final ImageView avatar=holder.itemView.findViewById(R.id.avatar);
            final TextView name=holder.itemView.findViewById(R.id.name);
            avatar.setImageResource(item2.getAvatar());
            name.setText(item2.getName());
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return listSolidItems.size();
    }

    public static final class VH extends RecyclerView.ViewHolder{

        private final View itemView;

        public VH(@NonNull View itemView) {
            super(itemView);
            this.itemView=itemView;
        }
    }
}
