package com.zilideus.jukebox.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zilideus.jukebox.DownloadSongDetailAndPlayOnClick;
import com.zilideus.jukebox.MainActivity;
import com.zilideus.jukebox.R;
import com.zilideus.jukebox.model.Station;

import java.util.List;

/**
 * Created by sandeeprana on 14/12/16.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */
class AdapterStationsList extends RecyclerView.Adapter<AdapterStationsList.ViewHolder> {


    private final Context context;
    private final List<Station> stationList;
    private DownloadSongDetailAndPlayOnClick downloadSongDetailAndPlayOnclick;

    AdapterStationsList(Context context, List<Station> stationList) {

        this.context = context;
        this.stationList = stationList;
    }


    @Override
    public AdapterStationsList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.itemlistrow, parent, false));
    }

    @Override
    public void onBindViewHolder(AdapterStationsList.ViewHolder holder, int position) {
        Station station = this.stationList.get(position);
        holder.title.setText(station.getName());
        holder.text.setText(station.getCtqueryString());
        holder.bt.setText(station.getBrbitrate() + " Hz");
        holder.genre.setText(station.getGenre());
        if (station.getLogo() != null) {
            Glide.with(context)
                    .load(station.getLogo())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageLogo);
        }

        if (station.isFavourite()) {
            holder.imageFavourite.setImageResource(R.drawable.favourite);
        } else {
            holder.imageFavourite.setImageResource(R.drawable.favourite_grey);
        }

    }

    @Override
    public int getItemCount() {
        return this.stationList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageLogo, imageFavourite;
        private final TextView title;
        private final TextView text;
        private final TextView bt;
        private final TextView genre;

        ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.text_list_title);
            text = (TextView) v.findViewById(R.id.text_list_text);
            bt = (TextView) v.findViewById(R.id.text_list_bt);
            genre = (TextView) v.findViewById(R.id.text_list_genre);
            imageLogo = (ImageView) v.findViewById(R.id.logo);
            imageFavourite = (ImageView) v.findViewById(R.id.favourite);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Station station = stationList.get(ViewHolder.this.getAdapterPosition());
                    if (station != null) {
                        if (downloadSongDetailAndPlayOnclick != null) {
                            downloadSongDetailAndPlayOnclick.cancel(true);
                        }
                        downloadSongDetailAndPlayOnclick = new DownloadSongDetailAndPlayOnClick(station, (MainActivity) context);
                        downloadSongDetailAndPlayOnclick.execute(station);
                    }
                }
            });

            imageFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Station station = stationList.get(ViewHolder.this.getAdapterPosition());
                    Log.e("Favorite", "clicked" + station.getStationId());
                    if (station.isFavourite()) {
                        station.delete();
                        ((ImageView) view).setImageResource(R.drawable.favourite_grey);
                    } else {
                        Station.save(station);
                        ((ImageView) view).setImageResource(R.drawable.favourite);
                    }

                }
            });

        }

    }
}
