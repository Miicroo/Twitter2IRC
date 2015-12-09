import java.util.Properties;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;


public final class BotTwitterStream {
	
	private ConfigurationBuilder configuration;
	private static final String DEFAULT_PROPERTY = "",
								CONSUMER_KEY = "OAuthConsumerKey",
								CONSUMER_SECRET = "OAuthConsumerSecret",
								ACCESS_TOKEN = "OAuthAccessToken",
								ACCESS_TOKEN_SECRET = "OAuthAccessTokenSecret";
	
	public BotTwitterStream() {
		loadConfiguration();
	}
	
	private void loadConfiguration() {
		Properties props = Configuration.getProperties();
		
		configuration = new ConfigurationBuilder();
		configuration.setDebugEnabled(true);
		configuration.setOAuthConsumerKey(props.getProperty(CONSUMER_KEY, DEFAULT_PROPERTY));
		configuration.setOAuthConsumerSecret(props.getProperty(CONSUMER_SECRET, DEFAULT_PROPERTY));
		configuration.setOAuthAccessToken(props.getProperty(ACCESS_TOKEN, DEFAULT_PROPERTY));
		configuration.setOAuthAccessTokenSecret(props.getProperty(ACCESS_TOKEN_SECRET, DEFAULT_PROPERTY));
	}
	
    public void listenToTwitter(final TweetReceiverCallback callback, String... filter) throws TwitterException {    	
        TwitterStream twitterStream = new TwitterStreamFactory(configuration.build()).getInstance();
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                callback.handleStatus(status);
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        
        twitterStream.addListener(listener);
        twitterStream.filter(filter);
    }
}