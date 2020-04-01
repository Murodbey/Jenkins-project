node { 
	properties([
		// Below line sets "Discard Builds more than 5"
		buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5')), 
		
		// Below line triggers this job every minute
		pipelineTriggers([pollSCM('* * * * *')]),
		parameters([choice(choices: [
			'dev1.mr-robot95.com', 
			'qa1.mr-robot95.com', 
			'stage1.mr-robot95.com', 
			'prod1.mr-robot95.com'], 
			description: 'Please choose an environment', 
			name: 'ENVIR')]), 
		])



    // Pulls a repo from developer
  stage("Pull Repo"){ 

    git "https://github.com/farrukh90/cool_website.git"

} 
   //Installs web server on different environment
  stage("Install Prerequisites"){
		sh """
		ssh centos@jenkins_worker1.mr-robot95.com                 sudo yum install httpd -y
		"""
}

   //Copies over developers files to different environment
  stage("Copy artifacts"){ 

    sh """
    scp -r *  centos@jenkins_worker1.mr-robot95.com:/tmp
		ssh centos@jenkins_worker1.mr-robot95.com                 sudo cp -r /tmp/index.html /var/www/html/
		ssh centos@jenkins_worker1.mr-robot95.com                 sudo cp -r /tmp/style.css /var/www/html/
		ssh centos@jenkins_worker1.mr-robot95.com				         sudo chown centos:centos /var/www/html/
		ssh centos@jenkins_worker1.mr-robot95.com				         sudo chmod 777 /var/www/html/*
		
    """

} 
  //Restarts web server
  stage("Restart web server"){ 

    sh """
    ssh centos@jenkins_worker1.mr-robot95.com                 sudo systemctl restart httpd 
    """

} 
   //Sends a message to slack
  stage("Slack"){ 

    slackSend color: '#BADA55', message: 'Hello, World!'

} 

} 