package com.moto.pixelr.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.moto.pixelr.R;
import com.moto.pixelr.activity.MainActivity;
import com.moto.pixelr.data.Global;
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

    private int currentSelection = -1;

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public PixelAdapter(Context context) {
        this.context = context;
        this.pixelList = PixelList.generatePixelList();
    }

    /** RECYCLER VIEW METHODS __________________________________________________________________ **/

    @Override
    public PixelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pixel_row, parent, false);
        return new PixelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PixelViewHolder holder, final int position) {

        int pixelImageResource = pixelList.get(position).getPixelResource();
        String pixelTextDescription = pixelList.get(position).getPixelDescription();

        // PIXEL IMAGE:
        Picasso.with(context)
                .load(pixelImageResource)
                .into(holder.pixelButton);

        // PIXEL TEXT:
        holder.pixelText.setText(pixelTextDescription);
        holder.pixelText.setShadowLayer(4, 2, 2, Color.BLACK);

        // Highlights the text selection.
        if (currentSelection == position) {
            holder.pixelText.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            holder.pixelText.setBackground(null);
        }

        // PIXEL BUTTON:
        holder.pixelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentSelection = position;

                // MUSIC:
                if (position == 7) {
                    ((MainActivity) context).displayMusicOptions(true);
                    ((MainActivity) context).displayEmojiOptions(false);
                }

                // EMOJI:
                else if (position == 0) {
                    ((MainActivity) context).displayEmojiOptions(true);
                    ((MainActivity) context).displayMusicOptions(false);
                }

                else {
                    ((MainActivity) context).displayMusicOptions(false);
                    ((MainActivity) context).displayEmojiOptions(false);
                    Global.cmd_key = 1;
                    Global.info = new byte[] {(byte)(position + 1)};
                }

                notifyDataSetChanged();
            }
        });
    }

    @Override

    public int getItemCount() {
        return pixelList.size();
    }

    /** SUBCLASSES _____________________________________________________________________________ **/

    public static class PixelViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pixel_icon) ImageView pixelButton;
        @BindView(R.id.pixel_description_text) TextView pixelText;

        PixelViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
