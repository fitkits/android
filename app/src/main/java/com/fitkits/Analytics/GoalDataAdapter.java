package com.fitkits.Analytics;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fitkits.R;
import java.util.ArrayList;
import java.util.List;


public class GoalDataAdapter extends RecyclerView.Adapter<GoalDataAdapter.ShowViewHolder>{
    List<GoalData> dataList =new ArrayList<GoalData>();
    Context context;
    Typeface GB,GM,GL;
    String type;
    int pos;


    public GoalDataAdapter(Context context, List<GoalData> dataList,int pos,String type) {
        this.dataList = dataList;
        this.context=context;
        this.pos=pos;
        this.type=type;



    }


    @Override
    public GoalDataAdapter.ShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goal_data_item, parent, false);

        GoalDataAdapter.ShowViewHolder showViewHolder = new GoalDataAdapter.ShowViewHolder(view);
        return showViewHolder;
    }

    @Override
    public void onBindViewHolder(final GoalDataAdapter.ShowViewHolder holder, final int position) {

        if(pos==0){
            if(position==0){
                holder.day.setText("Today");

            }
            else if(position==1){
                holder.day.setText("Yesterday");

            }
            else{

                holder.day.setText(dataList.get(position).getDate());
            }
        }
        else {

            holder.day.setText(dataList.get(position).getDate());
        }
        if(type=="weight"){
            holder.data.setText(String.format("%.2f", dataList.get(position).getVal()));
            if(dataList.get(position).getVal()>1)
            holder.dataType.setText("kgs");
            else
                holder.dataType.setText("kg");

        }
        else  if(type=="waterConsumptionPerDay"){
            holder.data.setText(String.valueOf((int) dataList.get(position).getVal()/250));
            if((dataList.get(position).getVal()/250)>1)
                holder.dataType.setText("glasses");
            else
                holder.dataType.setText("glass");

        }
        else if(type=="caloriesPerDay"){
            holder.data.setText(String.valueOf((int) dataList.get(position).getVal()));
            if(dataList.get(position).getVal()>1)
                holder.dataType.setText("cals");
            else
                holder.dataType.setText("cal");
        }
        else if(type=="activePerDay"){
            holder.data.setText(String.valueOf((int) dataList.get(position).getVal()));
            if(dataList.get(position).getVal()>1)
                holder.dataType.setText("hours");
            else
                holder.dataType.setText("hour");
        }
 else if(type=="sleepDurationPerDay"){
            holder.data.setText(String.valueOf((int) dataList.get(position).getVal()));
            if(dataList.get(position).getVal()>1)
                holder.dataType.setText("hours");
            else
                holder.dataType.setText("hour");
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    /**
     * Class for declaring the class variables for the showtime recycler view items.
     */
    public class ShowViewHolder extends RecyclerView.ViewHolder {

        TextView day,data,dataType;



        /**
         * Class constructor for binding the class variables with the corresponding views of the layout.
         * @param itemView Parent view in the corresponding layout.
         */
        public ShowViewHolder(View itemView) {
            super(itemView);

            day=(TextView) itemView.findViewById(R.id.day);

            data=(TextView)itemView.findViewById(R.id.data);
            dataType=(TextView)itemView.findViewById(R.id.dataType);


        }
    }

}
