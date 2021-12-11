package jennom.hot;

import com.google.gson.Gson;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
//
import org.springframework.jms.annotation.EnableJms;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import jennom.jms.JmsExceptionListener;
import net.timewalker.ffmq4.FFMQConstants;
import net.timewalker.ffmq4.local.FFMQEngine;
import net.timewalker.ffmq4.spring.FFMQServerBean;
//import net.timewalker.ffmq4.spring.FFMQServerBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

@Configuration
@ComponentScan(basePackages = {"jennom"})
@EnableAsync
@EnableJms
public class AppContext {

    private String concurrency = "1-4";
    //private String brokerURL = "tcp://localhost:61616";
    
    @PostConstruct
    public void afterBirn() {
        //InitialContext context=null;
        //FFMQEngine engine=null;
        /*try {
            context = new InitialContext();
            engine = (FFMQEngine) context.lookup("ffmqHarp07");
        } catch (NamingException ex) {
            Logger.getLogger(AppContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        engine.getName();*/
    }    

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
    
    //=========== new 21-11-2021
    @Bean
    @DependsOn("ffmqServer")
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(JmsExceptionListener jmsExceptionListener) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(jmsConnectionFactory(jmsExceptionListener));
        factory.setDestinationResolver(destinationResolver());
        factory.setConcurrency(concurrency);
        factory.setPubSubDomain(false);
        return factory;
    }

    @Bean
    @DependsOn("ffmqServer")
    public ConnectionFactory jmsConnectionFactory(JmsExceptionListener jmsExceptionListener) {
        return createJmsConnectionFactory(jmsExceptionListener);
    }

    private ConnectionFactory createJmsConnectionFactory(JmsExceptionListener jmsExceptionListener) {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, FFMQConstants.JNDI_CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, "tcp://localhost:10002");
        Context context=null;
        ConnectionFactory jcf=null;
        FFMQEngine engine=null;
        try {
            context = new InitialContext(env);
            jcf = (ConnectionFactory) context.lookup(FFMQConstants.JNDI_CONNECTION_FACTORY_NAME);
            //engine = (FFMQEngine) context.lookup("ffmqHarp07");
            //System.out.println("engine.getName() = "+engine.getName());
        } catch (NamingException ex) {
            Logger.getLogger(AppContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(jcf);
        connectionFactory.setExceptionListener(jmsExceptionListener);
        return connectionFactory;
    }
    
    @Bean(name="ffmqServer", initMethod="start", destroyMethod="stop")
    public FFMQServerBean ffmqServer() {
        FFMQServerBean ffmqServerBean = new FFMQServerBean();
        ffmqServerBean.setConfigLocation("classpath:ffmq-server.properties");
        ffmqServerBean.setEngineName("ffmqHarp07");
        /*try {
            ffmqServerBean.start();
            System.out.println("ffmqServerBean.getConfigLocation() = "+ffmqServerBean.getConfigLocation());
            System.out.println("ffmqServerBean.getEngineName() = "+ffmqServerBean.getEngineName());
        } catch (JMSException ex) {
            System.out.println("FFMQServerBean exception "+ex.getMessage());
            Logger.getLogger(AppContext.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        return ffmqServerBean;
    }    

    @Bean(name = "jmsQueueTemplate")
    @DependsOn("ffmqServer")
    public JmsTemplate createJmsQueueTemplate(ConnectionFactory jmsConnectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(jmsConnectionFactory);
        jmsTemplate.setDefaultDestinationName("harp07qq");//.setDefaultDestination(queue());
        return jmsTemplate;
    }

    /*@Bean(name = "jmsTopicTemplate")
    @DependsOn("ffmqServer")
    public JmsTemplate createJmsTopicTemplate(ConnectionFactory jmsConnectionFactory) {
        JmsTemplate template = new JmsTemplate(jmsConnectionFactory);
        // TOPIC NOT LISTEN WITH template.setPubSubDomain(true); !!!!!!!!!!!!!!!!!!
        //template.setPubSubDomain(true);
        return template;
    }*/

    @Bean
    @DependsOn("ffmqServer")
    public DestinationResolver destinationResolver() {
        return new DynamicDestinationResolver();
    }

    /// ========================= JMS ============================

    /*@Bean
    public Queue queue(){
        return new ActiveMQQueue("harp07qq");
    }
    
    @Bean
    public Topic topic(){
        return new ActiveMQTopic("harp07tt");
    }  
    
    @Bean 
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory amqCF=new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
        //amqCF.setPassword("admin");
        //amqCF.setUserName("admin");
	return amqCF;
    }

    @Bean
    public JmsListenerContainerFactory<DefaultMessageListenerContainer> jmsListenerContainerFactory() {
	DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
	factory.setConnectionFactory(connectionFactory());
	factory.setConcurrency("3-5");
	return factory;
    }

    @Bean 
    public JmsTemplate jmsTemplate() {
	JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        // Default Destination
	jmsTemplate.setDefaultDestination(queue());
        // then use jmsTemplate.setDeliveryDelay(5000L); in ActiveMQ -> ERROR !!!!!!!
        //jmsTemplate.setDeliveryDelay(5000L);
        //jmsTemplate.setPubSubDomain(true);
	return jmsTemplate;
    }  */
    //========================================
    @Bean(name = "gson")
    public Gson gson() {
        return new Gson();
    }

}
