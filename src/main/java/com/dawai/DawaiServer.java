package com.dawai;

import com.dawai.telegram.GetUpdates;
import com.dawai.telegram.SentMessage;
import com.dawai.telegram.TelegramResponse;
import com.dawai.telegram.Update;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.Thread.sleep;


public class DawaiServer {

    private static int fChatId;
    private static String fUsername;
    private static int fOffset = 0;
    private static CloseableHttpClient fHttpclient = HttpClients.createDefault();
    public static Logger fLogger = LogManager.getLogger(DawaiServer.class);
    private static Nurse fNurse;
    private static Calendar fCalendar;
    private static String BOT_TOKEN;
    private static String BOT_URL = "https://api.telegram.org/bot";

    public static void main(String[] args) {
        try {

            Path filePath = FileSystems.getDefault().getPath("botToken.txt");
            Files.lines(filePath).forEach(o -> BOT_TOKEN = String.valueOf(o));

            init();

            setupCalendar();
            startResponseListener();
            startNurseThread();
        } catch (Exception e) {
            fLogger.error(e.getMessage());
        }
    }

    private static void startResponseListener() {
        new Thread() {
            public void run() {
                while (true) {
                    try {

                        TelegramResponse telegramResponse = getLastResponse();
                        for (Update update : telegramResponse.getResult()) {
                            String updateText = update.getMessage().getText();
                            if (updateText != null) {
                                if (updateText.equalsIgnoreCase("done") || updateText.contains("done")) {
                                    sendMessage(fNurse.done());
                                } else if (updateText.equalsIgnoreCase("peek") || updateText.contains("peek")) {
                                    Enumeration<Dawai> dawaiList = fNurse.getWhatsLeft();
                                    while (dawaiList.hasMoreElements()) {
                                        sendMessage("You need to take " + dawaiList.nextElement().getfDawaiName());
                                    }
                                }
                            }

                            fOffset++;
                        }

                        sleep(1000);
                    } catch (Exception e) {
                        fLogger.error(e.getMessage());
                    }
                }
            }
        }.start();
    }

    private static void startNurseThread() {
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        List<Dawai> dawaiList = fNurse.getDawaiToTake(fCalendar);
                        for (Dawai d : dawaiList) {
                            sendMessage(String.format("Remember to take dawai - %s", d.getfDawaiName()));
                            sleep(1000);
                        }
                        //Thread.sleep(3600000);
                    } catch (Exception e) {

                    }
                }
            }
        }.start();
    }

    private static void sendMessage(String msg) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        fLogger.info("Trying to send message : " + msg);
        SentMessage message = new SentMessage(fChatId, msg);
        HttpPost httpPost = new HttpPost(BOT_URL + BOT_TOKEN + "/sendMessage");
        httpPost.setHeader("Content-Type", "application/json");
        Gson gson = new GsonBuilder().create();
        String jsonString = gson.toJson(message);
        HttpEntity entity = new StringEntity(jsonString);
        httpPost.setEntity(entity);
        httpclient.execute(httpPost);
    }

    private static void setupCalendar() {
        fCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata"));
    }

    private static void setupDawai() {
        List<Dawai> dawaiList = new ArrayList<>();
        dawaiList.add(new Dawai("Liquid drops", new TIME_FRAME[]{TIME_FRAME.MORNING, TIME_FRAME.NIGHT}));
        dawaiList.add(new Dawai("Sugar Pills", new TIME_FRAME[]{TIME_FRAME.MORNING, TIME_FRAME.AFTERNOON, TIME_FRAME.NIGHT}));
        dawaiList.add(new Dawai("Daily Powder", new TIME_FRAME[]{TIME_FRAME.DAY}));
        dawaiList.add(new Dawai("4th day Powder", new TIME_FRAME[]{TIME_FRAME.MON, TIME_FRAME.THU}));
        Prescription prescription = new Prescription(dawaiList);
        fNurse = new Nurse(prescription);
    }

    private static void init() throws IOException, InterruptedException {
        TelegramResponse telegramResponse;
        do {
            sleep(5000);
            telegramResponse = getLastResponse();
        } while (telegramResponse.getResult().length == 0);

        fOffset = telegramResponse.getResult()[0].getUpdate_id() + 1;
        fChatId = telegramResponse.getResult()[0].getMessage().getChat().getId();
        fUsername = telegramResponse.getResult()[0].getMessage().getFrom().getUsername();
        fLogger.info("Initialized with user : " + fUsername + " and chat id : " + fChatId + ". Offset set to : " + fOffset);

        setupDawai();
    }

    private static TelegramResponse getLastResponse() throws IOException {
        HttpPost httpPost = new HttpPost(BOT_URL + BOT_TOKEN + "/getUpdates");

        GetUpdates getUpdates = new GetUpdates(fOffset, 1);
        httpPost.setHeader("Content-Type", "application/json");
        Gson gson = new GsonBuilder().create();
        String jsonString = gson.toJson(getUpdates);
        HttpEntity entity = new StringEntity(jsonString);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = fHttpclient.execute(httpPost);
        gson = new GsonBuilder().create();
        entity = response.getEntity();
        Reader reader = new InputStreamReader(entity.getContent());
        return gson.fromJson(reader, TelegramResponse.class);
    }
}
