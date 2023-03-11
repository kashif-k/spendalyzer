package com.spendalyzer.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageReader {

    private final MessageHelper messageHelper;
    private static MessageReader messageReader;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private static final Calendar calendar = Calendar.getInstance();
    private MessageReader(MessageHelper messageHelper){
        this.messageHelper = messageHelper;
    }

    public static MessageReader getMessageReader(MessageHelper msgHelper) {
        if(messageReader == null) messageReader = new MessageReader(msgHelper);
        return messageReader;
    }

    public List<String> getDebitSmsList(){
        List<String> debitSmsList = new ArrayList<>();

        messageHelper.getDebitSMS().forEach(sms -> {
            String from = extractReceiver(sms.MESSAGE);
            if(from.length() > 0 && from.contains("(")) from = from.substring(0, from.indexOf('('));
            debitSmsList.add((from.length() > 0 ? from : sms.FROM) + "\nRs. " + extractAmount(sms.MESSAGE) + "\n" + sdf.format(new Date(Long.parseLong(sms.DATE))));

        });
        return debitSmsList;
    }

    public List<String> getCreditSmsList(){
        List<String> creditSmsList = new ArrayList<>();
        messageHelper.getCreditSMS().forEach(sms ->{
            String from = extractReceiver(sms.MESSAGE);
            if(from.length() > 0 && from.contains("(")) from = from.substring(0, from.indexOf('('));
            creditSmsList.add((from.length() > 0 ? from : sms.FROM) + "\nRs. " + extractAmount(sms.MESSAGE) + "\n" + sdf.format(new Date(Long.parseLong(sms.DATE))));
        });
        return creditSmsList;
    }



    private static BigDecimal extractAmount(String message){
        message = message.toLowerCase();
        String[] chunk = message.split(" ");

        for(int i = 0; i + 1 < chunk.length; i++){
            if(chunk[i].contains("rs") || chunk[i].contains("inr")){
                if(containsNumber(chunk[i])){
                    if(chunk[i].contains("rs.")) chunk[i] = chunk[i].replace("rs.", "");
                    if(chunk[i].contains("rs")) chunk[i] = chunk[i].replace("rs", "");
                    try{
                        return new BigDecimal(chunk[i].replace(",", ""));
                    }catch (Exception e){
                        return BigDecimal.ZERO;
                    }
                }
                try{
                    return new BigDecimal(chunk[i + 1].replace(",", ""));
                }catch (Exception e){
                    return BigDecimal.ZERO;
                }
            }
        }
        return BigDecimal.ZERO;
    }

    private static String extractReceiver(String message){
        message = message.toLowerCase();
        String[] chunk = message.split(" ");

        for(int i = 1; i + 1 < chunk.length; i++){
            if(chunk[i - 1].equals("to") && chunk[i].equals("vpa")){
                return chunk[i + 1];
            }
        }

        return "";
    }

    private static boolean containsNumber(String s) {
        List<Character> numberList = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
        for(char num: s.toCharArray()){
            if(numberList.contains(num)) return true;
        }
        return false;
    }

    //Today's spending
    public List<SmsUtil> getTodayDebitSmsList(){
        List<SmsUtil> debitSms = new ArrayList<>();
        final int todayDate = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        messageHelper.getDebitSMS().forEach(util -> {
            Calendar msgDate = Calendar.getInstance();
            msgDate.setTimeInMillis(Long.parseLong(util.DATE));
            if(todayDate == msgDate.get(Calendar.DAY_OF_MONTH)
                && month == msgDate.get(Calendar.MONTH)
                && year == msgDate.get(Calendar.YEAR)){
                debitSms.add(util);
            }
        });

        return debitSms;
    }

    public List<SmsUtil> getTodayCreditSmsList(){
        List<SmsUtil> creditSms = new ArrayList<>();
        final int todayDate = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        messageHelper.getCreditSMS().forEach(util -> {
            Calendar msgDate = Calendar.getInstance();
            msgDate.setTimeInMillis(Long.parseLong(util.DATE));
            if(todayDate == msgDate.get(Calendar.DAY_OF_MONTH)
                    && month == msgDate.get(Calendar.MONTH)
                    && year == msgDate.get(Calendar.YEAR)){
                creditSms.add(util);
            }
        });

        return creditSms;
    }

    public String getTodayDebit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getTodayDebitSmsList().forEach(util -> amount[0] = amount[0].add(extractAmount(util.MESSAGE)));
        return amount[0].toString();
    }

    public String getTodayCredit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getTodayCreditSmsList().forEach(util -> amount[0] = amount[0].add(extractAmount(util.MESSAGE)));
        return amount[0].toString();
    }

    //This month spending
    public List<SmsUtil> getMonthDebitSmsList(){
        List<SmsUtil> debitSms = new ArrayList<>();
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        messageHelper.getDebitSMS().forEach(util -> {
            Calendar msgDate = Calendar.getInstance();
            msgDate.setTimeInMillis(Long.parseLong(util.DATE));
            if(month == msgDate.get(Calendar.MONTH) && year == msgDate.get(Calendar.YEAR)){
                debitSms.add(util);
            }
        });

        return debitSms;
    }

    public List<SmsUtil> getMonthCreditSmsList(){
        List<SmsUtil> creditSms = new ArrayList<>();
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        messageHelper.getCreditSMS().forEach(util -> {
            Calendar msgDate = Calendar.getInstance();
            msgDate.setTimeInMillis(Long.parseLong(util.DATE));
            if(month == msgDate.get(Calendar.MONTH) && year == msgDate.get(Calendar.YEAR)){
                creditSms.add(util);
            }
        });

        return creditSms;
    }

    public String getMonthDebit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getMonthDebitSmsList().forEach(util -> amount[0] = amount[0].add(extractAmount(util.MESSAGE)));
        return amount[0].toString();
    }

    public String getMonthCredit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getMonthCreditSmsList().forEach(util -> amount[0] = amount[0].add(extractAmount(util.MESSAGE)));
        return amount[0].toString();
    }

    //This year spending
    public List<SmsUtil> getYearDebitSmsList(){
        List<SmsUtil> debitSms = new ArrayList<>();
        final int year = calendar.get(Calendar.YEAR);

        messageHelper.getDebitSMS().forEach(util -> {
            Calendar msgDate = Calendar.getInstance();
            msgDate.setTimeInMillis(Long.parseLong(util.DATE));
            if(year == msgDate.get(Calendar.YEAR)){
                debitSms.add(util);
            }
        });

        return debitSms;
    }

    public List<SmsUtil> getYearCreditSmsList(){
        List<SmsUtil> creditSms = new ArrayList<>();
        final int year = calendar.get(Calendar.YEAR);

        messageHelper.getCreditSMS().forEach(util -> {
            Calendar msgDate = Calendar.getInstance();
            msgDate.setTimeInMillis(Long.parseLong(util.DATE));
            if(year == msgDate.get(Calendar.YEAR)){
                creditSms.add(util);
            }
        });

        return creditSms;
    }

    public String getYearDebit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getYearDebitSmsList().forEach(util -> amount[0] = amount[0].add(extractAmount(util.MESSAGE)));
        return amount[0].toString();
    }

    public String getYearCredit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getYearCreditSmsList().forEach(util -> amount[0] = amount[0].add(extractAmount(util.MESSAGE)));
        return amount[0].toString();
    }

    //This week spending

    public List<SmsUtil> getWeekDebitSmsList(){
        List<SmsUtil> debitSms = new ArrayList<>();
        final int week = calendar.get(Calendar.WEEK_OF_YEAR);
        final int year = calendar.get(Calendar.YEAR);

        messageHelper.getDebitSMS().forEach(util -> {
            Calendar msgDate = Calendar.getInstance();
            msgDate.setTimeInMillis(Long.parseLong(util.DATE));
            if(week == msgDate.get(Calendar.WEEK_OF_YEAR) && year == msgDate.get(Calendar.YEAR)){
                debitSms.add(util);
            }
        });

        return debitSms;
    }

    public List<SmsUtil> getWeekCreditSmsList(){
        List<SmsUtil> creditSms = new ArrayList<>();
        final int week = calendar.get(Calendar.WEEK_OF_YEAR);
        final int year = calendar.get(Calendar.YEAR);

        messageHelper.getCreditSMS().forEach(util -> {
            Calendar msgDate = Calendar.getInstance();
            msgDate.setTimeInMillis(Long.parseLong(util.DATE));
            if(week == msgDate.get(Calendar.WEEK_OF_YEAR) && year == msgDate.get(Calendar.YEAR)){
                creditSms.add(util);
            }
        });

        return creditSms;
    }

    public String getWeekDebit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getWeekDebitSmsList().forEach(util -> amount[0] = amount[0].add(extractAmount(util.MESSAGE)));
        return amount[0].toString();
    }

    public String getWeekCredit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getWeekCreditSmsList().forEach(util -> amount[0] = amount[0].add(extractAmount(util.MESSAGE)));
        return amount[0].toString();
    }

    //Today UPI payment
    public String getTodayUPIDebit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getTodayDebitSmsList().forEach(util -> {
            if(util.MESSAGE.toLowerCase().contains("upi")) amount[0] = amount[0].add(extractAmount(util.MESSAGE));
        });
        return amount[0].toString();
    }

    public String getTodayUPICredit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getTodayCreditSmsList().forEach(util -> {
            if(util.MESSAGE.toLowerCase().contains("upi")) amount[0] = amount[0].add(extractAmount(util.MESSAGE));
        });
        return amount[0].toString();
    }

    //Week UPI payment

    public String getWeekUPIDebit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getWeekDebitSmsList().forEach(util -> {
            if(util.MESSAGE.toLowerCase().contains("upi")) amount[0] = amount[0].add(extractAmount(util.MESSAGE));
        });
        return amount[0].toString();
    }

    public String getWeekUPICredit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getWeekCreditSmsList().forEach(util -> {
            if(util.MESSAGE.toLowerCase().contains("upi")) amount[0] = amount[0].add(extractAmount(util.MESSAGE));
        });
        return amount[0].toString();
    }

    //Month UPI payment
    public String getMonthUPIDebit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getMonthDebitSmsList().forEach(util -> {
            if(util.MESSAGE.toLowerCase().contains("upi")) amount[0] = amount[0].add(extractAmount(util.MESSAGE));
        });
        return amount[0].toString();
    }

    public String getMonthUPICredit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getMonthCreditSmsList().forEach(util -> {
            if(util.MESSAGE.toLowerCase().contains("upi")) amount[0] = amount[0].add(extractAmount(util.MESSAGE));
        });
        return amount[0].toString();
    }

    //Year UPI Payment

    public String getYearUPIDebit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getYearDebitSmsList().forEach(util -> {
            if(util.MESSAGE.toLowerCase().contains("upi")) amount[0] = amount[0].add(extractAmount(util.MESSAGE));
        });
        return amount[0].toString();
    }

    public String getYearUPICredit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getYearCreditSmsList().forEach(util -> {
            if(util.MESSAGE.toLowerCase().contains("upi")) amount[0] = amount[0].add(extractAmount(util.MESSAGE));
        });
        return amount[0].toString();
    }

    //Today ATM payment
    public String getTodayATMDebit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getTodayDebitSmsList().forEach(util -> {
            if(util.MESSAGE.toLowerCase().contains("atm") || util.MESSAGE.toLowerCase().contains("withdrawn"))amount[0] = amount[0].add(extractAmount(util.MESSAGE));
        });
        return amount[0].toString();
    }


    //Week ATM payment

    public String getWeekATMDebit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getWeekDebitSmsList().forEach(util -> {
            if(util.MESSAGE.toLowerCase().contains("atm") || util.MESSAGE.toLowerCase().contains("withdrawn")) amount[0] = amount[0].add(extractAmount(util.MESSAGE));
        });
        return amount[0].toString();
    }

    //Month ATM payment
    public String getMonthATMDebit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getMonthDebitSmsList().forEach(util -> {
            if(util.MESSAGE.toLowerCase().contains("atm") || util.MESSAGE.toLowerCase().contains("withdrawn")) amount[0] = amount[0].add(extractAmount(util.MESSAGE));
        });
        return amount[0].toString();
    }

    //Year ATM Payment

    public String getYearATMDebit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getYearDebitSmsList().forEach(util -> {
            if(util.MESSAGE.toLowerCase().contains("atm") || util.MESSAGE.toLowerCase().contains("withdrawn")) amount[0] = amount[0].add(extractAmount(util.MESSAGE));
        });
        return amount[0].toString();
    }

    //Today Other payment
    public String getTodayOtherDebit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getTodayDebitSmsList().forEach(util -> {
            String msg = util.MESSAGE.toLowerCase();
            if((!msg.contains("upi") && !msg.contains("atm"))) amount[0] = amount[0].add(extractAmount(util.MESSAGE));
        });
        return amount[0].toString();
    }

    public String getTodayOtherCredit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getTodayCreditSmsList().forEach(util -> {
            String msg = util.MESSAGE.toLowerCase();
            if((!msg.contains("upi") && !msg.contains("atm"))) amount[0] = amount[0].add(extractAmount(util.MESSAGE));
        });
        return amount[0].toString();
    }

    //Week Other payment

    public String getWeekOtherDebit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getWeekDebitSmsList().forEach(util -> {
            String msg = util.MESSAGE.toLowerCase();
            if((!msg.contains("upi") && !msg.contains("atm"))) amount[0] = amount[0].add(extractAmount(util.MESSAGE));
        });
        return amount[0].toString();
    }

    public String getWeekOtherCredit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getWeekCreditSmsList().forEach(util -> {
            String msg = util.MESSAGE.toLowerCase();
            if((!msg.contains("upi") && !msg.contains("atm"))) amount[0] = amount[0].add(extractAmount(util.MESSAGE));
        });
        return amount[0].toString();
    }

    //Month UPI payment
    public String getMonthOtherDebit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getMonthDebitSmsList().forEach(util -> {
            String msg = util.MESSAGE.toLowerCase();
            if((!msg.contains("upi") && !msg.contains("atm"))) amount[0] = amount[0].add(extractAmount(util.MESSAGE));
        });
        return amount[0].toString();
    }

    public String getMonthOtherCredit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getMonthCreditSmsList().forEach(util -> {
            String msg = util.MESSAGE.toLowerCase();
            if((!msg.contains("upi") && !msg.contains("atm"))) amount[0] = amount[0].add(extractAmount(util.MESSAGE));
        });
        return amount[0].toString();
    }

    //Year UPI Payment

    public String getYearOtherDebit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getYearDebitSmsList().forEach(util -> {
            String msg = util.MESSAGE.toLowerCase();
            if((!msg.contains("upi") && !msg.contains("atm"))) amount[0] = amount[0].add(extractAmount(util.MESSAGE));
        });
        return amount[0].toString();
    }

    public String getYearOtherCredit(){
        final BigDecimal[] amount = {BigDecimal.ZERO};
        getYearCreditSmsList().forEach(util -> {
            String msg = util.MESSAGE.toLowerCase();
            if((!msg.contains("upi") && !msg.contains("atm"))) amount[0] = amount[0].add(extractAmount(util.MESSAGE));
        });
        return amount[0].toString();
    }
    
    //
    
    public List<Object> getCreditSmsFromDate(long from, long to){
        List<SmsUtil> creditSms = new ArrayList<>();
        BigDecimal[] total = { BigDecimal.ZERO };
        messageHelper.getCreditSMS().forEach(util -> {
            long msgTime = Long.parseLong(util.DATE);
            if((msgTime >= from) && (msgTime <= (to + 86400000) - 1)) {
                creditSms.add(util);
                total[0] = total[0].add(extractAmount(util.MESSAGE));
            }
        });
        return Arrays.asList(total[0].toString(), creditSms);
    }

    public List<Object> getDebitSmsFromDate(long from, long to){
        List<SmsUtil> debitSms = new ArrayList<>();
        BigDecimal[] total = { BigDecimal.ZERO };
        messageHelper.getDebitSMS().forEach(util -> {
            long msgTime = Long.parseLong(util.DATE);
            if((msgTime >= from) && (msgTime <= (to + 86400000) - 1)) {
                debitSms.add(util);
                total[0] = total[0].add(extractAmount(util.MESSAGE));
            }
        });
        return Arrays.asList(total[0].toString(), debitSms);
    }

    public List<String> toStringList(List<SmsUtil> smsList){
        List<String> stringList = new ArrayList<>();
        smsList.forEach(sms ->{
            String from = extractReceiver(sms.MESSAGE);
            if(from.length() > 0 && from.contains("(")) from = from.substring(0, from.indexOf('('));
            stringList.add((from.length() > 0 ? from : sms.FROM) + "\nRs. " + extractAmount(sms.MESSAGE) + "\n" + sdf.format(new Date(Long.parseLong(sms.DATE))));
        });
        return stringList;
    }



}
