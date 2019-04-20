    
node {
  properties([parameters([string(defaultValue: 'plan', description: 'Please provide what action you want? (plan,apply,destroy)', name: 'Terraform_plan_apply_destroy', trim: true), string(defaultValue: 'default_token_add_here', description: 'Please provide a token for vault', name: 'Provide token', trim: true)])])
      stage("Terraform init"){
        sh "terraform init"
    }
    stage("Terraform Plan/Apply/Destroy"){
        steps {
            script {
      if (params.terraformPlan) {
        sh 'terraform plan --auto-approve'
      } else {
        sh 'terraform apply --auto-approve'
        }
    //    else {
    //     sh 'terraform destroy --auto-approve'
    //     }
      }
    }
  }
}