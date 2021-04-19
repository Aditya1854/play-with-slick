play-with-slick [ ![Codeship Status for techmonad/play-slick-app](https://app.codeship.com/projects/8a9b68a0-4e15-0136-d88f-42007895f430/status?branch=master)](https://app.codeship.com/projects/293442)
=======

-----------------------------------------------------------------------
Instructions :-
-----------------------------------------------------------------------
Clone and run the app(default database is mysql and for testing default database is H2):

     $ git clone git@github.com:Aditya1854/play-with-slick.git
     $ cd play-with-slick
     $ sbt run
    
 Run the all unit test:

     $ sbt test
    
Run the app using Postgres database:

	first make postgres.conf file and add the configuration for postgres

     $ sbt 'run   -Dconfig.file=conf/postgres.conf'
    

-----------------------------------------------------------------------
###References :-
-----------------------------------------------------------------------

* [Play 2.8.x](https://www.playframework.com/documentation/2.8.x/Home)
* [Slick](http://slick.typesafe.com/)

