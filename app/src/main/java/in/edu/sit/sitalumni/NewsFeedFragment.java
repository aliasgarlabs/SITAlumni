package in.edu.sit.sitalumni;

import android.app.Dialog;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.Inflater;

/**
 * Created by aliasgarmurtaza on 16/04/16.
 */
public class NewsFeedFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerViewAdapter mAdapter;
    ArrayList <Feed> feedlist;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.newsfeed_fragment, container, false);

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

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private  void showDialog()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.newsfeed_dialog);


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
                String uid = sharedPreferences.getString("uid","0000");

                Calendar calendar = Calendar.getInstance();

                Firebase userRef = new Firebase("https://sitalumni.firebaseio.com/newsfeed");
                String postid = uid+(calendar.getTimeInMillis()%1000);
                userRef.child("postid").setValue(postid);
                Firebase postRef = new Firebase("https://sitalumni.firebaseio.com/newsfeed/"+postid);
                postRef.child("uid").setValue(uid);
                postRef.child("name").setValue(name);
                postRef.child("Date").setValue(calendar.toString());
                postRef.child("Message").setValue(messageString);
            }
        });
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //Do nothing
            }
        });
    }
}
