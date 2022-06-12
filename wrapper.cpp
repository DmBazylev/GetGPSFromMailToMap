#include "wrapper.h"

Wrapper::Wrapper(QObject *parent): QObject(parent)
{

}

Wrapper::~Wrapper()
{

}

void Wrapper::getXMLFileFromMail()//скачивание XML файла с почты в отдельном потоке (поток в Java)
{
    // QAndroidJniObject::callStaticMethod<void>("getGPSFromMailToMap/DMBazylev/GetDBFromMail", "call","()V");

     QAndroidJniObject::callStaticMethod<void>("getGPSFromMailToMap/DMBazylev/GetDBFromMail", "call","(Landroid/content/Context;)V",QtAndroid::androidActivity().object() );
}

void Wrapper::getPermissionREAD_EXTERNAL_STORAGE()
{
    QtAndroid::requestPermissionsSync(QStringList() << "android.permission.READ_EXTERNAL_STORAGE");
}

void Wrapper::getPermissionWRITE_EXTERNAL_STORAGE()
{
    QtAndroid::requestPermissionsSync(QStringList() << "android.permission.WRITE_EXTERNAL_STORAGE");
}
