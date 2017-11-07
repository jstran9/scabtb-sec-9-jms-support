package guru.springframework.services.jms;

import guru.springframework.config.JMSConfig;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Remember that this Jms listener happens on a separate thread than from the thread running the web request.
 * This class gets registered as a spring component and sets up as a listener on the text.messagequeue to listen for messages.
 */
@Component
public class TextMessageListener {

    @JmsListener(destination = JMSConfig.textMsgQueue)
    public void onMessage(String msg){
        System.out.println("###############################");
        System.out.println("###############################");
        System.out.println("I GOT A MESSAGE");
        System.out.println(msg);
        System.out.println("###############################");
        System.out.println("###############################");
    }
}
