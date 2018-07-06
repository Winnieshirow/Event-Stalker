package com.outer.fragments.drawer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.events.shirow.eventstalker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarHire_Fragment extends Fragment {


    public CarHire_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_car_hire_, container, false);

        return view;
    }

}
