package com.udylity.sensorific;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class SensDetailFragment extends Fragment{

    private static TextView nameTV, typeTV, versionTV, vendorTV, powerTV, rawDataTV, parsedDataTV, accuracyTV;

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_POSITION_ID = "pos_id";

    private static int position;

    public static Sensor mSensor;

    private static String mSensorName;
    private static String mSensorVendor;
    private static String mSensorRawData = "None";
    private static String mSensorParsedData;
    private static int mSensorType;
    private static int mSensorVersion;
    private static int mSensorAccuracy;
    private static float mSensorPower;

    private static boolean isLightSensor = false;
    private static boolean isProximitySensor = false;
    private static boolean isMagneticSensor = false;
    private static boolean isPressureSensor = false;
    private static boolean isAccelerometerSensor = false;
    private static boolean isGyroscopeSensor = false;
    private static boolean isGravitySensor = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SensListActivity mSensListActivity = new SensListActivity();
        position = getArguments().getInt(ARG_POSITION_ID);

        mSensor = mSensListActivity.deviceSensors.get(position);

        updateSensorType();

        mSensorName =  mSensor.getName();
        mSensorType = mSensor.getType();
        mSensorVersion = mSensor.getVersion();
        mSensorVendor = mSensor.getVendor();
        mSensorPower = mSensor.getPower();
    }

    public static void updateSensorType(){
        switch (mSensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                isAccelerometerSensor = true;
                break;
            case Sensor.TYPE_LIGHT:
                isLightSensor = true;
                break;
            case Sensor.TYPE_PROXIMITY:
                isProximitySensor = true;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                isMagneticSensor = true;
                break;
            case Sensor.TYPE_PRESSURE:
                isPressureSensor = true;
                break;
            case Sensor.TYPE_GYROSCOPE:
                isGyroscopeSensor = true;
                break;
            case Sensor.TYPE_GRAVITY:
                isGravitySensor = true;
                break;
            default:
                removeSensorType();
                break;
        }

    }
    public static void removeSensorType(){
        isLightSensor = false;
        isProximitySensor = false;
        isMagneticSensor = false;
        isPressureSensor = false;
        isAccelerometerSensor = false;
        isGyroscopeSensor = false;
        isGravitySensor = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sens_detail, container, false);

        if (rootView != null) {
            nameTV = (TextView) rootView.findViewById(R.id.sensorname);
            typeTV = (TextView) rootView.findViewById(R.id.sensortype);
            versionTV = (TextView) rootView.findViewById(R.id.sensorversion);
            vendorTV = (TextView) rootView.findViewById(R.id.sensorvender);
            powerTV = (TextView) rootView.findViewById(R.id.sensorpower);
            rawDataTV = (TextView) rootView.findViewById(R.id.sensorrawdata);
            parsedDataTV = (TextView) rootView.findViewById(R.id.sensorparseddata);
            accuracyTV = (TextView) rootView.findViewById(R.id.sensoraccuracy);

            nameTV.setText("Name: " + mSensorName);
            typeTV.setText("Type: " + mSensorType);
            versionTV.setText("Version: " + mSensorVersion);
            vendorTV.setText("Vendor: " + mSensorVendor);
            powerTV.setText("Power: " + mSensorPower + "mA");
        }

        return rootView;
    }

    public static void setAccuracy(int accuracy){
        mSensorAccuracy = accuracy;
        accuracyTV.setText("Accuracy: " + mSensorAccuracy);
    }

    public static void setEvent(SensorEvent event){
        setData(event.values);
    }

    private static void setData(float data[]){
        updateSensorType();
        if(isLightSensor){
            mSensorRawData = Float.toString(data[0]) + " lux";
        }else if(isProximitySensor){
            if (data[0] == mSensor.getMaximumRange()){
                mSensorRawData = "Far";
            }else{
                mSensorRawData = "Close";
            }
        }else if(isMagneticSensor){
            orientationData(data);
        }else if(isPressureSensor){
            mSensorRawData = Float.toString(data[0]) + "kPa";
        }else if(isAccelerometerSensor){
            orientationData(data);
            parseAccelerometerData(data);
        }else if(isGyroscopeSensor){
            orientationData(data);
        }else if(isGravitySensor){
            orientationData(data);
        }else{
            mSensorRawData = "Nothing";
        }
        rawDataTV.setText("Data: " + mSensorRawData);
    }

   private static void orientationData(float data[]){
       mSensorRawData =  "\n"  +
               "X: " + Float.toString(data[0]) + "\n" +
               "Y: " + Float.toString(data[1]) + "\n" +
               "Z: " + Float.toString(data[2]);
    }

    private static void parseAccelerometerData(float data[]){
        if(data[0] >= 3){
            //+X
            mSensorParsedData = "GO LEFT";
        }else if(data[0] <= -3){
            //-X
            mSensorParsedData = "GO RIGHT";
        }else{
            //Inbetween
            mSensorParsedData = "Stay In the middle";
        }
        if (data[1] <= 3){
            mSensorParsedData = mSensorParsedData + "\n" + "Full Speed";
        }else if(data[1] <= 7){
            mSensorParsedData = mSensorParsedData + "\n" + "Slow down";
        }else if(data[1] < 10){
            mSensorParsedData = mSensorParsedData + "\n" + "Stop";
        }
        parsedDataTV.setText("Parsed Data: \n" + mSensorParsedData);
    }

}
