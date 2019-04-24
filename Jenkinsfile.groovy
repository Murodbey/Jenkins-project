#!/usr/bin/env groovy
package com.lib
import groovy.json.JsonSlurper

node('master') {
  properties([parameters([
    choice(choices: ['vaultDeployment', 'grafanaDeployment', 'jiraDeployment', 'nexusDeployment'], description: 'Please choose which service do you want to deploy', name: 'Service'),
    booleanParam(defaultValue: false, description: 'Apply All Changes', name: 'terraformApply'),
    booleanParam(defaultValue: false, description: 'Destroy All', name: 'terraformDestroy'),
    string(defaultValue: 'test', description: 'Please provide namespace for deployment', name: 'namespace', trim: true),  
    string(defaultValue: 'secret', description: 'Please provide a secret for deployment', name: 'secret', trim: true)
    ]
    )])
    stage('Checkout SCM') {
      git 'https://github.com/Murodbey/Terraform-project.git'
    } 
    stage('Generate Vars')   {
      if ("${params.Service}" == "vaultDeployment") {
        echo "##### Creating tfvars file ####"
        def file = new File("${env.WORKSPACE}/Vault-deployment/vault.tfvars")
        file.write """
        secret              =  "${secret}"
        namespace           =  "${namespace}"
        """
      }
    }
    stage("Terraform init") {
      if ("${params.Service}" == "vaultDeployment") {
        dir("${env.WORKSPACE}/Vault-deployment") {
          echo "##### Terraform initializing ####"
          sh "terraform init"
        }
      }
    }
    stage("Terraform Apply/Plan"){
      if ("${params.Service}" == "vaultDeployment") {
        if (!params.terraformDestroy) {
          if (params.terraformApply) {
            dir("${env.WORKSPACE}/Vault-deployment") {
              echo "##### Terraform Applying the Changes ####"
              sh "terraform apply --auto-approve -var-file=vault.tfvars"
          }
      } else {
          dir("${env.WORKSPACE}/Vault-deployment") {
            echo "##### Terraform Plan (Check) the Changes ####"
            sh "terraform plan -var-file=vault.tfvars"
          }
        }
      }
    } 
  } 
    stage('Terraform Destroy') {
      if ("${params.Service}" == "vaultDeployment") {
        if (!params.terraformApply) {
          if (params.terraformDestroy) {
            dir("${env.WORKSPACE}/Vault-deployment") {
              echo "##### Terraform Destroying ####"
              sh "terraform destroy --auto-approve -var-file=vault.tfvars"
            }
          }
        } 
      }
    }
       if (params.terraformDestroy) {
         if (params.terraformApply) {
           println("""
           Sorry you can not destroy and apply at the same time
           """)
    }
  }

    stage('Generate Vars')   {
      if ("${params.Service}" == "grafanaDeployment") {
        echo "##### Creating tfvars file ####"
        def file = new File("${env.WORKSPACE}/Grafana-deployment/grafana.tfvars")
        file.write """
        secret              =  "${secret}"
        namespace           =  "${namespace}"
        """
      }
    }
    stage("Terraform init") {
      if ("${params.Service}" == "grafanaDeployment") {
        dir("${env.WORKSPACE}/Grafana-deployment") {
          echo "##### Terraform initializing ####"
          sh "terraform init"
        }
      }
    }
    stage("Terraform Apply/Plan"){
      if ("${params.Service}" == "grafanaDeployment") {
        if (!params.terraformDestroy) {
          if (params.terraformApply) {
            dir("${env.WORKSPACE}/Grafana-deployment") {
              echo "##### Terraform Applying the Changes ####"
              sh "terraform apply --auto-approve -var-file=grafana.tfvars"
            }
        }
      } else {
          dir("${env.WORKSPACE}/Grafana-deployment") {
            echo "##### Terraform Plan (Check) the Changes ####"
            sh "terraform plan -var-file=grafana.tfvars"
          }
        }
      } 
    }
    stage('Terraform Destroy') {
      if ("${params.Service}" == "grafanaDeployment") {
        if (!params.terraformApply) {
          if (params.terraformDestroy) {
            dir("${env.WORKSPACE}/Grafana-deployment") {
              echo "##### Terraform Destroying ####"
              sh "terraform destroy --auto-approve -var-file=grafana.tfvars"
            }
          }
        } 
      }
    }
       if (params.terraformDestroy) {
         if (params.terraformApply) {
           println("""
           Sorry you can not destroy and apply at the same time
           """)
    }
  }
    
    stage('Generate Vars')   {
      if ("${params.Service}" == "nexusDeployment") {
        echo "##### Creating tfvars file ####"
        def file = new File("${env.WORKSPACE}/Nexus-deployment/nexus.tfvars")
        file.write """
        namespace           =  "${namespace}"
        """
      }
    }
    stage("Terraform init") {
      if ("${params.Service}" == "nexusDeployment") {
        dir("${env.WORKSPACE}/Nexus-deployment") {
          echo "##### Terraform initializing ####"
          sh "terraform init"
        }
      }
    }
    stage("Terraform Apply/Plan"){
      if ("${params.Service}" == "nexusDeployment") {
        if (!params.terraformDestroy) {
          if (params.terraformApply) {
            dir("${env.WORKSPACE}/Nexus-deployment") {
              echo "##### Terraform Applying the Changes ####"
              sh "terraform apply --auto-approve -var-file=nexus.tfvars"
            }
        }
      } else {
          dir("${env.WORKSPACE}/Nexus-deployment") {
            echo "##### Terraform Plan (Check) the Changes ####"
            sh "terraform plan -var-file=nexus.tfvars"
          }
        }
      } 
    }
    stage('Terraform Destroy') {
      if ("${params.Service}" == "nexusDeployment") {
        if (!params.terraformApply) {
          if (params.terraformDestroy) {
            dir("${env.WORKSPACE}/Nexus-deployment") {
              echo "##### Terraform Destroying ####"
              sh "terraform destroy --auto-approve -var-file=nexus.tfvars"
            }
          }
        } 
      }
    }
       if (params.terraformDestroy) {
         if (params.terraformApply) {
           println("""
           Sorry you can not destroy and apply at the same time
           """)
    }
  }
    
    stage('Generate Vars')   {
      if ("${params.Service}" == "jiraDeployment") {
        echo "##### Creating tfvars file ####"
        def file = new File("${env.WORKSPACE}/Jira-deployment/jira.tfvars")
        file.write """
        namespace           =  "${namespace}"
        """
      }
    }
    stage("Terraform init") {
      if ("${params.Service}" == "jiraDeployment") {
        dir("${env.WORKSPACE}/Jira-deployment") {
          echo "##### Terraform initializing ####"
          sh "terraform init"
        }
      }
    }
    stage("Terraform Apply/Plan"){
      if ("${params.Service}" == "jiraDeployment") {
        if (!params.terraformDestroy) {
          if (params.terraformApply) {
            dir("${env.WORKSPACE}/Jira-deployment") {
              echo "##### Terraform Applying the Changes ####"
              sh "terraform apply --auto-approve -var-file=jira.tfvars"
            }
        }
      } else {
          dir("${env.WORKSPACE}/Jira-deployment") {
            echo "##### Terraform Plan (Check) the Changes ####"
            sh "terraform plan -var-file=jira.tfvars"
          }
        }
      } 
    }
    stage('Terraform Destroy') {
      if ("${params.Service}" == "jiraDeployment") {
        if (!params.terraformApply) {
          if (params.terraformDestroy) {
            dir("${env.WORKSPACE}/Jira-deployment") {
              echo "##### Terraform Destroying ####"
              sh "terraform destroy --auto-approve -var-file=jira.tfvars"
            }
          }
        } 
      }
    }
       if (params.terraformDestroy) {
         if (params.terraformApply) {
           println("""
           Sorry you can not destroy and apply at the same time
           """)
    }
  }
}