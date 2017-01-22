package com.moto.pixelr.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.moto.pixelr.R;
import com.moto.pixelr.data.PixelList;
import com.moto.pixelr.model.Pixel;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Michael Yoon Huh on 1/21/2017.
 */

public class PixelAdapter extends RecyclerView.Adapter<PixelAdapter.PixelViewHolder> {

    private static final String LOG_TAG = PixelAdapter.class.getSimpleName();

    private Context context;
    private List<Pixel> pixelList;

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public PixelAdapter(Context context) {
        this.context = context;
        this.pixelList = PixelList.generatePixelList();
    }

    /** RECYCLER VIEW METHODS __________________________________________________________________ **/

    @Override
    public PixelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pixel_row, parent, false);
        PixelViewHolder viewHolder = new PixelViewHolder(view, new PixelViewHolder.OnViewHolderClickListener() {

            @Override
            public void onPixelClick(View caller, int position) {
                // TODO: Select pixel image action here.
            }

        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PixelViewHolder holder, int position) {

        int pixelImageResource = pixelList.get(position).getPixelResource();
        String pixelTextDescription = pixelList.get(position).getPixelDescription();

        // PIXEL IMAGE:
        Picasso.with(context)
                .load(pixelImageResource)
                .into(holder.pixelButton);

        // PIXEL TEXT:
        holder.pixelText.setText(pixelTextDescription);
        holder.pixelText.setShadowLayer(4, 2, 2, Color.BLACK);
    }

    @Override
    public int getItemCount() {
        return pixelList.size();
    }

    /** SUBCLASSES _____________________________________________________________________________ **/

    public static class PixelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.pixel_icon) ImageButton pixelButton;
        @BindView(R.id.pixel_description_text) TextView pixelText;

        public OnViewHolderClickListener viewHolderClickListener;

        PixelViewHolder(View itemView, OnViewHolderClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            if (listener != null) {
                viewHolderClickListener = listener;
                pixelButton.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            int itemPos = getAdapterPosition();
            viewHolderClickListener.onPixelClick(v, itemPos);
        }

        public interface OnViewHolderClickListener {
            void onPixelClick(View caller, int position);
        }
    }
}
