package in.edu.sit.sitalumni;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Created by aliasgarmurtaza on 16/04/16.
 */
public class NewsFeedFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerViewAdapter mAdapter;
    ArrayList <Newsfeed> feedlist;
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        View rootView = inflater.inflate(R.layout.newsfeed_fragment, container, true);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_newsfeed);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        feedlist = new ArrayList<>();
        //TODO get feedlist from firebase



        mAdapter = new RecyclerViewAdapter(feedlist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        display();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void display()
    {
        //TODO get feedlist
        Firebase postRef = new Firebase("https://sitalumni.firebaseio.com/");
        postRef.child("Newsfeed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                feedlist = parseFeedList(snapshot);  //prints "Do you have data? You'll love Firebase."
                mAdapter = new RecyclerViewAdapter(feedlist);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });


    }

    public ArrayList<Newsfeed> parseFeedList(DataSnapshot dataSnapshot) {

        ArrayList<Newsfeed> feedArrayList = new ArrayList<>();

            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                Newsfeed feed = postSnapshot.getValue(Newsfeed.class);
                feedArrayList.add(feed);
            }

        return feedArrayList;
    }


    private  void showDialog()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.newsfeed_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        final EditText message_edittext = (EditText) dialog.findViewById(R.id.message_edittext);

        dialog.show();

        Button declineButton = (Button) dialog.findViewById(R.id.button_cancel);
        final Button postButton = (Button) dialog.findViewById(R.id.button_post);
        // if decline button is clicked, close the custom dialog

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO send data to server
                String messageString = message_edittext.getText().toString();
                SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("data", 0);
                String name = sharedPreferences.getString("name", "Anonymous");
                String uid = sharedPreferences.getString("uid", "0000");

                Calendar calendar = Calendar.getInstance();
                int date = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH)+1;
                int year = calendar.get(Calendar.YEAR);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                int sec = calendar.get(Calendar.SECOND);

                String time = ""+sec+min+hour+date+month+year;
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String readableDate = sdf.format(calendar.getTime());

                String postid = uid+time;
                Firebase postRef = new Firebase("https://sitalumni.firebaseio.com/Newsfeed/"+postid);
                postRef.child("uid").setValue(uid);
                postRef.child("name").setValue(name);
                postRef.child("date").setValue(readableDate);
                postRef.child("message").setValue(messageString);

                dialog.dismiss();


            }
        });
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //Do nothing
                dialog.dismiss();
            }
        });
    }
}
