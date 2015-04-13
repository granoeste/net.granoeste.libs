
package net.granoeste.scaffold.sample.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.inject.Inject;

import net.granoeste.scaffold.sample.R;
import net.granoeste.scaffold.sample.logic.Weather;
import net.granoeste.scaffold.app.ScaffoldFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import static net.granoeste.commons.util.LogUtils.LOGV;
import static net.granoeste.commons.util.LogUtils.makeLogTag;

@EFragment(R.layout.fragment_main)
public class MainFragment extends ScaffoldFragment implements OnClickListener {
    @SuppressWarnings("unused")
    private static final String TAG = makeLogTag(MainFragment.class);
    private final MainFragment self = this;

    /**
     * Injective Logic
     */
    @Inject
    private Weather mWeather;

    @ViewById(R.id.text_forecast)
    public TextView mTextForecast;
    @ViewById(R.id.button_prev)
    public Button mButtonPrev;
    @ViewById(R.id.button_next)
    public Button mButtonNext;

    @FragmentArg
    String myTitle;
    @FragmentArg
    String myMessage;


    public Weather getWeather() {
        return mWeather;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void buildView() {
        LOGV(TAG, "buildView()");
		mButtonPrev.setOnClickListener(this);
		mButtonNext.setOnClickListener(this);
		if (mWeather != null) {
			mTextForecast.setText(mWeather.forecast());
		}
	}

	@Override
	public void onClick(View v) {
		if (v.equals(mButtonNext)) {
			mWeather.next();
		} else if (v.equals(mButtonPrev)) {
			mWeather.prev();
		}
		mTextForecast.setText(mWeather.forecast());
	}

}
