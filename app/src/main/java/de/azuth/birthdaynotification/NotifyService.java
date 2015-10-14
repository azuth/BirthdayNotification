package de.azuth.birthdaynotification;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NotifyService extends IntentService {

    public NotifyService(String name) {
        super(name);
    }

    public NotifyService() {
        super("NotifyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        showNotification();
        Toast.makeText(this, "jo", Toast.LENGTH_LONG).show();
    }

    private void showNotification() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("-MM-dd");
        Cursor cur = getNames("%" + sdf.format(cal.getTime())); // "-08-25");//

        if(cur != null) {
            String names = "";
            while (cur.moveToNext()) {
                names += cur.getString(cur.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                if (!cur.isLast()) {
                    names += ", ";
                }
            }

            String title = "Todays Birthdays!";
            String text = names;
            int mNotificationId = 001;

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(android.R.drawable.ic_popup_reminder)
                            .setContentTitle(title)
                            .setContentText(text)
                            .setAutoCancel(true);

            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }

        // Stop the service when finished
        stopSelf();
    }

    private Cursor getNames(String date) {
        Uri uri = ContactsContract.Data.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Event.CONTACT_ID,
                ContactsContract.CommonDataKinds.Event.START_DATE
        };

        String where =
                ContactsContract.Data.MIMETYPE + "= ? " +
                        "AND " + ContactsContract.CommonDataKinds.Event.TYPE + "=" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY +
                        " AND " + ContactsContract.CommonDataKinds.Event.START_DATE + " LIKE '" + date + "'";
        String[] selectionArgs = new String[]{
                ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE
        };
        String sortOrder = ContactsContract.CommonDataKinds.Event.START_DATE + " ASC, " + ContactsContract.Data.DISPLAY_NAME + " ASC";

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(uri, projection, where, selectionArgs, sortOrder);
        return cur;
    }
}
