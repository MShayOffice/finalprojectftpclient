package ftp.test;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FtpList extends ListActivity{
	FTPClient aFTPClient = new FTPClient();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
       //this.setContentView(R.layout.ftplist);
        Intent newwindow = getIntent();
        String[] transfer = newwindow.getStringArrayExtra("transfer");
        //displayFolderNames(transfer);
        this.setListAdapter(new ArrayAdapter<String>(this, ftp.test.R.layout.ftplist, ftp.test.R.id.label, transfer));
        
        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
   
                // selected item. If replaced with an inputted String (Ex: "2001") everything will work.
            	// We want to get the value from the item the user clicks on. 
                String text = (String) ((TextView) view).getText();

            	if (ftpConnect(aFTPClient, "193.43.36.131", "anonymous", "anonymous", 21)) {
                String[] transfer = (ftpGetCurrentWorkingDirectory(aFTPClient, text));
                // Launching new Activity on selecting single List Item
                Intent newwindow = new Intent(FtpList.this, FtpList.class);
               // newwindow.putExtra("product", product);
                newwindow.putExtra("transfer", transfer);
                startActivity(newwindow);
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
	public String[] ftpGetCurrentWorkingDirectory(FTPClient mFTPClient, String text) {
		try {
			mFTPClient.changeWorkingDirectory("Radio/MP3/" + text);
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
}