package com.tony.tonythetiger.memind.MemoryList;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tony.tonythetiger.memind.MainActivity;
import com.tony.tonythetiger.memind.R;

import java.util.List;

/**
 * Created by tony on 2015-08-28.
 */
public class MemoryListAdapter extends RecyclerView.Adapter<MemoryListAdapter.ViewHolder> {
    private List <MemoryItem> itemsData;

    public MemoryListAdapter(List <MemoryItem> itemsData) {
        this.itemsData = itemsData;
    }

    @Override
    public int getItemViewType(int position) {
        return itemsData.get(position).getType();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MemoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {

        if(viewType==0){
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.memory_list_item, null);

            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }  else if (viewType==1){
            // dateview
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.date_list_item, parent, false);

            // create ViewHolder
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        } else {
            // starView
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.star_list_item, parent, false);

            // create ViewHolder
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        int viewType = getItemViewType(position);
        if(viewHolder!=null){

        }
        if(viewType==0){
            viewHolder.question.setText(itemsData.get(position).getQuestion());
        } else if(viewType==1){
            viewHolder.date.setText(itemsData.get(position).getDate());
        } else {

            viewHolder.star.removeAllViews();

            //staritem view
            float starCount = itemsData.get(position).getStar();
            for(int i=0; i<starCount; i++){
                ImageView starView = new ImageView(itemsData.get(position).getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10, 10, 0, 0);
                starView.setBackgroundResource(R.drawable.star);
                viewHolder.star.addView(starView, layoutParams);

                if(starCount-i==0.5){
                    ImageView halfStar = new ImageView(itemsData.get(position).getContext());
                    LinearLayout.LayoutParams halfParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(10, 10, 0, 0);
                    starView.setBackgroundResource(R.drawable.star_half);
                    viewHolder.star.addView(halfStar, halfParams);
                }
            }
        }


    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView question;
        public TextView exprience;
        public TextView date;
        public LinearLayout star;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            question = (TextView) itemLayoutView.findViewById(R.id.question);
            date = (TextView) itemLayoutView.findViewById(R.id.date);
            star = (LinearLayout) itemLayoutView.findViewById(R.id.starlayout);
        }

        @Override
        public void onClick(View v) {
            Log.d("main", "position -"+getPosition());
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.size();
    }
}

