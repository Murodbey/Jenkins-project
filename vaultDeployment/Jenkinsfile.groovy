    
node('master') {
  properties([parameters([string(defaultValue: 'plan', description: 'Please provide what action you want? (plan,apply,destroy)', name: 'terraformPlan', trim: true), string(defaultValue: 'default_token_add_here', description: 'Please provide a token for vault', name: 'Provide token', trim: true)])])
    stage("Terraform init") {
      dir("${workspace}") {
        sh 'ls'
        sh 'pwd'
        sh "terraform init"
      }
    stage("Terraform Plan/Apply/Destroy"){
      if (params.terraformPlan.toLowerCase() == 'plan') {
        dir("${workspace}") {
          sh "terraform plan --auto-approve"
        }
      } 
      if (params.terraformPlan.toLowerCase() == 'apply') {
          dir("${workspace}") {
            sh "terraform apply --auto-approve"
          }
        } 

      if (params.terraformPlan.toLowerCase() == 'destroy') {
         dir("${workspace}") {
            sh "terraform destroy --auto-approve"
          }
      }
    }
  }
}