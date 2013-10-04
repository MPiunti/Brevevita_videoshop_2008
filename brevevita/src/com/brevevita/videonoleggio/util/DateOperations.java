/*
 * DateOperations.java
 *
 * Created on 9 marzo 2008, 15.03
 *
 *  aliCE (c) 2007-2008
 *  Michele Piunti
 */

package com.brevevita.videonoleggio.util;


import java.sql.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author michelepiunti
 */
public class DateOperations {
    
    /** Creates a new instance of DateOperations */
    public DateOperations() {
        
    }
    
    public static long giorniFra(Date d1, Date d2) {
            long FROM = d1.getTime();
            long TO = d2.getTime();
         
            // First convert the from and to Calender to long (milli seconds)
            // MAKE SURE THE Hour, Seconds and Milli seconds are set to 0, if you
            // already have you own Claender object otherwise the time will be
            // used in the comparision, later on.
//                long from = new GregorianCalendar(2003, 1, 20).getTime().getTime();
//                long to = new GregorianCalendar(2003, 2, 5).getTime().getTime();

            // Next subtract the from date from the to date (make sure the
            // result is a double, this is needed in case of Winter and Summer
            // Time (changing the clock one hour ahead or back) the result will
            // then be not exactly rounded on days. If you use long, this slighty
            // different result will be lost.
            double difference = TO - FROM;

            // Next divide the difference by the number of milliseconds in a day
            // (1000 * 60 * 60 * 24). Next round the result, this is needed of the
            // Summer and Winter time. If the period is 5 days and the change from
            // Winter to Summer time is in the period the result will be
            // 5.041666666666667 instead of 5 because of the extra hour. The
            // same will happen from Winter to Summer time, the result will be
            // 4.958333333333333 instead of 5 because of the missing hour. The
            // round method will round both to 5 and everything is OKE....
            long days = Math.round((difference/(1000*60*60*24)));

            // Now we can print the day difference... Try it, it also works with
            // Feb 29...
            //System.out.println(days); 
            return days;
    }
    
    
     public static void main(String[] args) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        int oneday = 1000*60*60*24;
        Date D1 = new Date(c.getTimeInMillis()-oneday/2);
        Date D2 = new Date(c.getTimeInMillis());
        System.out.println("giorni fra " + giorniFra(D1,D2));
        
    }
    
}
