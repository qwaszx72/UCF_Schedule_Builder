package com.poos.cop.schedulebuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewScheduleFragment extends Fragment {

    public TextView textViewScheduleTitle, textViewStatus, textViewNumClasses, textViewTotalUnits,
            textViewInstructionModes, textViewLocations, textViewTerm;

    public LinearLayout linearLayoutSections;

    public Schedule schedule;

    public ArrayList<Section> sections;

    public ViewScheduleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        View view = inflater.inflate(R.layout.fragment_view_schedule, container, false);

        // Get all of the text views
        textViewScheduleTitle = (TextView) view.findViewById(R.id.textViewScheduleTitle);
        textViewStatus = (TextView) view.findViewById(R.id.textViewStatus);
        textViewNumClasses = (TextView) view.findViewById(R.id.textViewNumClasses);
        textViewTotalUnits = (TextView) view.findViewById(R.id.textViewTotalUnits);
        textViewInstructionModes = (TextView) view.findViewById(R.id.textViewInstructionModes);
        textViewLocations = (TextView) view.findViewById(R.id.textViewLocations);
        textViewTerm = (TextView) view.findViewById(R.id.textViewTerm);

        linearLayoutSections = (LinearLayout) view.findViewById(R.id.linearLayoutSections);

        restoreFromState(savedInstanceState);

        // Update the content
        updateContent();

        return view;
    }

    public void restoreFromState(Bundle savedInstanceState) {
        if(savedInstanceState == null) return;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_export_nbrs) {
            exportNbrs();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.view_schedule, menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
        this.sections = schedule.getSections();
    }

    private void exportNbrs() {
        getMainActivity().exportNbrs(schedule);
    }

    private void updateContent() {
        if(schedule == null) return;

        if(textViewScheduleTitle != null)
            textViewScheduleTitle.setText(String.format("Potential Schedule"));
        if(textViewStatus != null)
            textViewStatus.setText("" + schedule.getStatusString());
        if(textViewNumClasses != null)
            textViewNumClasses.setText("" + schedule.getNumSections());
        if(textViewTotalUnits != null)
            textViewTotalUnits.setText("" + schedule.getTotalUnits() + " credit hours");
        if(textViewInstructionModes != null)
            textViewInstructionModes.setText("" + schedule.getInstructionModesString());
        if(textViewLocations != null)
            textViewLocations.setText("" + schedule.getLocationsString());
        if(textViewTerm != null)
            textViewTerm.setText("" + schedule.getTerm());

        if(sections != null && linearLayoutSections != null) {
            linearLayoutSections.removeAllViews();
            for (int i = 0; i < sections.size() && i < 100; i++) {
                Section s = sections.get(i);

                final View view = View.inflate(getContext(), R.layout.layout_search_result, null);

                ImageView imageViewCartStatus = (ImageView) view.findViewById(R.id.imageViewCartStatus);
                imageViewCartStatus.setVisibility(View.GONE);

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

                updateSectionView(view);

                linearLayoutSections.addView(view);
            }
        }
    }

    public void updateSectionView(View view) {
        Object o = view.getTag();
        if(o == null || !(o instanceof Section))
            return;
        Section s = (Section) o;

        TextView textViewClassTitle = (TextView) view.findViewById(R.id.textViewClassTitle);
        TextView textViewInstructor = (TextView) view.findViewById(R.id.textViewInstructor);
        TextView textViewInstructionMode = (TextView) view.findViewById(R.id.textViewInstructionMode);
        TextView textViewMeetingInformation = (TextView) view.findViewById(R.id.textViewMeetingInformation);

        ImageView imageViewCartStatus = (ImageView) view.findViewById(R.id.imageViewCartStatus);
        imageViewCartStatus.setVisibility(View.GONE);

        textViewClassTitle.setText(String.format("%s %s - %s", s.subjectStr, s.courseNumberStr, s.courseName));
        textViewInstructor.setText(s.instructorStr);
        textViewInstructionMode.setText(s.instructionMode);
        textViewMeetingInformation.setText(String.format("%s   %s", s.room, s.meetingTimes));
    }
}