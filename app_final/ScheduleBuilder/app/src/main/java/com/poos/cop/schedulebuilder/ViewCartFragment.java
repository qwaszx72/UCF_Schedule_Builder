package com.poos.cop.schedulebuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.ContextMenu;
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

public class ViewCartFragment extends Fragment {

    public TextView textViewCartMessage;
    public LinearLayout linearLayoutCartContents;

    public ArrayList<Section> cartContents;

    public ViewCartFragment() {
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
        View view = inflater.inflate(R.layout.fragment_view_cart, container, false);

        // Get all of the text views
        textViewCartMessage = (TextView) view.findViewById(R.id.textViewCartMessage);

        linearLayoutCartContents = (LinearLayout) view.findViewById(R.id.linearLayoutCartContents);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear_cart) {
            clearCart();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.view_cart, menu);
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    public void clearCart() {
        getMainActivity().clearCart();
        if(cartContents != null) {
            for(Section s : cartContents)
                s.inCart = false;
            cartContents.clear();
        }
        updateContent();
    }

    public void setCartContents(ArrayList<Section> cartContents) {
        this.cartContents = cartContents;
    }

    private void updateCartMessage() {
        if(textViewCartMessage != null) {
            if(cartContents == null || cartContents.size() == 0)
                textViewCartMessage.setText("There are no classes in your cart! Search for some and add them to your cart to create a schedule.");
            else {
                int n = cartContents.size();
                textViewCartMessage.setText(String.format("%,d class section%s are currently in your cart.", n, n > 1 ? "s" : ""));
            }
        }
    }

    private void updateContent() {
        updateCartMessage();
        if(cartContents != null && linearLayoutCartContents != null) {
            linearLayoutCartContents.removeAllViews();
            for (int i = 0; i < cartContents.size(); i++) {
                Section s = cartContents.get(i);

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

                linearLayoutCartContents.addView(view);
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

        if(!s.inCart && linearLayoutCartContents != null) {
            linearLayoutCartContents.removeView(view);
            cartContents.remove(s);
            updateCartMessage();
        }
    }
}