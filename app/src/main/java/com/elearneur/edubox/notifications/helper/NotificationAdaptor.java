package com.elearneur.edubox.notifications.helper;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elearneur.edubox.R;
import com.elearneur.edubox.calendar.CalEvent;
import com.elearneur.edubox.calendar.PCalendar;
import com.elearneur.edubox.notifications.NotificationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class NotificationAdaptor extends RecyclerView.Adapter<NotificationAdaptor.ViewHolder>{
    private ArrayList<CalEvent> events;
    private static  PCalendar calendar;
    private Context context;

    Calendar currentCalendar = Calendar.getInstance();
    int month = currentCalendar.get(Calendar.MONTH) +1;
    int day = currentCalendar.get(Calendar.DAY_OF_MONTH);
    String current = Integer.toString(currentCalendar.get(Calendar.YEAR)) +'-'+month+'-'+day;

    public NotificationAdaptor(Context context, PCalendar cal, ArrayList<CalEvent> events){
        this.events = events;
        this.calendar = cal;
        this.context = context;
    }



    public class ViewHolder extends  RecyclerView.ViewHolder{

        ImageView readBlock;
        TextView eventTitle, eventDate, eventInfo, eventTime;
        int position;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            readBlock = itemView.findViewById(R.id.readBlock);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventDate = itemView.findViewById(R.id.eventContent);
            eventInfo = itemView.findViewById(R.id.eventInfo);
            eventTime = itemView.findViewById(R.id.eventTime);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    readBlock.setVisibility(View.INVISIBLE);
                    events.get(position).setIsRead(true);

                    try {
                        calendar.saveCalendar(context);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(itemView.getContext(), NotificationView.class);
                    intent.putExtra("eventTitle",eventTitle.getText());
                    intent.putExtra("eventContent",events.get(position).getContentType());
                    intent.putExtra("eventInfo",eventInfo.getText());
                    intent.putExtra("eventTime",eventTime.getText());
                    intent.putExtra("eventDate",eventDate.getText());
                    itemView.getContext().startActivity(intent);
                }
            });


        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(events.get(position));
        holder.eventTitle.setText(events.get(position).getTitle());
        holder.eventDate.setText(events.get(position).getDate());
        holder.eventTime.setText(events.get(position).getTime());
        holder.eventInfo.setText(events.get(position).getNote());

        holder.readBlock.setVisibility(events.get(position).getIsRead() ? View.INVISIBLE: View.VISIBLE);
        holder.position = position;

        if(events.get(position).getDate().compareTo(current) < 0){
            holder.readBlock.setVisibility(View.INVISIBLE);
            holder.eventTitle.setPaintFlags(holder.eventTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.eventDate.setPaintFlags(holder.eventDate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.eventTime.setPaintFlags(holder.eventTime.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.eventInfo.setPaintFlags(holder.eventInfo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }



    }

    @Override
    public int getItemCount() {
        return events.size();
    }


}
