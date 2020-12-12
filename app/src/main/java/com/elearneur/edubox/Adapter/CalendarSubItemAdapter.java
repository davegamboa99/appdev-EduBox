package com.elearneur.edubox.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elearneur.edubox.calendar.Event;
import com.elearneur.edubox.R;

import java.util.List;

public class CalendarSubItemAdapter extends RecyclerView.Adapter<CalendarSubItemAdapter.ViewHolder> {
    private List<Event> events;
    private Context context;

    public CalendarSubItemAdapter(Context context, List<Event> list){
        events = list;
        this.context = context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView bullet;
        TextView eventContentCalendar;
        TextView eventTitleCalendar;
        Dialog eventInfo = new Dialog(context);

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bullet = itemView.findViewById(R.id.bullet);
            eventContentCalendar = itemView.findViewById(R.id.eventContentCalendar);
            eventTitleCalendar = itemView.findViewById(R.id.eventTitleCalendar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventInfo.setContentView(R.layout.event_edit);
                    eventInfo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    eventInfo.show();
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_item, parent, false);
        return new CalendarSubItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.eventContentCalendar.setText(events.get(position).getContentType());
        holder.eventTitleCalendar.setText(events.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

}
