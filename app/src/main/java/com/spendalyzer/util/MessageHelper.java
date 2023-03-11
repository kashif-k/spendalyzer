package com.spendalyzer.util;

import static android.content.ContentValues.TAG;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MessageHelper {

    private static MessageHelper messageHelper;

    private Context context;

    private final List<SmsUtil> debitSMS, creditSMS;

    private MessageHelper(Context _context, ContentResolver contentResolver){
        context = _context;
        Uri inbox = Uri.parse("content://sms/inbox");
        Cursor cursor = contentResolver.query(inbox, null, null, null, null);
        debitSMS = new ArrayList<>();
        creditSMS = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{

                String from = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String mesg = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String type = null;
                if(isDebitTransaction(mesg)) type = "DEBIT";
                if(isCreditTransaction(mesg)) type = "CREDIT";
                if(type != null){
                    //System.out.println(from + "::" + mesg);
                    SmsUtil temp = new SmsUtil(from, mesg, type, date);
                    if(type.equals("DEBIT")) debitSMS.add(temp);
                    else creditSMS.add(temp);
                }

            }while(cursor.moveToNext());
            cursor.close();
        }
    }

    public static MessageHelper getHelper(Context _context, ContentResolver contentResolver){
        if(messageHelper == null) messageHelper = new MessageHelper(_context, contentResolver);
        return messageHelper;
    }

    public List<SmsUtil> getDebitSMS(){
        return debitSMS;
    }

    public List<SmsUtil> getCreditSMS(){
        return creditSMS;
    }

    private boolean isDebitTransaction(String message) {
        message = message.toLowerCase();
        return !message.contains("will be debited") && (message.contains("debited") || message.contains("withdrawn") || message.contains("txn") || message.contains("chq"));
    }

    private boolean isCreditTransaction(String message){
        message = message.toLowerCase();
        return !message.contains("card") && !message.contains("debited") && (message.contains("credited") || message.contains("deposited")) && message.contains("a/c");
    }

}
