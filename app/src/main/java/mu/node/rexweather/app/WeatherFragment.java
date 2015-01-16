package mu.node.rexweather.app;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import mu.node.rexweather.app.Helpers.TemperatureFormatter;
import mu.node.rexweather.app.ViewModels.WeatherViewModel;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

import java.util.ArrayList;

/**
 * Weather Fragment.
 * <p>
 * Displays the current weather as well as a 7 day forecast for our location. Data is loaded
 * from a web service.
 */
public class WeatherFragment extends Fragment {

    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mLocationNameTextView;
    private TextView mCurrentTemperatureTextView;
    private ListView mForecastListView;
    private TextView mAttributionTextView;

    PublishSubject<WeatherViewModel.RefreshEvent> refreshPublisher = PublishSubject.create();

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        final View rootView = initilizeUI(inflater, container);

        mSwipeRefreshLayout.setOnRefreshListener(() ->
                        refreshPublisher.onNext(new WeatherViewModel.RefreshEvent())
        );

        setupRefreshStream();
        return rootView;
    }

    private View initilizeUI(LayoutInflater inflater, ViewGroup container) {
        final View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        mLocationNameTextView = (TextView) rootView.findViewById(R.id.location_name);
        mCurrentTemperatureTextView = (TextView) rootView.findViewById(R.id.current_temperature);

        // Set up list view for weather forecasts.
        mForecastListView = (ListView) rootView.findViewById(R.id.weather_forecast_list);
        final WeatherForecastListAdapter adapter = new WeatherForecastListAdapter(this, new ArrayList<>(), getActivity());
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

        return rootView;
    }

    private void setupRefreshStream() {
        WeatherViewModel weatherViewModel = new WeatherViewModel();

        weatherViewModel.setRefreshSignal(refreshPublisher);
        mCompositeSubscription.add(
                weatherViewModel.whenGetCurrentWeather()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(currentWeather -> {
                                    mLocationNameTextView.setText(currentWeather.getLocationName());
                                    mCurrentTemperatureTextView.setText(
                                            TemperatureFormatter.format(currentWeather.getTemperature()));
                                },
                                Throwable::printStackTrace));

        mCompositeSubscription.add(
                weatherViewModel.whenGetCurrentWeather()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(currentWeather -> {
                            if (currentWeather != null)
                                mSwipeRefreshLayout.setRefreshing(false);
                        }));

        mCompositeSubscription.add(
                weatherViewModel.whenGetWeatherForecasts()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(weatherForecasts -> {
                            final WeatherForecastListAdapter adapter = (WeatherForecastListAdapter)
                                    mForecastListView.getAdapter();
                            adapter.clear();
                            adapter.addAll(weatherForecasts);
                            adapter.notifyDataSetChanged();
                        }));
    }


    @Override
    public void onDestroy() {
        mCompositeSubscription.unsubscribe();
        super.onDestroy();
    }

}
