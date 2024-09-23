package com.fit2081.assignment_2;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit2081.assignment_2.entities.Event;
import com.fit2081.assignment_2.entities.EventCategory;
import com.fit2081.assignment_2.providerEvent.EventViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListEvent extends Fragment {

    SharedPreferences sP;
    ArrayList<EventCategory> eventCategories;
    ArrayList<Event> events;

    String eventString;

    RecyclerView recyclerView;
    EventRecyclerAdapter adapter;

    Gson gson = new Gson();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EventViewModel eventViewModel;

    public FragmentListEvent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListEvent.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListEvent newInstance(String param1, String param2) {

        FragmentListEvent fragment = new FragmentListEvent();
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
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        sP = getActivity().getSharedPreferences(Keys.FILE_NAME, MODE_PRIVATE);
//        eventString = sP.getString(Keys.EVENT_LIST, "[]");
//        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
//        events = gson.fromJson(eventString, type);

        //Toast.makeText(getContext(), "Size" + eventCategories.size(), Toast.LENGTH_SHORT).show();

        View v = inflater.inflate(R.layout.fragment_list_event, container, false);
        recyclerView = v.findViewById(R.id.recyclerViewEvent);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        adapter = new EventRecyclerAdapter();
//        adapter.setData(events);

        recyclerView.setAdapter(adapter);

        // subscribe to LiveData of type ArrayList<Item>,
        // any changes detected in the database will be notified to MainActivity
        eventViewModel.getAllEvents().observe(getViewLifecycleOwner(), newData -> {
            // cast List<Item> to ArrayList<Item>
            System.out.println("eventViewModel.getAllEvent()");
            adapter.setDataEvent(newData);
            adapter.notifyDataSetChanged();
        });

        return v;
    }
}