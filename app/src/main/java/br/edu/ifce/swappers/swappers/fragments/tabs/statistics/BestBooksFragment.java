package br.edu.ifce.swappers.swappers.fragments.tabs.statistics;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.edu.ifce.swappers.swappers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BestBooksFragment extends Fragment {


    public BestBooksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_best_books, container, false);
    }


}
