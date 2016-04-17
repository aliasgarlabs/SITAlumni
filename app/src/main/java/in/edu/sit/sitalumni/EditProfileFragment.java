package in.edu.sit.sitalumni;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by aliasgarmurtaza on 16/04/16.
 */
public class EditProfileFragment extends Fragment {
    View rootView;
    ProgressBar progressBar;
    private AutoCompleteTextView mEmailView;
    private EditText editTextName;
    private EditText batch;
    private EditText branch;
    private EditText programme;
    private RadioButton male, female;
    private Button save;
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            setDateLocal(year, monthOfYear, dayOfMonth);


        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (container != null) {
            container.removeAllViews();
        }

        rootView = inflater.inflate(R.layout.editprofile_fragment, container, true);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_edit);
        progressBar.setVisibility(View.VISIBLE);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) rootView.findViewById(R.id.email);


        editTextName = (EditText) rootView.findViewById(R.id.Name);

        male = (RadioButton) rootView.findViewById(R.id.male);
        female = (RadioButton) rootView.findViewById(R.id.female);

        batch = (EditText) rootView.findViewById(R.id.batch);
        branch = (EditText) rootView.findViewById(R.id.branch);
        programme = (EditText) rootView.findViewById(R.id.programme);
        save = (Button) rootView.findViewById(R.id.confirm);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("data", 0);
                String uid = sharedPreferences.getString("uid", "0000");

                Firebase userRef = new Firebase("https://sitalumni.firebaseio.com/users/" + uid);
                userRef.child("name").setValue(editTextName.getText().toString());
                userRef.child("email").setValue(mEmailView.getText().toString());
                if (male.isChecked())
                    userRef.child("gender").setValue("Male");
                else
                    userRef.child("gender").setValue("Female");

                userRef.child("batch").setValue(batch.getText().toString());
                userRef.child("branch").setValue(branch.getText().toString());
                userRef.child("programme").setValue(programme.getText().toString());

                Toast.makeText(getActivity(), "Saved Successfully!", Toast.LENGTH_LONG).show();

            }
        });

        display();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void display() {
        //TODO get feedlist
        Firebase postRef = new Firebase("https://sitalumni.firebaseio.com/");
        postRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = null;

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    user = postSnapshot.getValue(User.class);

                }
                if (user != null)
                    populate(user);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });


    }

    private void populate(User user) {
        //TODO

        batch.setText(user.getBatch());
        branch.setText(user.getBranch());
        mEmailView.setText(user.getEmail());
        editTextName.setText(user.getName());
        programme.setText(user.getProgramme());

        if (user.getGender().equals("Male")) {
            male.setChecked(true);
            female.setChecked(false);
        } else {
            male.setChecked(false);
            female.setChecked(true);
        }


        //setDateLocal(year,month,date);
    }

    public void setDateLocal(int year, int monthOfYear, int dayOfMonth) {

//        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//        c.set(Calendar.MONTH, monthOfYear);
//        c.set(Calendar.YEAR, year);
//
//
//        date = "" + Utility.getMonth(monthOfYear) + " " + dayOfMonth + ", " + +year;
//        birthdate.setText("" + date);


    }

}

