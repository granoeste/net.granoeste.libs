package net.granoeste.scaffold.sample.app;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import net.granoeste.scaffold.app.ScaffoldLifecycleListener;

import java.util.List;

import static net.granoeste.commons.util.LogUtils.LOGV;
import static net.granoeste.commons.util.LogUtils.makeLogTag;

public class DeviceSensorListener extends ScaffoldLifecycleListener implements SensorEventListener {
    public static final String TAG = makeLogTag(DeviceSensorListener.class);

    private SensorManager mSensorManager;
    private boolean mIsMagSensor = false;
    private boolean mIsAccSensor = false;


    @Override
    public void onCreated(Context context) {
        // SensorManagerのインスタンスを取得
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public void onStarted(Context context) {
        // センサーの取得
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        // センサーマネージャーにイベントリスナーを登録
        for (Sensor sensor : sensors) {
            // 地磁気
            if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
                mIsMagSensor = true;
            }
            // 加速度
            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
                mIsAccSensor = true;
            }
        }

    }

    @Override
    public void onStopped(Context context) {
        // センサーマネージャーからイベントリスナーを解除
        if (mIsMagSensor || mIsAccSensor) {
            mSensorManager.unregisterListener(this);
            mIsMagSensor = false;
            mIsAccSensor = false;
        }

    }

    @Override
    public void onDestroyed(Context context) {

    }

    // ------------------------------------------------------------------------
    // SensorEventListener
    // ------------------------------------------------------------------------
    private static final int MATRIX_SIZE = 16;
    /* 回転行列 */
    float[] inR = new float[MATRIX_SIZE];
    float[] outR = new float[MATRIX_SIZE];
    float[] I = new float[MATRIX_SIZE];

    /* センサーの値 */
    float[] orientationValues = new float[3];
    float[] magneticValues = new float[3];
    float[] accelerometerValues = new float[3];

    int rotation;
    int z;
    int x;
    int y;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }

        switch (event.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                magneticValues = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerValues = event.values.clone();
                break;
        }

        if (magneticValues != null && accelerometerValues != null) {

            SensorManager.getRotationMatrix(inR, I, accelerometerValues, magneticValues);

            // Activityの表示が縦固定の場合。横向きになる場合、修正が必要です
            // デバイス軸と実世界の軸をマッピングする。
            // https://www.evernote.com/shard/s9/sh/3ccae460-038b-4ac6-8435-d65875ee7388/4b8689c52f966241722e994ad77ad1fc
            // https://www.evernote.com/shard/s9/sh/d156e4e2-331c-4019-a7c1-d60f3a6ac31f/e884332cea159a60f2d15f438c8627cc
            // http://kamoland.com/wiki/wiki.cgi?TYPE_ORIENTATION%A4%F2%BB%C8%A4%EF%A4%BA%A4%CB%CA%FD%B0%CC%B3%D1%A4%F2%BC%E8%C6%C0
            // Portrait 縦
//			SensorManager.remapCoordinateSystem(
//					inR, SensorManager.AXIS_X, SensorManager.AXIS_Z, outR);
            // Landscape 横
//			SensorManager.remapCoordinateSystem(
//					inR, SensorManager.AXIS_X, SensorManager.AXIS_Y, outR);
            SensorManager.remapCoordinateSystem(
                    inR, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, outR);

            SensorManager.getOrientation(outR, orientationValues);

//			LOGV(TAG, radianToDegree(orientationValues[0]) + ", " + // Z軸方向, azimuth
//					radianToDegree(orientationValues[1]) + ", " + // X軸方向, pitch
//					radianToDegree(orientationValues[2])); // Y軸方向, roll
            // Z軸方向, azimuth = 方向:水平で無いとズレる。
            // X軸方向, pitch = 前後
            // Y軸方向, roll = 左右
			LOGV(TAG, String.format("Z:%4d X:%4d Y:%4d",
					radianToDegree(orientationValues[0]),
					radianToDegree(orientationValues[1]),
                    radianToDegree(orientationValues[2])));

            this.z = radianToDegree(orientationValues[0]);
            this.x = radianToDegree(orientationValues[1]);
            this.y = radianToDegree(orientationValues[2]);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // NOP
    }

    // ------------------------------------------------------------------------
    // Utility
    // ------------------------------------------------------------------------
    int radianToDegree(float rad) {
        return (int) Math.floor(Math.toDegrees(rad));
    }
}
