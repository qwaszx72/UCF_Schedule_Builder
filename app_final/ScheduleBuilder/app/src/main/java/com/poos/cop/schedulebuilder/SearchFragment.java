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

public class SearchFragment extends Fragment {

    public Spinner spinnerTerm, spinnerLocation, spinnerModeOfInstruction;
    public AutoCompleteTextView autoCompleteTextViewSubject, autoCompleteTextViewCourseNumber, autoCompleteTextViewCourseName;
    public EditText editTextNbr, editTextInstructorFirstName, editTextInstructorLastName;
    public CheckBox checkBoxOpenOnly;
    public Button buttonSearch;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ClassUtils.initCourses(getActivity().getApplicationContext());

        // Create the view
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Get all of the input views
        autoCompleteTextViewSubject = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextViewSubject);
        autoCompleteTextViewCourseNumber = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextViewCourseNumber);
        autoCompleteTextViewCourseName = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextViewCourseName);

        spinnerTerm = (Spinner) view.findViewById(R.id.spinnerTerm);
        spinnerLocation = (Spinner) view.findViewById(R.id.spinnerLocation);
        spinnerModeOfInstruction = (Spinner) view.findViewById(R.id.spinnerModeOfInstruction);

        editTextNbr = (EditText) view.findViewById(R.id.editTextNbr);
        editTextInstructorFirstName = (EditText) view.findViewById(R.id.editTextInstructorFirstName);
        editTextInstructorLastName = (EditText) view.findViewById(R.id.editTextInstructorLastName);

        checkBoxOpenOnly = (CheckBox) view.findViewById(R.id.checkBoxOpenOnly);

        buttonSearch = (Button) view.findViewById(R.id.buttonSearch);

        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, ClassUtils.SUBJECTS);
        autoCompleteTextViewSubject.setAdapter(subjectAdapter);
        autoCompleteTextViewSubject.setThreshold(1);

        autoCompleteTextViewCourseNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) return;

                String subject = autoCompleteTextViewSubject.getText().toString().toUpperCase();
                String[] arr = ClassUtils.COURSE_NUMBERS_MAP.get(subject);
                if(arr == null) {
                    autoCompleteTextViewCourseNumber.setAdapter(null);
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, arr);
                    autoCompleteTextViewCourseNumber.setAdapter(adapter);
                }

            }
        });
        autoCompleteTextViewCourseNumber.setThreshold(1);

        autoCompleteTextViewCourseName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) return;

                String subject = autoCompleteTextViewSubject.getText().toString().toUpperCase();
                String[] arr = ClassUtils.COURSE_NAMES_MAP.get(subject);
                if(arr == null) {
                    autoCompleteTextViewCourseName.setAdapter(null);
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, arr);
                    autoCompleteTextViewCourseName.setAdapter(adapter);
                }

            }
        });
        autoCompleteTextViewCourseName.setThreshold(2);

        ArrayAdapter<CharSequence> termAdapter = ArrayAdapter.createFromResource(getContext(), R.array.terms_array, android.R.layout.simple_spinner_item);
        termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTerm.setAdapter(termAdapter);
        if(spinnerTerm.getSelectedItemPosition() < 0)
            spinnerTerm.setSelection(0);

        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(getContext(), R.array.locations_array, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(locationAdapter);

        ArrayAdapter<CharSequence> modeOfInstructionAdapter = ArrayAdapter.createFromResource(getContext(), R.array.modes_of_instruction_array, android.R.layout.simple_spinner_item);
        modeOfInstructionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModeOfInstruction.setAdapter(modeOfInstructionAdapter);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeSearch();
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
        //if(textViewClassTitle != null)
        //    textViewClassTitle.setText("" + section.name);

    }

    public void executeSearch() {
        final SearchQuery searchQuery = createSearchQuery();
        if(searchQuery == null) {
            //TODO Insert error message
            return;
        }
        getMainActivity().executeSearch(searchQuery);
    }

    public SearchQuery createSearchQuery() {
        if(spinnerTerm == null) return null;
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setNbr(editTextNbr.getText().toString());
        if(spinnerTerm.getSelectedItem() != null)
            searchQuery.setTerm(spinnerTerm.getSelectedItem().toString());
        searchQuery.setSubject(autoCompleteTextViewSubject.getText().toString());
        searchQuery.setCourseNumber(autoCompleteTextViewCourseNumber.getText().toString());
        searchQuery.setCourseName(autoCompleteTextViewCourseName.getText().toString());
        if(spinnerLocation.getSelectedItem() != null)
            searchQuery.setLocation(spinnerLocation.getSelectedItem().toString());
        if(spinnerModeOfInstruction.getSelectedItem() != null)
            searchQuery.setModeOfInstruction(spinnerModeOfInstruction.getSelectedItem().toString());
        searchQuery.setInstructorFirstName(editTextInstructorFirstName.getText().toString());
        searchQuery.setInstructorLastName(editTextInstructorLastName.getText().toString());
        searchQuery.setOpenClassesOnly(checkBoxOpenOnly.isChecked());
        searchQuery.setIncludeLabs(false);

        return searchQuery;
    }

}