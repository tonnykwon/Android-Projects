package tutorials;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tony.tonythetiger.memind.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TutorialMidFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TutorialMidFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TutorialMidFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ImageView imageView;
    int tutorialClick;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TutorialMidFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TutorialMidFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TutorialMidFragment newInstance(String param1, String param2) {
        TutorialMidFragment fragment = new TutorialMidFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tutorial_mid, container, false);

        imageView = (ImageView) v.findViewById(R.id.tutorialimg);
        imageView.setBackgroundResource(R.drawable.tutorial1);
        RelativeLayout fragment = (RelativeLayout) v.findViewById(R.id.tutorialmid);

        tutorialClick=0;

        //tutorial 클릭시 이벤트
        fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tutorialClick==0){
                    imageView.setBackgroundResource(R.drawable.tutorial2);
                    mListener.onFragmentInteraction(tutorialClick);
                    tutorialClick=tutorialClick+1;
                } else if(tutorialClick==1){
                    imageView.setBackgroundResource(R.drawable.tutorial3);
                    mListener.onFragmentInteraction(tutorialClick);
                    tutorialClick=tutorialClick+1;
                } else if(tutorialClick==2){
                    imageView.setBackgroundResource(R.drawable.tutorial4);
                    mListener.onFragmentInteraction(tutorialClick);
                    tutorialClick=tutorialClick+1;
                } else{
                    mListener.onFragmentInteraction(tutorialClick);
                }
            }
        });

        return v;

    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int number);
    }
}
