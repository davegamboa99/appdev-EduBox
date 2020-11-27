package com.elearneur.edubox;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CalendarItemAdapter extends RecyclerView.Adapter<CalendarItemAdapter.ViewHolder> {

    private List<TemporaryItem> list;
    private Context context;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public CalendarItemAdapter(Context context,List<TemporaryItem> list){
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView time;
        private RecyclerView subItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.timeItem);
            subItem = itemView.findViewById(R.id.subItem);
        }
    }

    @NonNull
    @Override
    public CalendarItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.head_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarItemAdapter.ViewHolder holder, int position) {
        holder.time.setText(list.get(position).getTime());

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.subItem.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        //layoutManager.setInitialPrefetchItemCount(list.get(position).getEventSize());

        CalendarSubItemAdapter calendarSubItemAdapter = new CalendarSubItemAdapter(context,list.get(position).getEvents());
        holder.subItem.setLayoutManager(layoutManager);
        holder.subItem.setAdapter(calendarSubItemAdapter);
        holder.subItem.setRecycledViewPool(viewPool);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
