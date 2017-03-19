package com.example.sasha.okhear.contacts;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.sasha.okhear.Overlay_;
import com.example.sasha.okhear.R;
import com.example.sasha.okhear.contacts.contacts_recycler_view.ContactItem;
import com.example.sasha.okhear.contacts.contacts_recycler_view.ContactItemViewHolder;
import com.example.sasha.okhear.contacts.contacts_recycler_view.ContactsAdapter;
import com.example.sasha.okhear.contacts.contacts_recycler_view.ContactsDataSource;
import com.example.sasha.okhear.utils.Preferences;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

@EFragment(R.layout.fragment_contacts)
public class ContactsFragment extends Fragment {

    @ViewById(R.id.contacts_recycler_view)
    RecyclerView recyclerView;

    @ColorRes(R.color.colorPrimaryShow)
    int primaryShowColor;

    @ColorRes(R.color.colorPrimarySpeak)
    int primarySpeakColor;

    @Bean
    Preferences preferences;

    Overlay_ overlay;

    public ContactsFragment() {
    }

    @AfterViews
    void init() {
        ContactsDataSource dataSource = new ContactsDataSource(recyclerView);
        ContactsAdapter adapter = new ContactsAdapter(getActivity(), dataSource);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        for (int i = 0; i < 30; i++) {
            dataSource.addContact(new ContactItem());
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                setCallButtonsColor(preferences.getSpeakOrShow() == Preferences.SPEAK ? primarySpeakColor : primaryShowColor);
                if (dy > 50 || dy < -50) {
                    overlay.showControls(false);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    overlay.showControls(true);
                }
            }
        });
    }

    public void setOverlay(Overlay_ overlay) {
        this.overlay = overlay;
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
