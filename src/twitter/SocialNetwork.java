package twitter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     *
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet. This must be implemented. Other kinds
     *         of evidence may be used at the implementor's discretion.
     *         All the Twitter usernames in the returned social network must be
     *         either authors or @-mentions in the list of tweets.
     */
	public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
	    Map<String, Set<String>> followsGraph = new HashMap<>();
	    
	    // Pattern to match mentions
	    Pattern mentionPattern = Pattern.compile("@(\\w+)", Pattern.CASE_INSENSITIVE);

	    for (Tweet tweet : tweets) {
	        String author = tweet.getAuthor().toLowerCase();
	        Matcher matcher = mentionPattern.matcher(tweet.getText());
	        
	        // Check if there are mentions in the tweet
	        boolean hasMentions = false;

	        while (matcher.find()) {
	            String mentionedUser = matcher.group(1).toLowerCase();
	            // Only add to the followsGraph if the mentioned user is not the author
	            if (!mentionedUser.equals(author)) {
	                followsGraph.putIfAbsent(author, new HashSet<>());
	                followsGraph.get(author).add(mentionedUser);
	                hasMentions = true; // Mark that there are mentions
	            }
	            // Ensure the mentioned user is in the graph
	            followsGraph.putIfAbsent(mentionedUser, new HashSet<>());
	        }

	        // Only add the author to the graph if they mentioned someone
	        if (hasMentions) {
	            followsGraph.putIfAbsent(author, new HashSet<>());
	        }
	    }

	    return followsGraph;
	}



	
    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     *
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
	public static List<String> influencers(Map<String, Set<String>> followsGraph) {
	    Map<String, Integer> followerCount = new HashMap<>();

	    // Count followers for each user
	    for (String user : followsGraph.keySet()) {
	        for (String followed : followsGraph.get(user)) {
	            followerCount.put(followed, followerCount.getOrDefault(followed, 0) + 1);
	        }
	    }

	    // Ensure every user is included in the followerCount map
	    for (String user : followsGraph.keySet()) {
	        followerCount.putIfAbsent(user, 0);
	    }

	    // Collect influencers: those with more than 0 followers
	    List<String> influencers = new ArrayList<>();
	    for (Map.Entry<String, Integer> entry : followerCount.entrySet()) {
	        if (entry.getValue() > 0) { // Only include users with at least one follower
	            influencers.add(entry.getKey());
	        }
	    }

	    // Sort influencers by follower count in descending order
	    influencers.sort((a, b) -> followerCount.get(b) - followerCount.get(a));

	    return influencers;
	}

}
