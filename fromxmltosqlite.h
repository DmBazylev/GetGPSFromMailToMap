#ifndef FROMXMLTOSQLITE_H
#define FROMXMLTOSQLITE_H


#include <QObject>

#include <QDomDocument>
#include <QDomElement>
#include <QDomNode>
#include <QXmlStreamReader>
#include <QFile>
#include <QIODevice>
#include <QDebug>


#include <QSqlDatabase>
#include <QSqlQuery>
#include <QSqlRecord>
#include <QSqlError>

#include <QGeoCoordinate>

//===============================================

#include <QtAndroid>
#include <QAndroidJniObject>
#include <QAndroidIntent>
#include <QAndroidJniEnvironment>

class FromXMLtoSqlite: public QObject
{
    Q_OBJECT

public:
    explicit FromXMLtoSqlite(QObject *parent = nullptr);

    ~FromXMLtoSqlite();

    void readXMLfile();//парсим XML файл и  закачиваем сразу сведения в БД

   Q_INVOKABLE QVariantList getTrackFromBDToQML();// create a path for MapPolyline in QML from the database and returning it (passes the path to QML)
    Q_INVOKABLE  void selectLastApproxPosInTime(QString timeToFind);//select the last suitable value close to the parameter

    Q_INVOKABLE double getQueryLatitude();
    Q_INVOKABLE  double getQueryLongitude();
    Q_INVOKABLE  QString getQueryTime();

private:

    QSqlDatabase db;//creating a database class object
    QSqlQuery* query;//creating a class object for queries (work) with the database

    QString DATABASE_NAME = "myDatabase.db";//название базы данных
    QString DATABASE_TABLE = "PositioningInTimeTable";//название таблицы

    QString DATABASE_CREATE ="CREATE TABLE  IF NOT EXISTS "+ DATABASE_TABLE
    +" ( _id integer primary key autoincrement,"
    +"time TEXT,"
    +"latitude REAL,"
    +"longitude REAL);";

     double queryLatitude;//latitude requested from the database
     double queryLongitude;//longitude requested from the database
     QString queryTime;//time requested from the database

};

#endif // FROMXMLTOSQLITE_H
