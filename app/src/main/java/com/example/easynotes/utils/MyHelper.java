package com.example.easynotes.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.easynotes.dataClass.Notes;

import java.util.ArrayList;
import java.util.Iterator;


public class MyHelper {

    Context context;

    public MyHelper(Context context) {
        this.context = context;
    }



    // reverse string for show last added value to show in first
    public ArrayList<Notes> reverseListOrder(ArrayList<Notes> notes) {
        Iterator<Notes> it = notes.iterator();
        ArrayList<Notes> destination = new ArrayList<>();
        while (it.hasNext()) {
            destination.add(0, it.next());
            it.remove();
        }
        return destination;
    }

    // rate us app
    void rateUsOurApp() {
        try {
            Uri marketUri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            context.startActivity(marketIntent);
        } catch (ActivityNotFoundException e) {
            Uri marketUri = Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName());
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            context.startActivity(marketIntent);
        }
    }

    // share app
    public void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Easy Notes");
            String shareMessage = "\nLet me recommend you this application for write the notes easily.\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + context.getPackageName() + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    // follow on instagram
    public void goToInstagram() {
        Uri uri = Uri.parse("http://instagram.com/_u/aditya_kumar4");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            context.startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/aditya_kumar4")));
        }
    }

    // contact us
    public void dialContactPhone(final String phoneNumber) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }


    // get months
    public String getMonths(String month) {
        switch (month) {
            case "1":
                return "January";
            case "2":
                return "February";
            case "3":
                return "March";
            case "4":
                return "April";
            case "5":
                return "May";
            case "6":
                return "June";
            case "7":
                return "July";
            case "8":
                return "August";
            case "9":
                return "September";
            case "10":
                return "October";
            case "11":
                return "November";
            case "12":
                return "December";
            default:
                return null;
        }
    }


}
