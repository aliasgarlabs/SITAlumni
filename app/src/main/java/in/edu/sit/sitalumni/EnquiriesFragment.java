package in.edu.sit.sitalumni;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
public class EnquiriesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        View rootView = inflater.inflate(R.layout.enquiry_fragment, container, true);

        Button submit = (Button) rootView.findViewById(R.id.button_post);
        final EditText subject = (EditText) rootView.findViewById(R.id.message_edittext);
        final EditText enquiry = (EditText) rootView.findViewById(R.id.enquiry_edittext);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectString = subject.getText().toString();
                String enquiryString = enquiry.getText().toString();
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
                Firebase postRef = new Firebase("https://sitalumni.firebaseio.com/Enquiry/"+postid);
                postRef.child("uid").setValue(uid);
                postRef.child("name").setValue(name);
                postRef.child("date").setValue(readableDate);
                postRef.child("enquiry").setValue(enquiryString);
                postRef.child("subject").setValue(subjectString);

                Toast.makeText(getActivity(), "Thank you for your enquiry! Someone from the team will get back to you shortly.", Toast.LENGTH_LONG).show();

                subject.setText("");
                enquiry.setText("");

            }
        });


        return super.onCreateView(inflater, container, savedInstanceState);
    }


}
