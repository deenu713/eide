
//license wtfpl 2.0

//by aenu 2018,2019
//   email:202983447@qq.com

package aenu.eide;
import android.app.Application;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import aenu.eide.util.OSUtils;
import java.io.File;
import android.content.res.ObbScanner;
import android.content.res.ObbInfo;
import android.content.Context;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import java.net.URL;
import java.net.MalformedURLException;
import android.net.*;
import android.content.Intent;
import android.app.ActivityManager;

public class E_Application extends Application
{
    private final ActivityLifecycleCallbacks Activity_LifecycleCallbacks=new ActivityLifecycleCallbacks(){
        @Override
        public void onActivityCreated(Activity p1,Bundle p2){
        }
        @Override
        public void onActivityStarted(Activity p1){
        }
        @Override
        public void onActivityResumed(Activity p1){
        }
        @Override
        public void onActivityPaused(Activity p1){
        }
        @Override
        public void onActivityStopped(Activity p1){
        }
        @Override
        public void onActivitySaveInstanceState(Activity p1,Bundle p2){
        }
        @Override
        public void onActivityDestroyed(Activity p1){
        }
    };
    
    static final boolean clearConfigVersion(Context context){
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(context);
        return sp.edit().remove("version").commit();
    }
    
    static final boolean updateConfigVersion(Context context){
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(context);
        return sp.edit().putString("version",getAppVersion()).commit();
    }
    
    static final String getConfigVersion(Context context){
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("version",null);
    }
    
    static final String getAppVersion(){
        return "0.1";
    }
	
	public static final String getAppPackageName(){
        return "aenu.eide";
    }
    
    
    public static final File getTmpDir(){
        return new File("/data/data/aenu.eide/eide-tmp");
    }
    
    public static final File getBinDir(){
        return new File("/data/data/aenu.eide/eide-bin");    
    }
    
    public static final File getLibDir(){
        return new File("/data/data/aenu.eide/eide-lib");      
    }
    
    public static final File getNdkDir(){
        return new File("/data/data/aenu.eide/eide-ndk");      
    }
    
    public static final File getDexDir(){
        return new File("/data/data/aenu.eide/eide-dex");      
    }
    
    public static final File getProjectDir(){
        return new File("/storage/emulated/0/eide");      
    }
    
    public static final File getAppPrivateDir(){
        return new File("/storage/emulated/0/.eide");      
    }
    
    public static final URL getNdkUrl() throws MalformedURLException{
        //return new URL("file:///sdcard/AppProjects/eide-extra/ndk.tar.xz");
        return new URL("https://github.com/aenu/eide-extra/blob/0.1/ndk.tar.xz?raw=true");
    }
    
    public static final URL getAndroidJarUrl() throws MalformedURLException{
        //return new URL("file:///sdcard/AppProjects/eide-extra/android.jar");   
        return new URL("https://github.com/aenu/eide-extra/blob/0.1/android.jar?raw=true");
    }
	
	public static final Uri getAlipayDonateUri(){
		return Uri.parse("alipays://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2FFKX01086YZLWGY2BBVP962%3F_s%3Dweb-other");
	}
    
    @Override
    public void onCreate(){
        super.onCreate();    
		if(OSUtils.getProcessName(this,android.os.Process.myPid())
			.equals(E_ErrorActivity.PROCESS_NANE)) return;
			
        Thread.setDefaultUncaughtExceptionHandler(H);
        initDir();
    }

    private final void initDir(){
        {
            File tmpDir=getTmpDir();
            if(!tmpDir.exists())
                if(!tmpDir.mkdirs())
                    throw new RuntimeException("mkdir "+tmpDir.getAbsolutePath()+" fail!");
        }    
        {
            File binDir=getBinDir();
            if(!binDir.exists())
                if(!binDir.mkdirs())
                    throw new RuntimeException("mkdir "+binDir.getAbsolutePath()+" fail!");
            
        }   
        {
            File libDir=getLibDir();
            if(!libDir.exists())
                if(!libDir.mkdirs())
                    throw new RuntimeException("mkdir "+libDir.getAbsolutePath()+" fail!");
            
        }  
        {
            File ndkDir=getNdkDir();
            if(!ndkDir.exists())
                if(!ndkDir.mkdirs())
                    throw new RuntimeException("mkdir "+ndkDir.getAbsolutePath()+" fail!");
        }
        {
            File dexDir=getDexDir();
            if(!dexDir.exists())
                if(!dexDir.mkdirs())
                    throw new RuntimeException("mkdir "+dexDir.getAbsolutePath()+" fail!");
        }
        
        {
            File pDir=getProjectDir();
            if(!pDir.exists())
                if(!pDir.mkdirs())
                    throw new RuntimeException("mkdir "+pDir.getAbsolutePath()+" fail!");
        }
        
        {
            File apDir=getAppPrivateDir();
            if(!apDir.exists())
                if(!apDir.mkdirs())
                    throw new RuntimeException("mkdir "+apDir.getAbsolutePath()+" fail!");
        }
    }
    
    public static void PrintException(String tag,Throwable e){
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        PrintStream strm=new PrintStream(out);
        e.printStackTrace(strm);
        String err=out.toString();
        strm.close();
        out.reset();
        Log.e(tag,err);
    }
    
    private final Thread.UncaughtExceptionHandler H=new Thread.UncaughtExceptionHandler(){
        @Override
        public void uncaughtException(Thread p1, Throwable p2){
			Intent intent=new Intent(E_ErrorActivity.ACTION_ERROR);
			intent.putExtra(E_ErrorActivity.EXTRA_ERROR_MESSAGE,getExceptionMessage(p2));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			System.exit(0);
        }              
        
        private String getExceptionMessage(Throwable e){
            ByteArrayOutputStream out=new ByteArrayOutputStream();
			PrintStream strm=new PrintStream(out);
			e.printStackTrace(strm);
			String err=out.toString();
			strm.close();
			out.reset();
			return err;
        }
    } ;
}
