package mu.node.rexweather.app;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import mu.node.rexweather.app.Helpers.TemperatureFormatter;
import mu.node.rexweather.app.Models.CurrentWeather;
import mu.node.rexweather.app.Models.WeatherForecast;
import mu.node.rexweather.app.Services.LocationService;
import mu.node.rexweather.app.Services.WeatherService;
import mu.node.rexweather.app.ViewModels.WeatherViewModel;
import org.apache.http.HttpException;
import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Weather Fragment.
 * <p>
 * Displays the current weather as well as a 7 day forecast for our location. Data is loaded
 * from a web service.
 */
public class WeatherFragment extends Fragment {

    private static final String KEY_CURRENT_WEATHER = "key_current_weather";
    private static final String KEY_WEATHER_FORECASTS = "key_weather_forecasts";
    private static final long LOCATION_TIMEOUT_SECONDS = 20;

    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mLocationNameTextView;
    private TextView mCurrentTemperatureTextView;
    private ListView mForecastListView;
    private TextView mAttributionTextView;

    private WeatherViewModel weatherViewModel;

    PublishSubject<WeatherViewModel.RefreshEvent> refreshPublisher = PublishSubject.create();

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        mLocationNameTextView = (TextView) rootView.findViewById(R.id.location_name);
        mCurrentTemperatureTextView = (TextView) rootView
                .findViewById(R.id.current_temperature);

        // Set up list view for weather forecasts.
        mForecastListView = (ListView) rootView.findViewById(R.id.weather_forecast_list);
        final WeatherForecastListAdapter adapter = new WeatherForecastListAdapter(this,
                new ArrayList<>(), getActivity());
        mForecastListView.setAdapter(adapter);

        mAttributionTextView = (TextView) rootView.findViewById(R.id.attribution);
        mAttributionTextView.setVisibility(View.INVISIBLE);

        // Set up swipe refresh layout.
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView
                .findViewById(R.id.swipe_refresh_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.brand_main,
                android.R.color.black,
                R.color.brand_main,
                android.R.color.black);

        mSwipeRefreshLayout.setOnRefreshListener(() ->
                        refreshPublisher.onNext(new WeatherViewModel.RefreshEvent())
        );

        setupRefreshStream();
        return rootView;
    }

    private void setupRefreshStream() {
        weatherViewModel = new WeatherViewModel();

        weatherViewModel.setRefreshSignal(refreshPublisher)
                .whenGetCurrentWeather()
                .subscribe(currentWeather -> {
                            mLocationNameTextView.setText(currentWeather.getLocationName());
                            mCurrentTemperatureTextView.setText(
                                    TemperatureFormatter.format(currentWeather.getTemperature()));
                        },
                        Throwable::printStackTrace);

        weatherViewModel.whenGetCurrentWeather()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currentWeather -> {
                    if (currentWeather != null)
                        mSwipeRefreshLayout.setRefreshing(false);
                });

        weatherViewModel.whenGetWeatherForecasts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weatherForecasts -> {
                    final WeatherForecastListAdapter adapter = (WeatherForecastListAdapter)
                            mForecastListView.getAdapter();
                    adapter.clear();
                    adapter.addAll(weatherForecasts);
                    adapter.notifyDataSetChanged();
                });
    }


    @Override
    public void onDestroy() {
        mCompositeSubscription.unsubscribe();
        super.onDestroy();
    }

}
