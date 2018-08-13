package com.fitkits;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.fitkits.Model.User;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ShowViewHolder>  {
    User userMasterGoal;
    Activity context;
    SharedPreferences myPrefs;
    View view;
    Bundle bundle;

    android.support.v4.app.FragmentManager fragmentManager;

    public GoalAdapter(android.support.v4.app.FragmentManager fragmentManager,Activity context, User userMasterGoal) {
        this.userMasterGoal=userMasterGoal;
        this.context=context;
        this.fragmentManager=fragmentManager;
        myPrefs= PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }




    @Override
    public GoalAdapter.ShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==0) {
            view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goal_item_even, parent, false);
        }
        else if(viewType==1){
            view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goal_item_odd, parent, false);
        }
        GoalAdapter.ShowViewHolder showViewHolder = new GoalAdapter.ShowViewHolder(view);
        return showViewHolder;
    }

    @Override
    public void onBindViewHolder(final GoalAdapter.ShowViewHolder holder, final int position) {


       // Picasso.with(context).load(Constants.base_url+userMasterGoalList.get(position).getImageURL()).into(holder.goalIcon);

if(position==0) {
    holder.goalIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.veg));
    holder.goalName.setText("CALORIES");
    DecimalFormat decimalFormat = new DecimalFormat("#.00");
    float cals=userMasterGoal.getGoals().getCaloriesPerDay().getValue();
    if(cals<10){
        holder.goalVal.setText("0"+String.valueOf(cals));
    }
    else{
        holder.goalVal.setText(decimalFormat.format(cals));

    }
    if(cals<2){
        holder.goalUnit.setText("cal");
    }
    else{
        holder.goalUnit.setText("cals");
    }
    if (userMasterGoal.getGoals().getCaloriesPerDay().getValue() == 0) {
        holder.goalValLay.setVisibility(View.GONE);
        holder.goalText.setText("Set a daily calorie goal!");
        holder.goalDesc.setText("Set yourself a calorie goal. Stay within the limits daily, go!");
    } else {
        holder.goalValLay.setVisibility(View.VISIBLE);
        holder.goalText.setText("Your daily calorie goal");
        holder.goalDesc.setText("Click to edit");
    }
}

else if(position==1) {
    holder.goalIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.fit));
    holder.goalName.setText("ACTIVE HOURS");
    int activeHours=userMasterGoal.getGoals().getActivePerDay().getValue();
    if(activeHours<10){
        holder.goalVal.setText("0"+String.valueOf(activeHours));
    }
    else{
        holder.goalVal.setText(String.valueOf(activeHours));

    }
    if(activeHours<2){
        holder.goalUnit.setText("hour");
    }
    else{
        holder.goalUnit.setText("hours");
    }

    if (userMasterGoal.getGoals().getActivePerDay().getValue() == 0) {
        holder.goalValLay.setVisibility(View.GONE);
        holder.goalText.setText("Set a daily active hours goal!");
        holder.goalDesc.setText("Keep yourself active daily, walk/bicycle wherever possible.");
    } else {
        holder.goalValLay.setVisibility(View.VISIBLE);
        holder.goalText.setText("Your daily active hours goal");
        holder.goalDesc.setText("Click to edit");
    }
}

