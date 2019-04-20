    
node('master') {
  properties([parameters([
    string(defaultValue: 'plan', description: 'Please provide what action you want? (plan,apply,destroy)', name: 'terraformPlan', trim: true), 
    string(defaultValue: 'default_token_add_here', description: 'Please provide a token for vault', name: 'Provide token', trim: true)
    ]
    )])
    checkout scm
    stage("Terraform init") {
      dir("${workspace}/vaultDeployment/") {
        sh 'ls'
        sh 'pwd'
        sh "terraform init"
      }
    stage("Terraform Plan/Apply/Destroy"){
      if (params.terraformPlan.toLowerCase() == 'plan') {
        dir("${workspace}/vaultDeployment/") {
          sh "terraform plan"
          sh "${token}"
        }
      } 
      if (params.terraformPlan.toLowerCase() == 'apply') {
          dir("${workspace}/vaultDeployment/") {
            sh "terraform apply --auto-approve"
          }
        } 

      if (params.terraformPlan.toLowerCase() == 'destroy') {
         dir("${workspace}/vaultDeployment/") {
            sh "terraform destroy --auto-approve"
          }
      }
    }
  }
}