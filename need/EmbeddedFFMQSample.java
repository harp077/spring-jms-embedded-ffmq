/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jennom.hot;

import java.util.Properties;
import net.timewalker.ffmq4.FFMQCoreSettings;
import net.timewalker.ffmq4.listeners.ClientListener;
import net.timewalker.ffmq4.listeners.tcp.io.TcpListener;
import net.timewalker.ffmq4.local.FFMQEngine;
import net.timewalker.ffmq4.management.destination.definition.QueueDefinition;
import net.timewalker.ffmq4.utils.Settings;

public class EmbeddedFFMQSample implements Runnable {

    private FFMQEngine engine;

    /*
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            // Create engine settings
            Settings settings = createEngineSettings();

            // Create the engine itself
            engine = new FFMQEngine("myLocalEngineName", settings);
            //   -> myLocalEngineName will be the engine name.
            //       - It should be unique in a given JVM
            //       - This is the name to be used by local clients to establish
            //         an internal JVM connection (high performance)
            //         Use the following URL for clients :   vm://myLocalEngineName
            //

            // Deploy the engine
            System.out.println("Deploying engine : " + engine.getName());
            engine.deploy();
            //  - The FFMQ engine is not functional until deployed.
            //  - The deploy operation re-activates all persistent queues
            //    and recovers them if the engine was not properly closed.
            //    (May take some time for large queues)

            // Adding a TCP based client listener
            System.out.println("Starting listener ...");
            ClientListener tcpListener = new TcpListener(engine, "0.0.0.0", 10002, settings, null);
            tcpListener.start();

            // This is how you can programmatically define a new queue
            if (!engine.getDestinationDefinitionProvider().hasQueueDefinition("foo")) {
                QueueDefinition queueDef = new QueueDefinition();
                queueDef.setName("foo");
                queueDef.setUseJournal(false);
                queueDef.setMaxNonPersistentMessages(1000);
                queueDef.check();
                engine.createQueue(queueDef);
            }

            // You could also define a queue using some java Properties
            if (!engine.getDestinationDefinitionProvider().hasQueueDefinition("foo2")) {
                Properties queueProps = new Properties();
                queueProps.put("name", "foo2");
                queueProps.put("persistentStore.useJournal", "false");
                queueProps.put("memoryStore.maxMessages", "1000");
                QueueDefinition queueDef2 = new QueueDefinition(new Settings(queueProps));
                engine.createQueue(queueDef2);
            }

            // Run for some time
            System.out.println("Running ...");
            Thread.sleep(30 * 1000);

            // Stopping the listener
            System.out.println("Stopping listener ...");
            tcpListener.stop();

            // Undeploy the engine
            System.out.println("Undeploying engine ...");
            engine.undeploy();
            //   - It is important to properly shutdown the engine 
            //     before stopping the JVM to make sure current transactions 
            //     are nicely completed and storages properly closed.

            System.out.println("Done.");
        } catch (Exception e) {
            // Oops
            e.printStackTrace();
        }
    }
        
    private Settings createEngineSettings() {
        // Various ways of creating engine settings

        // 1 - From a properties file
        /*Properties externalProperties = new Properties();
        try {
            FileInputStream in = new FileInputStream("../conf/ffmq-server.properties");
            externalProperties.load(in);
            in.close();
        } catch (Exception e) {
            throw new RuntimeException("Cannot load external properties", e);
        }
        Settings settings = new Settings(externalProperties);*/

        // 2 - Explicit Java code
        Settings settings = new Settings();
        settings.setStringProperty(FFMQCoreSettings.DESTINATION_DEFINITIONS_DIR, ".");
        settings.setStringProperty(FFMQCoreSettings.BRIDGE_DEFINITIONS_DIR, ".");
        settings.setStringProperty(FFMQCoreSettings.TEMPLATES_DIR, ".");
        settings.setStringProperty(FFMQCoreSettings.DEFAULT_DATA_DIR, ".");
        return settings;
    }        
    
}
