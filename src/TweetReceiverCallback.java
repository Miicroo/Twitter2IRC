import twitter4j.Status;


public interface TweetReceiverCallback {
	public void handleStatus(Status tweet);
}
