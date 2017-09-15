package utility;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by pc on 9/13/2016.
 */
public class PersianNumberTextView extends TextView {
    public PersianNumberTextView(Context context) {
        super(context);
        init(context, null, 0);
    }


    public PersianNumberTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs, 0);
    }

    public PersianNumberTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text!=null && text.length() >0)
            text = Utility.toPersianNumber(text.toString());
        super.setText(text, type);
    }

    private void init(Context context, AttributeSet attrs, int defStyle){
        try {
            CharSequence text = getText();
            if (text!=null && text.length() >0)
                setText(Utility.toPersianNumber(text.toString()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(){
        try {
            CharSequence text = getText();
            if (text!=null && text.length() >0)
                setText(Utility.toPersianNumber(text.toString()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

