package com.niels.mobilegameofwords;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link inputLocRelevantWord.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link inputLocRelevantWord#newInstance} factory method to
 * create an instance of this fragment.
 */
public class inputLocRelevantWord extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view;
    EditText locRelevantEditText;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public inputLocRelevantWord() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment inputLocRelevantWord.
     */
    // TODO: Rename and change types and number of parameters
    public static inputLocRelevantWord newInstance(String param1, String param2) {
        inputLocRelevantWord fragment = new inputLocRelevantWord();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
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
        view = inflater.inflate(R.layout.fragment_input_loc_relevant_word, container, false);

        locRelevantEditText = (EditText) view.findViewById(R.id.locRelevantEditText);

        Button continueToSlidersBtn = (Button) view.findViewById(R.id.continueToSlidersBtn);
        continueToSlidersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String locRelevantWord = locRelevantEditText.getText().toString();
                ((GameOfWords) getActivity().getApplication()).setLocRelevantWord(locRelevantWord);

                if (locRelevantEditText.length() != 0) {
                    try {
                        sendNewWord(locRelevantWord);
                    } catch (Exception e) {}


                    closeKeyboard(getActivity(), locRelevantEditText.getWindowToken());

                    //TODO: replace with inputLocRelevantWord()
                    Fragment fragment = new rateWordsSlider();

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    locRelevantEditText.setError("A location relevant word must be provided.");
                }
            }
        });

        return view;
    }

    private void sendNewWord(final String locRelevantWord) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = MainActivity.getIP() + "addnewword.php";

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("GameOfWords", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("GameOfWords", "Error: " + error.getMessage());
                Log.d("GameOfWords", "" + error.getMessage() + "," + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("dataString", locRelevantWord);
                return params;
            }

            /** Passing some request headers * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        // Adding request to request queue
        queue.add(sr);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        public void onFragmentInteraction(Uri uri);
    }

}
