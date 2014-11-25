package com.agctonline.blogreadermasterdetailproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;


public class BlogWebViewActivity extends Activity {

    protected String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_web_view);

        Intent intent = getIntent();//get the intent from the activity that called this activity
        Uri blogUri = intent.getData();//getData gets the data (the URL) from the setData of previous activity

        mUrl = blogUri.toString();

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(mUrl); //requires conversion of the URL to string first before loadUrl


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_blog_web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_share) {

            //Toast.makeText(this,"DDDD",Toast.LENGTH_LONG).show();

            sharePost(); //call sharePost() if clicked on share

        }

        return super.onOptionsItemSelected(item);
    }

    private void sharePost() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        /*shareIntent.setType("message/rfc822");


        shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "jckdsilva@gmail.com" });
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "enter something");
*/
        shareIntent.putExtra(Intent.EXTRA_TEXT,mUrl);
        startActivity(Intent.createChooser(shareIntent,"How do you want to share?"));
        //the createChooser has 2 params, first is intent, second is the title of app chooser. The createChooser will find all the apps that can use the data type that was set in setType.
    }


}
