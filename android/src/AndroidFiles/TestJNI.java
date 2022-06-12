package getGPSFromMailToMap.DMBazylev;//пакет

//import	java.lang.System;
import android.util.Log;


public class TestJNI
{


    public static native void callFromJava();//нативный метод без реализации - вызвается из кода Java его реализация в коде С++

//    static{
//           System.loadLibrary("xxxx");
//       }



    public static void test ()
    {
        Log.d("","public static void test ()");



        TestJNI.callFromJava();//вызов нативного метода

//        TestJNI w=new TestJNI ();
//        w.java_call();

      }


}
