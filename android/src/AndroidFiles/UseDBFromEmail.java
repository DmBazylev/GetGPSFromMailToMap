
package getGPSFromMailToMap.DMBazylev;//пакет

import android.util.Log;
import android.widget.Toast;


import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import java.io.IOException;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import android.database.DatabaseErrorHandler;


public class UseDBFromEmail extends SQLiteOpenHelper //implements DatabaseErrorHandler    использование готовой базы данных - полученного с почты файла bd sqlite
{

    //================== переменные строк для использования в языке SQL при работе с базой данных
    private static final String DATABASE_PATH = "/storage/emulated/0/Download/";// место где будет создана БД - это внешнее хранилище
   // private static final String DATABASE_PATH = "/data/user/0/org.qtproject.example/databases/";// место где будет создана БД - это внутреннее хранилище
    private static final String DATABASE_NAME = "UsedTempDB.db";//название создаваемой БД куда будут записаны сведения из файла
    private static final String DATABASE_TABLE = "TableToKeepGPS";//название таблицы создаваемой БД


    private SQLiteDatabase myDataBase;
  //  final Context contextToast;


    public UseDBFromEmail(Context context) //обязательно должен быть конструктор
    {
            super(context, DATABASE_NAME, null, 1);
         //   contextToast=context;
    }




    public void createDataBase(String filePath) throws IOException // Создает пустую базу данных и перезаписывает ее нашей собственной базой
    {
        //ниже закомментированный код записывает базу только когда ее еще нет
     /*
        boolean dbExist = checkDataBase();

        if(dbExist){
                //ничего не делать - база уже есть
                Log.d("","createDataBase - ничего не делать - база уже есть");
        }else{
                //вызывая этот метод создаем пустую базу, позже она будет перезаписана
                this.getReadableDatabase();
                this.close();
                try {
                        copyDataBase(filePath);
                        Log.d("","copyDataBase(filePath)");
                } catch (IOException e) {
                        throw new Error("Error copying database");
                }
        }
    */

//вызывая этот метод создаем пустую базу, позже она будет перезаписана - этот когда ВСЕГДА перезаписывает базу (даже если она есть) тк вновь приходящий по почте файл всегда другой
this.getReadableDatabase();
try {
        copyDataBase(filePath);
        Log.d("","copyDataBase(filePath)");
} catch (IOException e) {
        throw new Error("Error copying database");
}


    }


//Проверяет, существует ли уже эта база, чтобы не копировать каждый раз при запуске приложения
//@return true если существует, false если не существует

private boolean checkDataBase()
{
        SQLiteDatabase checkDB = null;

        try{
                String myPath = DATABASE_PATH + DATABASE_NAME;
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
                //база еще не существует
        }
        if(checkDB != null){
                checkDB.close();
        }
        return checkDB != null ? true : false;
    }


//Копирует базу из файла по опред пути  вместо созданной локальной БД
//Выполняется путем копирования потока байтов


private void copyDataBase(String filePath) throws IOException
{

        InputStream myInput =new FileInputStream(filePath);//откуда копируем - "/storage/emulated/0/Download/TempFileInExStorage.db"

        //Путь ко вновь созданной БД
        String outFileName = DATABASE_PATH + DATABASE_NAME;

        Log.d("myInput.available()",String.valueOf(myInput.available()));//

        //Открываем пустую базу данных как исходящий поток
        OutputStream myOutput = new FileOutputStream(outFileName);//создаем файл БД куда копируем//

        //перемещаем байты из входящего файла в исходящий
        byte[] buffer = new byte[1024];
        int length;

         Log.d("myInput.read(buffer))",String.valueOf(myInput.read(buffer)));//

         int count=0;

        while ((length = myInput.read(buffer))>0)
        {
            ++count;
            Log.d("count",String.valueOf(count));

            myOutput.write(buffer, 0, length);
        }

//Toast.makeText(contextToast, "скопирована в БД из файла", Toast.LENGTH_LONG).show();//вывод всплывающего окна//
Log.d("","скопирована в БД из файла");
        //закрываем потоки
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException{
        //открываем БД
        String myPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);//открываем базу данных


    //    Toast.makeText(contextToast, "открытие БД", Toast.LENGTH_LONG).show();//вывод всплывающего окна//

    Log.d("","открытие БД");

        //ниже истребуем сведения из БД - именно в этом методе чтоб использовать именно этого объект класса с уже записанными из файла данными
        Cursor cursorFromEmail =  myDataBase.rawQuery("SELECT * from "+DATABASE_TABLE, null);
//                //   cursorFromEmail.moveToFirst();
        while (cursorFromEmail.moveToNext())//по циклу догоняется до самой последней записи, т.е. самое близкое время к запрошенному
        {
            Log.d("",String.valueOf(cursorFromEmail.getInt(cursorFromEmail.getColumnIndex("_id"))));
            Log.d("",cursorFromEmail.getString(cursorFromEmail.getColumnIndex("time")));
            Log.d("",String.valueOf(cursorFromEmail.getDouble(cursorFromEmail.getColumnIndex("latitude"))));
            Log.d("",String.valueOf(cursorFromEmail.getDouble(cursorFromEmail.getColumnIndex("longitude"))));
        }

    }


public void getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("db.isOpen ()",String.valueOf(db.isOpen ()));


Cursor cursorFromEmail =  db.rawQuery("SELECT * from "+DATABASE_TABLE, null);


        while (cursorFromEmail.moveToNext())//по циклу догоняется до самой последней записи, т.е. самое близкое время к запрошенному
        {
             Log.d("",String.valueOf(cursorFromEmail.getInt(cursorFromEmail.getColumnIndex("_id"))));
            Log.d("",cursorFromEmail.getString(cursorFromEmail.getColumnIndex("time")));
            Log.d("",String.valueOf(cursorFromEmail.getDouble(cursorFromEmail.getColumnIndex("latitude"))));
            Log.d("",String.valueOf(cursorFromEmail.getDouble(cursorFromEmail.getColumnIndex("longitude"))));
        }

    }


//эти два нижеуказанных метода в этом классе обязательно должны быть переопределены, есть и другие методы но их обязательно переопределять не надо
    //при использовании готовой базы данных в методах onCreate и onUpgrade пусто
       @Override
       public void onCreate(SQLiteDatabase db)
       {
           //пусто
       }
       @Override
       public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion)//переход к новой версии
       {
           //пусто
       }

   @Override
   public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) //переход к старой версии
   {
     db.setVersion(oldVersion);
   }

//@Override
//public void onCorruption (SQLiteDatabase dbObj)
//{
//    Log.d("","onCorruption");
// }



}

