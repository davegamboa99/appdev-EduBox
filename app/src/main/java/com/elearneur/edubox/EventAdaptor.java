package com.elearneur.edubox;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventAdaptor extends RecyclerView.Adapter<EventAdaptor.ViewHolder> {
    private ArrayList<Event> events;

    private ItemClicked activity;
    public interface  ItemClicked{
        void onItemClicked(int index);
    }

    public EventAdaptor(Context context, ArrayList<Event> list){
        events = list;
        activity = (ItemClicked) context;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        ImageView readBlock;
        TextView eventTitle;
        TextView eventContent;
        TextView eventInfo;
        TextView eventTime;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            readBlock = itemView.findViewById(R.id.readBlock);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventContent = itemView.findViewById(R.id.eventContent);
            eventInfo = itemView.findViewById(R.id.eventInfo);
            eventTime = itemView.findViewById(R.id.eventTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    readBlock.setVisibility(View.INVISIBLE);
                    events.get(position).setRead(true);
                    Intent intent = new Intent(itemView.getContext(),NotificationView.class);
                    intent.putExtra("eventTitle",eventTitle.getText());
                    intent.putExtra("eventContent",eventContent.getText());
                    intent.putExtra("eventInfo",eventInfo.getText());
                    intent.putExtra("eventTime",eventTime.getText());
                    intent.putExtra("eventDate",events.get(position).getDate());
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }

    @NonNull
    @Override
    public EventAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdaptor.ViewHolder holder, int position) {
        holder.itemView.setTag(events.get(position));
        holder.eventTitle.setText(events.get(position).getTitle());
        holder.eventContent.setText(events.get(position).getContentType());
        holder.eventTime.setText(events.get(position).getTime());
        holder.eventInfo.setText(events.get(position).getInfo());
        holder.readBlock.setVisibility(events.get(position).isRead() ? View.INVISIBLE: View.VISIBLE);
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
