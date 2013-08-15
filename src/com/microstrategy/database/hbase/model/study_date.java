package com.microstrategy.database.hbase.model;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 
 * @author wlu
 * 
 */
public class study_date {

	public static void main(String args[]) throws ParseException {
		Timestamp timestamp = Timestamp.valueOf("2013-02-28 08:41:07.240");
		System.out.println(timestamp);

		// "Feb 28, 2013 7:27:50 AM"

		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss",
				Locale.US);
		Date d = sdf.parse("Feb 28, 2013 7:27:50 AM");
		System.out.println(d);
		Date d2 = sdf.parse("Feb 28, 2013 7:27:50 PM");

		System.out.println(d2);

		Timestamp ts = new Timestamp(d.getTime());
		Timestamp ts2 = new Timestamp(d2.getTime() + 12 * 60 * 60 * 1000);
		System.out.println("timestamp: " + ts);
		System.out.println("timestamp: " + ts2);

		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
		System.out.println(sdf2.format(d));
		System.out.println(sdf2.format(d2));
		
		System.out.println(new Timestamp(new Date().getTime()));
	}

}
