package in.edu.sit.sitalumni;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by aliasgarmurtaza on 16/04/16.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
