package com.udylity.sensorific;

import android.annotation.TargetApi;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class SensDetailActivity extends AppCompatActivity implements SensorEventListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sens_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(SensDetailFragment.ARG_ITEM_ID, getIntent()
                    .getStringExtra(SensDetailFragment.ARG_ITEM_ID));
            arguments.putInt(SensDetailFragment.ARG_POSITION_ID, getIntent()
                    .getIntExtra(SensDetailFragment.ARG_POSITION_ID, 0));
            SensDetailFragment fragment = new SensDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sens_detail_container, fragment).commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                    NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        SensDetailFragment.setAccuracy(accuracy);
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        SensDetailFragment.setEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SensDetailFragment.updateSensorType();
        SensListActivity.mSensorManager.registerListener(this, SensDetailFragment.mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SensDetailFragment.removeSensorType();
        SensListActivity.mSensorManager.unregisterListener(this);
    }
}
