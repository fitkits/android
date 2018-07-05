package com.fitkits;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fitkits.Constants.PicassoTrustAll;
import com.fitkits.Model.Feed;
import java.util.ArrayList;
import java.util.List;


public class FitkitsNewsAdapter extends RecyclerView.Adapter<FitkitsNewsAdapter.ShowViewHolder>{
    List<Feed> fitkitsNewsItemList=new ArrayList<Feed>();
    Context context;
    SharedPreferences myPrefs;
    Typeface GB,GM,GL;


    public FitkitsNewsAdapter(Context context, List<Feed> fitkitsNewsItemList) {
        this.fitkitsNewsItemList=fitkitsNewsItemList;
        this.context=context;



    }



    @Override
    public FitkitsNewsAdapter.ShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fitkits_news_item, parent, false);
        myPrefs= PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        FitkitsNewsAdapter.ShowViewHolder showViewHolder = new FitkitsNewsAdapter.ShowViewHolder(view);
        return showViewHolder;
    }

    @Override
    public void onBindViewHolder(final FitkitsNewsAdapter.ShowViewHolder holder, final int position) {

        //Picasso.with(context).load(transformList.get(position).getImageUrl()).into(holder.transformImage);
        holder.title.setText(fitkitsNewsItemList.get(position).getTitle());
        holder.desc.setText(Html.fromHtml(fitkitsNewsItemList.get(position).getDescription()));
        holder.news_item_lay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,FitkitsNewsDetailActivity.class);
                intent.putExtra("blog_id",fitkitsNewsItemList.get(position).getId());
                context.startActivity(intent);
            }
        });

        if(fitkitsNewsItemList.get(position).getImageURL()!=null){

            PicassoTrustAll
                .getInstance(context).load(Constants.base_url+fitkitsNewsItemList.get(position).getImageURL()).into(holder.coverImage);


        }





    }

    @Override
    public int getItemCount() {
        return fitkitsNewsItemList.size();
    }
    /**
     * Class for declaring the class variables for the showtime recycler view items.
     */
    public class ShowViewHolder extends RecyclerView.ViewHolder {

        ImageView coverImage;
        TextView title,desc;
        LinearLayout news_item_lay;



        /**
         * Class constructor for binding the class variables with the corresponding views of the layout.
         * @param itemView Parent view in the corresponding layout.
         */
        public ShowViewHolder(View itemView) {
            super(itemView);
            coverImage=(ImageView)itemView.findViewById(R.id.coverImage);

            title=(TextView) itemView.findViewById(R.id.title);

            desc=(TextView)itemView.findViewById(R.id.desc);

            news_item_lay=(LinearLayout)itemView.findViewById(R.id.news_item_lay);


        }
    }




}
