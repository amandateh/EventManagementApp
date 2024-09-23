package com.fit2081.assignment_2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.assignment_2.entities.Event;

import java.util.ArrayList;
import java.util.List;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> {

    List<Event> data;

    public void setDataEvent(List<Event> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_event, parent, false); //CardView inflated as RecyclerView list item


        ViewHolder viewHolder = new ViewHolder(v);
        Log.d("Assignment-AK", "onCreateViewHolder");
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewId.setText(data.get(position).getEventId());
        holder.textViewTickets.setText(String.valueOf(data.get(position).getTicketsAvailable()));
        holder.textViewName.setText(String.valueOf(data.get(position).getName()));
        holder.textViewCategoryId.setText(data.get(position).getCategoryId());
        holder.isActive.setText(data.get(position).isActive() ? "Active" : "InActive");

        holder.cardView.setOnClickListener(v -> {
            String eventNameString = data.get(position).getName();
            Context context = holder.cardView.getContext();
            Intent intent = new Intent(context, EventGoogleResultActivity.class);
            intent.putExtra("Event", eventNameString);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        if (this.data != null) { // if data is not null
            return this.data.size(); // then return the size of ArrayList
        }
        // else return zero if data is null
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewId;
        public TextView textViewCategoryId;
        public TextView textViewTickets;
        public TextView textViewName;
        public TextView isActive;
        public View cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView;
            textViewId = itemView.findViewById(R.id.card_event_id);
            textViewCategoryId = itemView.findViewById(R.id.card_category_id);
            textViewTickets = itemView.findViewById(R.id.card_event_tickets);
            textViewName = itemView.findViewById(R.id.card_event_name);
            isActive = itemView.findViewById(R.id.card_is_active);
        }
    }
}
