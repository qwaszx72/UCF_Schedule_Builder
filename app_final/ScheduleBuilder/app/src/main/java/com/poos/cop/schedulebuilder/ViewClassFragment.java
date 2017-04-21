package com.poos.cop.schedulebuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewClassFragment extends Fragment {

    public TextView textViewClassTitle, textViewClassKeyDescription, textViewStatus, textViewNbr,
            textViewUnits, textViewInstructionMode, textViewLocation, textViewInstructor,
            textViewDaysAndTimes, textViewRoom, textViewMeetingDates, textViewCapacity,
            textViewEnrollmentTotal, textViewAvailableSeats, textViewWaitListCapacity,
            textViewWaitListTotal, textViewDescription, textViewTerm;

    public Section section;

    public final static String STATE_SECTION_JSON = "section_json";

    public ViewClassFragment() {
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
        View view = inflater.inflate(R.layout.fragment_view_class, container, false);

        // Get all of the text views
        textViewClassTitle = (TextView) view.findViewById(R.id.textViewClassTitle);
        textViewClassKeyDescription = (TextView) view.findViewById(R.id.textViewClassKeyDescription);
        textViewStatus = (TextView) view.findViewById(R.id.textViewStatus);
        textViewNbr = (TextView) view.findViewById(R.id.textViewNbr);
        textViewUnits = (TextView) view.findViewById(R.id.textViewUnits);
        textViewInstructionMode = (TextView) view.findViewById(R.id.textViewInstructionMode);
        textViewLocation = (TextView) view.findViewById(R.id.textViewLocation);
        textViewTerm = (TextView) view.findViewById(R.id.textViewTerm);
        textViewInstructor = (TextView) view.findViewById(R.id.textViewInstructor);
        textViewDaysAndTimes = (TextView) view.findViewById(R.id.textViewDaysAndTimes);
        textViewRoom = (TextView) view.findViewById(R.id.textViewRoom);
        textViewMeetingDates = (TextView) view.findViewById(R.id.textViewMeetingDates);
        textViewCapacity = (TextView) view.findViewById(R.id.textViewCapacity);
        textViewEnrollmentTotal = (TextView) view.findViewById(R.id.textViewEnrollmentTotal);
        textViewAvailableSeats = (TextView) view.findViewById(R.id.textViewAvailableSeats);
        textViewWaitListCapacity = (TextView) view.findViewById(R.id.textViewWaitListCapacity);
        textViewWaitListTotal = (TextView) view.findViewById(R.id.textViewWaitListTotal);
        textViewDescription = (TextView) view.findViewById(R.id.textViewDescription);

        restoreFromState(savedInstanceState);

        // Update the content
        updateContent();

        return view;
    }

    public void restoreFromState(Bundle savedInstanceState) {
        if(savedInstanceState == null) return;

        String sectionJson = savedInstanceState.getString(STATE_SECTION_JSON);
        if(sectionJson != null) {
            setSection(new Section(sectionJson));
            updateContent();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(section != null)
            outState.putString(STATE_SECTION_JSON, Section.toJson(section));
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    public void setSection(Section section) {
        this.section = section;
    }

    private void updateContent() {
        if(section == null) return;
        if(textViewClassTitle != null)
            textViewClassTitle.setText(String.format("%s %s - %s", section.subjectStr, section.courseNumberStr, section.courseName));
        if(textViewClassKeyDescription != null)
            textViewClassKeyDescription.setText("" + section.keyDescription);
        if(textViewStatus != null)
            textViewStatus.setText("" + section.status);
        if(textViewNbr != null)
            textViewNbr.setText("" + section.nbr);
        if(textViewUnits != null)
            textViewUnits.setText("" + section.units + " credit hours");
        if(textViewInstructionMode != null)
            textViewInstructionMode.setText("" + section.instructionMode);
        if(textViewLocation != null)
            textViewLocation.setText("" + section.location);
        if(textViewTerm != null)
            textViewTerm.setText("" + section.term);
        if(textViewInstructor != null)
            textViewInstructor.setText("" + section.instructorStr);
        if(textViewDaysAndTimes != null)
            textViewDaysAndTimes.setText("" + section.meetingTimes);
        if(textViewRoom != null)
            textViewRoom.setText("" + section.room);
        if(textViewMeetingDates != null)
            textViewMeetingDates.setText("" + section.meetingDates);
        if(textViewCapacity != null)
            textViewCapacity.setText("" + section.capacity);
        if(textViewEnrollmentTotal != null)
            textViewEnrollmentTotal.setText("" + section.enrollmentTotal);
        if(textViewAvailableSeats != null)
            textViewAvailableSeats.setText("" + section.availableSeats);
        if(textViewWaitListCapacity != null)
            textViewWaitListCapacity.setText("" + section.waitlistCapacity);
        if(textViewWaitListTotal != null)
            textViewWaitListTotal.setText("" + section.waitlistTotal);
        if(textViewDescription != null)
            textViewDescription.setText("" + section.description);
    }
}