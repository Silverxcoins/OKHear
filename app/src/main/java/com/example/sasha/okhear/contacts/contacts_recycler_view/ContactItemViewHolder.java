package com.example.sasha.okhear.contacts.contacts_recycler_view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sasha.okhear.R;
import com.example.sasha.okhear.utils.BarsUtil;

public class ContactItemViewHolder extends RecyclerView.ViewHolder {

    private final View itemView;
    private final TextView contactName;
    private final View contactAvatar;
    private final ImageView callButton;

    private final int speakColor;
    private final int showColor;

    private final int searchBarHeight;
    private final int contactItemViewHeight;

    public ContactItemViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        contactName = (TextView) itemView.findViewById(R.id.contact_name);
        contactAvatar = itemView.findViewById(R.id.contact_avatar);
        callButton = (ImageView) itemView.findViewById(R.id.call_button);
        speakColor = itemView.getResources().getColor(R.color.colorPrimarySpeak);
        showColor = itemView.getResources().getColor(R.color.colorPrimaryShow);
        searchBarHeight = itemView.getResources().getDimensionPixelSize(R.dimen.search_bar_height);
        contactItemViewHeight = itemView.getResources().getDimensionPixelSize(R.dimen.contact_item_view_height);
    }

    public void bind(ContactItem contact, Context context) {
        int height;
        if (getItemViewType() == ContactsAdapter.FIRST_EMPTY_CONTACT_ITEM_VIEW) {
            height = searchBarHeight + BarsUtil.getStatusBarHeight(itemView.getContext());
        } else {
            height = contactItemViewHeight;
        }
        if (itemView.getLayoutParams().height != height) {
            itemView.getLayoutParams().height = height;
            itemView.invalidate();
        }
    }

    public void setCallButtonColor(int color) {
        if (callButton != null) {
            callButton.setColorFilter(color);
        }
    }
}
