package com.fitkits.Misc.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fitkits.R;


public class WaterGlassAdapter extends RecyclerView.Adapter<WaterGlassAdapter.ShowViewHolder>{

    int goalWateConsumption,waterConsumed;
View view;
public WaterGlassAdapter(int goalWateConsumption,int waterConsumed ) {
        this.goalWateConsumption=goalWateConsumption;
        this.waterConsumed=waterConsumed;


    }

    @Override
    public int getItemViewType(int position) {
        if(position<waterConsumed){
            return 1;
        }
        else{
            return 0;
        }
    }

    @Override
    public WaterGlassAdapter.ShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==1) {
            view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filled_glass_item, parent, false);
        }
        else{
            view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.empty_glass_item, parent, false);
        }
        WaterGlassAdapter.ShowViewHolder showViewHolder = new WaterGlassAdapter.ShowViewHolder(view);
        return showViewHolder;
    }

    @Override
    public void onBindViewHolder(final WaterGlassAdapter.ShowViewHolder holder, final int position) {

        //Picasso.with(context).load(transformList.get(position).getImageUrl()).into(holder.transformImage);





    }

    @Override
    public int getItemCount() {
    if(waterConsumed<=goalWateConsumption) {
        return goalWateConsumption;
    }
    else{
        return waterConsumed;

    }
    }
    /**
     * Class for declaring the class variables for the showtime recycler view items.
     */
    public class ShowViewHolder extends RecyclerView.ViewHolder {

        ImageView transformImage;



        /**
         * Class constructor for binding the class variables with the corresponding views of the layout.
         * @param itemView Parent view in the corresponding layout.
         */
        public ShowViewHolder(View itemView) {
            super(itemView);

        }
    }

}
