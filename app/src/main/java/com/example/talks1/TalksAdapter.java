package com.example.talks1;

/**import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;**/
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talks1.Models.Talk;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class TalksAdapter extends RecyclerView.Adapter<TalksAdapter.MyViewHolder> {

    private Fragment mContext;
    private Activity mActivity;
    private List<Talk> talkList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, speaker;
        public ImageView cover, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            speaker = view.findViewById(R.id.speaker);
            cover = view.findViewById(R.id.cover);
            overflow = view.findViewById(R.id.overflow);

            /*speaker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext.getContext(), TalkDetailsActivity.class);
                    startActivity(intent);
                }
            });*/
        }
    }


    public TalksAdapter(Fragment mContext, List<Talk> talkList) {
        this.mContext = mContext;
        this.talkList = talkList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.talk_card, parent, false);
        final MyViewHolder myViewHolder = new MyViewHolder(itemView);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventId = talkList.get(myViewHolder.getAdapterPosition()).getId();
                Intent intent = new Intent(mContext.getContext(), TalkDetailsActivity.class);
                intent.putExtra("talkId", eventId);
                mContext.getContext().startActivity(intent);
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Talk talk = talkList.get(position);
        holder.title.setText(talk.getTitle());
        holder.speaker.setText(talk.getSpeaker());


        // loading album cover using Glide library
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("events").child(talk.getPicture());
        GlideApp.with(mContext).load(ref).into(holder.cover);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.talk_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext.getContext(), "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext.getContext(), "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return talkList.size();
    }
}
