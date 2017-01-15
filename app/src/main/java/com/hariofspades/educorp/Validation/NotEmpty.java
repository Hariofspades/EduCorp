package com.hariofspades.educorp.Validation;

import android.content.Context;
import android.text.TextUtils;

import com.hariofspades.educorp.R;


public class NotEmpty extends BaseValidation {

    public static NotEmpty build(Context context) {
        return new NotEmpty(context);
    }

    private NotEmpty(Context context) {
        super(context);
    }

    @Override
    public String getErrorMessage() {
        return mContext.getString(R.string.zvalidations_empty);
    }

    @Override
    public boolean isValid(String text) {
        return !TextUtils.isEmpty(text);
    }
}