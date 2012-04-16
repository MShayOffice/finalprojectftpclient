package ftp.test;

//To test, 193.43.36.131, anonymous, anonymous, 21
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.graphics.*;
import android.widget.AdapterView.OnItemClickListener;
import java.util.*;
import java.io.*;

public class FtpList extends ListActivity{
	FTPClient aFTPClient = new FTPClient();
//	String directory;
	String[] thisConnection;
	ArrayList<String> thisFolder;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Intent newwindow = getIntent();
        String[] transfer = newwindow.getStringArrayExtra("transfer");
//        directory = newwindow.getStringExtra("directory");
        thisConnection = newwindow.getStringArrayExtra("thisConnection");
        thisFolder = newwindow.getStringArrayListExtra("thisFolder");

//        checkDir(aFTPClient);
        //displayFolderNames(transfer);
        this.setListAdapter(new ArrayAdapter<String>(this, ftp.test.R.layout.ftplist, ftp.test.R.id.label, transfer));
        
        ListView lv = getListView();
        
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
   
                // selected item. If replaced with an inputted String (Ex: "2001") everything will work.
            	// We want to get the value from the item the user clicks on.
             
            	thisFolder.add(((TextView) view).getText().toString());
            	  if (thisFolder.contains("."))
                  {
            		  String[] transfer = (ftpGetCurrentWorkingDirectory(aFTPClient));
            		  DownloadFile(transfer, thisConnection[0], Integer.parseInt(thisConnection[3]));
                  } else {
            	
		                //For some reason, the connection drops between windows. Keeping this here will temporarily keep the connection.
		            	if (ftpConnect(aFTPClient, thisConnection[0], thisConnection[1], thisConnection[2], Integer.parseInt(thisConnection[3]))) {
			                String[] transfer = (ftpGetCurrentWorkingDirectory(aFTPClient));
			                
			                
			                // Launching new Activity on selecting single List Item
			                Intent newwindow = new Intent(FtpList.this, FtpList.class);
			                newwindow.putExtra("transfer", transfer);
		//	                newwindow.putExtra("directory", directory);
			                newwindow.putExtra("thisConnection", thisConnection);
			                newwindow.putExtra("thisFolder", thisFolder);
			                startActivity(newwindow);
		            	}
                  }
            }
          });
	}
	
	public void displayFolderNames (String[] names)
    {
    	/*for (int i = 0; i < names.length; i++)
    	{
    		Button[] listItem = new Button[5];
    		listItem.setId(2000 + i);
    		listItem.setClickable(true);
    		listItem.setText(names[i]);
    		 listItem[i].setClickable(true);
    	        listItem[i].setOnClickListener(new View.OnClickListener() {
    	            @Override
    	            public void onClick(View v) {
    	                // TODO Auto-generated method stub
    	                name[i].setText("kjghjbjhb");

    		buttonList.addView(listItem);
    		resultText.append(names[i] + "\n");
    	            }
    	        };
    	}*/

    }
	
	public String[] ftpGetCurrentWorkingDirectory(FTPClient mFTPClient) {
		try {
			//directory = mFTPClient.printWorkingDirectory();
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
			Toast.makeText(getApplicationContext(), "Cannot get current dir", 4)
					.show();
			return null;
		}
	}
	
	public void checkDir(FTPClient mFTPClient) {
		try {
//			directory = mFTPClient.printWorkingDirectory();
			
		} catch (Exception e) {
			// Log.d(TAG, "Error: could not get current working directory.");
			String msg = "cannot get current working dir";
			Toast.makeText(getApplicationContext(), "Cannot get current dir", 4)
					.show();
		}
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	        // do something on back.
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
	//                newwindow.putExtra("directory", directory);
	                newwindow.putExtra("thisConnection", thisConnection);
	                newwindow.putExtra("thisFolder", thisFolder);
	                startActivity(newwindow);
	        	}
		        return true;
	    	}
	    }

	    return super.onKeyDown(keyCode, event);
	}
	public void DownloadFile(String[] srcFileSpec, String host, int port) {
	    File pathSpec = new File("ftp://193.43.36.131/Radio/MP3/1999/after-Mitch.mp3");
	    FTPClient aFTPClient = new FTPClient();
	    BufferedOutputStream fos = null;
	    try {
	        aFTPClient.connect(host, port);
	        aFTPClient.enterLocalPassiveMode(); // important!
	        aFTPClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
	        fos = new BufferedOutputStream(
	                              new FileOutputStream(pathSpec.toString()+"/"+ "fuckthis"));
	        aFTPClient.retrieveFile("ftp://193.43.36.131/Radio/MP3/1999/after-Mitch.mp3", fos);
	        }//try 
	        catch (IOException e) {
	            //Log.e("FTP", "Error Getting File");
	            e.printStackTrace();
	            }//catch
	        finally {
	            try {
	                if (fos != null) fos.close();
	                aFTPClient.disconnect();
	                }//try
	                catch (IOException e) {
	                    //Log.e("FTP", "Disconnect Error");
	                    e.printStackTrace();
	                    }//catch
	                }//finally
	  //  Log.v("FTP", "Done");    
	    }//getfileFTP

}