package com.example.sasha.okhear.contacts.contacts_recycler_view;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.sasha.okhear.R;

public class ContactsAdapter extends RecyclerView.Adapter {

    private final int EMPTY_CONTACT_ITEM_VIEW = 1;
    private final int CONTACT_ITEM_VIEW = 2;

    private final Activity activity;
    private final ContactsDataSource dataSource;

    public ContactsAdapter(Activity activity, ContactsDataSource dataSource) {
        this.activity = activity;
        this.dataSource = dataSource;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == CONTACT_ITEM_VIEW) {
            return new ContactItemViewHolder(
                    activity.getLayoutInflater().inflate(R.layout.contact_item_view, parent, false)
            );
        } else {
            return new ContactItemViewHolder(
                    activity.getLayoutInflater().inflate(R.layout.first_or_last_contact_item_view, parent, false)
            );
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ContactItem contact = dataSource.getContact(position);
        ((ContactItemViewHolder) holder).bind(contact, activity);
    }

    @Override
    public int getItemCount() {
        return dataSource.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return (position != 0 && position != dataSource.getCount() - 1) ? CONTACT_ITEM_VIEW : EMPTY_CONTACT_ITEM_VIEW;
    }

}
