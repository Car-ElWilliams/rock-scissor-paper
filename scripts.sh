
#!/bin/bash

function startBackend {
echo \"Starting backend server\"
cd backend && gradle bootRun
}

function startFrontend {
 cd frontend
    if [ -d "node_modules" ]
    then 
      echo "Starting Angular on localhost:4200"
      npx ng serve
    else
      echo "Node moduels is not installed, installing..."
      npm i --force
      echo "Starting Angular on localhost:4200"
      npx ng serve
      exit 1
    fi
}