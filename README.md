## Just some little utilities and experiments to understand using Curator and Zookeeper from Java ##

edit LeaderElectionMain.properties to specify your zookeeper host and port, and how many threads you want to run.

mvn package to build the jar with dependencies.

Start up the app with java -jar federate-lztesting-0.0.1-SNAPSHOT-jar-with-dependencies.jar

* The green light indicates which thread has leader.
* Use the 'Relinquish' button to give up leadership and watch another thread be elected leader.
* The individual threads will not requeue until the Requeue button is pressed. If there are no threads queued, no leader will be elected.
* Stop the Zookeeper server and watch the threads handle the state changes. The current leader will relinquish control.
* Restart Zookeeper to see a new leader election happen.

You can run multiple separate instances of the app to see leadership jump across JVMs.
