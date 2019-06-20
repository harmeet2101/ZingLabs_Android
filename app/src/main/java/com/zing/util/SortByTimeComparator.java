package com.zing.util;

import com.zing.model.response.CalendarSlotResponse.RecommendedShift;

import java.util.Comparator;

public class SortByTimeComparator implements Comparator<RecommendedShift> {


    @Override
    public int compare(RecommendedShift o1, RecommendedShift o2) {
         if((o1.getShift_start_timestamp()-o2.getShift_start_timestamp()<0))
             return 1;
         else if((o1.getShift_start_timestamp()-o2.getShift_start_timestamp()>0))
             return -1;
         else return 0;
    }
}
