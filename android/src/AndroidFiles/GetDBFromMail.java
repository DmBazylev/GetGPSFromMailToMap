package getGPSFromMailToMap.DMBazylev;//пакет

import android.os.AsyncTask;
import android.util.Log;
import com.sun.mail.pop3.POP3Store;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.Part;

import java.io.IOException;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import java.io.File;

import android.widget.Toast;
import android.content.Context;
import javax.xml.transform.Result;


public class GetDBFromMail extends AsyncTask<Void, Void, Void>//скачиваем XML файл с почты в внешнее хранилище - работа идет в отдельном потоке
{

String user = "******************@gmail.com";//почта (не логин) с которой получаем письмо
String password = "*********************";//пароль почты - требуется специальный пароль для внешних приложений, а не обычный пароль для почтового ящика
String host = "pop.gmail.com";//host для mail.ru

    static Context contextToast;

    public static void call(Context context)//в этом методе запускаем поток (и вообще работу  объекта класса)Context context
    {
        GetDBFromMail getEmail = new GetDBFromMail();//создаем экземпляр класс   context
        contextToast=context;

        getEmail.execute();//этим методом запускаем переопределенный метод doInBackground(Void... voids) в теле которого реализуется работа в отдельном потоке

     }


    @Override
    protected Void doInBackground(Void... voids)
    {

        try {

            Properties properties = new Properties();//создаем свойства
            properties.put("mail.pop3.host", host);//используем протокол pop3 для mail.ru
            properties.put("mail.pop3.port", "995");//для протокола pop3 используется порт 995
            properties.put("mail.pop3.starttls.enable", "true");
            properties.put("mail.pop3.ssl.enable", "true");

            Session emailSession = Session.getDefaultInstance(properties);//открываем сессию на основе этих свойств
            Store store = emailSession.getStore("pop3s");//на основе сессии создаем Store
            store.connect(host, user, password);//связываем Store  с почтой где user - почта (но не логин!) а password пароль к почте

            // Папка входящих сообщений, "INBOX" - это папка входящих сообщений
            Folder inbox = store.getFolder("INBOX");//из этого Store берем папку входящих сообщений

            // Открываем папку в режиме только для чтения
            inbox.open(Folder.READ_ONLY);//открываем ее для чтения

            Message[] messages = inbox.getMessages();//берем все сообщения из папки входящие

            Message message = messages[messages.length-1];//берем последнее сообщение (то есть самое свежее)
            String contentType = message.getContentType();//определяем тип контента этого сообщения
            String saveDirectory="/storage/emulated/0/Download/";//директория где будет храниться извлеченный из почтового вложения файл

            // content may contain attachments- если контент содержит вложения
            if (contentType.contains("multipart"))
            {

            // content may contain attachments
            Multipart multiPart = (Multipart) message.getContent();
            MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(1);//вторая часть Мультипата, содержащая файл, первая часть под номером 0 - название сообщения

            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()))
            {

            //  part.saveFile(saveDirectory + File.separator + fileName);//а так не работает - дублируется путь (получаете неправильный - слишком удлиненный)
            part.saveFile("/storage/emulated/0/Download/rows.xml");//сохраняем полученную часть вложения в виде файла в файл по определенному пути

            Log.d("","файл сохранен во внешнее хранилище");

            }

        }

        inbox.close(false);//закрываем папку входящих сообщений с флагом false
        store.close();//закрываем Store
            }    catch (MessagingException exp)
                    {
                            exp.printStackTrace();
                    }
                    catch (Exception exp)
                    {
                            exp.printStackTrace();
                    }


    return null;// обратить внимание что есть возврат несмотря на тип Void (здесь вэтом методе он с большой буквы V)
    }

@Override
    protected void onPostExecute(Void result) {
      super.onPostExecute(result);

       Toast.makeText(contextToast, "файл скачан", Toast.LENGTH_LONG).show();//вывод всплывающего окна
    }




}
