node { 
    properties([
      // Below line sets "Discards Builds more than 5"
      buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5')), 
      // Below line triggers the job every minute
      pipelineTriggers([pollSCM('* * * * *')])])




  stage("Pull Repo"){ 

    git "https://github.com/farrukh90/cool_website.git"

} 

  stage("Install prerequisites"){ 

    sh """
stage("Install Prerequisites"){
		sh """
		ssh centos@jenkins_worker1.mr-robot95.com                 sudo yum install httpd -y

		"""
}

  stage("Copy artifacts"){ 

    sh """
    scp -r *  centos@jenkins_worker1.mr-robot95.com:/tmp
		ssh centos@jenkins_worker1.mr-robot95.com                 sudo cp -r /tmp/index.html /var/www/html/
		ssh centos@jenkins_worker1.mr-robot95.com                 sudo cp -r /tmp/style.css /var/www/html/
		ssh centos@jenkins_worker1.mr-robot95.com				         sudo chown centos:centos /var/www/html/
		ssh centos@jenkins_worker1.mr-robot95.com				         sudo chmod 777 /var/www/html/*
		
    """

} 

  stage("Restart web server"){ 

    sh """
    ssh centos@jenkins_worker1.mr-robot95.com                 sudo systemctl restart httpd 
    """

} 

  stage("Slack"){ 

    slackSend color: '#BADA55', message: 'Hello, World!'

} 

} 