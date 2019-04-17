package com.zing.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zing.R;
import com.zing.fragment.CalendarMonthFragment;
import com.zing.interfaces.FragmentInterface;
import com.zing.model.CalendarDataModel;
import com.zing.util.AppTypeface;
import com.zing.util.SessionManagement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CalendarCustomView extends LinearLayout {
    private static final String TAG = CalendarCustomView.class.getSimpleName();
    //    private ImageView previousButton, nextButton;
    private GridView calendarGridView;
    private static final int MAX_CALENDAR_COLUMN = 42;
    private int month, year;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    public Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private Context context;
    public GridAdapter mAdapter;
    private TextView tvMonth, tvMonthName;
    private HashMap<String, ArrayList<CalendarDataModel>> calendarData;
    private HashMap<String, String> shiftData;
    SessionManagement session;
    private FragmentInterface fragmentInterface;

    public CalendarCustomView(Context context) {
        super(context);
    }

    public CalendarCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        int current_year = cal.get(Calendar.YEAR);
        int current_month = cal.get(Calendar.MONTH);

        session = new SessionManagement(context);
        String date = session.getRegistrationDate();
        String[] separated = date.split("-");

        int year = Integer.parseInt(separated[0]);
        int month = Integer.parseInt(separated[1]);

        year = year - current_year;
        month = month - current_month;
        cal.add(Calendar.MONTH, month-1);
        cal.add(Calendar.YEAR, year);

        initializeUILayout();
        setUpCalendarAdapter();
        setGridCellClickEvents();
        Log.d(TAG, "I need to call this method");
    }

    public CalendarCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initializeUILayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);

        tvMonth = view.findViewById(R.id.tvMonth);
        AppTypeface.getTypeFace(getContext());
        tvMonth.setTypeface(AppTypeface.avenieNext_regular);

        calendarGridView = view.findViewById(R.id.calendar_grid);
        calendarGridView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }
        });
    }

    public void setNextButtonClickEvent(int position, HashMap<String, ArrayList<CalendarDataModel>> calendarData,
                                        HashMap<String, String> shiftData,
                                        FragmentInterface fragmentInterface) {
        this.calendarData = calendarData;
        this.shiftData = shiftData;
        this.fragmentInterface = fragmentInterface;
        cal.add(Calendar.MONTH, position);
        setUpCalendarAdapter();
    }

    private void setGridCellClickEvents() {
        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "Clicked " + position, Toast.LENGTH_LONG).show();
            }
        });
    }

    private String sDate;

    public void setUpCalendarAdapter() {

        List<Date> dayValueInCells = new ArrayList<Date>();

        Calendar mCal = (Calendar) cal.clone();
        mCal.set(Calendar.DAY_OF_MONTH, 0);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);

        while (dayValueInCells.size() < MAX_CALENDAR_COLUMN) {
            dayValueInCells.add(mCal.getTime());
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        Log.d(TAG, "Number of date " + dayValueInCells.size());
        sDate = formatter.format(cal.getTime());
        tvMonth.setText(sDate);
//      CalendarMonthFragment.tvMonth.setText(sDate);

        mAdapter = new GridAdapter(context, dayValueInCells, cal, calendarData, shiftData, fragmentInterface);
        calendarGridView.setAdapter(mAdapter);
    }

    public String getMonth() {
        return sDate;
    }
}