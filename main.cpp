#include <QApplication>
#include <QQmlContext>
#include <QQmlApplicationEngine>
#include "wrapper.h"

#include <QGuiApplication>
#include <QQmlApplicationEngine>


int main(int argc, char *argv[])
{
    QCoreApplication::setAttribute(Qt::AA_EnableHighDpiScaling);

    QGuiApplication app(argc, argv);


    Wrapper w;
    w.getPermissionREAD_EXTERNAL_STORAGE();//предоставление разрешений
    w.getPermissionWRITE_EXTERNAL_STORAGE();

    qmlRegisterType<Wrapper>("com.myinc.Wrapper",1,0,"Wrapper");//register class Wrapper in QML
    qmlRegisterType<FromXMLtoSqlite>("com.myinc.FromXMLtoSqlite",1,0,"FromXMLtoSqlite");//register class FromXMLtoSqlite in QML

    QQmlApplicationEngine engine;
    const QUrl url(QStringLiteral("qrc:/main.qml"));
    QObject::connect(&engine, &QQmlApplicationEngine::objectCreated,
                     &app, [url](QObject *obj, const QUrl &objUrl) {
        if (!obj && url == objUrl)
            QCoreApplication::exit(-1);
    }, Qt::QueuedConnection);



    engine.load(url);

    return app.exec();
}
