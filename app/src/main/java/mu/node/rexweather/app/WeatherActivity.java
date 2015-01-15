package mu.node.rexweather.app;

import android.app.Activity;
import android.os.Bundle;

/**
 * Weather Activity.
 *
 * This is the main activity for our app. It simply serves as a container for the Weather Fragment.
 * We prefer to build our implementation in a fragment because that enables future reuse if, for
 * example we build a tablet version of this app.
 */
public class WeatherActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new WeatherFragment())
                    .commit();
        }
    }

}
