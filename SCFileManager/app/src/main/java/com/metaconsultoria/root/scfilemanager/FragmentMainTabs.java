package com.metaconsultoria.root.scfilemanager;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

public class FragmentMainTabs extends Fragment
        implements TabLayout.OnTabSelectedListener{

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MyTabAdapter myTabAdapter;

    public FragmentMainTabs() {
        // Required empty public constructor
    }


    public static FragmentMainTabs newInstance() {
        FragmentMainTabs fragment = new FragmentMainTabs();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_fragment_main_tabs, container, false);
            mViewPager = (ViewPager) view.findViewById(R.id.viewPager_locate);
            mViewPager.setOffscreenPageLimit(1);
            myTabAdapter = new MyTabAdapter(getActivity(), getContext(), getChildFragmentManager());
            mViewPager.setAdapter(myTabAdapter);

            mTabLayout = (TabLayout) view.findViewById(R.id.tabs_locate);
            mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_1_title));
            mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_2_title));

            mTabLayout.addOnTabSelectedListener(this);
            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

            getActivity().findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    //interface das tabs
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int i=tab.getPosition();
        if(i==0){MainNavigationDrawerActivity activity =(MainNavigationDrawerActivity) getActivity();
            activity.findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);
            if(activity.searchItem!=null){
                activity.searchItem.setVisible(false);}
            else{activity.setTabselected(i);}
        }
        else{
            MainNavigationDrawerActivity activity =(MainNavigationDrawerActivity) getActivity();
            activity.findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);
            if(activity.searchItem!=null){
                activity.searchItem.setVisible(true);}
            else{activity.setTabselected(i);}
        }
        mViewPager.setCurrentItem(i);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    public void performClick(int i){
        mViewPager.setCurrentItem(i);
    }
    public int getCurentTab(){
        return mViewPager.getCurrentItem();
    }
}
