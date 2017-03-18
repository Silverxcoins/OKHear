package com.example.sasha.okhear.contacts.contacts_recycler_view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sasha.okhear.R;

public class ContactItemViewHolder extends RecyclerView.ViewHolder {

    private final TextView contactName;
    private final View contactAvatar;
    private final ImageView callButton;

    private final int speakColor;
    private final int showColor;

    public ContactItemViewHolder(View itemView) {
        super(itemView);
        contactName = (TextView) itemView.findViewById(R.id.contact_name);
        contactAvatar = itemView.findViewById(R.id.contact_avatar);
        callButton = (ImageView) itemView.findViewById(R.id.call_button);
        speakColor = itemView.getResources().getColor(R.color.colorPrimarySpeak);
        showColor = itemView.getResources().getColor(R.color.colorPrimaryShow);
    }

    public void bind(ContactItem contact, Context context) {
    }

    public void setCallButtonColor(int color) {
        if (callButton != null) {
            callButton.setColorFilter(color);
        }
    }
}
