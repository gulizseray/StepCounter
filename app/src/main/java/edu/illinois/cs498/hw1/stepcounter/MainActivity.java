package edu.illinois.cs498.hw1.stepcounter;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import static android.util.FloatMath.sqrt;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor senAccelerometer;
    private Sensor senGyroscope;
    private Sensor senMagnetometer;
    private Sensor senLight;
    private long startTime = System.currentTimeMillis();


    public static final float ALPHA = (float) 0.7;

    private File readingsFile;
    private FileOutputStream readingsOutputStream;
    private String sensorFileName = "sensorReadings.csv";

    //cached values for the sensor readings
    float [] cachedAccelerometer = {0,0,0};
    float cachedAcceleration = 0;
    float [] cachedGyroscope = {0,0,0};
    float [] cachedMagnetometer = {0,0,0};
    float cachedLightSensor = 0;

    private int numSteps = 0;
    private long lastStepCountTime = 0;

    private TextView stepsTextView = null;

    public void initializeFile(){
        try {
            readingsFile = new File(Environment.getExternalStorageDirectory(), sensorFileName);
            readingsOutputStream = new FileOutputStream(readingsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeSensors(){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //initialize accelerometer
        senAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_FASTEST);

        //initialize gyroscope
        senGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(this, senGyroscope, SensorManager.SENSOR_DELAY_FASTEST);

        //initialize magnetometer
        senMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(this, senMagnetometer, SensorManager.SENSOR_DELAY_FASTEST);

        //initialize light sensor
        senLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this, senLight, SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        long currTime = System.currentTimeMillis();

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            cachedAcceleration = (1-ALPHA) * cachedAcceleration + ALPHA * sqrt(x*x + y*y + z*z);

            System.arraycopy(sensorEvent.values, 0, cachedAccelerometer, 0, sensorEvent.values.length);


            if(cachedAcceleration > 11.5)
            {
                if (currTime - lastStepCountTime > 300)
                {
                    numSteps++;
                    lastStepCountTime = currTime;
                    stepsTextView.setText(String.valueOf(numSteps));
                }
            }


        }
        else if (mySensor.getType() == Sensor.TYPE_GYROSCOPE){
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            System.arraycopy(sensorEvent.values, 0, cachedGyroscope, 0, sensorEvent.values.length);
        }
        else if (mySensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            System.arraycopy(sensorEvent.values, 0, cachedMagnetometer, 0, sensorEvent.values.length);

        }
        else if (mySensor.getType() == Sensor.TYPE_LIGHT){

            cachedLightSensor = sensorEvent.values[0];
        }

        writeAllReadingsToFile(currTime - startTime);

    }

    public void writeAllReadingsToFile(long timestamp){
        String acc = cachedAccelerometer[0] + "," + cachedAccelerometer[1] + "," + cachedAccelerometer[2] + ",";
        String gyr = cachedGyroscope[0] + "," + cachedGyroscope[1] + "," + cachedGyroscope[2] + ",";
        String mag = cachedMagnetometer[0] + "," + cachedMagnetometer[1] + "," + cachedMagnetometer[2] + ",";

        String all = timestamp + "," + acc + gyr + mag + String.valueOf(cachedLightSensor) + " , " + cachedAcceleration+ "\n";
        try {
                readingsOutputStream.write( all.getBytes() );
                readingsOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stepsTextView = (TextView) findViewById(R.id.stepCounter);

        initializeSensors();
        initializeFile();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}