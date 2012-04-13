package ftp.test;

//import org.apache.commons.net.ftp.FTPClient;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class FinalProjectActivity extends Activity {
    /** Called when the activity is first created. */
    private Button connectButton;
    private Button disconnectButton;
	private EditText host;
	private EditText user;
	private EditText password;
	private EditText port;
	private EditText resultText;
	String directory;

	FTPClient aFTPClient = new FTPClient();

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
       this.connectButton = (Button)findViewById(R.id.button2);
        this.disconnectButton = (Button)findViewById(R.id.button1);
        this.host = (EditText)findViewById(R.id.editText1);
        this.user = (EditText)findViewById(R.id.editText2);
        this.password = (EditText)findViewById(R.id.editText3);
        this.port = (EditText)findViewById(R.id.editText4);
        this.resultText = (EditText)findViewById(R.id.resultMain);
        
        this.connectButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {

				String hostEntry = null;
				String userEntry = null;
				String passwordEntry = null;
				int portEntry = 0;
				
				//Modularizing this into a method caused issues. Values would not be grabbed from the UI.
				
				hostEntry = host.getText().toString();
				userEntry = user.getText().toString();
				passwordEntry = password.getText().toString();
				portEntry = Integer.parseInt(port.getText().toString());
				
				
				if (ftpConnect(aFTPClient, hostEntry, userEntry, passwordEntry, portEntry)) {
					resultText.append("Connection success!");
					resultText.append("\n");
					String[] transfer = (ftpGetCurrentWorkingDirectory(aFTPClient));
					Intent newwindow = new Intent(FinalProjectActivity.this, FtpList.class);
					newwindow.putExtra("transfer", transfer);
					newwindow.putExtra("directory", directory);
    			    startActivity(newwindow);
    			    
					//displayFolderNames(ftpGetCurrentWorkingDirectory(aFTPClient),resultText);
				} else {

				   
					resultText.append("Connection failed :(");
					resultText.append("\n");
				}

			}
        });
        this.disconnectButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (ftpDisconnect(aFTPClient))
					{
					resultText.setText("Disconnected");
					}
				else
				{
					resultText.setText("You are not connected to any server.");
				}
			}
        });
    }
    
    public boolean ftpConnect(FTPClient mFTPClient, String host,
			String username, String password, int port) {
		try {
			// connecting to the host
			mFTPClient.connect(host, port);
			// now check the reply code, if positive mean connection success
			if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
				// login using username & password4
				boolean status = mFTPClient.login(username, password);

				mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
				mFTPClient.enterLocalPassiveMode();

				return status;
			}
		} catch (Exception e) {

		}

		return false;
	}

    public boolean ftpDisconnect(FTPClient mFTPClient) {
		try {
			mFTPClient.logout();
			mFTPClient.disconnect();
			resultText.append("Disconnected");
			return true;
		} catch (Exception e) {
			//Log.d(TAG, "Error occurred while disconnecting from ftp server.");
		}

		return false;
	}
    public String[] ftpGetCurrentWorkingDirectory(FTPClient mFTPClient) {
		try {
			mFTPClient.changeWorkingDirectory("/");
			directory = mFTPClient.printWorkingDirectory();
			String[] workingDir = mFTPClient.listNames();
			// display current directory
			Toast.makeText(getApplicationContext(),
					"You are at: " + mFTPClient.printWorkingDirectory(), 4)
					.show();
			
			return workingDir;
		} catch (Exception e) {
			// Log.d(TAG, "Error: could not get current working directory.");
			String msg = "cannot get current working dir";
			Toast.makeText(getApplicationContext(), "Cannot get current dir", 4)
					.show();
			return null;
		}
	}
    
    public void displayFolderNames (String[] names, TextView view)
    {
    	for (int i = 0; i < names.length; i++)
    	{
    		resultText.append(names[i] + "\n");
    	}
    }

}