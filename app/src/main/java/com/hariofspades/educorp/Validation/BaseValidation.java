package com.hariofspades.educorp.Validation;

import android.content.Context;




abstract class BaseValidation implements Validation {

    protected Context mContext;

    protected BaseValidation(Context context) {
        mContext = context;
    }

}