package ppd.com.mubler.ui.user;

import ppd.com.mubler.data.entity.User;

public class UserSession {
    private User user;
    private String token;
    private static UserSession SESSION;

    private UserSession() {
        this.user = null;
        this.token = null;
    }

    public static UserSession getInstance(){
        if(SESSION != null){
            return SESSION;
        }else{
            SESSION = new UserSession();
        }
        return SESSION;
    }

    public void initSession(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public boolean isConnected() {
        if(user != null && token != null){
            return true;
        }
        return false;
    }

    public static UserSession getSession() {
        if(getInstance().getCurrentUser() == null){
            throw new IllegalStateException("Session not initialized !");
        }else{
            return getInstance();
        }
    }

    public void updateCurrentUser(User user) {
        if(SESSION != null){
            this.user = user;
        }
    }

    public User getCurrentUser() {
        if (user == null) {
            throw new IllegalStateException("Current user has not been initialized yet");
        }

        return user;
    }

    public String getToken() {
        return this.token;
    }

    public void logout() {
        this.user = null;
        this.token = null;
        SESSION = null;
    }
}
