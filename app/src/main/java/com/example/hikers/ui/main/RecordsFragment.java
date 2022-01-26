package com.example.hikers.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hikers.R;

public class RecordsFragment extends Fragment {

    private TextView textView;
    private ListView spotListView;
    private CalendarView calendarView;
    private ArrayAdapter<String> spotArrayAdapter;

    final static String[] items = {"コンビニ","山","スーパー"};

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_records,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = (TextView) view.findViewById(R.id.textView);
        spotListView = (ListView) view.findViewById(R.id.spotListView);
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        spotArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,items);


        spotListView.setAdapter(spotArrayAdapter);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                textView.setText(String.format("%d年%d月%d日",year,month+1,day));
            }
        });

    }
}
