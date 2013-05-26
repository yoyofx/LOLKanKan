package com.maxzhang.lolkankan;

import android.*;
import android.R;
import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.Toast;
import com.maxzhang.BindingSourceAdapter.BindingSourceAdapter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Maxzhang8
 * Date: 13-5-23
 * Time: 下午3:46
 * To change this template use File | Settings | File Templates.
 */
public class AsyncHtmlRequestTask extends AsyncTask<String, Integer, String>
{
    private ListActivity listActivity = null;
    public AsyncHtmlRequestTask(ListActivity context)
    {
        listActivity = context;

    }


    private List<String> pageList = new ArrayList<String>();
    private ArrayList<VideoInfo> videoList = new ArrayList<VideoInfo>();

    private void saveFile(String result){
        File root = new File(Environment.getExternalStorageDirectory(), "Notes");
        if (!root.exists())
        {
            root.mkdirs();
        }
        try
        {
            File gpxfile = new File(root, "log.txt");
            FileWriter writer = new FileWriter(gpxfile,false);
            writer.write(result);
            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();

        }

    }

    @Override
    protected String doInBackground(String... params) {
        String strResult = "";
        String httpUrl = params[0];
        try {
            strResult = HttpHelper.getHtmlCode(httpUrl);
            Log.v("log",strResult);
//            saveFile(strResult);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //Log.e("error",e.getMessage());
            System.out.print(e.getMessage());
        }
        int postion = httpUrl.indexOf(".html");
        String match = httpUrl.substring(0,postion) + "_\\d*" + httpUrl.substring(postion);
        Log.v(match, "log");
        Pattern pattern = Pattern.compile(match);
        Matcher matcher = pattern.matcher(strResult);
        while(matcher.find())
        {

            String s= matcher.group();
            Log.v(s,"log");
            pageList.add(s);
        }

        String match1 = "<dt><a href=\"(.*?)\".*title=\"(.*?)\".*background-image:[\\s]*url\\(\\'(.*?)\\'\\);\"><span>(.*)</span>[\\s]*(<strong>(.*)</strong>)?";
        Pattern pattern1 = Pattern.compile(match1);
        Matcher matcher1 = pattern1.matcher(strResult);

        while(matcher1.find())
        {
            VideoInfo info = new VideoInfo();
            info.setUrl(matcher1.group(1));
            info.setTitle(matcher1.group(2));
            info.setImageUrl(matcher1.group(3));
            info.setTimeSpan(matcher1.group(4));
            videoList.add(info);
        }

        return strResult;

    }


    @Override
    protected void onPostExecute(String s) {


        BindingSourceAdapter<VideoInfo> adapter = (BindingSourceAdapter<VideoInfo>)listActivity.getListAdapter();
        adapter.addAll(this.videoList);
        adapter.notifyDataSetChanged();
        super.onPostExecute(s);    //To change body of overridden methods use File | Settings | File Templates.
    }
}