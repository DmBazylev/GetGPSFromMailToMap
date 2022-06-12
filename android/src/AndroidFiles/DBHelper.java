
package getGPSFromMailToMap.DMBazylev;//пакет

import android.util.Log;


import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;




public class DBHelper extends SQLiteOpenHelper
{

    //================== переменные строк для использования в языке SQL при работе с базой данных
    private static final String DATABASE_NAME = "myDatabase.db";
    private static final String DATABASE_TABLE = "PositioningInTime";

    private static final String DATABASE_CREATE ="CREATE TABLE  IF NOT EXISTS "+ DATABASE_TABLE
    +" ( _id integer primary key autoincrement,"
    +"time TEXT,"
    +"latitude REAL,"
    +"longitude REAL);";


    public DBHelper(Context context) //обязательно должен быть конструктор
    {
            super(context, DATABASE_NAME, null, 1);

    }


//эти два нижеуказанных метода в этом классе обязательно должны быть переопределены, есть и другие методы но их обязательно переопределять не надо
    @Override
       public void onCreate(SQLiteDatabase db)
       {
           db.execSQL(DATABASE_CREATE);//создание таблицы в базе
       }
       @Override
       public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion)
       {
           db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);//обновление версии базы данных
           onCreate(db);
       }

}
