package updatehandlers;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import entity.Document;
import entity.filebeat.FileBeatDocument;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import props.BotConfig;
import utils.EventListener;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class UpdateSecurityHandler extends TelegramLongPollingBot implements EventListener {
    private final ReentrantLock lock = new ReentrantLock();

    String[] array = {
            "**REMOVED**", // **REMOVED**
            "**REMOVED**" // **REMOVED**
    };

    Set<String> seen = new HashSet<String>();

    PebbleEngine engine;

    public UpdateSecurityHandler() {
        super();
        SendMessage sendmessage = new SendMessage();
        sendmessage.setChatId("1104257862");
        sendmessage.setText("hello");
        try {
            execute(sendmessage);
        } catch (TelegramApiRequestException e) {

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        this.engine = new PebbleEngine.Builder().loader(new StringLoader()).build();

    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().isUserMessage()) {
            try {
                execute(getHelpMessage(update.getMessage()));
            } catch (TelegramApiException e) {

            }
        }
    }


    @Override
    public String getBotUsername() {
        return BotConfig.SECURITY_TESTING_BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BotConfig.SECURITY_TESTING_BOT_TOKEN;
    }

    private static SendMessage getHelpMessage(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Hello 🤟 Give this number to your admin: " + message.getChatId());
        return sendMessage;
    }

    @Override
    public void update(String eventType, List<? extends Document> docs, String template, HashMap<String, Object> context) {
        String idtoAdd = null;
        lock.lock();
        try {
            // critical section
            for (String chatId: array) {
                for (Document doc: docs) {
                    if (!seen.contains(doc.getId())) {
                        idtoAdd = doc.getId();
                        HashMap<String, Object> templateContext =  new HashMap<>();
                        try {
                            for (Map.Entry<String, Object> entry: context.entrySet()) {
                                templateContext.put(entry.getKey(), PropertyUtils.getProperty(doc, entry.getValue().toString()));
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                        Writer writer = new StringWriter();
                        PebbleTemplate compiledTemplate = engine.getTemplate(template);
                        try {
                            compiledTemplate.evaluate(writer, templateContext);
                        } catch (IOException e) {
                            log.error("Couldn't evaluate templalte ", template, e);
                        }

                        SendMessage sendmessage = new SendMessage();
                        sendmessage.setChatId(chatId);
                        sendmessage.setText(writer.toString());
                        try {
                            execute(sendmessage);
                        } catch (TelegramApiRequestException e) {

                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
            if (idtoAdd != null) {
                seen.add(idtoAdd);
            }

        } finally {
            lock.unlock();
        }


    }
}
