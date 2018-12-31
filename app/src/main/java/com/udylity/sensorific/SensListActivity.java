package com.udylity.sensorific;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class SensListActivity extends AppCompatActivity
        implements SensListFragment.Callbacks {

    public static SensorManager mSensorManager;
    public static List<Sensor> deviceSensors;

    private static boolean mTwoPane;

    private SensDetailFragment mSensDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        setContentView(R.layout.activity_sens_list);

        if (findViewById(R.id.sens_detail_container) != null) {
            mTwoPane = true;
            ((SensListFragment) getSupportFragmentManager().findFragmentById(
                    R.id.sens_list)).setActivateOnItemClick();
        }

    }

    @Override
    public void onItemSelected(String name, int position) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(SensDetailFragment.ARG_ITEM_ID, name);
            arguments.putInt(SensDetailFragment.ARG_POSITION_ID, position);
            mSensDetailFragment = new SensDetailFragment();
            mSensDetailFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.sens_detail_container, mSensDetailFragment).commit();

        } else {
            Intent detailIntent = new Intent(this, SensDetailActivity.class);
            detailIntent.putExtra(SensDetailFragment.ARG_ITEM_ID, name);
            detailIntent.putExtra(SensDetailFragment.ARG_POSITION_ID, position);
            startActivity(detailIntent);
        }
    }

}
