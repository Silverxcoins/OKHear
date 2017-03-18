package com.example.sasha.okhear;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.sasha.okhear.Utils.Preferences;
import com.example.sasha.okhear.contacts.contacts_recycler_view.ContactItem;
import com.example.sasha.okhear.contacts.contacts_recycler_view.ContactItemViewHolder;
import com.example.sasha.okhear.contacts.contacts_recycler_view.ContactsAdapter;
import com.example.sasha.okhear.contacts.contacts_recycler_view.ContactsDataSource;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.contacts_recycler_view)
    RecyclerView recyclerView;

    @ViewById(R.id.overlay)
    Overlay_ overlay;

    @Bean
    Preferences preferences;

    @ColorRes(R.color.colorPrimaryShow)
    int primaryShowColor;

    @ColorRes(R.color.colorPrimarySpeak)
    int primarySpeakColor;

    @AfterViews
    protected void init() {
        ContactsDataSource dataSource = new ContactsDataSource(recyclerView);
        ContactsAdapter adapter = new ContactsAdapter(this, dataSource);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        for (int i = 0; i < 30; i++) {
            dataSource.addContact(new ContactItem());
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                setCallButtonsColor(preferences.getSpeakOrShow() == Preferences.SPEAK ? primarySpeakColor : primaryShowColor);
                if (dy > 50 || dy < -50) {
                    overlay.hideControls();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    overlay.showControls();
                }
            }
        });
    }

    public void setCallButtonsColor(int color) {
        if (recyclerView != null) {
            for (int i = 0; i < recyclerView.getAdapter().getItemCount(); i++) {
                View child = recyclerView.getChildAt(i);
                if (child != null) {
                    ((ContactItemViewHolder) recyclerView.getChildViewHolder(child)).setCallButtonColor(color);
                }
            }
        }
    }
}
