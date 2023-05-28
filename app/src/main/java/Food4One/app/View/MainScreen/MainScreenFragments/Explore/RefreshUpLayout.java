package Food4One.app.View.MainScreen.MainScreenFragments.Explore;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class RefreshUpLayout extends SwipeRefreshLayout {
    private float startY;

    public RefreshUpLayout(@NonNull Context context) {
        super(context);
    }
    public RefreshUpLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentY = ev.getY();
                float deltaY = currentY - startY;

                // Personaliza la distancia y dirección del desplazamiento para activar el gesto de actualización
                if (deltaY > 0 && deltaY > ViewConfiguration.getTouchSlop()) {
                    return super.onInterceptTouchEvent(ev);
                }
                break;
        }

        return false;
    }
}

