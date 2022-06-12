import QtQuick 2.12
import QtQuick.Window 2.12
import QtQuick.Controls 2.12
import com.myinc.Wrapper 1.0
import com.myinc.FromXMLtoSqlite 1.0

import QtLocation 5.6
import QtPositioning 5.6//to set MapQuickItem property coordinate:  QtPositioning.coordinate(customPosition.latitude, customPosition.longitude);


Window {
    id:window
    visible: true
    width: 640
    height: 480

    Plugin //плагин для карты
    {
        id: osmMapPlugin
        name: "osm"

        //provide the address of the tile server to the plugin
        PluginParameter {
            name: "osm.mapping.custom.host"
            value: "http://localhost/osm/"    //ip address of source
        }

        /*disable retrieval of the providers information from the remote repository.
       If this parameter is not set to true (as shown here), then while offline,
       network errors will be generated at run time*/
        PluginParameter {
            name: "osm.mapping.providersrepository.disabled"
            value: true
        }
    }

    //сама карта
    Map {
        id:map
        anchors.fill: parent
        plugin: osmMapPlugin
        visible: true

        center{
            latitude:43.10
            longitude:131.86
        }

        zoomLevel: 14//map scaling - the larger the number, the larger the map and vice versa

    }


    //==========================================Position at a certain time=========================================================
    MapQuickItem
    {
        id:markerInTime
        visible:false
        sourceItem: Rectangle
        {
            //id:rect
            width: 4
            height:width
            radius:width/2
            color:"black"
        }

        Component.onCompleted: //добавление осуществляется через метод addMapItem() который вызывается при получении сигнала от Component о том что он создан
        {
            map.addMapItem(markerInTime)//добавление в карту элемента маркера (точка нахождения телефона на карте в определенное время)
        }

        zoomLevel: 0.0//объект остается одного и того же размера на экране при всех уровнях масштабирования, можно 0.0 и не указывать это умолчанию

    }

    //======================================================The path in a certain period of time on map=================================================================

    MapPolyline{
        id:createWay
        width:150
        height:30

        line.color:"black"
        line.width:1
        Component.onCompleted: {
            map.addMapItem(createWay)
           }

    }


    //========================================ToolBar==================================================================================
    Row
    {

        x:10
        y:15
        anchors.horizontalCenter: parent.Center

        spacing:10


        //=================button GetTrackFromMail=========================================================
        Rectangle {
            width:80
            height:30
            radius:5
            border.color:"red"
            color: "transparent"
            Text{
                anchors.centerIn: parent
                font.pixelSize:9
                 text:"GetXMLFile"

            }
            MouseArea{
                anchors.fill:parent
                onClicked: {
                      wrapper.getXMLFileFromMail();

                }
            }
        }


        //=================button GetCoordInTime=========================================================
        Rectangle {
            width:80
            height:30
            radius:5
            border.color:"blue"
            color: "transparent"
            Text{
                anchors.centerIn: parent
                font.pixelSize:10
                text:"ShowWay"
            }
            MouseArea{
                anchors.fill:parent
                onClicked: {
                createWay.path=fromXMLtoSqlite.getTrackFromBDToQML();
                }
            }
        }

        //=================Найти позицию во времени=========================================================
        Rectangle {
            width:80
            height:30
            radius:5
            border.color:"#C0C0C0"
            color: "transparent"
            Text{
                anchors.centerIn: parent
                font.pixelSize:8
                text:"Find pos"+'\n'+"in time"
            }
            MouseArea{
                anchors.fill:parent
                onClicked: {
                findPosInTime.visible=(findPosInTime.visible===true)?false:true
                }
            }
        }

        //=================button EXIT=========================================================
        Rectangle {
            width:80
            height:30
            radius:5
            border.color:"green"
            color: "transparent"
            Text{
                anchors.centerIn: parent
                font.pixelSize:10
                text:"EXIT"
            }
            MouseArea{
                anchors.fill:parent
                onClicked: {

                 Qt.quit();//exit from application
                }
            }
        }


    }

Wrapper
{
    id:wrapper
}

FromXMLtoSqlite
{
    id:fromXMLtoSqlite
}




//========================================================Visual element to find Position in Time===============================================================================

Rectangle{
    id: findPosInTime

    anchors.centerIn: parent
    width: 170
    height:120
    border.color:"#C0C0C0"  //silver color
    border.width:2
    color: "transparent"
    radius:10
    visible:false

    Column{
        anchors.centerIn: parent
        spacing:15

        TextField//элемент ввода данных, он визуально отображается сам по себе, то есть в прямоугольник Rectangle его не обязательно засовывать
        {
            id:txtFd
            width:140
            height:30
            font.pointSize: 10
            placeholderText: "Enter date and time:\n yyyy.mm.dd.hh.mm.ss"
        }

        Rectangle
        {
            id:rect
            width:140
            height:30
            border.color:"black"
            color:"transparent"
            radius:5

            property double lat:0
            property double lon:0
            property string time:""

            Text{
                anchors.centerIn: parent
                color:"darkRed"
                font.pixelSize: 10
                text:"ENTER"
            }

            MouseArea{
                anchors.fill:parent
                onClicked: {

                    fromXMLtoSqlite.selectLastApproxPosInTime(txtFd.text);// the text field contains the value entered in the field
                    txtFd.text=""//after that clear data entry element


                    rect.lat=fromXMLtoSqlite.getQueryLatitude();
                    rect.lon=fromXMLtoSqlite.getQueryLongitude();
                    rect.time=fromXMLtoSqlite.getQueryTime();

                    console.debug("lat: "+rect.lat+" lon "+rect.lon);

                    markerInTime.coordinate=QtPositioning.coordinate(rect.lat, rect.lon);//элемент карты ставится в координаты присылаемые источником геопозиционирования (координаты в определенное время из базы данных)

                    map.center.latitude=rect.lat;//карта выставляется в центре с точкой с запрошенными координатами
                    map.center.longitude=rect.lon;
                    timeText.text=rect.time;

                    markerInTime.visible=true;
                    findPosInTime.visible=false;
                    time.visible=true;

                }
            }


        }


    }

}

//======================================================Visual element of the output of the time of obtaining coordinates=================================================================

Rectangle{
    id:time
    x:markerInTime.x-50
    y:markerInTime.y-20

    width:120
    height:25

    color:"transparent"
    visible:false;

    Text{
        id:timeText
        font.pixelSize:10
        text:""
    }


}



}//Window







