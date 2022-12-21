# :moneybag:Currency Exchange Rates:moneybag:

<p><img align="center" alt="gif" src="https://31.media.tumblr.com/8763d825f0f8a7e1b1c5a86687e9685b/tumblr_n78d30rtFl1te639do1_500.gif" height="220" /></p>

To run the project clone the repository. 

Using command prompt to navigate to the cloned repository.

First have to create a docker network. 

      docker network create my_network

First we need to set-up a database server through docker. In this example I used MariaDb as my database and the platform I used for the database was Dbeaver.

To connect database server copy the command below and run this in cmd:

      docker run --name exchange_rates -e MARIADB_USER=root -e MARIADB_PASSWORD=root --network=my_network -e MYSQL_ROOT_PASSWORD=root -e MARIADB_DATABASE=exchange_rates  -p 3306:3306 -d mariadb:latest

After running it, there should be a container that has been created in your Docker app. Check if it is there.
It should look like this:

![image](https://user-images.githubusercontent.com/99561972/208785666-47d2730a-498e-4ce2-ac2d-075037746621.png)

To connect it to my local database platform(Dbeaver), I selected "new database connection" --> MariaDB --> then check the following so that it matches the image below.

The password is same as username.

![image](https://user-images.githubusercontent.com/99561972/208785769-4c4b6aa1-e07d-4b66-92b4-ea9551efcd79.png)

After connecting to the database we need to create a Docker image using CMD. To create an docker image run the command below:
For this example I named my image "irock", but you can chose whatever name you wish.

    docker build -t irock .
    
Now that our image has been created we need to run our application using the docker image.

:+1:To connect to database use the command below

      docker run --network=my_network --name connects_to_db irock
    
:+1:To connect to database and insert values in the database table run    

      docker run --network=my_network --name inserted_data irock "insertdata"

:+1:To activate both endpoints run
     
     docker run -d --network=my_network -p 8080:8080 --name endpoints irock "endpoints"
     
Now that our endpoints are active, we should check them in an API platform like postman or in a web browser

Will be running a get method which will return all the currencies and its data that is available in the database

      http://localhost:8080 
      
This endpoint will create a get method which will return specific chosen currency. 

      http://localhost:8080/CURRENCY      
      
<p><img align="center" alt="gif" src="http://www.reactiongifs.com/r/mone.gif" width="500" height="320" /></p>     
      
## Result:trumpet:

![image](https://user-images.githubusercontent.com/99561972/208789723-af92c5cd-e2c9-4de1-b421-73480a4f7e25.png)

     
    

      
