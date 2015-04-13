
package net.granoeste.scaffold.sample.app;

import android.util.Log;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import static net.granoeste.commons.util.LogUtils.LOGV;
import static net.granoeste.commons.util.LogUtils.makeLogTag;

import net.granoeste.commons.util.LogUtils;
import net.granoeste.scaffold.di.config.BaseModule;
import net.granoeste.scaffold.sample.BuildConfig;
import net.granoeste.scaffold.app.ScaffoldApplication;
import net.granoeste.scaffold.sample.di.config.ProductionModule;

import org.androidannotations.annotations.EApplication;

@EApplication
public class MainApplication extends ScaffoldApplication {
    @SuppressWarnings("unused")
    private static String TAG;
    private final MainApplication self = this;

    public MainApplication() {
        LogUtils.LOG_PREFIX = "scaf_";
        LogUtils.DEBUG = BuildConfig.DEBUG;
        TAG = makeLogTag(MainApplication.class);
    }

    private static final String CLASS_NAME="net.granoeste.scaffold.sample.di.config.DevelModule";

	@Override
	protected Injector newInjector() {
		LOGV(TAG, "newInjector()");
        BaseModule module = null;
		if (BuildConfig.DEBUG) {
			//デバッグ環境では開発用モジュール(DevelModule)を使う
			try {
				@SuppressWarnings("unchecked")
				Class<BaseModule> cls = (Class<BaseModule>) Class.forName(CLASS_NAME);
				module = cls.newInstance();

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}
		if (module == null) {
			//本番環境では本番用のモジュールを使う
			module = new ProductionModule();
		}

        LOGV(TAG, "Module:" + module);

        //Injectorを生成して返す
		return Guice.createInjector(module);
	}

}
