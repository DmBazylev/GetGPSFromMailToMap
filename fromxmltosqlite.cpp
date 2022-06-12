#include "fromxmltosqlite.h"

FromXMLtoSqlite::FromXMLtoSqlite(QObject *)
{

    db=QSqlDatabase::addDatabase("QSQLITE");//the class object is created by a static method to which the database driver is passed, in this case "QSQLITE"

    db.setDatabaseName("./"+DATABASE_NAME);//create a database file by specifying its location in the build folder (this is also suitable for ANDROID)

    if (db.open())
    {
        qDebug()<<"open";
    }
    else
    {
        qDebug()<<"no open";
    }

    query=new QSqlQuery(db);//creating an object of the database class (for queries to it), we pass an object of the database class to it, but you can also not transfer it, it will take it by default

    /*the string inside the query->exec() is the language of SQL, he has a different syntax for different databases, in this case, the syntax for "QSQLITE"
specifically, this line creates a table called Positioning In Time with three columns time (the type of the variable QString),latitude (type double variable),longitude (variable type - double)
types of variables in SQL if not strict then to insert the variable is not of this type, it will not result in an error, the database converts it to the desired type
    */
    query->exec(DATABASE_CREATE);//creating a table, in "SQLITE" when creating a table, the query must be whole, i.e. one whole row and not several


}


FromXMLtoSqlite::~FromXMLtoSqlite()
{

}


void FromXMLtoSqlite::readXMLfile()//парсим XML файл и  закачиваем сразу сведения в БД
{   
    query->exec("DELETE FROM "+DATABASE_TABLE+";");//delete all records from the database
    query->exec("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = '"+DATABASE_TABLE+"'");//reset auto-increment

    bool isTime=false,isLatitude=false,isLongitude=false;
    QString Time="",Latitude="", Longitude="";


    QFile file ( "/storage/emulated/0/Download/rows.xml");
    if(file.open(QIODevice::ReadOnly))
    {
        QXmlStreamReader sr(&file);
        do
        {
            sr.readNext();

            if (sr.tokenString()=="Characters"&&isTime)
            {

                Time=sr.text ().toString();
                qDebug () << "Time" << sr.text ();//
                isTime=false;
            }
            if (sr.tokenString()=="Characters"&&isLatitude)
            {
                Latitude=sr.text ().toString();
                qDebug () << "Latitude" << sr.text ();//
                isLatitude=false;
            }
            if (sr.tokenString()=="Characters"&&isLongitude)
            {

                Longitude=sr.text ().toString();
                qDebug () << "Longitude" << sr.text ();//
                isLongitude=false;
            }

            if(Time!=""&&Latitude!=""&&Longitude!="")
            {
                query->prepare("INSERT INTO "+DATABASE_TABLE+" (time,latitude,longitude) "
                               "VALUES (:time, :latitude, :longitude)");

                query->bindValue(":time", Time);//in the time column, the current date and time in a certain format is passed to the current.toString method("dd.MM.yyyy.hh.mm.ss") the required date and time format in the string version
                query->bindValue(":latitude", Latitude.toDouble());//in the latitude column, the latitude value
                query->bindValue(":longitude", Longitude.toDouble());//in the longitude column, the longitude value
                query->exec();//execution of the prepared request

                Time="",Latitude="", Longitude="";//обнуляем временные переменные
            }

            if (sr.tokenString()=="StartElement"&&sr.name()=="Time")
            {
                isTime=true;
            }
            if (sr.tokenString()=="StartElement"&&sr.name()=="Latitude")
            {
                isLatitude=true;
            }
            if (sr.tokenString()=="StartElement"&&sr.name()=="Longitude")
            {
                isLongitude=true;
            }


        }while ( !sr.atEnd ());

        if (sr.hasError()) {
            qDebug() << "Error:" << sr.errorString();
        }

    }

}


QVariantList FromXMLtoSqlite::getTrackFromBDToQML()
{

qDebug()<<"после QML getTrackFromBDToQML()";

    readXMLfile();//распарсиваем XML файл

    //====================================================================


    QList<QVariant> listVariantGeoCoordinateToDrawWay;
    QVariant var;//creating an object of the QVariant class to fill the path listVariantGeoCoordinateToDrawWay

   //select all coordinates from the database -track
    query->exec("SELECT latitude,longitude FROM "+DATABASE_TABLE+";");//выбираем все координаты из списка

   QSqlRecord rec=query->record();//the selected values (records) of the table are accessed via the query->record() method; which returns an object of the QSqlRecord class

   while(query->next())//assignment of selected records is VERY IMPORTANT when selecting records, the first selected record is indicated via the query->next() method, etc. i.e. if you simply specify the rec of the current record, nothing will come out
   {
       //assign a temporary object of the QVariant class the value of the next coordinate of the path from the database
       var.setValue(QGeoCoordinate(query->value(rec.indexOf("latitude")).toDouble(),query->value(rec.indexOf("longitude")).toDouble()));

       listVariantGeoCoordinateToDrawWay.append(var);//and put this object into list
   }

   return listVariantGeoCoordinateToDrawWay;
}

void FromXMLtoSqlite::selectLastApproxPosInTime(QString timeToFind)
{
    query->exec("SELECT time,latitude,longitude FROM "+DATABASE_TABLE+" WHERE time<='"+timeToFind+"';");//if the value is entered by the user during the execution of the program

    QSqlRecord rec=query->record();//the selected values (records) of the table are accessed via the query->record() method; which returns an object of the QSqlRecord class

    while(query->next())//assignment of selected records is VERY IMPORTANT when selecting records, the first selected record is indicated via the query->next() method, etc. i.e. if you simply specify the rec of the current record, nothing will come out
    {
        //assign the selected records to the class parameters for from transfer to QML
        queryTime=query->value(rec.indexOf("time")).toString();
        queryLatitude=query->value(rec.indexOf("latitude")).toDouble();
        queryLongitude=query->value(rec.indexOf("longitude")).toDouble();
    }
}

double FromXMLtoSqlite::getQueryLatitude()
{
    return queryLatitude;//latitude requested from the database
}

double FromXMLtoSqlite::getQueryLongitude()
{
    return queryLongitude;//longitude requested from the database
}

QString FromXMLtoSqlite::getQueryTime()
{
    return queryTime;//time requested from the database
}




