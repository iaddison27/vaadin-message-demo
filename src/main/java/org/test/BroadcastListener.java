package org.test;

public interface BroadcastListener {
	
    void receiveBroadcast(String sender, String time, String message);
    
    void receiveBroadcastLogin(String username);
    
    void receiveBroadcastLogout(String username);
    
}
