package com.poos.cop.schedulebuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ScheduleResultsFragment extends Fragment {

    public TextView textViewResultsMessage;
    public LinearLayout linearLayoutScheduleResults;

    public ArrayList<Schedule> schedules;
    public final static int MAX_SCHEDULES = 30;

    public ScheduleResultsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_schedule_results, container, false);

        // Get all of the text views
        textViewResultsMessage = (TextView) view.findViewById(R.id.textViewResultsMessage);

        linearLayoutScheduleResults = (LinearLayout) view.findViewById(R.id.linearLayoutScheduleResults);

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

    public void setSchedules(ArrayList<Schedule> schedules) {
        this.schedules = schedules;
    }

    private void updateContent() {

        if(textViewResultsMessage != null) {
            if(schedules == null || schedules.size() == 0)
                textViewResultsMessage.setText("Unfortunately no schedules matched your search.");
            else {
                int n = schedules.size();
                if(n <= MAX_SCHEDULES)
                    textViewResultsMessage.setText(String.format("%,d schedule%s matched your search.", n, n > 1 ? "s" : ""));
                else
                    textViewResultsMessage.setText(String.format("%,d schedule%s matched your search but only showing the first %d results.", n, n > 1 ? "s" : "", MAX_SCHEDULES));
            }
        }

        if(schedules != null && linearLayoutScheduleResults != null) {
            linearLayoutScheduleResults.removeAllViews();
            for (int i = 0; i < schedules.size() && i < MAX_SCHEDULES; i++) {
                Schedule s = schedules.get(i);

                final View view = View.inflate(getContext(), R.layout.layout_schedule_result, null);

                view.setTag(s);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object o = v.getTag();
                        if (o == null || !(o instanceof Schedule))
                            return;
                        Schedule s = (Schedule) o;
                        getMainActivity().viewSchedule(s);
                    }
                });

                updateScheduleResultView(view);

                linearLayoutScheduleResults.addView(view);
            }
        }
    }

    public void updateScheduleResultView(View view) {
        Object o = view.getTag();
        if(o == null || !(o instanceof Schedule))
            return;
        Schedule s = (Schedule) o;


        TextView textViewNumClasses = (TextView) view.findViewById(R.id.textViewNumClasses);
        TextView textViewTotalUnits = (TextView) view.findViewById(R.id.textViewTotalUnits);
        TextView textViewStatus = (TextView) view.findViewById(R.id.textViewStatus);
        TextView textViewInstructionModes = (TextView) view.findViewById(R.id.textViewInstructionModes);

        textViewNumClasses.setText(s.getScheduleInfoString());
        textViewTotalUnits.setText(String.format("%d credit hour%s", s.getTotalUnits(), s.getTotalUnits() != 1 ? "s" : ""));
        textViewStatus.setText(s.getStatusString());
        textViewInstructionModes.setText(s.getInstructionModesString());
    }
}