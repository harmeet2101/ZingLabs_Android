package com.zing.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.zing.activity.LoginActivity;

/**
 * Created by savita on 2/5/18.
 */

public class SessionManagement {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "zinglabs";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_DEVICE_TOKEN = "token_id";

    public static final String KEY_API_TOKEN = "api_token";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_DATA_UPDATED = "data_updated";
    public static final String KEY_USER_TOKEN = "user_token";
    public static final String KEY_STATUS = "status";
    public static final String KEY_STREET_ADDRESS = "add";
    public static final String KEY_STATE = "state";
    public static final String KEY_ZIPCODE = "zip";
    public static final String KEY_SSN = "ssn";
    public static final String KEY_PROFILE_PIC = "pic";
    public static final String KEY_APT = "apt";

    public static final String KEY_FROM = "from";
    public static final String KEY_SHIFT_ID = "shift_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_DAY = "day";
    public static final String KEY_EXPECTED_EARNING = "expected_earning";
    public static final String KEY_TIME_SLOT = "time_slot";
    public static final String KEY_LOCATION = "loc";
    public static final String KEY_ROLE = "role";
    public static final String KEY_MANAGER_NUMBER = "manager_number";
    public static final String KEY_REGISTRATION_DATE = "registration_date";
    public static final String KEY_IMAGE = "img";

    public static final String KEY_COUNTRY_NAME = "country_name";
    public static final String KEY_COUNTRY_ID = "country_id";

    public static final String KEY_NEXT_SHIFT = "nextShiftId";

    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setDialogData(String from, String shift_id, String date, String day,
                              String expectedEarning, String timeSlot, String location, String role) {
        editor.putString(KEY_FROM, from);
        editor.putString(KEY_SHIFT_ID, shift_id);
        editor.putString(KEY_DATE, date);
        editor.putString(KEY_DAY, day);
        editor.putString(KEY_EXPECTED_EARNING, expectedEarning);
        editor.putString(KEY_TIME_SLOT, timeSlot);
        editor.putString(KEY_LOCATION, location);
        editor.putString(KEY_ROLE, role);
        editor.commit();
    }

    public void setManagerNumber(String number) {
        editor.putString(KEY_MANAGER_NUMBER, number);
        editor.commit();
    }

    public void setImage(String img) {
        editor.putString(KEY_IMAGE, img);
        editor.commit();
    }

    public void setRegistrationDate(String date) {
        editor.putString(KEY_REGISTRATION_DATE, date);
        editor.commit();
    }

    public String getFrom() {
        return pref.getString(KEY_FROM, "");
    }
    public String getSsn() {
        return pref.getString(KEY_SSN, "");
    }

    public String getImage() {
        return pref.getString(KEY_IMAGE, "");
    }

    public String getRegistrationDate() {
        return pref.getString(KEY_REGISTRATION_DATE, "");
    }

    public String getManagerNumber() {
        return pref.getString(KEY_MANAGER_NUMBER, "");
    }

    public String getShiftId() {
        return pref.getString(KEY_SHIFT_ID, "");
    }

    public String getDate() {
        return pref.getString(KEY_DATE, "");
    }

    public String getDay() {
        return pref.getString(KEY_DAY, "");
    }

    public String getExpectedEarning() {
        return pref.getString(KEY_EXPECTED_EARNING, "");
    }

    public String getTimeSlot() {
        return pref.getString(KEY_TIME_SLOT, "");
    }

    public String getLocation() {
        return pref.getString(KEY_LOCATION, "");
    }

    public String getRole() {
        return pref.getString(KEY_ROLE, "");
    }

    public void setUserData(String user_id, String first_name, String last_name, String phone,
                            String data_updated, String user_token, int status, String apt,
                            String streetAddress, String state, String zipCode, String ssn,
                            String profilePic, String password,String countryName,String countryId) {
        editor.putString(KEY_USER_ID, user_id);
        editor.putString(KEY_FIRST_NAME, first_name);
        editor.putString(KEY_LAST_NAME, last_name);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_DATA_UPDATED, data_updated);
        editor.putString(KEY_USER_TOKEN, user_token);
        editor.putInt(KEY_STATUS, status);
        editor.putString(IS_LOGIN, "1");
        editor.putString(KEY_APT, apt);
        editor.putString(KEY_STREET_ADDRESS, streetAddress);
        editor.putString(KEY_STATE, state);
        editor.putString(KEY_ZIPCODE, zipCode);
        editor.putString(KEY_SSN, ssn);
        editor.putString(KEY_PROFILE_PIC, profilePic);
        editor.putString(KEY_PASSWORD, password);

        editor.putString(KEY_COUNTRY_ID, countryId);
        editor.putString(KEY_COUNTRY_NAME, countryName);
        editor.commit();
    }

    public void setDeviceId(String refreshedToken) {
        editor.putString(KEY_DEVICE_TOKEN, refreshedToken);
        editor.commit();
    }

    public String getDeviceToken() {
        return pref.getString(KEY_DEVICE_TOKEN, "");
    }
    public String getPassword() {
        return pref.getString(KEY_PASSWORD, "");
    }

    public String getProfilePic() {
        return pref.getString(KEY_PROFILE_PIC, "");
    }

    public String getUserId() {
        return pref.getString(KEY_USER_ID, "");
    }

    public String getUserFirstName() {
        return pref.getString(KEY_FIRST_NAME, "");
    }

    public String getUserApt() {
        return pref.getString(KEY_APT, "");
    }

    public String getUserZip() {
        return pref.getString(KEY_ZIPCODE, "");
    }

    public String getUserState() {
        return pref.getString(KEY_STATE, "");
    }

    public String getUserAddress() {
        return pref.getString(KEY_STREET_ADDRESS, "");
    }

    public String getUserPhone() {
        return pref.getString(KEY_PHONE, "");
    }

    public String getUserLastName() {
        return pref.getString(KEY_LAST_NAME, "");
    }

    public String getUserToken() {
        return pref.getString(KEY_USER_TOKEN, "");
    }

    public String getCountryName() {
        return pref.getString(KEY_COUNTRY_NAME, "");
    }
    public String getKeyCountryId() {
        return pref.getString(KEY_COUNTRY_ID, "");
    }


    public void clearSession() {
        editor.putString(IS_LOGIN, "0");
        editor.commit();
    }

    public void logoutUser() {
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public String isLoggedIn() {
        return pref.getString(IS_LOGIN, "0");
    }


    public void setNextShift(String str) {
        editor.putString(KEY_NEXT_SHIFT, str);
        editor.commit();
    }

    public String getNextShift() {
        return pref.getString(KEY_NEXT_SHIFT, "");
    }

}
