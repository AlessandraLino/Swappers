package br.edu.ifce.swappers.swappers.activities;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import br.edu.ifce.swappers.swappers.R;
import br.edu.ifce.swappers.swappers.fragments.tabs.detail_place.AvailableBooksFragment;
import br.edu.ifce.swappers.swappers.fragments.tabs.detail_place.InformationFragment;
import br.edu.ifce.swappers.swappers.fragments.tabs.profile.DonatedBooksFragment;
import br.edu.ifce.swappers.swappers.fragments.tabs.profile.FavoriteBooksFragment;
import br.edu.ifce.swappers.swappers.fragments.tabs.profile.RetrievedBooksFragment;

public class DetailPlaceActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_place);

        this.initToolbar();
        this.initTabHost();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null){
            this.setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void initTabHost() {
        FragmentTabHost placeTabHost;

        placeTabHost = (FragmentTabHost) findViewById(R.id.place_tabHost);
        placeTabHost.setup(this, this.getSupportFragmentManager(), android.R.id.tabcontent);

        TabHost.TabSpec placeInformationTab   = placeTabHost.newTabSpec("placeInformationTab");
        TabHost.TabSpec availableBooksTab     = placeTabHost.newTabSpec("availableBooksTab");

        placeInformationTab.setIndicator("INFORMATION");
        availableBooksTab.setIndicator("AVAILABLE BOOKS");

        placeTabHost.addTab(placeInformationTab, InformationFragment.class, null);
        placeTabHost.addTab(availableBooksTab, AvailableBooksFragment.class, null);

        placeTabHost.setCurrentTab(0);

        this.stylizeTabsTextView(placeTabHost);
    }

    private void stylizeTabsTextView(FragmentTabHost tabHost){
        ColorStateList tabTextColors;
        TabWidget tabWidget;
        TextView tabTextView;
        View tabView;

        int tabAmount;

        tabWidget     = tabHost.getTabWidget();
        tabTextColors = this.getResources().getColorStateList(R.color.tab_text_selector);
        tabAmount     = tabWidget.getTabCount();

        for (int i = 0; i < tabAmount; i++){
            tabView = tabWidget.getChildTabViewAt(i);
            tabView.setBackgroundResource(R.drawable.tab_indicator);

            tabTextView = (TextView) tabView.findViewById(android.R.id.title);
            tabTextView.setTextColor(tabTextColors);
        }
    }

}
