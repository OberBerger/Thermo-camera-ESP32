package com.example.thermocamera;

import android.app.Activity;
import android.util.Log;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import com.example.thermocamera.CanvasView;
import com.example.thermocamera.R;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;

public class MainActivity extends Activity {
	private static final String TAG = "Bluetooth";
	private BluetoothAdapter btAdapter = null;
	private static final int REQUEST_ENABLE_BT = 1;
	private OutputStream outStream = null;
	private BluetoothSocket btSocket = null;
	private InputStream inStream = null;
	// Insert your bluetooth devices MAC address
	private static String address = "80:7D:3A:CA:28:6E";
	//private static String address = "98:D3:31:50:20:83";
	// Well known SPP UUID
	private static final UUID MY_UUID =
	    UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	public static byte[] DataTable = new byte[68] ;
	
	private boolean BT_Connect=false;
	private boolean enabled=false;
	private CanvasView customCanvas;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
		new MyThread().start();

		
		// BLUETOOTH
		customCanvas = (CanvasView) findViewById(R.id.signature_canvas);
		btAdapter = BluetoothAdapter.getDefaultAdapter();
	    checkBTState();    
	    
        final Button Button1 = (Button) findViewById(R.id.button1);
        
        Button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendData("b");
			}
		});
        
        final Button Button2 = (Button) findViewById(R.id.button2);
        
        Button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendData("c");
			}
		});
        
        final Button Button3 = (Button) findViewById(R.id.button3);
        
        Button3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendData("d");
			}
		});
        
        final Button Button4 = (Button) findViewById(R.id.button4);
        
        Button4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendData("e");
			}
		});
        
        
	    
	}
	
	
	public void onResume() {
		super.onResume();
		Start_BT();
	}
	

	
	@Override
	protected void onPause() {
		super.onPause();
		
	// BLUETOOTH
		BT_Connect=false;
		Log.d(TAG, "...In onPause()...");

	    if (outStream != null) {
	      try {
	        outStream.flush();
	      } catch (IOException e) {
	        errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
	      }
	    }

	    try     {
	      btSocket.close();
	    } catch (IOException e2) {
	      errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
	    }
	    enabled=false;
		
	}
	
	private class MyThread extends Thread {
		public void run () {
			final TextView t1 = (TextView) findViewById(R.id.textView1);
			final TextView t2 = (TextView) findViewById(R.id.textView2);

			while (true) {
				//Log.d(TAG, "Ide ért");
			  if (enabled) {

				if (BT_Connect) {
					filltable();
						customCanvas.postInvalidate();
					t1.post(new Runnable() {
					    public void run() {
					        t1.setText(String.valueOf(DataTable[64])+" °C");
					    } 
					});
					t2.post(new Runnable() {
					    public void run() {
					        t2.setText(String.valueOf(DataTable[65])+" °C");
					    } 
					});
				} else {
					//Log.d(TAG, "Else ág BT_Connect="+BT_Connect);
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					Start_BT();
				}
			  } else {
				  
			  }
			}
		}
	}	
	
	private void Start_BT()  {
		
		// BLUETOOTH
		Log.d(TAG, "...Start_BT - Attempting client connect...");
		  
	    // Set up a pointer to the remote node using it's address.
		
		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        if(pairedDevices.size()>0){
            for(BluetoothDevice device: pairedDevices){
                String devicename = device.getName();
                String macAddress = device.getAddress();
                if (devicename.equals("Thermo Camera")) address=macAddress;}
        }
				
	    BluetoothDevice device = btAdapter.getRemoteDevice(address);
	    

	  
	    // Two things are needed to make a connection:
	    //   A MAC address, which we got above.
	    //   A Service ID or UUID.  In this case we are using the
	    //     UUID for SPP.
	      
	    
	    
	    try {
	      btSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
	    } catch (IOException e) {
	      errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
	    } 

	    // Discovery is resource intensive.  Make sure it isn't going on
	    // when you attempt to connect and pass your message.
	    btAdapter.cancelDiscovery();
	  
	    // Establish the connection.  This will block until it connects.
	    Log.d(TAG, "...Connecting to Remote...");
	    
	    
	    try {
	      btSocket.connect();
	      Log.d(TAG, "...Connection established and data link opened...");
	    } catch (IOException e) {
	      try {
	        btSocket.close();
	      } catch (IOException e2) {
	        errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
	      }
	      //Toast.makeText(this,"Bluetooth device not found!", Toast.LENGTH_SHORT).show();
	      //finish();
	      return;
	    }

	    // Create a data stream so we can talk to server.
	    Log.d(TAG, "...Creating Socket...");
	    BT_Connect=true;

	    try {
	      outStream = btSocket.getOutputStream();
	    } catch (IOException e) {
	      errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
	    }
	    
	    try {
		      inStream = btSocket.getInputStream();
		    } catch (IOException e) {
		      errorExit("Fatal Error", "In onResume() and input stream creation failed:" + e.getMessage() + ".");
		    }
	    
	    try {
			while(inStream.available() != 0) {
				try {
					inStream.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    enabled=true;
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void filltable()  {
		
	int timeout = 0;
	int maxTimeout = 100; // leads to a timeout of 3 seconds
	int j=0;
	boolean dataOK=true;
		
	
	
    	sendData("a");
    
	try {
		while((inStream.available()) == 0 && timeout < maxTimeout) {
		    timeout++;
		    // throws interrupted exception
		    try {
				Thread.sleep(10); //250
			} catch (InterruptedException e1) {
				onPause();
				e1.printStackTrace();
			}
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
		
    try {
		Thread.sleep(70); //100
	} catch (InterruptedException e1) {
		e1.printStackTrace();
	}
    
	
		for (j = 0; j <68; j++) {
			try {
				if(inStream.available() > 0){ DataTable[j] = (byte) inStream.read();}
				 else {
					 Log.d(TAG, String.valueOf(j)+" adat jött meg a 68-ból!");
					 if (j==0) {
						 BT_Connect=false;
						 //onPause();
					 }
					 dataOK=false;
				 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
				
		
		if (!dataOK) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try {
				while (inStream.available() > 0) j = inStream.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

   		
	}
	
	
	  private void checkBTState() {
		    // Check for Bluetooth support and then check to make sure it is turned on

		    // Emulator doesn't support Bluetooth and will return null
		    if(btAdapter==null) { 
		      errorExit("Fatal Error", "Bluetooth Not supported. Aborting.");
		    } else {
		      if (btAdapter.isEnabled()) {
		        Log.d(TAG, "...Bluetooth is enabled...");
		      } else {
		        //Prompt user to turn on Bluetooth
		        @SuppressWarnings("static-access")
				Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
		        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		      }
		    }
	  }
	  
	  
	  private void errorExit(String title, String message){
		    Toast msg = Toast.makeText(getBaseContext(),
		        title + " - " + message, Toast.LENGTH_SHORT);
		    msg.show();
		    finish();
	  }
	  
	  public void sendData(String message) {
		    byte[] msgBuffer = message.getBytes();

		    //Log.d(TAG, "...Sending data: " + message + "...");

		    try {
		      outStream.write(msgBuffer);
		    } catch (IOException e) {
		      String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
		      if (address.equals("00:00:00:00:00:00")) 
		        msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
		      msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";
		      
		      errorExit("Fatal Error", msg);       
		    }

	  }
		  
	  
	  public static byte get_point(int s){
			return (DataTable[s]);
		}
	
	  public static byte[] get_pixs() {
			return (DataTable);
		}
	  
}
