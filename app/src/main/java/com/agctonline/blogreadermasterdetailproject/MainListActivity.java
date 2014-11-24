package com.agctonline.blogreadermasterdetailproject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class MainListActivity extends ListActivity {

    /*protected String[] mBlogPostTitles= {
            "Cupcake","Froyo","HoneyComb","Ice Cream","Jelly Bean","Mango","Apple","Orange"

    };*///moved the array into string resource file

    //protected String[] mBlogPostTitles; //this array variable can be restrictive because data can't be added to it dynamically, it is restricted by array size declaration.

    public static final int NUMBER_OF_POSTS = 20;
    //static keyword allows this variable to be access without class instantiation.
    public static final String TAG = MainListActivity.class.getSimpleName();
    protected JSONObject mBlogData;
    protected ProgressBar mProgressBar;

    private final String KEY_TITLE = "title";
    private final String KEY_AUTHOR = "author";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

       /* Resources resources =getResources();
        //unlike getString, get array from string.xml
        // needs to create a resources object before accessing the
        // getStringArray method
        mBlogPostTitles = resources.getStringArray(R.array.android_names);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, mBlogPostTitles);
        //create an adapter that takes the mAndroidName array and put it in a list view, and output the list view of the array into "this" xml view.
        //The statement above prepare the adapter, the statement below executes the adapter.
        setListAdapter(adapter);


        String message =getString(R.string.no_items);
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();*/

        //----Above this line is the code block for accessing internal data, below is the code block for accessing external data ---//

        //Don't try to access network on the main thread!!!
        //the try catch block below does not work to pull data because it is trying to access network on the main thread

        /*try {
            //create a URL object
            URL blogFeedUrl = new URL("http://blog.teamtreehouse.com/api/get_recent_summary/?count="+NUMBER_OF_POSTS);
            //create a connection object
            HttpURLConnection connection = (HttpURLConnection) blogFeedUrl.openConnection();
            //connect a connection
            Log.v(TAG, "line 60");
            connection.connect();

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURL Exception caught", e);
        } catch (IOException e) {
            Log.e(TAG, "IO Exception caught", e);
        }
        catch (Exception e){
            Log.e(TAG, "Generic Exception caught", e);
        }*/
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (isNetworkAvailable()) {
            //show progressbar before executing background task, will hide it before calling the updatelist
            mProgressBar.setVisibility(View.VISIBLE);

            GetBlogPostsTask getBlogPostsTask = new GetBlogPostsTask();
            getBlogPostsTask.execute();

            Toast.makeText(MainListActivity.this, "Network is on", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Network is not available", Toast.LENGTH_LONG).show();
        }

    }

    private boolean isNetworkAvailable() {
        //code to check network connections
        //these are built in classes
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo !=null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    public class GetBlogPostsTask extends AsyncTask<Object, Void, JSONObject>  {

    //AsyncTask has 3 generic types, use "Void" for a generic type if not required
    //The first type is the input data type, the second type is the internal data type for the class, the third type is the output data type.  This is very similar to the jquery ajax method where the methods are built in, and requires the user of the class to understand the steps.
        @Override
        protected JSONObject doInBackground(Object... arg0) {
            int responseCode=-1;

            JSONObject jsonResponse=null;

            try {

                //create a URL object

                URL blogFeedUrl = new URL("http://blog.teamtreehouse.com/api/get_recent_summary/?count=" + NUMBER_OF_POSTS);
                //create a connection object
                HttpURLConnection connection = (HttpURLConnection) blogFeedUrl.openConnection();
                //connect a connection
                connection.connect();
                responseCode=connection.getResponseCode();
                if(responseCode==HttpURLConnection.HTTP_OK){

                    InputStream inputStream = connection.getInputStream();
                    Reader reader = new InputStreamReader(inputStream);
                    //When a successful request has been made, the data is stored in the inputStream object inside the connection object, the inputstream is only data as bytes that is a readable. The reader object is used to read the data and store it in a variable.
                    int contentLength = connection.getContentLength();
                    //The connection object has the built in method to get the length of the inputstream data.
                    char[] charArray = new char[contentLength];
                    reader.read(charArray);
                    //read and store input stream in charArray
                    String responseData = new String(charArray);
                    //String(charArray) constructor converts charArray into a string
                    //Log.v(TAG,responseData);

                    jsonResponse = new JSONObject(responseData);
                    //JSONObject(responseData) constructor to convert responseData into JSON
                    /*String status = jsonResponse.getString("status");
                    Log.v(TAG,status);

                    JSONArray jsonPosts = jsonResponse.getJSONArray("posts");
                    //JSONArray object variable set to a JSONArray of "posts"

                    for (int i=0; i<jsonPosts.length(); i++){
                        JSONObject jsonPost = jsonPosts.getJSONObject(i);
                        String title = jsonPost.getString("title");
                        Log.v(TAG,"Post "+i+": " +title);
                    }*/



                }
                else {
                        Log.i(TAG, "unsuccessful HTTP response code "+responseCode);

                     }

                Log.v(TAG, "line 60: Response Code "+responseCode);


            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURL Exception caught", e);
            } catch (IOException e) {
                Log.e(TAG, "IO Exception caught", e);
            } catch (Exception e) {
                Log.e(TAG, "Generic Exception caught", e);
            }

            return jsonResponse;
        }

        @Override

        protected void onPostExecute(JSONObject result){
            //the variable "result" contains the data of jsonResponse passed from doInBkGrd
            mBlogData = result;
            handleBlogResponse();//
        }
    }

    private void handleBlogResponse() {
        mProgressBar.setVisibility(View.INVISIBLE); //hide progressbar
        if (mBlogData==null){

            //can just leave everything inside this if statement by not refactor a new method
            updateDisplayForError();
        }
        else {
            try {
                JSONArray jsonPosts = mBlogData.getJSONArray("posts");
                //mBlogPostTitles = new String[jsonPosts.length()];

                //use ArrayList instead of arrayList size can expand dynamically
                ArrayList<HashMap<String, String>> blogPosts =
                        new ArrayList<HashMap<String, String>>();

                //Create an ArrayList that contains a Hashmap of authors and titles
                //loop through the JSON data and fetch each author/title and put in hashmap
                //add individual hashmap to ArrayList


                for (int i =0; i<jsonPosts.length();i++){
                    JSONObject post = jsonPosts.getJSONObject(i);
                    String title = post.getString(KEY_TITLE);
                    title = Html.fromHtml(title).toString();

                    //mBlogPostTitles[i] = title;

                    String author = post.getString(KEY_AUTHOR);
                    title = Html.fromHtml(author).toString();

                    HashMap<String, String> blogPost = new HashMap<String, String>();
                    blogPost.put(KEY_TITLE,title);
                    blogPost.put(KEY_AUTHOR,author);

                    blogPosts.add(blogPost);

                }
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, mBlogPostTitles); //old code for using Array, not needed since switching to ArrayList

                String[] keys ={KEY_TITLE,KEY_AUTHOR};
                int[] ids ={android.R.id.text1,android.R.id.text2};
                //the 2 arrays above are created so they can be used as the last 2 params in the SimpleAdapter
                SimpleAdapter adapter = new SimpleAdapter(this,blogPosts,android.R.layout.simple_list_item_2,keys,ids);
                //


                setListAdapter(adapter);

                Log.d(TAG, mBlogData.toString(2));
            } catch (JSONException e) {
                Log.e(TAG, "JSON Exception caught: ", e);
            }
        }
    }

    private void updateDisplayForError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Oops, Sorry!");
        builder.setMessage("Try again");
        //use getString(R.string.xxx) if created string resources
        builder.setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();

        //To get to the Listview's empty view, must use this method below instead of the normal findViewById method.
        TextView emptyTextView = (TextView) getListView().getEmptyView();
        emptyTextView.setText(getString(R.string.no_items));
    }

}
