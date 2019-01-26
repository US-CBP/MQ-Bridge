# WMQExtractor

#### A connector piece to read messages from an IBM MQ, places them in a folder, and attempts to post. 

To run this in a development environment:

* Configure dependencies
* Change the properties files mentioned below
* Run this package as a **Spring Boot** service (mvn spring-boot:run)
* Copy the application.EXAMPLE.yml file in the resources folder to a file called application.yml. Fill in the properties as appropriate. **Do not modify the application.EXAMPLE.yml file.**
* Edit cron-job task parameters in your "application.yml" file, to avoid runtime errors:
	* failLog.cronjob.task.1: cron job time 1 goes here - e.g. 0 0 * * * *     -------->  failLog.cronjob.task.1: 0 0 * * * *
	* failLog.cronjob.task.2: cron job times 2 goes here - e.g. 0 30 * * * *   -------->  failLog.cronjob.task.2: 0 30 * * * *

