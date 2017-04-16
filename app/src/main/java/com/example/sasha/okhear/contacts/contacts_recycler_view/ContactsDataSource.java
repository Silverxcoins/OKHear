package com.example.sasha.okhear.contacts.contacts_recycler_view;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactsDataSource {
    private final List<ContactItem> contacts = new ArrayList<>();

    private RecyclerView recyclerView;

    public ContactsDataSource(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public int getCount() {
        return contacts.size();
    }

    public ContactItem getContact(int position) {
        return contacts.get(position);
    }

    public void addContact(ContactItem item) {
        contacts.add(item);
        recyclerView.getAdapter().notifyItemInserted(contacts.size() - 1);
    }
}
