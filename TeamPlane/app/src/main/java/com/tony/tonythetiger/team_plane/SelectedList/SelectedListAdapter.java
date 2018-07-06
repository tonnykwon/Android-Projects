package com.tony.tonythetiger.team_plane.SelectedList;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tony.tonythetiger.team_plane.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by tony on 2017-11-19.
 */

public class SelectedListAdapter extends RecyclerView.Adapter<SelectedListAdapter.ViewHolder>{

    private List <SelectedItem> itemsData;

    //constructor
    public SelectedListAdapter(List<SelectedItem> itemsData){
        this.itemsData=itemsData;
    }

    // recycler view 생성
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //viewType 0은 일반, 1은 지도나 그 외
        if(viewType==0){
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.selected_list_item, null);

            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }  else {
            // starView
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.selected_list_item, parent, false);

            // create ViewHolder
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }
    }


    // replace the contents of a view
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        int viewType = getItemViewType(position);
        if(viewHolder!=null){

        }
        if(viewType==0){
            // set title and content
            viewHolder.title.setText(itemsData.get(position).getTitle());
            viewHolder.content.setText(itemsData.get(position).getContent());
        } else if(viewType==1){
            // content
        }

    }


    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public TextView content;
        public ImageView logo;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            // xml 연결하기
            title =  itemLayoutView.findViewById(R.id.selectedTitle);
            content =  itemLayoutView.findViewById(R.id.selectedContent);
            logo = itemLayoutView.findViewById(R.id.selected_image);
        }

        @Override
        public void onClick(View v) {
            Log.d("main", "position -"+getPosition());
        }
    }

    // return size;
    @Override
    public int getItemCount() {
        return itemsData.size();
    }
}
