package com.niks.calanderdemo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import java.util.GregorianCalendar;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void user2Fill(View v) {
		Intent intent = new Intent(Intent.ACTION_INSERT);

		intent.setData(Events.CONTENT_URI);

		startActivity(intent);
	}

	public void preFilledEvent(View v) {
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra(Events.TITLE, "This is Title");
		intent.putExtra(Events.EVENT_LOCATION, "This is Location");
		intent.putExtra(Events.DESCRIPTION, "This is the big description");

		// For setting dates
		GregorianCalendar gDate = new GregorianCalendar(2012, 0, 19);
		intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
				gDate.getTimeInMillis());
		intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
				gDate.getTimeInMillis());

		// Make it a full day event
		intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
		// Make it a recurring Event
		intent.putExtra(Events.RRULE,
				"FREQ=WEEKLY;COUNT=11;WKST=SU;BYDAY=TU,TH");

		// Making it private and shown as busy
		intent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);
		intent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
		startActivity(intent);
	}

	public void addProgramatically(View v) {
//		Uri eventsUri;
//		Uri remainderUri;
//		Cursor cursor;
//		if (android.os.Build.VERSION.SDK_INT <= 7) 
//		  {
//		    eventsUri = Uri.parse("content://calendar/events");
//		    remainderUri = Uri.parse("content://calendar/reminders");
//		    cursor = getContentResolver().query(
//		    Uri.parse("content://calendar/calendars"), new String[] { "_id", "displayName" }, null, null,null);
//
//		   } 
//
//		   else if(android.os.Build.VERSION.SDK_INT <= 14)
//		   {
//		     eventsUri = Uri.parse("content://com.android.calendar/events");
//		     remainderUri = Uri.parse("content://com.android.calendar/reminders");
//		     cursor = getContentResolver().query(
//		     Uri.parse("content://com.android.calendar/calendars"),new String[] { "_id", "displayName" }, null, null,null);
//
//		   }
//
//		   else
//		  {
//		     eventsUri = Uri.parse("content://com.android.calendar/events");
//		     remainderUri = Uri.parse("content://com.android.calendar/reminders");
//		     cursor = getContentResolver().query(
//		     Uri.parse("content://com.android.calendar/calendars"),new String[] { "_id", "calendar_displayName" }, null, null,null);
//		   
//		  }
//		ContentValues values = new ContentValues();
//
////        values.put(CalendarContract.Events.DTSTART, dtstart);
//        values.put(CalendarContract.Events.TITLE,"This is the title");
//        values.put(CalendarContract.Events.DESCRIPTION, "This sis the distcription");
//
//        TimeZone timeZone = TimeZone.getDefault();
//        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
//
//    	// For setting dates
////		GregorianCalendar gDate = new GregorianCalendar(2012, 6, 20);
//        long valueee = System.currentTimeMillis();
//        values.put(CalendarContract.EXTRA_EVENT_BEGIN_TIME, valueee);
//        values.put(CalendarContract.EXTRA_EVENT_END_TIME, valueee);
//		
//        // default calendar
//        values.put(CalendarContract.Events.CALENDAR_ID, 1);
//
//        values.put(CalendarContract.Events.RRULE, "FREQ=WEEKLY;COUNT=11;WKST=SU;BYDAY=TU,TH");
//        //for one hour
//        values.put(CalendarContract.Events.DURATION, "+P1H");
//
//        values.put(CalendarContract.Events.HAS_ALARM, 1);
//		getContentResolver().insert(eventsUri, values);
		
		
		myToast(""+pushAppointmentsToCalender(MainActivity.this, "Mytitle", "AddInfo", "Place", 1, System.currentTimeMillis(), true, true));
		myToast("");
	}
	private void myToast(String string) {
Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();

	}

	public static long pushAppointmentsToCalender(Activity curActivity, String title, String addInfo, String place, int status, long startDate, boolean needReminder, boolean needMailService) {
	    /***************** Event: note(without alert) *******************/

	    String eventUriString = "content://com.android.calendar/events";
	    ContentValues eventValues = new ContentValues();

	    eventValues.put("calendar_id", 1); // id, We need to choose from
	                                        // our mobile for primary
	                                        // its 1
	    eventValues.put("title", title);
	    eventValues.put("description", addInfo);
	    eventValues.put("eventLocation", place);

	    eventValues.put("eventTimezone", "India");
	    
	    long endDate = startDate + 1000 * 60 * 60; // For next 1hr
	    
 
	    
	    
	    eventValues.put("dtstart", startDate);
	    eventValues.put("dtend", endDate);

	    // values.put("allDay", 1); //If it is bithday alarm or such
	    // kind (which should remind me for whole day) 0 for false, 1
	    // for true
	    eventValues.put("eventStatus", status); // This information is
	    // sufficient for most
	    // entries tentative (0),
	    // confirmed (1) or canceled
	    // (2):

	   /*Comment below visibility and transparency  column to avoid java.lang.IllegalArgumentException column visibility is invalid error */

	    /*eventValues.put("visibility", 3); // visibility to default (0),
	                                        // confidential (1), private
	                                        // (2), or public (3):
	    eventValues.put("transparency", 0); // You can control whether
	                                        // an event consumes time
	                                        // opaque (0) or transparent
	                                        // (1).
	      */
	    eventValues.put("hasAlarm", 1); // 0 for false, 1 for true

	    Uri eventUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(eventUriString), eventValues);
	    long eventID = Long.parseLong(eventUri.getLastPathSegment());

	    if (needReminder) {
	        /***************** Event: Reminder(with alert) Adding reminder to event *******************/

	        String reminderUriString = "content://com.android.calendar/reminders";

	        ContentValues reminderValues = new ContentValues();

	        reminderValues.put("event_id", eventID);
	        reminderValues.put("minutes", 5); // Default value of the
	                                            // system. Minutes is a
	                                            // integer
	        reminderValues.put("method", 1); // Alert Methods: Default(0),
	                                            // Alert(1), Email(2),
	                                            // SMS(3)

	        Uri reminderUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);
	    }

	    /***************** Event: Meeting(without alert) Adding Attendies to the meeting *******************/

	    if (needMailService) {
	        String attendeuesesUriString = "content://com.android.calendar/attendees";

	        /********
	         * To add multiple attendees need to insert ContentValues multiple
	         * times
	         ***********/
	        ContentValues attendeesValues = new ContentValues();

	        attendeesValues.put("event_id", eventID);
	        attendeesValues.put("attendeeName", "xxxxx"); // Attendees name
	        attendeesValues.put("attendeeEmail", "yyyy@gmail.com");// Attendee
	                                                                            // E
	                                                                            // mail
	                                                                            // id
	        attendeesValues.put("attendeeRelationship", 0); // Relationship_Attendee(1),
	                                                        // Relationship_None(0),
	                                                        // Organizer(2),
	                                                        // Performer(3),
	                                                        // Speaker(4)
	        attendeesValues.put("attendeeType", 0); // None(0), Optional(1),
	                                                // Required(2), Resource(3)
	        attendeesValues.put("attendeeStatus", 0); // NOne(0), Accepted(1),
	                                                    // Decline(2),
	                                                    // Invited(3),
	                                                    // Tentative(4)

	        Uri attendeuesesUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(attendeuesesUriString), attendeesValues);
	    }

	    return eventID;

	}
}
