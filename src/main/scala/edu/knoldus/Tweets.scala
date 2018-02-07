package edu.knoldus

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import twitter4j._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.JavaConverters._

class Tweets {

  val log = Logger.getLogger(this.getClass)
  val count = 100

  def getTweets(twitter: Twitter, hashtag: String): Future[List[Status]] = {
    /**
      * retrieving tweets
      */
    val query = new Query(hashtag)
    query.setCount(count)
    query.setSince("2018-01-31")
    log.info("tweets")

    Future {
      val result = twitter.search(query).getTweets.asScala.toList
      result
    }
  }

  def tweetsCount(twitter: Twitter, query: Query): Future[Int] = {
    /**
      * retrieving tweets count
      */
    query.setCount(count)
    query.setSince("2018-01-31")
    log.info("tweets count")

    Future {
      try {
        twitter.search(query).getTweets.size()
      }
      catch {
        case exception: Exception => throw exception
      }
    }
  }

  def averageTweets(twitter: Twitter, startDate: String, endDate: String, query: Query): Future[Long] = Future {
    /**
      * retrieving average tweets count
      */

    try {
      query.setSince(startDate)
      query.setUntil(endDate)
      twitter.search(query).getTweets.size() / 7
    }
    catch {
      case exception: Exception => throw exception
    }
  }


  def averageLikes(twitter: Twitter, query: Query): Future[Int] = {
    /**
      * average average likes count
      */

    log.info("Average Likes Count")
    Future {

      try {
        query.setCount(count)
        query.setSince("2018-01-31")
        val tweets = twitter.search(query).getTweets.asScala.toList
        tweets.map(_.getFavoriteCount).size / tweets.size
      }
      catch {
        case exception: Exception => throw exception
      }
    }
  }

  def getReTweetCount(hashTag: String, twitter: Twitter): Future[Int] = Future {
    /**
      * retrieving retweets count
      */
    try {
      val query = new Query(hashTag)
      val tweets = twitter.search(query).getTweets.asScala.toList
      tweets.map(_.getRetweetCount).size / tweets.size
    }
    catch {
      case exception: Exception => throw exception
    }
  }

  def averageRetweets(twitter: Twitter, startDate: String, endDate: String, query: Query): Future[Long] = Future {
    /**
      * retrieving average retweets count
      */

    log.info("Average Retweets Count")
    try {
      val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
      query.setSince("2018-01-31")
      query.setUntil("2018-02-05")
      val tweets = twitter.search(query).getTweets.asScala.toList
      tweets.map(_.getRetweetCount).size / tweets.size
    }
    catch {
      case exception: Exception => throw exception
    }

  }
}
