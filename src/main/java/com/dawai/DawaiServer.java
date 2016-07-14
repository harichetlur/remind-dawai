package com.dawai;

import com.dawai.telegram.GetUpdates;
import com.dawai.telegram.ReplyKeyboardMarkup;
import com.dawai.telegram.SendMessage;
import com.dawai.telegram.TelegramResponse;
import com.dawai.telegram.Update;
import com.dawai.telegram.factory.ReplyKeyboardMarkupFactory;
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
	private static int fOffset = 1;
	private static CloseableHttpClient fHttpclient = HttpClients.createDefault();
	public static Logger fLogger = LogManager.getLogger(DawaiServer.class);
	private static Nurse fNurse;
	private static Calendar fCalendar;
	private static String BOT_TOKEN;
	private static String BOT_URL = "https://api.telegram.org/bot";
	private static String fState = null;

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
								if (updateText.equalsIgnoreCase("done") || updateText.contains("Done with a dawai")) {
									sendMessage("Which dawai?", ReplyKeyboardMarkupFactory.getDawaiKeyboardMarkup(fNurse.getWhatsLeft()));
									fState = "done";
									//sendMessage(fNurse.done());
								} else if (updateText.equalsIgnoreCase("see") || updateText.contains("See what's left")) {
									for (Dawai d : fNurse.getWhatsLeft())
										sendMessage("You need to take " + d.getDawaiName(), ReplyKeyboardMarkupFactory.getBaseCommandsKeyboardMarkup());
								} else if ("done".equals(fState)) {
									try {
										int dawaiId = Integer.parseInt(updateText.split("-")[0]);
										sendMessage(fNurse.done(dawaiId), ReplyKeyboardMarkupFactory.getBaseCommandsKeyboardMarkup());
										fState = null;
									} catch (IllegalArgumentException e) {
										sendMessage("I didn't understand which dawai you are done with. Let's try again.", ReplyKeyboardMarkupFactory.getDawaiKeyboardMarkup(fNurse.getWhatsLeft()));
									}
								}

								fOffset = update.getUpdate_id() + 1;
							}
						}

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
						DupliMap dawaiMap = fNurse.getDawaiToTake(fCalendar);
						for(List<Dawai> dList : dawaiMap.values()) {
							for (Dawai d : dList) {
								sendMessage(String.format("Remember to take dawai - %s", d.getDawaiName()), ReplyKeyboardMarkupFactory.getBaseCommandsKeyboardMarkup());
							}
						}
						//Thread.sleep(3600000);
					} catch (Exception e) {
						fLogger.error(e.getMessage());
					}
				}
			}
		}.start();
	}

	private static void sendMessage(String msg, ReplyKeyboardMarkup keyboardMarkup) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		fLogger.info("Trying to send message : " + msg);
		SendMessage message = new SendMessage(fChatId, msg, keyboardMarkup);
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
		dawaiList.add(new Dawai("Liquid drops", new TimeFrame[]{TimeFrame.MORNING, TimeFrame.NIGHT}, 1));
		dawaiList.add(new Dawai("Sugar Pills", new TimeFrame[]{TimeFrame.MORNING, TimeFrame.AFTERNOON, TimeFrame.NIGHT}, 2));
		dawaiList.add(new Dawai("Daily Powder", new TimeFrame[]{TimeFrame.DAY}, 3));
		dawaiList.add(new Dawai("4th day Powder", new TimeFrame[]{TimeFrame.MON, TimeFrame.THU}, 4));
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

		GetUpdates getUpdates = new GetUpdates(fOffset++, 1);
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
