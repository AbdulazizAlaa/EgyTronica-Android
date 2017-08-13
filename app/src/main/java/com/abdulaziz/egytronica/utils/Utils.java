package com.abdulaziz.egytronica.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.abdulaziz.egytronica.data.local.PreferencesHelper;
import com.abdulaziz.egytronica.data.model.AlternateResponse;
import com.abdulaziz.egytronica.data.model.Response;
import com.abdulaziz.egytronica.data.remote.Service;
import com.abdulaziz.egytronica.ui.home.HomeActivity;
import com.abdulaziz.egytronica.ui.landing.LandingPage;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by abdulaziz on 5/25/16.
 */
public class Utils {

    public static String getRemainingDays(String timestamp) {

        String datetime = "";

        SimpleDateFormat sdf = null;
        TimeZone tz = TimeZone.getDefault();


        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTimeInMillis(System.currentTimeMillis());
        todayCalendar.add(Calendar.MILLISECOND, tz.getOffset(todayCalendar.getTimeInMillis()));
        Date todayDate = (Date) todayCalendar.getTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(timestamp) * 1000);
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        Date date = (Date) calendar.getTime();

        long diffInMillies = todayDate.getTime() - date.getTime();
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        return String.valueOf(diffInDays);
    }

    public static String timeStampToTime(String timestamp){

        String datetime = "";

        SimpleDateFormat sdf = null;
        TimeZone tz = TimeZone.getDefault();


        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTimeInMillis(System.currentTimeMillis());
        todayCalendar.add(Calendar.MILLISECOND, tz.getOffset(todayCalendar.getTimeInMillis()));
        Date todayDate = (Date) todayCalendar.getTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(timestamp) * 1000);
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        Date date = (Date) calendar.getTime();

        //return time only
        sdf = new SimpleDateFormat("hh:mm a");
        datetime = sdf.format(date);

        return datetime;
    }

    public static String timeStampToFormattedDate(String timestamp){

        String datetime = "";

        SimpleDateFormat sdf = null;
        TimeZone tz = TimeZone.getDefault();


        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTimeInMillis(System.currentTimeMillis());
        todayCalendar.add(Calendar.MILLISECOND, tz.getOffset(todayCalendar.getTimeInMillis()));
        Date todayDate = (Date) todayCalendar.getTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(timestamp) * 1000);
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        Date date = (Date) calendar.getTime();

        if(todayCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR) &&
                todayCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)){
            //same day so return only the hour
            sdf = new SimpleDateFormat("hh:mm a");
            datetime = sdf.format(date);

        }else if(todayCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)){
            //same year so return the day and month
            datetime = getMonthName(calendar.get(Calendar.MONTH))+" "+calendar.get(Calendar.DAY_OF_MONTH);
        }else{
            //return day-month-year
            sdf = new SimpleDateFormat("dd/MM/yyyy");
            datetime = sdf.format(date);
        }

        return datetime;
    }

    public static String getMonthName(int month) {
        switch (month + 1) {
            case 1:
                return "Jan";

            case 2:
                return "Feb";

            case 3:
                return "Mar";

            case 4:
                return "Apr";

            case 5:
                return "May";

            case 6:
                return "Jun";

            case 7:
                return "Jul";

            case 8:
                return "Aug";

            case 9:
                return "Sep";

            case 10:
                return "Oct";

            case 11:
                return "Nov";

            case 12:
                return "Dec";
        }

        return "";
    }

    public static String handleThrowable(Throwable e, String TAG){
        String errorMessage = "Undefined Error";
        Log.i(TAG, "error: "+e.getMessage());
        if(e instanceof HttpException){
            HttpException response = (HttpException) e;
            int code = response.code();
            String message = response.message();
            Log.i(TAG, "code: "+code);
            Log.i(TAG, "message: "+message);

            String errorBodyStr = "";
            ResponseBody errorBody = null;
            try {
                errorBodyStr = response.response().errorBody().string();
                Log.i(TAG, "error body: "+errorBodyStr);

                if(errorBodyStr.contains("error")){
                    // Look up a converter for the Error type on the Retrofit instance.
                    Converter<ResponseBody, AlternateResponse> errorConverter =
                            Service.Creator.retrofit.responseBodyConverter(AlternateResponse.class, new Annotation[0]);
                    // Convert the error body into our Error type.

                    errorBody = ResponseBody.create(MediaType.parse("application/json"), errorBodyStr);

                    AlternateResponse error = null;
                    error = errorConverter.convert(errorBody);

                    errorMessage = error.error == null? message:error.error;
                }else{
                    // Look up a converter for the Error type on the Retrofit instance.
                    Converter<ResponseBody, Response> errorConverter =
                            Service.Creator.retrofit.responseBodyConverter(Response.class, new Annotation[0]);
                    // Convert the error body into our Error type.

                    errorBody = ResponseBody.create(MediaType.parse("application/json"), errorBodyStr);

                    Response error = null;
                    error = errorConverter.convert(errorBody);

                    errorMessage = error.status == null? message:error.status;
                }

                Log.i(TAG, "status: "+errorMessage);
            }catch (Exception e1){
//                e1.printStackTrace();
                Log.i(TAG, "error: "+e1.getMessage());
                errorMessage = message;
            }
        }

        return errorMessage;
    }

    public static void logout(Context mContext, String errorMessage){
        Log.e(GlobalEntities.UTILS_CLASS_TAG, "logout: 1");
        if(errorMessage.contains("token") || errorMessage.contains("user_not_found")){
            Log.e(GlobalEntities.UTILS_CLASS_TAG, "logout: 2");

            PreferencesHelper.getInstance().clear();

            Intent i = LandingPage.getStartIntent(mContext);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
//            finish();
        }
    }

    public static String formatPhoneNumber(String phone){
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneProto = phoneUtil.parse(phone, "CH");
            phone = phoneUtil.format(phoneProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
        } catch (NumberParseException e) {
            Log.e(GlobalEntities.UTILS_CLASS_TAG, "NumberParseException was thrown: " + e.toString());
        }finally {
            return phone;
        }
    }

    public static void makeDialog(Context context, String title, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setCancelable(true)
                .setMessage(message)
                .setTitle(title)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    public static void hideSoftKeyboard(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void copyToClipboard(Context context, String text, String label){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time*1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    public static boolean hasActiveInternetConnection(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection url = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                url.setRequestProperty("User-Agent", "Test");
                url.setRequestProperty("Connection", "close");
                url.setConnectTimeout(1500);
                url.connect();
                return (url.getResponseCode() == 204 && url.getContentLength() == 0);
            } catch (IOException e) {
                Log.e(GlobalEntities.UTILS_CLASS_TAG, "Error checking internet connection", e);
            }
        } else {
            Log.d(GlobalEntities.UTILS_CLASS_TAG, "No network available!");
        }
        return false;
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static String saveToInternalStorage(Context context, Bitmap image, String Filename){
        ContextWrapper cw = new ContextWrapper(context);

        File directory = cw.getDir(GlobalEntities.APP_DIR_TAG, Context.MODE_PRIVATE);


        Log.i(GlobalEntities.UTILS_CLASS_TAG, "saveToInternalStorage: "+directory + Filename);

//        final File dir = new File(context.getFilesDir() + Filename);
//        dir.mkdirs(); //create folders where write files
//        final File file = new File(dir, "BlockForTest.txt");
        File imagePath = new File(directory, Filename);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imagePath);
//            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            image.compress(Bitmap.CompressFormat.JPEG, 30, fos);

            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return directory.getAbsolutePath();
    }

    public static Boolean isImageExistInStorage(Context mContext, String filename){
        ContextWrapper cw = new ContextWrapper(mContext);
        File dir = cw.getDir(GlobalEntities.APP_DIR_TAG, Context.MODE_PRIVATE);

        File file = new File(dir, filename);
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            return inputStream!=null;
        }
    }

    public static Bitmap loadImageFromStorage(Context mContext, String filename){
        ContextWrapper cw = new ContextWrapper(mContext);
        File dir = cw.getDir(GlobalEntities.APP_DIR_TAG, Context.MODE_PRIVATE);

        Bitmap image = null;
        File file = new File(dir, filename);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            image = BitmapFactory.decodeStream(inputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            return image;
        }
    }

    public static int randInt(int min, int max) {
        Random rand = new Random(System.currentTimeMillis());
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public static String getPath(Context context, Uri uri) {
        // just some safety built in
        if( uri == null ) {
            Log.i(GlobalEntities.UTILS_CLASS_TAG, "Uri is null");
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        cursor.close();
        return uri.getPath();
    }
}
