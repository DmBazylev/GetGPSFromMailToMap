#ifndef WRAPPER_H
#define WRAPPER_H

#include <QObject>

#include <QString>
#include <QDebug>

#include <QtAndroid>
#include <QAndroidJniObject>
#include <QAndroidIntent>
#include <QAndroidJniEnvironment>

#include "fromxmltosqlite.h"

class Wrapper: public QObject
{
    Q_OBJECT

public:
    explicit Wrapper(QObject *parent = nullptr);
    ~Wrapper();

public:

    void getPermissionREAD_EXTERNAL_STORAGE();
    void getPermissionWRITE_EXTERNAL_STORAGE();

    Q_INVOKABLE void getXMLFileFromMail();

};

#endif // WRAPPER_H