else if(position==2) {
    holder.goalIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.weight));
    holder.goalName.setText("WEIGHT");
    int weight=userMasterGoal.getGoals().getWeight().getValue();
    if(weight<10){
        holder.goalVal.setText("0"+String.valueOf(weight));
    }
    else{
        holder.goalVal.setText(String.valueOf(weight));

    }
    if(weight<2){
        holder.goalUnit.setText("kg");
    }
    else{
        holder.goalUnit.setText("kgs");
    }
    if (userMasterGoal.getGoals().getWeight().getValue() == 0) {
        holder.goalValLay.setVisibility(View.GONE);
        holder.goalText.setText("Set a daily weight goal!");
        holder.goalDesc.setText("What's your goal weight ");
    } else {
        holder.goalValLay.setVisibility(View.VISIBLE);
        holder.goalText.setText("Your daily weight goal");
        holder.goalDesc.setText("Click to edit");
    }
}
else if(position==3) {
    holder.goalIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.sleep));
    holder.goalName.setText("SLEEP");
    int sleepHours=userMasterGoal.getGoals().getSleepDurationPerDay().getValue();
    if(sleepHours<10){
        holder.goalVal.setText("0"+String.valueOf(sleepHours));
    }
    else{
        holder.goalVal.setText(String.valueOf(sleepHours));

    }
    if(sleepHours<2){
        holder.goalUnit.setText("hour");
    }
    else{
        holder.goalUnit.setText("hours");
    }

    if (userMasterGoal.getGoals().getSleepDurationPerDay().getValue() == 0) {
        holder.goalValLay.setVisibility(View.GONE);
        holder.goalText.setText("Set a daily sleep goal!");
        holder.goalDesc.setText("Sleep is vital. Stick to a routine every night.");
    } else {
        holder.goalValLay.setVisibility(View.VISIBLE);
        holder.goalText.setText("Your daily sleep goal");
        holder.goalDesc.setText("Click to edit");
    }
}
else if(position==4) {
    holder.goalIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.tap));
    holder.goalName.setText("WATER");
    int glassCount=userMasterGoal.getGoals().getWaterConsumptionPerDay().getValue()/250;
    if(glassCount<10){
        holder.goalVal.setText("0"+String.valueOf(glassCount));
    }
    else{
        holder.goalVal.setText(String.valueOf(glassCount));

    }
    if(glassCount<2){
        holder.goalUnit.setText("glass");
    }
    else{
        holder.goalUnit.setText("glasses");
    }

    if (userMasterGoal.getGoals().getWaterConsumptionPerDay().getValue() == 0) {
        holder.goalValLay.setVisibility(View.GONE);
        holder.goalText.setText("Set a daily water goal!");
        holder.goalDesc.setText("Keep yourself hydrated, carry a sipper with you always.");
    } else {
        holder.goalValLay.setVisibility(View.VISIBLE);
        holder.goalText.setText("Your daily water goal");
        holder.goalDesc.setText("Click to edit");
    }
}




        holder.goalLay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position==0) {
                    CalorieDialog calorieDialog=new CalorieDialog();
                    bundle=new Bundle();
                    bundle.putString("type","goal");
                    calorieDialog.setArguments(bundle);
                    calorieDialog.show(fragmentManager,"Calorie");
                }
                else if(position==1) {
                    ActiveDialog activeDialog=new ActiveDialog();
                    bundle=new Bundle();
                    bundle.putString("type","goal");
                    activeDialog.setArguments(bundle);

                    activeDialog.show(fragmentManager,"Acive Hours");
                }

                else if(position==2) {
                    WeightDialog weightDialog=new WeightDialog();
                    bundle=new Bundle();
                    bundle.putString("type","goal");
                    weightDialog.setArguments(bundle);
                    weightDialog.show(fragmentManager,"Weight");
                }
                else if(position==3)
                {
                    SleepDialog sleepDialog=new SleepDialog();
                    bundle=new Bundle();
                    bundle.putString("type","goal");
                    sleepDialog.setArguments(bundle);

                    sleepDialog.show(fragmentManager,"Sleep");
                }
                else if(position==4)
                    {
                    WaterDialog waterDialog=new WaterDialog();
                    bundle=new Bundle();
                        bundle.putString("type","goal");
                        waterDialog.setArguments(bundle);

                        waterDialog.show(fragmentManager,"Water");
                }


            }
        });








    }

    @Override
    public int getItemViewType(int position) {
        if((position+1)%2==0)
        return 0;
        else
            return 1;
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    /**
     * Initiates Payment through RazorPay.
     */

    /**
     * Class for declaring the class variables for the showtime recycler view items.
     */
    public class ShowViewHolder extends RecyclerView.ViewHolder {

        ImageView goalIcon;
        TextView goalName,goalVal,goalText,goalDesc,goalUnit;
        LinearLayout goalValLay,goalCard;
        RelativeLayout goalLay;




        /**
         * Class constructor for binding the class variables with the corresponding views of the layout.
         * @param itemView Parent view in the corresponding layout.
         */
        public ShowViewHolder(View itemView) {
            super(itemView);
            goalIcon=(ImageView)itemView.findViewById(R.id.goal_icon);

            goalName=(TextView) itemView.findViewById(R.id.goal_title);

            goalText=(TextView) itemView.findViewById(R.id.goal_text);

            goalDesc=(TextView) itemView.findViewById(R.id.goal_desc);

            goalUnit=(TextView) itemView.findViewById(R.id.goal_unit);
            goalVal=(TextView) itemView.findViewById(R.id.goal_val);



            goalLay=(RelativeLayout) itemView.findViewById(R.id.goalLay);
            goalValLay=(LinearLayout) itemView.findViewById(R.id.goal_val_lay);

            goalCard=(LinearLayout) itemView.findViewById(R.id.goal_card);




        }
    }


}
