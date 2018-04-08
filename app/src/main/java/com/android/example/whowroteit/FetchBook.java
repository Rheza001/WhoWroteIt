package com.android.example.whowroteit;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FetchBook extends AsyncTask<String, Void, String>{
  private TextView mTitleText, mAuthorText;

  public FetchBook(TextView mTitleText, TextView mAuthorText){
    this.mTitleText = mTitleText;
    this.mAuthorText = mAuthorText;
  }
  @Override
  protected String doInBackground(String... params) {
    return NetworkUtils.getBookInfo(params[0]);
  }

  @Override
  protected void onPostExecute(String s) {
    super.onPostExecute(s);

    try{
      JSONObject jsonObject = new JSONObject(s);
      JSONArray itemsArray = jsonObject.getJSONArray("items");

      //Iterate through the results
      for (int i = 0; i < itemsArray.length(); i++) {
        JSONObject book = itemsArray.getJSONObject(i); //Get current item
        String title = null, authors = null;
        JSONObject volumeInfo = book.getJSONObject("volumeInfo");

        try{
          title = volumeInfo.getString("title");
          authors = volumeInfo.getString("authors");
        } catch (Exception e){
          e.printStackTrace();
        }

        //If both title and author exist, update the TextViews and return
        if(title != null && authors != null){
          mTitleText.setText(title);
          mAuthorText.setText(authors);
          return;
        }
      }

      mTitleText.setText("No Results Found");
      mAuthorText.setText("");

    } catch (JSONException e) {
      mTitleText.setText("No Results Found");
      mAuthorText.setText("");
      e.printStackTrace();
    }
  }
}
