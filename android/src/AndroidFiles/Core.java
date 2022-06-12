package getGPSFromMailToMap.DMBazylev;//пакет

import android.content.Context;
import java.io.IOException;
import java.util.Locale;

import java.util.List;


import java.io.IOException;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;

public class Core {

    public static void putFileFromEmailToBD(Context context)//используем файл полученный по почте как базу данных
    {
        //используем готовую базу данных - полученный по почте файл bd SQLITE

        UseDBFromEmail myDbHelper = new UseDBFromEmail(context);

        try {
                myDbHelper.createDataBase("/storage/emulated/0/Download/TempFileInExStorage.db");//создаем базу - в параметр передаем полный путь к уже существующему во внешнем ханилище файлу из которого создается БД
        } catch (IOException ioe) {
                throw new Error("Unable to create database");
        }

        try {
                myDbHelper.openDataBase();//открываем базу и в том же методе берем из базы данные ( если брать в отдельном методе то вроде как будет брать уже из другого объекта и будет ошибка что нет такой таблицы)

        }catch(SQLException sqle){
                throw sqle;
        }


     }

}
