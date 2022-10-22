node {
   def mvnHome
   stage('Preparation') { // for display purposes
      // Get some code from a GitHub repository
      git branch: 'main', url: 'https://github.com/optima-growth-stock/organization-service.git'
      // Get the Maven tool.
      // ** NOTE: This 'M3' Maven tool must be configured
      // **       in the global configuration.
      mvnHome = tool 'M2_HOME'
   }
   stage('Build') {
      // Run the maven build
      withEnv(["MVN_HOME=$mvnHome"]) {
         if (isUnix()) {
            sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean package"
         } else {
            bat(/"%MVN_HOME%\bin\mvn" -Dmaven.test.failure.ignore clean package/)
         }
      }
   }
   stage('Build image') {
       sh "'${mvnHome}/bin/mvn' -Ddocker.image.prefix=072085842419.dkr.ecr.eu-west-1.amazonaws.com/ostock -Ddocker.image.version=latest dockerfile:build"
   }
   stage('Push image') {
       docker.withRegistry('https://072085842419.dkr.ecr.eu-west-1.amazonaws.com', 'ecr:eu-west-1:ecr-user') {
           sh "docker push 072085842419.dkr.ecr.eu-west-1.amazonaws.com/ostock/organization-service:latest"
       }
   }
   stage('Kubernetes deploy') {
       withCredentials([sshUserPrivateKey(credentialsId: 'kubessh', keyFileVariable: 'keyfile')]) {
           sh "ssh -i ${keyfile} ec2-user@ec2-54-216-119-92.eu-west-1.compute.amazonaws.com kubectl apply -f organizationservice-deployment.yaml"
       }
   }
}
