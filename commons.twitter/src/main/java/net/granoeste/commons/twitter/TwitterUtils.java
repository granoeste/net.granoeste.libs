
package net.granoeste.commons.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class TwitterUtils {
	private static final String TOKEN = "token";
	private static final String TOKEN_SECRET = "token_secret";
	private static final String PREF_NAME = "twitter_access_token";

	/**
	 * Twitterインスタンスを取得します。アクセストークンが保存されていれば自動的にセットします。
	 * 
	 * @param context Context
	 * @param consumerKey Twitter Consumer Key
	 * @param consumerSecret Twitter Consumer Secret
	 * @return Twitter
	 */
	public static Twitter getTwitterInstance(Context context, String consumerKey,
			String consumerSecret) {

		TwitterFactory factory = new TwitterFactory();
		Twitter twitter = factory.getInstance();
		twitter.setOAuthConsumer(consumerKey, consumerSecret);

		if (hasAccessToken(context)) {
			twitter.setOAuthAccessToken(loadAccessToken(context));
		}
		return twitter;
	}

	/**
	 * アクセストークンをプリファレンスに保存します。
	 * 
	 * @param context Context
	 * @param accessToken AccessToken
	 */
	public static void storeAccessToken(Context context, AccessToken accessToken) {
		SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(TOKEN, accessToken.getToken());
		editor.putString(TOKEN_SECRET, accessToken.getTokenSecret());
		editor.apply();
	}

	/**
	 * アクセストークンをプリファレンスから読み込みます。
	 * 
	 * @param context Context
	 * @return AccessToken
	 */
	public static AccessToken loadAccessToken(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		String token = preferences.getString(TOKEN, null);
		String tokenSecret = preferences.getString(TOKEN_SECRET, null);
		if (token != null && tokenSecret != null) {
			return new AccessToken(token, tokenSecret);
		} else {
			return null;
		}
	}

	/**
	 * アクセストークンをプリファレンスから消去します。
	 * 
	 * @param context Context
	 */
	public static void clearAccessToken(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.clear().apply();
	}

	/**
	 * アクセストークンが存在する場合はtrueを返します。
	 * 
	 * @return true:has access token. false:don't has access token.
	 */
	public static boolean hasAccessToken(Context context) {
		return loadAccessToken(context) != null;
	}
}
