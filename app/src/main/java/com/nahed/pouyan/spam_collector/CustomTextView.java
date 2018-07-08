package com.nahed.pouyan.spam_collector;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by pouyan on 3/15/18.
 */

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {

    private Context mContext;

    public CustomTextView(Context context) {
        super(context);
        init(context);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context mContext){
        this.mContext = mContext;

        setTypeface(Core.getInstance().getApplicationFont());
    }

}
