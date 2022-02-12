import csv
import tweepy
from tweepy import OAuthHandler
import config
import sys

consumer_key = config.twitter['consumer_key']
consumer_secret = config.twitter['consumer_secret']
access_key = config.twitter['access_key']
access_secret = config.twitter['access_secret']


auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_key, access_secret)
api = tweepy.API(auth)


query = 'corona'
file_name = query+'.csv'

csvFile = open(file_name, 'w', encoding='utf-8')

csvWriter = csv.writer(csvFile)

tweet_count = int(sys.argv[1])

tweet_num = 1
media_files = set()

for tweet in tweepy.Cursor(api.search_tweets,q=query,count=100,lang="en").items(tweet_count):
    if tweet.place is not None:
            print ('tweet number: {}'.format(tweet_num), tweet.text)
            csvWriter.writerow([tweet.created_at,
                                tweet.user.location,
                                tweet.user.followers_count,
                                tweet.user.friends_count,
                                tweet.text,
                                tweet.place.bounding_box.coordinates,
                                tweet.place.country,
                                tweet.place.country_code])
            tweet_num += 1
            
for media_file in media_files:
    wget.download(media_file, out='images')

print("Total number of Tweets pulled with Location: {0}".format(tweet_num))
print("Total number of Tweets without Location: {0}".format(tweet_count-tweet_num))