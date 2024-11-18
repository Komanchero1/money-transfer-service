package org.example.moneytransferservice.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;


//класс реализует метод интерфейса Logger
public class LoggerImpl implements Logger {
    private static Logger instance ; //для хранения экземпляра Logger
    //для хранения объекта FileWriter дающего доступ к методам позволяющим записывать данные в файл
    private FileWriter writer;

    public LoggerImpl(FileWriter writer) {
        this.writer = writer;
    }

    private LoggerImpl() {
        try {
            //создание файла , true - указывает что записи добовляются в конец файла
            writer = new FileWriter("log.txt", true);
        } catch (IOException e) {
            e.printStackTrace();//при возникновении ошибки в консоль выводится сообщение
        }
    }


    //метод для получения экземпляра Singleton
    public static Logger getInstance() {
        if (instance == null) {// если экземпляр LoggerImpl не создан
            instance = new LoggerImpl();//создание нового экземпляра LoggerImpl, если он еще не существует
        }
        return instance;//возвращение единственного экземпляра класса LoggerImpl
    }


    //реализация метода интерфейса Logger
    public void log(String msg) {
        try {
            writer.append("[")
                    .append(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))//получение текущей даты
                    .append(":")
                    .append(LocalTime.now().format(DateTimeFormatter.ofPattern("HH.mm.ss.nnn")))//получение текущего времени
                    .append("] ")
                    .append(msg)//добавление сообщения в лог
                    .append("\n");
            writer.flush();//принудительное сбрасывание содержимого буфера в файл
        } catch (IOException e) {
            System.err.println("Журнал ошибок " + msg);//в случае ошибки при формировании и записи сообщения в консоль выводится сообщение
        }
    }
}