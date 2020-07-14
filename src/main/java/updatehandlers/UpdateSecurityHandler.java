package updatehandlers;

import entity.Document;
import entity.auditbeat.AuditBeatDocument;
import entity.filebeat.FileBeatDocument;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import props.BotConfig;
import utils.EventListener;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;


public class UpdateSecurityHandler extends TelegramLongPollingBot implements EventListener {
    private final ReentrantLock lock = new ReentrantLock();

    String[] array = {
            "***REMOVED***", // ***REMOVED***
            "***REMOVED***" // ***REMOVED***
    };

    Set<String> seen = new HashSet<String>();


    public UpdateSecurityHandler() {
        super();
        SendMessage sendmessage = new SendMessage();
        sendmessage.setChatId("***REMOVED***");
        sendmessage.setText("hello");
        try {
            execute(sendmessage);
        } catch (TelegramApiRequestException e) {

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

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
        System.out.println("hello ***REMOVED***");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.enableMarkdown(true);
        sendMessage.setText("hello ***REMOVED*** " + message.getChatId());
        return sendMessage;
    }

    @Override
    public void update(String eventType, List<? extends Document> docs) {
        System.out.println("okay notification here");
        String idtoAdd = null;
        lock.lock();
        try {
            // critical section
            for (String chatId: array) {
                for (Document doc: docs) {
                    if (doc instanceof FileBeatDocument) {
                        if (!seen.contains(((FileBeatDocument) doc).getId())) {
                            idtoAdd = ((FileBeatDocument) doc).getId();
                            System.out.println("hooray");
                            System.out.println(doc);
                            String user_agent = ((FileBeatDocument) doc).getUser_agent().getOriginal();
                            String url_original = ((FileBeatDocument) doc).getUrl().getOriginal();
                            SendMessage sendmessage = new SendMessage();
                            sendmessage.setChatId(chatId);
                            sendmessage.setText(url_original + "🙄" + user_agent + "🤷‍♂️" + ((FileBeatDocument) doc).getSource().getIp());
                            try {
                                execute(sendmessage);
                            } catch (TelegramApiRequestException e) {

                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }

                    } else if (doc instanceof AuditBeatDocument) {
                        if (!seen.contains( ((AuditBeatDocument) doc).getId())) {
                            idtoAdd = ((AuditBeatDocument) doc).getId();
                            System.out.println("hooray audit");
                            System.out.println(((AuditBeatDocument) doc).getId());
                            System.out.println(doc);
                            String message = ((AuditBeatDocument) doc).getMessage();
                            SendMessage sendmessage = new SendMessage();
                            sendmessage.setChatId(chatId);
                            String Out = "Alert ❗❗ \nLogin on " + ((AuditBeatDocument) doc).getHost().getName() + "\nFrom " + ((AuditBeatDocument) doc).getSource().getIp() + " with user 🤦‍♂️: " + ((AuditBeatDocument) doc).getUser().getName();
                            sendmessage.setText(Out);
                            try {
                                execute(sendmessage);
                            } catch (TelegramApiRequestException e) {

                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
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
