package com.fitkits;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fitkits.Model.Feed;
import java.util.ArrayList;
import java.util.List;


public class FitkitsNewsShortAdapter extends RecyclerView.Adapter<FitkitsNewsShortAdapter.ShowViewHolder>{
    List<Feed> fitkitsNewsItemList=new ArrayList<Feed>();
    Context context;
    SharedPreferences myPrefs;
    Typeface GB,GM,GL;


    public FitkitsNewsShortAdapter(Context context, List<Feed> fitkitsNewsItemList) {
        this.fitkitsNewsItemList=fitkitsNewsItemList;
        this.context=context;



    }



    @Override
    public FitkitsNewsShortAdapter.ShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_item, parent, false);
        myPrefs= PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        FitkitsNewsShortAdapter.ShowViewHolder showViewHolder = new FitkitsNewsShortAdapter.ShowViewHolder(view);
        return showViewHolder;
    }

    @Override
    public void onBindViewHolder(final FitkitsNewsShortAdapter.ShowViewHolder holder, final int position) {

        //Picasso.with(context).load(transformList.get(position).getImageUrl()).into(holder.transformImage);
        holder.title.setText(fitkitsNewsItemList.get(position).getTitle());





    }

    @Override
    public int getItemCount() {
        return 2;
    }
    /**
     * Class for declaring the class variables for the showtime recycler view items.
     */
    public class ShowViewHolder extends RecyclerView.ViewHolder {

        TextView title;



        /**
         * Class constructor for binding the class variables with the corresponding views of the layout.
         * @param itemView Parent view in the corresponding layout.
         */
        public ShowViewHolder(View itemView) {
            super(itemView);

            title=(TextView) itemView.findViewById(R.id.title);


        }
    }




}
