package ftp.test;

import org.apache.commons.net.ftp.FTPClient;

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
   
                // selected item
                String product = ((TextView) view).getText().toString();
                //String[] transfer = (ftpGetCurrentWorkingDirectory(aFTPClient, product));
                // Launching new Activity on selecting single List Item
                Intent newwindow = new Intent(FtpList.this, testList.class);
                //newwindow.putExtra("product", product);
                //newwindow.putExtra("transfer", transfer);
                startActivity(newwindow);
   
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
	public String[] ftpGetCurrentWorkingDirectory(FTPClient mFTPClient, String product) {
		try {
			mFTPClient.changeWorkingDirectory("Radio/MP3/" + product);
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
}