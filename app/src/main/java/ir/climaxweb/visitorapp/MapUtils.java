package ir.climaxweb.visitorapp;

import android.app.Activity;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.Interpolator;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.Marker;

public class MapUtils
{

    // private static Context mContext;
//    private static final int PATTERN_DASH_LENGTH_PX = 50;
//    private static final int PATTERN_GAP_LENGTH_PX = 20;
//    private static final Dot DOT = new Dot();
//    private static final Dash DASH = new Dash(PATTERN_DASH_LENGTH_PX);
//    private static final Gap GAP = new Gap(PATTERN_GAP_LENGTH_PX);
//    private static final List<PatternItem> PATTERN_DOTTED = Arrays.asList(DOT, GAP);
//    private static final List<PatternItem> PATTERN_DASHED = Arrays.asList(DASH, GAP);
//    private static final List<PatternItem> PATTERN_MIXED = Arrays.asList(DOT, GAP, DASH, GAP);

//    public enum patternType
//    {
//        DEFAULT, DOT, DASH, MIXED
//    }

//    public static void initialize(Context context)
//    {
//        mContext = context;
//    }

    public static boolean checkPlayServices(Activity activity)
    {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(activity);
        if (result == ConnectionResult.SUCCESS)
            return true;
        else
        {
            if (googleAPI.isUserResolvableError(result))
                googleAPI.getErrorDialog(activity, result, 9000).show();
            return false;
        }
    }

//    public static boolean checkMapIsReady(GoogleMap mMap)
//    {
//        if (mMap == null)
//        {
//            Toast.makeText(mContext, "Map Is Not Ready Yet", Toast.LENGTH_LONG).show();
//            return false;
//        }
//        else
//            return true;
//    }

    public static void animateMarker(final Marker marker, final long duration, final Interpolator interpolator)
    {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                long elapsed = SystemClock.uptimeMillis() - start;
                float x = 1 - interpolator.getInterpolation((float) elapsed / duration);
                Log.i("myMapLog", "X = " + x + "");
                float t = Math.max(x, 0);
                marker.setAnchor(0.5f, 1.0f + 2 * t);

                if (t > 0.0)
                {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

//    public static List<PatternItem> getPattern(patternType type)
//    {
//        switch (type)
//        {
//            case DEFAULT:
//                return null;
//            case DOT:
//                return PATTERN_DOTTED;
//            case DASH:
//                return PATTERN_DASHED;
//            case MIXED:
//                return PATTERN_MIXED;
//            default:
//                return null;
//        }
//    }

//    public static void setPolylineColor(Polyline polyline, final int progress)
//    {
//        polyline.setColor(Color.HSVToColor(Color.alpha(polyline.getColor()), new float[]{progress, 1, 1}));
//    }

//    public static void setPolylineAlpha(Polyline polyline, int alphaProgress)
//    {
//        float[] prevHSV = new float[3];
//        Color.colorToHSV(polyline.getColor(), prevHSV);
//        polyline.setColor(Color.HSVToColor(alphaProgress, prevHSV));
//    }
//
//    public static void setPolygonFillColor(Polygon polygon, int progress)
//    {
//        polygon.setFillColor(Color.HSVToColor(Color.alpha(polygon.getFillColor()), new float[]{progress, 1, 1}));
//    }

//    public static void setPolygonFillAlpha(Polygon polygon, int progress)
//    {
//        int prevColor = polygon.getFillColor();
//        polygon.setFillColor(Color.argb(progress, Color.red(prevColor), Color.green(prevColor), Color.blue(prevColor)));
//    }
//
//    public static void setPolygonStrokeColor(Polygon polygon, int progress)
//    {
//        polygon.setStrokeColor(Color.HSVToColor(Color.alpha(polygon.getStrokeColor()), new float[]{progress, 1, 1}));
//    }

//    public static void setPolygonStrokeAlpha(Polygon polygon, int progress)
//    {
//        int prevColorArgb = polygon.getStrokeColor();
//        polygon.setStrokeColor(Color.argb(progress, Color.red(prevColorArgb), Color.green(prevColorArgb), Color.blue(prevColorArgb)));
//    }
//
//
//    public static void setCircleFillColor(Circle circle, int progress)
//    {
//        circle.setFillColor(Color.HSVToColor(Color.alpha(circle.getFillColor()), new float[]{progress, 1, 1}));
//    }
//
//    public static void setCircleFillAlpha(Circle circle, int progress)
//    {
//        int prevColor = circle.getFillColor();
//        circle.setFillColor(Color.argb(progress, Color.red(prevColor), Color.green(prevColor), Color.blue(prevColor)));
//    }
//
//    public static void setCircleStrokeColor(Circle circle, int progress)
//    {
//        circle.setStrokeColor(Color.HSVToColor(Color.alpha(circle.getStrokeColor()), new float[]{progress, 1, 1}));
//    }
//
//    public static void setCircleStrokeAlpha(Circle circle, int progress)
//    {
//        int prevColorArgb = circle.getStrokeColor();
//        circle.setStrokeColor(Color.argb(progress, Color.red(prevColorArgb), Color.green(prevColorArgb), Color.blue(prevColorArgb)));
//    }
}
