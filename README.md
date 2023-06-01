# Car-El Williams | rock-scissor-paper

Rock paper scissor game with a spring boot backend and an angular frontend

# Requirements:

Node and Gradle must be installed (Recommend: install both via homebrew).
Verify that both Gradle and Node are installed by opening a terminal and run the commands independantly:
`gradle --version`
`node --version`

# Instructions - Initialize Spring Backend

Clone the project into a folder, then open up a terminal and navigate to the project e.g `your-path/rock-paper-scissor/backend`
In the "backend" directory run the command `gradle bootRun`this will initialize the spring boot backend on localhost:8000

# Start the Angular application

After initializing the backend navigate back to the frontend folder e.g `your-path/rock-paper-scissor/frontend`.
In this folder first run the command:`npm i`. After the node_modules folder has been added run the following command: `npx ng serve`

Now your all set! Open a webbrowser and in the address field type: `localhost:4200` and try to beat the computer!
