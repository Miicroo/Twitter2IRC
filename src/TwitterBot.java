import java.io.IOException;
import java.util.Properties;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

import twitter4j.Status;
import twitter4j.TwitterException;


public class TwitterBot extends PircBot implements TweetReceiverCallback {

	private BotTwitterStream stream;
	private Properties props;
	private static final String DEFAULT_PROPERTY = "",
								BOT_NAME = "BotName",
								BOT_PASSWORD = "BotPassword",
								BOT_LOGIN = "BotLogin",
								BOT_CHANNEL = "BotChannel",
								TWITTER_FILTER = "TwitterFilter";
	
	public TwitterBot() {
		stream = new BotTwitterStream();
		props = Configuration.getProperties();
	}
	
	
	public void startBot() throws NickAlreadyInUseException, IOException, IrcException {
        connect("irc.chalmers.it");
	}
	
	@Override
	public void onConnect() {		
        setVerbose(true);
        setName(props.getProperty(BOT_NAME, DEFAULT_PROPERTY));
        setLogin(props.getProperty(BOT_LOGIN, DEFAULT_PROPERTY));
        changeNick(props.getProperty(BOT_NAME, DEFAULT_PROPERTY));
        
        joinChannel(props.getProperty(BOT_CHANNEL, DEFAULT_PROPERTY));
        
        try {
        	stream.listenToTwitter(this, props.getProperty(TWITTER_FILTER, DEFAULT_PROPERTY));
        } catch(TwitterException te) {
        	te.printStackTrace();
        	System.err.println("Cannot connect to Twitter! Disconnecting...");
        	
        	disconnect();
        }
	}
	
	@Override
	public void handleStatus(Status tweet) {
		StringBuilder message = new StringBuilder();
		message.append(tweet.getText());
		
		sendMessage(props.getProperty(BOT_CHANNEL, DEFAULT_PROPERTY), message.toString());
	}

}
