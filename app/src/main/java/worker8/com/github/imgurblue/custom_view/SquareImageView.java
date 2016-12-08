package worker8.com.github.imgurblue.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * This is an {@link ImageView} that will ignore the height, it will always be a square, following the width spec
 */
public class SquareImageView extends ImageView {

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
