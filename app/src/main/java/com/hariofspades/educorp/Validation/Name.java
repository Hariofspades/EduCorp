package com.hariofspades.educorp.Validation;

import android.content.Context;
import android.text.TextUtils;

import com.hariofspades.educorp.R;



public class Name extends BaseValidation {

    private static final String EMAIL_PATTERN = "[a-zA-Z. ]*";

    private Name(Context context) {
        super(context);
    }

    public static Validation build(Context context) {
        return (Validation) new Name(context);
    }

    @Override
    public String getErrorMessage() {
        return mContext.getString(R.string.valid_name);
    }

    @Override
    public boolean isValid(String text) {
        if(text.equals(""))
            return !TextUtils.isEmpty(text);
        else
            return text.matches(EMAIL_PATTERN);
    }
}
