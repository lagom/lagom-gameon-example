
# Game On! with Lagom

[Lagom](https://www.lagomframework.com/) is a framework for developing reactive microservices in Java or Scala. Created by [Lightbend](https://www.lightbend.com/), Lagom is built on the proven [Akka](http://akka.io/) toolkit and [Play Framework](https://playframework.com/), and provides a highly productive, guided path for creating responsive, resilient, elastic, message-driven applications.

[Game On!](https://gameontext.org/) is both a sample microservices application, and a throwback text adventure brought to you by the WASdev team at IBM.

This project was created by the Maven archetype from https://github.com/lagom/lagom-gameon-maven-archetype.

See the `README.md` file in that repository for more information.

## Deploying to Bluemix

1.  Log in to Bluemix with the workshop email address you were provided:
    ```
    bx login -a https://api.ng.bluemix.net -c 1e892c355e0ba37560f028df670c2719
    ```

    You will be prompted for the email address and password. Enter these as provided to you.

2.  Initialize the Bluemix Container Service plugin:
    ```
    bx cs init
    ```

3.  Download the configuration files for the Kubernetes cluster:
    ```
    bx cs cluster-config gameon
    ```

    This should print details similar to the following, though the details might differ slightly:
    ```
    OK
    The configuration for javaone was downloaded successfully. Export environment variables to start using Kubernetes.

    export KUBECONFIG=/home/ubuntu/.bluemix/plugins/container-service/clusters/gameon/kube-config-dal10-gameon.yml
    ```

4.  Copy and run the provided `export` command to configure the Kubernetes CLI. Note that you should copy the command printed to your terminal, which might differ slightly from the example above.

5.  Test that you can run `kubectl` to list the resources in the Kubernetes cluster:
    ```
    kubectl get all
    ```

6.  Log in to the Bluemix Container Registry:
    ```
    bx cr login
    ```

7.  Build the Docker image for your service:
    ```
    mvn clean package docker:build
    ```


8.  Push the Docker image to the Bluemix registry:
    ```
    docker tag javaone/gameon17s99-impl:1.0-SNAPSHOT registry.ng.bluemix.net/javaone/gameon17s99-impl:1.0-SNAPSHOT
    docker push registry.ng.bluemix.net/javaone/gameon17s99-impl:1.0-SNAPSHOT
    ```

9.  Deploy the service to Kubernetes:
    ```
    kubectl create -f deploy/kubernetes/resources/service/
    ```

10. Wait for the service to begin running:
    ```
    kubectl get -w pod gameon17s99-0
    ```
    Press control-C to exit once this prints a line with "1/1" and "Running".

** Your room is up!**

Do a quick verification: http://gameon.us-south.containers.mybluemix.net/gameon17s99

## Register your room

1.  Go to [Game On!](https://gameontext.org) and sign in.

2.  Click on the building icon in the top right of the game screen to go to the Room Management page.

3.  Make sure **Create a new room** is selected from the **Select a room** drop-down.

4.  Provide a descriptive title for your room, e.g. `The Shortest Cut`

5.  A short nickname will be generated, but you can change it if you like (e.g. use your lab id: `gameon17s99`), but remember what it is!

6.  Describe your room (optional). The description provided here is used by the interactive map and other lists or indexes of defined rooms. The decription seen in the game will come from your code.

7. The repository field is optional. Come back and fill it in if you decide to push this into a public repository.

8.  Specify the http endpoint as a basic health endpoint: `http://gameon.us-south.containers.mybluemix.net/gameon17s99`

9.  Use a WebSocket URL for the WebSocket endpoint: `ws://gameon.us-south.containers.mybluemix.net/gameon17s99`

10. Leave the token blank for now. That is an [Advanced adventure](https://book.gameontext.org/walkthroughs/createMore.html) for another time.

11. Describe the doors to your room (Optional). Describe each door as seen [from the outside](https://book.gameontext.org/walkthroughs/registerRoom.html#doors)

12.  Click **Register** to register the room and add it to the Map!

You can come back to this page to update your room registrations at any time. Choose the room you want to update from the drop-down, make any desired changes, and click either **Update** to save the changes or **Delete** to delete the registration entirely.

## Hello World!

Use the arrow in the top right to go back to the game screen. Go Play!

* Use `/help` to see available commands (will vary by room).
* Use `/exits` to list the exits in the room.

Remember that shortname you set earlier? To visit your room:

    /teleport <nickname>


