package in.edu.sit.sitalumni;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by aliasgarmurtaza on 16/04/16.
 */
public class FeedbackFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


            View rootView = inflater.inflate(R.layout.feedback_fragment, container, false);


            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

