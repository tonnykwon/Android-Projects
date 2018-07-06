package com.tony.tonythetiger.team_plane.ViewList;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tony.tonythetiger.team_plane.R;

import java.util.List;

/**
 * Created by tony on 2017-11-19.
 */

public class ViewListAdapter extends RecyclerView.Adapter<ViewListAdapter.ViewHolder>{

    private List <ViewItem> itemsData;

    //constructor
    public ViewListAdapter(List<ViewItem> itemsData){
        this.itemsData=itemsData;
    }

    // recycler view 생성
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //viewType 0은 개인, 1은 팀
        // 10은 요청 여부
        if(viewType>1){
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_list_item, parent, false);
        }
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_item, parent, false);

        return new ViewHolder(itemLayoutView);
    }


    // replace the contents of a view
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        int viewType = getItemViewType(position);
        if(viewHolder!=null){
            if(viewType==0){
                viewHolder.id.setText(itemsData.get(position).getName());
                //분야 + 세부분야
                viewHolder.field.setText(itemsData.get(position).getField());
                viewHolder.intro.setText(itemsData.get(position).getIntro());
                viewHolder.disc.setText(itemsData.get(position).getDisc_sim());
                switch(itemsData.get(position).getLogo()){
                    case 0:
                        viewHolder.logo.setImageResource(R.drawable.team_log_ex);
                        break;
                    case 1:
                        viewHolder.logo.setImageResource(R.drawable.team_logo_ex1);
                        break;
                    case 2:
                        viewHolder.logo.setImageResource(R.drawable.team_logo_ex2);
                        break;
                    case 3:
                        viewHolder.logo.setImageResource(R.drawable.team_logo_ex3);
                        break;
                    case 4:
                        viewHolder.logo.setImageResource(R.drawable.team_logo_ex4);
                        break;
                    case 5:
                        viewHolder.logo.setImageResource(R.drawable.team_logo_ex5);
                        break;
                    case 6:
                        viewHolder.logo.setImageResource(R.drawable.team_logo_ex6);
                        break;
                    case 7:
                        viewHolder.logo.setImageResource(R.drawable.team_logo_ex7);
                        break;
                    case 8:
                        viewHolder.logo.setImageResource(R.drawable.team_logo_ex8);
                        break;
                }
            } else if(viewType==1) {
            }
        }

    }


    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView id;
        public TextView field;
        public TextView intro;
        public ImageView logo;
        public TextView disc;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            id = itemLayoutView.findViewById(R.id.list_id);
            field = itemLayoutView.findViewById(R.id.list_field);
            intro = itemLayoutView.findViewById(R.id.list_intro);
            logo = itemLayoutView.findViewById(R.id.list_logo);
            disc = itemLayoutView.findViewById(R.id.list_disc);
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
