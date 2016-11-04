package org.test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Broadcaster singleton that registers UIs and broadcasts messages to them safely
 */
public class Broadcaster {
	
	private static ExecutorService executorService = Executors.newSingleThreadExecutor();

	// Map of username -> BroadcastListener
    private static Map<String, BroadcastListener> listeners = new HashMap<>();

    /**
     * Register the BroadcastListener belonging to the specified user
     * @param username user this listener belongs to
     * @param listener BroadcastListener to register
     */
    public static synchronized void register(String username, BroadcastListener listener) {
        listeners.put(username, listener);
    }

    /**
     * Unregister the BroadcastListener for the specified user
     * @param username user whose BroadcastListener to unregister
     */
    public static synchronized void unregister(String username) {
        listeners.remove(username);
    }

    /**
     * Broadcasts the given message to the specified recipient
     * @param sender sender who sent this message
     * @param recipient recipient to broadcast message to. If "All" the message is sent to all listeners
     * @param message message to broadcast
     */
    public static synchronized void broadcast(final String sender, final String recipient, final String message) {
    	if (recipient.equals("All")) {
    		for (final BroadcastListener listener: listeners.values()) {
                executorService.execute(() -> {
                    listener.receiveBroadcast(sender, LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")), message);
                });
            }	
    	} else {
    		 executorService.execute(() -> {
                 listeners.get(recipient).receiveBroadcast(sender, LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")), message);
             });
    	}
        
    }
    
    /**
     * Broadcasts a message to all listeners to inform them the specified user has logged in
     * @param username user that has logged in
     */
    public static synchronized void broadcastLogin(String username) {
    	for (final BroadcastListener listener: listeners.values()) {
            executorService.execute(() -> {
                listener.receiveBroadcastLogin(username);
            });
        }
    }
    
    /**
     * Broadcasts a message to all listeners to inform them the specified user has logged out
     * @param username user that has logged out
     */
    public static synchronized void broadcastLogout(String username) {
    	for (final BroadcastListener listener: listeners.values()) {
            executorService.execute(() -> {
                listener.receiveBroadcastLogout(username);
            });
        }
    }
}
