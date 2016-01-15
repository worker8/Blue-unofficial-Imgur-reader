package worker8.com.github.imgurdiscovery.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.MediaController;

public class MediaControllerWithCallback extends MediaController {
    public OnClickListener mOnShowListener, mOnHideListener;

    public MediaControllerWithCallback(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MediaControllerWithCallback(Context context, boolean useFastForward) {
        super(context, useFastForward);
    }

    public MediaControllerWithCallback(Context context) {
        super(context);
    }

    @Override
    public void hide() {
        super.hide();
        if (mOnHideListener != null) {
            mOnHideListener.onClick(this);
        }
    }

    @Override
    public void show() {
        super.show();
        if (mOnShowListener != null) {
            mOnShowListener.onClick(this);
        }
    }
}
