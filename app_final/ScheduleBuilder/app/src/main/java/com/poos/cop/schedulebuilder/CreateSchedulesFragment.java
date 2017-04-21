package com.poos.cop.schedulebuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class CreateSchedulesFragment extends Fragment {

    public Spinner[] spinnerEarliestTimes, spinnerLatestTimes;
    public CheckBox[] checkBoxes;
    public CheckBox checkBoxOpenOnly;

    public Button buttonCreateSchedules;

    public CreateSchedulesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ClassUtils.initCourses(getActivity().getApplicationContext());

        // Create the view
        View view = inflater.inflate(R.layout.fragment_create_schedules, container, false);

        // Get all of the input views
        spinnerEarliestTimes = new Spinner[5];
        spinnerEarliestTimes[0] = (Spinner) view.findViewById(R.id.spinnerEarliestTimeMonday);
        spinnerEarliestTimes[1] = (Spinner) view.findViewById(R.id.spinnerEarliestTimeTuesday);
        spinnerEarliestTimes[2] = (Spinner) view.findViewById(R.id.spinnerEarliestTimeWednesday);
        spinnerEarliestTimes[3] = (Spinner) view.findViewById(R.id.spinnerEarliestTimeThursday);
        spinnerEarliestTimes[4] = (Spinner) view.findViewById(R.id.spinnerEarliestTimeFriday);

        spinnerLatestTimes = new Spinner[5];
        spinnerLatestTimes[0] = (Spinner) view.findViewById(R.id.spinnerLatestTimeMonday);
        spinnerLatestTimes[1] = (Spinner) view.findViewById(R.id.spinnerLatestTimeTuesday);
        spinnerLatestTimes[2] = (Spinner) view.findViewById(R.id.spinnerLatestTimeWednesday);
        spinnerLatestTimes[3] = (Spinner) view.findViewById(R.id.spinnerLatestTimeThursday);
        spinnerLatestTimes[4] = (Spinner) view.findViewById(R.id.spinnerLatestTimeFriday);

        checkBoxes = new CheckBox[5];
        checkBoxes[0] = (CheckBox) view.findViewById(R.id.checkBoxMonday);
        checkBoxes[1] = (CheckBox) view.findViewById(R.id.checkBoxTuesday);
        checkBoxes[2] = (CheckBox) view.findViewById(R.id.checkBoxWednesday);
        checkBoxes[3] = (CheckBox) view.findViewById(R.id.checkBoxThursday);
        checkBoxes[4] = (CheckBox) view.findViewById(R.id.checkBoxFriday);

        checkBoxOpenOnly = (CheckBox) view.findViewById(R.id.checkBoxOpenOnly);

        buttonCreateSchedules = (Button) view.findViewById(R.id.buttonCreateSchedules);

        for(int i = 0; i < 5; i++) {
            ArrayAdapter<CharSequence> earliestTimeAdapter = ArrayAdapter.createFromResource(getContext(), R.array.earliest_times_array, android.R.layout.simple_spinner_item);
            earliestTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            if(spinnerEarliestTimes != null && spinnerEarliestTimes[i] != null)
                spinnerEarliestTimes[i].setAdapter(earliestTimeAdapter);

            ArrayAdapter<CharSequence> latestTimeAdapter = ArrayAdapter.createFromResource(getContext(), R.array.latest_times_array, android.R.layout.simple_spinner_item);
            latestTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            if(spinnerLatestTimes != null && spinnerLatestTimes[i] != null) {
                spinnerLatestTimes[i].setAdapter(latestTimeAdapter);
                spinnerLatestTimes[i].setSelection(latestTimeAdapter.getCount() - 1);
            }

            final int dayNumber = i;

            if(checkBoxes != null && checkBoxes[i] != null)
                checkBoxes[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDayCheckboxChanged(dayNumber);
                    }
                });
        }

        buttonCreateSchedules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeScheduleSearch();
            }
        });

        // Update the content
        updateContent();

        return view;
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    private void updateContent() {
        for(int i = 0; i < 5; i++)
            onDayCheckboxChanged(i);
    }

    private void onDayCheckboxChanged(int day) {
        if(checkBoxes == null || checkBoxes[day] == null)
            return;
        if(checkBoxes[day].isChecked()) {
            if(spinnerEarliestTimes != null && spinnerEarliestTimes[day] != null)
                spinnerEarliestTimes[day].setEnabled(true);
            if(spinnerLatestTimes != null && spinnerLatestTimes[day] != null)
                spinnerLatestTimes[day].setEnabled(true);
        } else {
            if(spinnerEarliestTimes != null && spinnerEarliestTimes[day] != null)
                spinnerEarliestTimes[day].setEnabled(false);
            if(spinnerLatestTimes != null && spinnerLatestTimes[day] != null)
                spinnerLatestTimes[day].setEnabled(false);
        }
    }


    private void executeScheduleSearch() {
        ScheduleQuery scheduleQuery = getScheduleQuery();
        getMainActivity().executeScheduleSearch(scheduleQuery);
    }

    public ScheduleQuery getScheduleQuery() {
        ScheduleQuery scheduleQuery = new ScheduleQuery();
        for(int i = 0; i < 5; i++) {
            if(checkBoxes == null || checkBoxes[i] == null)
                continue;
            if(!checkBoxes[i].isChecked()) {
                scheduleQuery.setEarliestStartTime(i, "11:59 PM");
                scheduleQuery.setLatestEndTime(i, "12:00 AM");
            } else {
                if(spinnerEarliestTimes[i] != null && spinnerEarliestTimes[i].getSelectedItem() != null)
                    scheduleQuery.setEarliestStartTime(i, spinnerEarliestTimes[i].getSelectedItem().toString());
                if(spinnerLatestTimes[i] != null && spinnerLatestTimes[i].getSelectedItem() != null)
                    scheduleQuery.setLatestEndTime(i, spinnerLatestTimes[i].getSelectedItem().toString());
            }
        }
        if(checkBoxOpenOnly != null)
            scheduleQuery.setOpenOnly(checkBoxOpenOnly.isChecked());
        return scheduleQuery;
    }

}