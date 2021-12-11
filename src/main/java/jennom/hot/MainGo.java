package jennom.hot;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import jennom.jms.MessageSenderObj;
import jennom.jms.MessageSenderTxt;
import jennom.jms.User;
import net.timewalker.ffmq4.FFMQCoreSettings;
import net.timewalker.ffmq4.utils.Settings;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
@DependsOn(value = {"txtSender", "objSender", "universalMessageListener"})
public class MainGo extends javax.swing.JFrame {

    private User user;
    @Inject
    private MessageSenderTxt txtSender;
    @Inject
    private MessageSenderObj objSender;
    @Inject
    private Gson gson;

    @PostConstruct
    public void afterBirn() {
        //System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");
        // JMS-test
        for (int i = 1; i < 10; ++i) {
            user = new User();
            user.setLogin("tt" + i + i);
            user.setPassw("tt" + i + i);
            txtSender.sendMessageQ("harp07qq", gson.toJson(user));
            txtSender.sendMessageT("harp07tt", gson.toJson(user));
        }
        for (int i = 1; i < 10; ++i) {
            user = new User();
            user.setLogin("oo" + i + i);
            user.setPassw("oo" + i + i);
            // NOT SEND OBJECT !!!!!!!!!!!!!!!!!!!!!!!!!
            //objSender.sendMessage("harp07qq", user);
        }
    }

    @PreDestroy
    public void beforeKill() {
        System.out.println("stop application...........");
    }

    public synchronized static void main(String args[]) {
        /*String currentPath="";
        try {
            currentPath = new java.io.File(".").getCanonicalPath();
        } catch (IOException ex) {
            Logger.getLogger(MainGo.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Current dir:" + currentPath); */
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current absolute path is: " + s + File.separator + "ffmq" + File.separator);  
        System.setProperty("FFMQ_BASE", s + File.separator + "ffmq" + File.separator);
        String fileSeparator = FileSystems.getDefault().getSeparator();
        //
        AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(AppContext.class);
        ctx.registerShutdownHook();
        //System.out.println("Enter 'stop' to close");
        Scanner sc = new Scanner(System.in);
        /*new Thread(() -> {
            while (true) {
                if (sc.next().toLowerCase().trim().equals("stop")) {
                    System.out.println("Closing");
                    System.exit(0);
                    break;
                }
            }
        }).start();*/
        System.out.println("Enter 'stop' to close");
    }
}
