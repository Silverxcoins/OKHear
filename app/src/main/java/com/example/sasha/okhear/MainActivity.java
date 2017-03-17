package com.example.sasha.okhear;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.sasha.okhear.contacts.contacts_recycler_view.ContactItem;
import com.example.sasha.okhear.contacts.contacts_recycler_view.ContactsAdapter;
import com.example.sasha.okhear.contacts.contacts_recycler_view.ContactsDataSource;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.contacts_recycler_view)
    RecyclerView recyclerView;

    @ViewById(R.id.right_main_button_icon)
    View rightButtonIcon;

    @AfterViews
    protected void init() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryShow));

        ContactsDataSource dataSource = new ContactsDataSource(recyclerView);
        ContactsAdapter adapter = new ContactsAdapter(this, dataSource);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        for (int i = 0; i < 20; i++) {
            dataSource.addContact(new ContactItem());
        }
    }
}
