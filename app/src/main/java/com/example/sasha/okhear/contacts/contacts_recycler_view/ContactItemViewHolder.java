package com.example.sasha.okhear.contacts.contacts_recycler_view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sasha.okhear.R;

public class ContactItemViewHolder extends RecyclerView.ViewHolder {

    private final TextView contactName;
    private final View contactAvatar;

    public ContactItemViewHolder(View itemView) {
        super(itemView);
        contactName = (TextView) itemView.findViewById(R.id.contact_name);
        contactAvatar = itemView.findViewById(R.id.contact_avatar);
    }

}
