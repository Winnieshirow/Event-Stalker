package com.outer.fragments.drawer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;

import com.com.inner.fragment.Broadwayshow_fragment;
import com.com.inner.fragment.Concerts_fragment;
import com.com.inner.fragment.Conference_fragment;
import com.com.inner.fragment.Discovery_fragment;
import com.com.inner.fragment.Movies_fragment;
import com.com.inner.fragment.Sports_fragment;
import com.events.shirow.eventstalker.R;
import com.navigation.adapter.MyFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shirow on 10/29/16.
 */
public class Home_fragment extends Fragment implements TabHost.OnTabChangeListener,ViewPager.OnPageChangeListener  {
    ViewPager viewPager;
    TabHost tabHost;
    private MyFragmentPagerAdapter myViewPagerAdapter;
    int i = 0;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_main_toolbar,container,false);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("EventStalker");

        i++;

        // init tabhost
        this.initTabHost(savedInstanceState);

        // init ViewPager
        this.initViewPager();


        return v;
    }

    public class FakeContent implements TabHost.TabContentFactory {
        Context context;

        public FakeContent(Context mcontext){
            context = mcontext;
        }

        @Override
        public View createTabContent(String tag) {
            View fakeView = new View(context);
            fakeView.setMinimumHeight(0);
            fakeView.setMinimumWidth(0);
            return fakeView;
        }
    }

    private void initViewPager() {
        viewPager = (ViewPager) v.findViewById(R.id.view_pager);

        List<Fragment> listFragments = new ArrayList<Fragment>();
        listFragments.add(new Discovery_fragment());
        listFragments.add(new Movies_fragment());
        listFragments.add(new Broadwayshow_fragment());
        listFragments.add(new Concerts_fragment());
        listFragments.add(new Sports_fragment());
        listFragments.add(new Conference_fragment());

        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(
                getChildFragmentManager(),listFragments);

        viewPager.setAdapter(myFragmentPagerAdapter);

        viewPager.addOnPageChangeListener(this);

    }
    private void initTabHost(Bundle savedInstanceState) {
        tabHost = (TabHost) v.findViewById(R.id.tabhost);

        tabHost.setup();

        String[] tabNames = {"Discover","Movies","Broadway Shows","Concerts","Sports","Conferences"};

        for (int i=0;i<tabNames.length;i++)
        {
            TabHost.TabSpec tabSpec;
            tabSpec = tabHost.newTabSpec(tabNames[i]);

            tabSpec.setIndicator(tabNames[i]);
            tabSpec.setContent(new FakeContent(getActivity()) );
            tabHost.addTab(tabSpec);
        }

        tabHost.setOnTabChangedListener(this);
    }

    @Override
    public void onTabChanged(String tabId) {
        int selectedItem = tabHost.getCurrentTab();
        viewPager.setCurrentItem(selectedItem);

        HorizontalScrollView hScrollView = (HorizontalScrollView) v.findViewById(R.id.h_scroll_view);
        View tabView = tabHost.getCurrentTabView();
        int ScollPos = tabView.getLeft()-(hScrollView.getWidth()-tabView.getWidth())/2;

        hScrollView.smoothScrollTo(ScollPos,0);

    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int selectedItem) {
        tabHost.setCurrentTab(selectedItem);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
