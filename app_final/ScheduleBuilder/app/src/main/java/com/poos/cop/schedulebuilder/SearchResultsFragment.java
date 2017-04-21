package com.poos.cop.schedulebuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchResultsFragment extends Fragment {

    public TextView textViewResultsMessage;
    public LinearLayout linearLayoutSearchResults;

    public ArrayList<Section> results;

    public SearchResultsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Create the view
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);

        // Get all of the text views
        textViewResultsMessage = (TextView) view.findViewById(R.id.textViewResultsMessage);

        linearLayoutSearchResults = (LinearLayout) view.findViewById(R.id.linearLayoutSearchResults);

        restoreFromState(savedInstanceState);

        // Update the content
        updateContent();

        return view;
    }

    public void restoreFromState(Bundle savedInstanceState) {
        if(savedInstanceState == null) return;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        /*
        if (v.getId() == R.id.linearLayoutSearchResults) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

            System.out.println("Hello =)");

        }*/
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    public void setResults(ArrayList<Section> results) {
        this.results = results;
    }

    private void updateContent() {
        if(textViewResultsMessage != null) {
            if(results == null || results.size() == 0)
                textViewResultsMessage.setText("Unfortunately no classes matched your search.");
            else {
                int n = results.size();
                if(n <= 100)
                    textViewResultsMessage.setText(String.format("%,d class section%s matched your search.", n, n > 1 ? "s" : ""));
                else
                    textViewResultsMessage.setText(String.format("%,d class section%s matched your search but only showing the first 100 results.", n, n > 1 ? "s" : ""));
            }
        }

        if(results != null && linearLayoutSearchResults != null) {
            linearLayoutSearchResults.removeAllViews();
            for (int i = 0; i < results.size() && i < 100; i++) {
                Section s = results.get(i);

                final View view = View.inflate(getContext(), R.layout.layout_search_result, null);

                ImageView imageViewCartStatus = (ImageView) view.findViewById(R.id.imageViewCartStatus);
                imageViewCartStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object o = view.getTag();
                        if (o == null || !(o instanceof Section))
                            return;
                        Section s = (Section) o;
                        getMainActivity().toggleSectionInCart(s);
                        updateSearchResultView(view);
                    }
                });

                view.setTag(s);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object o = v.getTag();
                        if (o == null || !(o instanceof Section))
                            return;
                        Section s = (Section) o;
                        getMainActivity().viewClassDetails(s);
                    }
                });

                updateSearchResultView(view);

                linearLayoutSearchResults.addView(view);
            }
        }
    }

    public void updateSearchResultView(View view) {
        Object o = view.getTag();
        if(o == null || !(o instanceof Section))
            return;
        Section s = (Section) o;

        TextView textViewClassTitle = (TextView) view.findViewById(R.id.textViewClassTitle);
        TextView textViewInstructor = (TextView) view.findViewById(R.id.textViewInstructor);
        TextView textViewInstructionMode = (TextView) view.findViewById(R.id.textViewInstructionMode);
        TextView textViewMeetingInformation = (TextView) view.findViewById(R.id.textViewMeetingInformation);

        ImageView imageViewCartStatus = (ImageView) view.findViewById(R.id.imageViewCartStatus);
        if(s.inCart)
            imageViewCartStatus.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_in_cart));
        else
            imageViewCartStatus.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_not_in_cart));

        textViewClassTitle.setText(String.format("%s %s - %s", s.subjectStr, s.courseNumberStr, s.courseName));
        textViewInstructor.setText(s.instructorStr);
        textViewInstructionMode.setText(s.instructionMode);
        textViewMeetingInformation.setText(String.format("%s   %s", s.room, s.meetingTimes));
    }
}