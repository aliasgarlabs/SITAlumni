package in.edu.sit.sitalumni;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by aliasgarmurtaza on 16/04/16.
 */
public class FeedbackFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        View rootView = inflater.inflate(R.layout.feedback_fragment, container, true);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        Button submit = (Button) rootView.findViewById(R.id.button_post);
        final EditText feedback = (EditText) rootView.findViewById(R.id.message_edittext);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageString = feedback.getText().toString();
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
                Firebase postRef = new Firebase("https://sitalumni.firebaseio.com/Feedback/"+postid);
                postRef.child("uid").setValue(uid);
                postRef.child("name").setValue(name);
                postRef.child("date").setValue(readableDate);
                postRef.child("message").setValue(messageString);

                Toast.makeText(getActivity(),"Thank you for your feedback!", Toast.LENGTH_LONG).show();

                feedback.setText("");

            }
        });


        return super.onCreateView(inflater, container, savedInstanceState);
    }


}
