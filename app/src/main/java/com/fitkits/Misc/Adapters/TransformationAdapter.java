package com.fitkits.Misc.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fitkits.Misc.Constants;
import com.fitkits.Misc.Constants.PicassoTrustAll;
import com.fitkits.Model.Stats;
import com.fitkits.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class TransformationAdapter extends RecyclerView.Adapter<TransformationAdapter.ShowViewHolder>{
    List<Stats> transformList=new ArrayList<Stats>();
    Context context;
    Typeface GB,GM,GL;


    public TransformationAdapter(Context context, List<Stats> transformList) {
        this.transformList=transformList;
        this.context=context;



    }



    @Override
    public TransformationAdapter.ShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_upload_card, parent, false);
        TransformationAdapter.ShowViewHolder showViewHolder = new TransformationAdapter.ShowViewHolder(view);
        return showViewHolder;
    }

    @Override
    public void onBindViewHolder(final TransformationAdapter.ShowViewHolder holder, final int position) {

        //Picasso.with(context).load(transformList.get(position).getImageUrl()).into(holder.transformImage);
        holder.transformName.setText("Image "+(position+1));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
        SimpleDateFormat sdf_new = new SimpleDateFormat("dd-MM-yyyy");

        try {
            holder.transformDate.setText(sdf_new.format(sdf.parse(transformList.get(position).getTimeStamp())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PicassoTrustAll.getInstance(context).load(Constants.base_url+transformList.get(position).getImage()).into(holder.transformImage);




    }

    @Override
    public int getItemCount() {
        return transformList.size();
    }
    /**
     * Class for declaring the class variables for the showtime recycler view items.
     */
    public class ShowViewHolder extends RecyclerView.ViewHolder {

        ImageView transformImage;
        TextView transformName,transformDate;



        /**
         * Class constructor for binding the class variables with the corresponding views of the layout.
         * @param itemView Parent view in the corresponding layout.
         */
        public ShowViewHolder(View itemView) {
            super(itemView);
            transformImage=(ImageView)itemView.findViewById(R.id.transformImage);

            transformDate=(TextView) itemView.findViewById(R.id.transformDate);

            transformName=(TextView)itemView.findViewById(R.id.transformName);


        }
    }

}
