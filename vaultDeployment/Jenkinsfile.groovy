    
node('master') {
  properties([parameters([string(defaultValue: 'plan', description: 'Please provide what action you want? (plan,apply,destroy)', name: 'terraformPlan', trim: true), string(defaultValue: 'default_token_add_here', description: 'Please provide a token for vault', name: 'Provide token', trim: true)])])
    stage("Pull repo"){
      steps{
          git "https://github.com/Murodbey/Jenkins-project.git"
          }
        }
    stage("Terraform init") {
      ws("${workspace}/deployment/vault/") {
        sh 'ls'
        sh 'pwd'
        sh "terraform init"
      }
    stage("Terraform Plan/Apply/Destroy"){
      if (params.terraformPlan.toLowerCase() == 'plan') {
        ws("${workspace}/deployment/vault/") {
          sh "terraform plan --auto-approve"
        }
      } 
      if (params.terraformPlan.toLowerCase() == 'apply') {
          ws("${workspace}/deployment/vault/") {
            sh "terraform apply --auto-approve"
          }
        } 

      if (params.terraformPlan.toLowerCase() == 'destroy') {
         ws("${workspace}/deployment/vault/") {
            sh "terraform destroy --auto-approve"
          }
      }
    }
  }
}