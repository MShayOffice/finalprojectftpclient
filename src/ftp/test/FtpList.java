package ftp.test;

//To test, 193.43.36.131, anonymous, anonymous, 21
import org.apache.commons.net.ftp.*;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.graphics.*;
import android.widget.AdapterView.*;
import java.util.*;
import java.io.*;

public class FtpList extends ListActivity {
	FTPClient aFTPClient = new FTPClient();
	String[] thisConnection = null;
	ArrayList<String> thisFolder = new ArrayList<String>();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        Intent newwindow = getIntent();
        String[] transfer = newwindow.getStringArrayExtra("transfer");
        thisConnection = newwindow.getStringArrayExtra("thisConnection");
        thisFolder = newwindow.getStringArrayListExtra("thisFolder");

        this.setListAdapter(new ArrayAdapter<String>(this, ftp.test.R.layout.ftplist, ftp.test.R.id.label, transfer));
        ListView lv = getListView();      
        
        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//        		String test = ((TextView) view).getText().toString();

            	thisFolder.add(((TextView) view).getText().toString());        			
            	
            	if (ftpConnect(aFTPClient, thisConnection[0], thisConnection[1], thisConnection[2], Integer.parseInt(thisConnection[3]))) {
	                String[] transfer = (ftpGetCurrentWorkingDirectory(aFTPClient));
	                
	                
	                // Launching new Activity on selecting single List Item
	                Intent newwindow = new Intent(FtpList.this, FtpList.class);
	                newwindow.putExtra("transfer", transfer);
	                newwindow.putExtra("thisConnection", thisConnection);
	                newwindow.putExtra("thisFolder", thisFolder);
	                startActivity(newwindow);
            	}
            }
          });
	}
	
	public String[] ftpGetCurrentWorkingDirectory(FTPClient mFTPClient) {
		try {
			String directory =  "/";
			for(int x = 0; x<thisFolder.size(); x++)
			{
				directory = directory + "/" + thisFolder.get(x);
			}
			
			if (new File(directory).isDirectory()){			
			mFTPClient.changeWorkingDirectory(directory);
			
			String[] workingDir = mFTPClient.listNames();
			// display current directory
			Toast.makeText(getApplicationContext(),
					"You are at: " + mFTPClient.printWorkingDirectory(), 4)
					.show();
			return workingDir;
			}
			else if (new File(directory).isFile()){
		    	thisFolder.remove(thisFolder.get(thisFolder.size()-1));				
		    	return null;
			}
			else{
		    	thisFolder.remove(thisFolder.get(thisFolder.size()-1));				
				String msg = "Could not access that filepath";
				Toast.makeText(getApplicationContext(), msg, 4)
						.show();
				return null;				
			}
		} catch (Exception e) {
			// Log.d(TAG, "Error: could not get current working directory.");
			String msg = "cannot get current working dir";
			Toast.makeText(getApplicationContext(), msg, 4)
					.show();
			return null;
		}
	}
	
	public boolean ftpConnect(FTPClient mFTPClient, String host,
			String username, String password, int port) {
		try {
			mFTPClient.connect(host, port);
			if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
				boolean status = mFTPClient.login(username, password);

				mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
				mFTPClient.enterLocalPassiveMode();

				return status;
			}
		} catch (Exception e) {

		}

		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	        // re-open previous window on back.
	    	if (thisFolder.size() == 0)
			{
	    		startActivity(new Intent(FtpList.this, FinalProjectActivity.class));
			}
	    	else
	    	{
		    	thisFolder.remove(thisFolder.get(thisFolder.size()-1));
	        	if (ftpConnect(aFTPClient, thisConnection[0], thisConnection[1], thisConnection[2], Integer.parseInt(thisConnection[3]))) {
	                String[] transfer = (ftpGetCurrentWorkingDirectory(aFTPClient));
	                
	                // Launching new Activity on selecting single List Item
	                Intent newwindow = new Intent(FtpList.this, FtpList.class);
	                newwindow.putExtra("transfer", transfer);
	                newwindow.putExtra("thisConnection", thisConnection);
	                newwindow.putExtra("thisFolder", thisFolder);
	                startActivity(newwindow);
	        	}
		        return true;
	    	}
	    }

	    return super.onKeyDown(keyCode, event);
	}

}