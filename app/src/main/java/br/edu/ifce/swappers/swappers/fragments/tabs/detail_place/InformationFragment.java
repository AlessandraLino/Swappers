package br.edu.ifce.swappers.swappers.fragments.tabs.detail_place;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.edu.ifce.swappers.swappers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment {


    public InformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_information, container, false);

        TextView streetAddress       = (TextView) rootView.findViewById(R.id.street_text_view);
        TextView neighborhoodAndCity = (TextView) rootView.findViewById(R.id.neighborhood_and_city_text_view);
        TextView stateAndCountry     = (TextView) rootView.findViewById(R.id.state_and_country_text_view);
        TextView businessHoursDescription = (TextView) rootView.findViewById(R.id.business_hours_description_text_view);

        return rootView;
    }
}
