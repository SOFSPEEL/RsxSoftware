package com.rsxsoftware.exceptionthrower;

import android.content.pm.PackageInfo;
import android.os.Build;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: steve.fiedelberg
 * Date: 9/29/13
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationLogger extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

//        ParseUser.enableAutomaticUser();

        ParseObject.registerSubclass(Exception.class);
        Parse.initialize(this, "ORL3ZTS0YA4r0nCNRZ4G7C6pXswluZN0axqAFUAx", "VIX0KqI2ew5XJf14eIZt72iBi6Zr1QhFAL1Q9FVS");
    }


    private Thread.UncaughtExceptionHandler defaultUEH;

    // handler listener
    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler;

    public static final String TAG = "ApplicationLogger";

    {
        _unCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {

                // here I do logging of exception to a db
                try {
                    final Exception exception = new Exception();
                    exception.put("user", ParseUser.logIn("Steve", "Junk"));
                    exception.put("content", ExceptionUtils.getStackTrace(ex));
                    exception.put("message", ex.getMessage());
                    exception.put("done", false);
                    exception.put("logcat", fetchLogs());
                    exception.put("version", Build.VERSION.RELEASE);

                    final String packageName = getPackageName();
                    final PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, 0);
                    exception.put("versionCode", packageInfo.versionCode);
                    exception.put("versionName", packageInfo.versionName);
                    exception.put("packageName", packageName);
                    exception.addAll("threads", captureThreadDump());
//                     logger.put("threadDump", captureThreadDump());
                    exception.saveEventually();



                } catch (java.lang.Exception e) {


                }

                // re-throw critical exception further to the os (important)
                defaultUEH.uncaughtException(thread, ex);
            }



            private String fetchLogs() throws IOException, ParseException {
                Process process = Runtime.getRuntime().exec("logcat -t 25");
                return IOUtils.toString(process.getInputStream());
            }
        };
    }


    public ApplicationLogger() {
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();

        // setup handler for uncaught exception
        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);
    }

    public static ArrayList<String> captureThreadDump()
    {
        Map allThreads = Thread.getAllStackTraces();
        Iterator iterator = allThreads.keySet().iterator();
        final ArrayList<String> list = new ArrayList<String>();


        while(iterator.hasNext())
        {
            StringBuffer stringBuffer = new StringBuffer();
            Thread key = (Thread)iterator.next();
            StackTraceElement[] trace = (StackTraceElement[])allThreads.get(key);
            stringBuffer.append(key+"\r\n");
            for(int i = 0; i < trace.length; i++)
            {
                stringBuffer.append(" "+trace[i]+"\r\n");
            }
            stringBuffer.append("");
            list.add(stringBuffer.toString());

        }
        return list;
    }
}
