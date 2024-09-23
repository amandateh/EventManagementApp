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

import com.fit2081.assignment_2.entities.EventCategory;

import java.util.ArrayList;
import java.util.List;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder> {

    List<EventCategory> data;

    private final int HEADER_TYPE = 0;
    private final int ITEM_TYPE = 1;

    public void setData(List<EventCategory> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType==HEADER_TYPE){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_header, parent, false); //CardView inflated as RecyclerView list item
        }
        else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false); //CardView inflated as RecyclerView list item

        }
        ViewHolder viewHolder = new ViewHolder(v);
//        Log.d("Assignment-AK", "onCreateViewHolder");
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position==0){
            holder.textViewCategoryId.setText("Id");
            holder.textViewName.setText("Name");
            holder.textViewCount.setText("Event Count");
        }else {
            holder.textViewCount.setText(String.valueOf(data.get(position-1).getEventCount()));
            holder.textViewName.setText(String.valueOf(data.get(position-1).getName()));
            holder.textViewCategoryId.setText(data.get(position-1).getCategoryId());
            holder.isActive.setText(data.get(position-1).isActive() ? "Yes" : "No");
        }
        holder.cardView.setOnClickListener(v -> {
            String locationString = data.get(position-1).getLocation();
            Context context = holder.cardView.getContext();
            Intent intent = new Intent(context, GoogleMapActivity.class);
            intent.putExtra("location", locationString);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        if (this.data != null) { // if data is not null
            return this.data.size()+1; // then return the size of ArrayList
        }
        // else return zero if data is null
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER_TYPE;
        else
            return ITEM_TYPE;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewCategoryId;
        public TextView textViewCount;
        public TextView textViewName;
        public TextView isActive;
        public View cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView;
            textViewCategoryId = itemView.findViewById(R.id.card_category_id);
            textViewCount = itemView.findViewById(R.id.card_event_count);
            textViewName = itemView.findViewById(R.id.card_name);
            isActive = itemView.findViewById(R.id.card_is_active);
        }
    }
}
