import csv
import tweepy
from tweepy import OAuthHandler
import config
import wget
import preprocessor as p
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
import re
import string

consumer_key = config.twitter['consumer_key']
consumer_secret = config.twitter['consumer_secret']
access_key = config.twitter['access_key']
access_secret = config.twitter['access_secret']


auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_key, access_secret)
api = tweepy.API(auth, wait_on_rate_limit=True)


query = 'location'

csvFile_1 = open('tweet_pulled_with_Location.csv', 'w', encoding='utf-8')
csvFile_2 = open('tweet_pulled_without_Location.csv', 'w', encoding='utf-8')

csvWriter_1 = csv.writer(csvFile_1)
csvWriter_2 = csv.writer(csvFile_2)

tweet_num = 0
media_files = set()

emoticons_happy = set([
    ':-)', ':)', ';)', ':o)', ':]', ':3', ':c)', ':>', '=]', '8)', '=)', ':}',
    ':^)', ':-D', ':D', '8-D', '8D', 'x-D', 'xD', 'X-D', 'XD', '=-D', '=D',
    '=-3', '=3', ':-))', ":'-)", ":')", ':*', ':^*', '>:P', ':-P', ':P', 'X-P',
    'x-p', 'xp', 'XP', ':-p', ':p', '=p', ':-b', ':b', '>:)', '>;)', '>:-)',
    '<3'
    ])

emoticons_sad = set([
    ':L', ':-/', '>:/', ':S', '>:[', ':@', ':-(', ':[', ':-||', '=L', ':<',
    ':-[', ':-<', '=\\', '=/', '>:(', ':(', '>.<', ":'-(", ":'(", ':\\', ':-c',
    ':c', ':{', '>:\\', ';('
    ])

emoji_pattern = re.compile("["
         u"\U0001F600-\U0001F64F"
         u"\U0001F300-\U0001F5FF"
         u"\U0001F680-\U0001F6FF"
         u"\U0001F1E0-\U0001F1FF"
         u"\U00002702-\U000027B0"
         u"\U000024C2-\U0001F251"
         "]+", flags=re.UNICODE)

emoticons = emoticons_happy.union(emoticons_sad)


def clean_tweets(tweet):

    stop_words = set(stopwords.words('english'))
    word_tokens = word_tokenize(tweet)

    tweet = re.sub(r':', '', tweet)
    tweet = re.sub(r'[^\x00-\x7F]+',' ', tweet)
    tweet = emoji_pattern.sub(r'', tweet)

    filtered_tweet = [w for w in word_tokens if not w in stop_words]
    filtered_tweet = []

    for w in word_tokens:

        if w not in stop_words and w not in emoticons and w not in string.punctuation:
            filtered_tweet.append(w)
    return ' '.join(filtered_tweet)

for tweet in tweepy.Cursor(api.search_tweets,q=query,count=100,lang="en").items(20000):
    if tweet.place is not None:
        media = tweet.entities.get('media', [])
        if(len(media) > 0):
            media_files.add(media[0]['media_url'])
        print ('tweet number: {}'.format(tweet_num), tweet.text)
        clean_text = p.clean(tweet.text)
        filtered_tweet=clean_tweets(clean_text)
        print(filtered_tweet)
        csvWriter_1.writerow([tweet.created_at,
                            tweet.user.name,
                            tweet.user.location,
                            tweet.text,
                            filtered_tweet,
                            tweet.place.bounding_box.coordinates,
                            tweet.place.country,
                            tweet.place.country_code])
        tweet_num += 1

    else:
        media = tweet.entities.get('media', [])
        if(len(media) > 0):
            media_files.add(media[0]['media_url'])
        print ('tweet number: {}'.format(tweet_num), tweet.text)
        clean_text = p.clean(tweet.text)
        filtered_tweet=clean_tweets(clean_text)
        print(filtered_tweet)
        csvWriter_2.writerow([tweet.created_at,
                            tweet.user.name,
                            tweet.user.location,
                            tweet.text])
        tweet_num += 1

for media_file in media_files:
    wget.download(media_file, out='images')
