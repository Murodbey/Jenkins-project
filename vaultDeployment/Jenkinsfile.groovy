    
node {
  properties([parameters([string(defaultValue: 'plan', description: 'Please provide what action you want? (plan,apply,destroy)', name: 'terraformPlan', trim: true), string(defaultValue: 'default_token_add_here', description: 'Please provide a token for vault', name: 'Provide token', trim: true)])])
  // stages{
  //   stage("Git Pull"){
  //           steps{
  //               git 'https://github.com/Murodbey/Jenkins-project.git'
  //           }
  //       }
    stage("Terraform init") {
        sh 'ls'
        sh 'pwd'
        sh "terraform init"
      }
    stage("Terraform Plan/Apply/Destroy"){
      if (params.terraformPlan.toLowerCase() == 'plan') {
        dir("${workspace}/vaultDeployment") {
          sh "terraform plan --auto-approve"
        }
      } 
      if (params.terraformPlan.toLowerCase() == 'apply') {
          dir("${workspace}/vaultDeployment") {
            sh "terraform apply --auto-approve"
          }
        } 

      if (params.terraformPlan.toLowerCase() == 'destroy') {
         dir("${workspace}/vaultDeployment") {
            sh "terraform destroy --auto-approve"
          }
      }
    }
}