package edu.knoldus

import com.typesafe.config.ConfigFactory
import twitter4j.conf.ConfigurationBuilder
import twitter4j.{Logger, Query, Twitter, TwitterFactory}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.util.{Failure, Success}

object Application extends App {

  //  val tweet = new Operation
  //  tweet.getHashTagTweets("#Budget2018")

  val tweet = new Tweets
  val hashtag = "#Budget2018"
  val query = new Query(hashtag)
  query.setSince("2018-1-31")
  val count=100
  query.setCount(count)
  val config = ConfigFactory.load("application.conf")
  val log = Logger.getLogger(this.getClass)
  val consumerKey = config.getString("Twitter.key.consumerKey")
  val consumerSecretKey = config.getString("Twitter.key.consumerSecretKey")
  val accessToken = config.getString("Twitter.key.accessToken")
  val accessSecretToken = config.getString("Twitter.key.accessSecretToken")
  val configurationBuilder = new ConfigurationBuilder()
  configurationBuilder.setDebugEnabled(false)

    .setOAuthConsumerKey(consumerKey)
    .setOAuthConsumerSecret(consumerSecretKey)
    .setOAuthAccessToken(accessToken)
    .setOAuthAccessTokenSecret(accessSecretToken)
  val twitter = new TwitterFactory(configurationBuilder.build()).getInstance()
  val startDate = "2018-01-31"
  val endDate = "2018-02-5"
  val LikesPerTweet = tweet.averageLikes(twitter, query)

  val TweetsRecord = tweet.getTweets(twitter, hashtag)
  TweetsRecord onComplete {
    case Success(value) => log.info("\n\n Tweets are :" + value)
    case Failure(value) => log.info(value.getMessage)
  }

  val avgTweet = tweet.averageTweets(twitter, startDate, endDate, query)
  TweetsRecord onComplete {
    case Success(value) => log.info("\n\n Average Tweets are :" + value)
    case Failure(value) => log.info(value.getMessage)
  }

  val noOfTweets = tweet.tweetsCount(twitter, query)
  noOfTweets onComplete {
    case Success(value) => log.info("\n\nTweets Count is : " + value)
    case Failure(value) => log.info(value.getMessage)
  }

  val AverageTweets = tweet.averageRetweets(twitter, startDate, endDate, query)
  AverageTweets onComplete {
    case Success(value) => log.info("\n\nAverage number of Re-tweets are :" + value)
    case Failure(value) => log.info(value.getMessage)
  }

  LikesPerTweet onComplete {
    case Success(value) => log.info("\n\nlikes per Tweets are:" + value)
    case Failure(value) => log.info(value.getMessage)
  }
  Thread.sleep(200)
}

