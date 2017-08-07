/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.usdm.web.util;

import java.io.IOException;

import com.rabbitmq.client.ShutdownSignalException;

import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import egovframework.com.cmm.EgovProperties;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

@Component
public class RabbitMQManager {

    static final private String queueServer = EgovProperties.getProperty("rabbitmq.ip");
    static final private int    queuePort   = Integer.parseInt(EgovProperties.getProperty("rabbitmq.port"));
    static final private String queueName   = EgovProperties.getProperty("rabbitmq.queue");
    
    static final private String queueClientID  = EgovProperties.getProperty("rabbitmq.username");
    static final private String queueClientPWD = EgovProperties.getProperty("rabbitmq.password");
    
    @Autowired
    //NotificationService notificationService;
    
    static ConnectionFactory factory ;
    static Connection connection;
    static Channel channel;
    
    public static Channel getChannel() throws IOException, TimeoutException {
    	if (channel !=null) return channel;
    	
	    factory = new ConnectionFactory();

	    factory.setHost(queueServer);
	    factory.setPort(queuePort);
	    factory.setUsername(queueClientID);
	    factory.setPassword(queueClientPWD);

		connection = factory.newConnection();
	    channel = connection.createChannel();
	    
	    return channel;
	   
    }
    /*
    public boolean add() throws IOException, TimeoutException {
    	if (channel==null || !channel.isOpen()) this.getChannel();
    	
    	channel.queueDeclare(queueName, true, false, false, null);
    	channel.close();
    	channel = null;
    	
    	return true;
    }
    
    public boolean remove() throws IOException, TimeoutException {
    	if (channel==null || !channel.isOpen()) this.getChannel();
    	
   		channel.queueDelete(queueName);
   		
   		return true;
    }
    */
    public static boolean sendMessage(String message) throws IOException, TimeoutException {
    	
    	// 운영배포시 주석해제 START
    	if (channel==null || !channel.isOpen()) getChannel();
    	
    	channel.basicPublish("", queueName, null, message.getBytes());
    	// 운영배포시 주석해제 END

		return true;
    }
    /*
    public static boolean sendMessages(List<String> queueNames, String message) throws IOException, TimeoutException {
    	if (queueNames==null || queueNames.isEmpty()) return false;
    	if (channel==null || !channel.isOpen()) getChannel();
    	
    	for (int i=0;i<queueNames.size();i++)
    		channel.basicPublish("", queueNames.get(i), null,message.getBytes());

		System.out.println(" [x] Sent ^^" + message);
		
	    return true;
    }
    
    public boolean consumer(String queueName) throws IOException {
    	boolean autoAck = false;
    	
    	try {
    		getChannel();

			channel.basicConsume(queueName, autoAck, "myConsumerTag",
			    new DefaultConsumer(channel) {
			        @Override
			        public void handleDelivery(String consumerTag,
			                                    Envelope envelope,
			                                    AMQP.BasicProperties properties,
			                                    byte[] body)
			            throws IOException
			        {
			            String routingKey = envelope.getRoutingKey();
			            String contentType = properties.getContentType();
			            long deliveryTag = envelope.getDeliveryTag();
			            // (process the message components here ...)
			            String message = new String(body, "UTF-8");
			            
			            System.out.println(" [x] Received '" + message + "'");
			            
			            try {
							notificationService.runRules(JsonUtil.toEventVO(message));
						
			            } catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			            
			            channel.basicAck(deliveryTag, false);
			        }
			    });
			
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	
    	return true;
    }
    
    public boolean close() throws IOException, TimeoutException {
    	
    	if (channel==null || !channel.isOpen()) channel.close();
    	
	    return true;
	   
    }
    
    @PostConstruct
    public void initConsumer() throws IOException, TimeoutException {
    	try {
    		add("uspapi");
			consumer("uspapi");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
    }
    */
}