# Car-El Williams | rock-scissor-paper

Rock paper scissor game with a spring boot backend and an angular frontend

# Requirements:

Node and Gradle must be installed (Recommend: install both via homebrew).
You can verify that both Gradle and Node are installed by opening a terminal and running the commands independently:
`gradle --version`
`node --version`

# Instructions - Clone and Start Project

Clone the project into a folder, then open up a terminal and navigate to the project e.g `your-path/rock-paper-scissor/backend`

If you use a bash terminal you can quickly start the project
from this directory by running the scripts `npm run backend` and `npm run frontend`. Keep in mind that each script needs to be run in it's own terminal. If this fails see the manual steps below.

# Instructions - Initialize Spring Backend Manually

Navigate to the the "backend" directory and run the command `gradle bootRun`. This will initialize the spring boot backend on localhost:8000

# Start the Angular Application Manually

After initializing the backend navigate back to the frontend folder e.g `your-path/rock-paper-scissor/frontend`.
In this folder first run the command:`npm i --force` (I have not yet fixed some dependency conflicts hence the force flag). After the node_modules folder has been added run the following command: `npx ng serve`

Now your all set! Open a web browser and in the address field type: `localhost:4200` and try to beat the computer!
