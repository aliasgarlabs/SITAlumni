package in.edu.sit.sitalumni;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Calendar;
import java.util.Map;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    boolean shouldProceed = false;
    ProgressDialog dialog;
    String date;
    private UserLoginTask mAuthTask = null;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private View mLoginFormView;
    private CheckBox checkboxStudent;
    private EditText editTextSchoolName;
    private EditText editTextName;
    private TextView alreadySignUp;
    private TextView gender;
    private RadioGroup genderGroup;
    private boolean isSignedUp = true;
    private View inFocusView;
    private TextView birthdate;
    private TextView birthday;
    private RadioButton male, female;
    private Calendar c;
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            setDateLocal(year, monthOfYear, dayOfMonth);


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);


        gender = (TextView) findViewById(R.id.gender);
        genderGroup = (RadioGroup) findViewById(R.id.genderGroup);

        editTextSchoolName = (EditText) findViewById(R.id.School_Name);
        editTextName = (EditText) findViewById(R.id.Name);

        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);

        birthdate = (TextView) findViewById(R.id.birthdate);
        birthday = (TextView) findViewById(R.id.birthdayString);
        birthdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);


            }
        });

        c = Calendar.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);


        mPasswordView = (EditText) findViewById(R.id.password);
        mConfirmPasswordView = (EditText) findViewById(R.id.confirmPassword);
        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        final Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);


        checkboxStudent = (CheckBox) findViewById(R.id.checkbox_student);
        editTextSchoolName = (EditText) findViewById(R.id.School_Name);
        checkboxStudent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    editTextSchoolName.setVisibility(View.VISIBLE);
                else
                    editTextSchoolName.setVisibility(View.GONE);
            }
        });

        alreadySignUp = (TextView) findViewById(R.id.alreadySignUp);

        inFocusView = getCurrentFocus();
        alreadySignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!isSignedUp) {
                    isSignedUp = true;
                    editTextSchoolName.setVisibility(View.GONE);
                    mConfirmPasswordView.setVisibility(View.GONE);
                    checkboxStudent.setVisibility(View.GONE);
                    gender.setVisibility(View.GONE);
                    male.setVisibility(View.INVISIBLE);
                    female.setVisibility(View.INVISIBLE);
                    editTextName.setVisibility(View.GONE);
                    genderGroup.setVisibility(View.GONE);
                    mEmailSignInButton.setText(R.string.action_sign_in);
                    alreadySignUp.setText(R.string.notalreadySignUp);
                    birthdate.setVisibility(View.GONE);
                    birthday.setVisibility(View.GONE);

                } else {
                    isSignedUp = false;
                    editTextSchoolName.setVisibility(View.GONE);
                    mConfirmPasswordView.setVisibility(View.VISIBLE);
                    checkboxStudent.setVisibility(View.VISIBLE);
                    checkboxStudent.setChecked(false);
                    male.setVisibility(View.VISIBLE);
                    female.setVisibility(View.VISIBLE);
                    gender.setVisibility(View.VISIBLE);
                    editTextName.setVisibility(View.VISIBLE);
                    genderGroup.setVisibility(View.VISIBLE);
                    mEmailSignInButton.setText(R.string.action_sign_up);
                    alreadySignUp.setText(R.string.alreadySignUp);
                    birthdate.setVisibility(View.VISIBLE);
                    birthday.setVisibility(View.VISIBLE);
                    mConfirmPasswordView.setImeActionLabel(getString(R.string.action_sign_up), R.id.login);
                }


            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                // set date picker as current date

                return new DatePickerDialog(this, pickerListener,
                        1970, 01, 01);
        }
        return null;
    }

    public void setDateLocal(int year, int monthOfYear, int dayOfMonth) {

        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.YEAR, year);


        date = "" + Utility.getMonth(monthOfYear) + " " + dayOfMonth + ", " + +year;
        birthdate.setText("" + date);


    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        editTextName.setError(null);
        editTextSchoolName.setError(null);
        mConfirmPasswordView.setError(null);
        female.setError(null);


        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirmPassword = mConfirmPasswordView.getText().toString();
        String schoolname = editTextSchoolName.getText().toString();
        String name = editTextName.getText().toString();
        String genderString = "";
        boolean isStudent = checkboxStudent.isChecked();


        boolean cancel = false;
        View focusView = null;

        switch (genderGroup.getCheckedRadioButtonId()) {
            case R.id.male:
                genderString = "Male";
                break;
            case R.id.female:
                genderString = "Female";
                break;

        }


        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            mConfirmPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!password.equals(confirmPassword) && !isSignedUp) {
            mPasswordView.setError("Passwords do not match");
            mConfirmPasswordView.setError("Passwords do not match");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }


        // Check for a valid school name
        if (TextUtils.isEmpty(schoolname) && isStudent) {
            editTextSchoolName.setError(getString(R.string.error_field_required));
            focusView = editTextSchoolName;
            cancel = true;
        }

        // Check for a valid gender.
        if (!male.isChecked() && !female.isChecked() && !isSignedUp) {
            female.setError(getString(R.string.error_field_required));
            focusView = male;
            cancel = true;
        }

        // Check for a valid name
        if (TextUtils.isEmpty(name) && !isSignedUp) {
            editTextName.setError(getString(R.string.error_field_required));
            focusView = editTextName;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.

            focusView.requestFocus();

        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            // showProgress(true);
            mAuthTask = new UserLoginTask(email, password, schoolname, isStudent, name, date, genderString);

            if (isSignedUp) {


                mAuthTask.execute((Void) null);

            } else {

                showAlert();


            }

        }
    }

    public void startTask() {
        mAuthTask.execute((Void) null);
    }

    public void showAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);


        // Setting Dialog Message
        alertDialog.setMessage("TERMS AND CONDITIONS\n\n\n" + "Welcome to our App. If you continue to browse and use this App, you are agreeing to comply with and be bound by the following terms and conditions of use, which together with our privacy policy govern Space Training Adventure ’s relationship with you in relation to this website. If yu disagree with any part of these terms and conditions, please do not use our App.\n" +
                "\n" +
                "The term \"Space Training Adventure\" or ‘us’ or ‘we’ refers to the owner of the App. The term ‘you’ refers to the user or viewer of our App.\n" +
                "\n" +
                "The use of this App is subject to the following terms of use:\n" +
                "\n" +
                "·      The content of the pages of this website is for your general information and use only. It is subject to change without notice.\n" +
                "\n" +
                "·      This App collects your personal information to provide you with a better service. Your information will not be shared to anyone else. The list of information that you authorize us to store when you sign up includes : E-Mail ID, Name, Sex, Age, Date of Birth, Address, Educational Institution, Billing Information. \n" +
                "\n" +
                "·      Neither we nor any third parties provide any warranty or guarantee as to the accuracy, timeliness, performance, completeness or suitability of the information and materials found or offered on this website for any particular purpose. You acknowledge that such information and materials may contain inaccuracies or errors and we expressly exclude liability for any such inaccuracies or errors to the fullest extent permitted by law.\n" +
                "\n" +
                "·      Your use of any information or materials on this App is entirely at your own risk, for which we shall not be liable. It shall be your own responsibility to ensure that any products, services or information available through this App meet your specific requirements.\n" +
                "\n" +
                "·      This App contains material which is owned by or licensed to us. This material includes, but is not limited to, the design, layout, look, appearance and graphics. Reproduction is prohibited other than in accordance with the copyright notice, which forms part of these terms and conditions.\n" +
                "\n" +
                "·      All trademarks reproduced in this website, which are not the property of, or licensed to the operator, are acknowledged on the website.\n" +
                "\n" +
                "·      The 3D models are not authorized to be downloaded to your personal devices. Unauthorized use of this App may give rise to a claim for damages and/or be a criminal offence.\n" +
                "\n" +
                "·      From time to time, this App may also include links to other websites. These links are provided for your convenience to provide further information. They do not signify that we endorse the website(s). We have no responsibility for the content of the linked website(s).\n" +
                "\n" +
                "·      The terms and conditions are subject to change. Your use of this App and any dispute arising out of such use of the website is subject to the laws of The United States of America.");

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("I AGREE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                startTask();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setCancelable(false).setNegativeButton("I DISAGREE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event

                mAuthTask = null;
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean doesPasswordsMatch(String password, String confirm_password) {
        //TODO: Replace this with your own logic
        return password.equals(confirm_password);
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        String email, password, schoolName, name;
        boolean isStudent;
        String birthday;
        String genderString = "";

        UserLoginTask(String email, String password, String schoolName, boolean isStudent, String name, String birthday, String genderString) {

            this.email = email;
            this.password = password;
            this.schoolName = schoolName;
            this.name = name;
            this.isStudent = isStudent;
            this.genderString = genderString;
            this.birthday = birthday;


        }

        @Override
        protected Boolean doInBackground(Void... params) {


            if (!isSignedUp) {
                Firebase myFirebaseRef = new Firebase("https://sitalumni.firebaseio.com/users");


                myFirebaseRef.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        String uid = result.get("uid").toString();
                        System.out.println("Successfully created user account with uid: " + uid);

                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("uid", uid);
                        editor.putString("name", name);
                        editor.commit();



                        Firebase userRef = new Firebase("https://sitalumni.firebaseio.com/users" + uid);
                        userRef.child("Name").setValue(name);
                        userRef.child("Email").setValue(email);
                        userRef.child("Date Of Birth").setValue(birthday);
                        userRef.child("Gender").setValue(genderString);


                        if (isStudent)
                            userRef.child("School").setValue(schoolName);
                        //Signed Up
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // there was an error
                        //Error
                        Toast.makeText(getApplicationContext(), "" + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                //Sign in

                Firebase myFirebaseRef = new Firebase("https://sitalumni.firebaseio.com/users");
                myFirebaseRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        //Error
                        Toast.makeText(getApplicationContext(), "" + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



            }

            return false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if(dialog!=null)
                dialog.dismiss();

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;

        }
    }
}

