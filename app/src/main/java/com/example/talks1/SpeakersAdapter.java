package com.example.talks1;


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

import com.bumptech.glide.Glide;
import com.example.talks1.Models.Speaker;
import com.example.talks1.Models.Talk;
import com.example.talks1.Models.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Map;

public class SpeakersAdapter extends RecyclerView.Adapter<SpeakersAdapter.MyViewHolder> {

    private Fragment mContext;
    private List<User> speakers;
    private Map<String, Object> speakersList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView cover, overflow;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name_speaker);
            cover = view.findViewById(R.id.cover_speaker);
            overflow = view.findViewById(R.id.overflow);
        }
    }


    public SpeakersAdapter(Fragment mContext, List<User> speakers) {
        this.mContext = mContext;
        this.speakers = speakers;
    }

    @Override
    public SpeakersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.speaker_card, parent, false);
        final SpeakersAdapter.MyViewHolder myViewHolder = new SpeakersAdapter.MyViewHolder(itemView);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speakerId = speakers.get(myViewHolder.getAdapterPosition()).getId();
                Intent intent = new Intent(mContext.getContext(), SpeakerDetailsActivity.class);
                intent.putExtra("userID", speakerId);
                mContext.getContext().startActivity(intent);
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final SpeakersAdapter.MyViewHolder holder, int position) {
        User user = speakers.get(position);
        holder.name.setText(user.getName());


        if (user.getPicture() != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference ref = storage.getReference().child("images").child(user.getPicture());

            GlideApp.with(mContext).load(ref).into(holder.cover);
        }
        else {
            holder.cover.setImageResource(0);
        }


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
        popup.setOnMenuItemClickListener(new SpeakersAdapter.MyMenuItemClickListener());
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
        return speakers.size();
    }
}
