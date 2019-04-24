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
        def file = new File("${env.WORKSPACE}/Vault-deployment/vault.tfvars")
        file.write """
        secret              =  "${secret}"
        namespace           =  "${namespace}"
        """
        }
      }
    }
    stage("Terraform init") {
      if ("${params.Service}" == "vaultDeployment") {
        dir("${env.WORKSPACE}/Vault-deployment") {
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
        }
      } else {
          dir("${env.WORKSPACE}/Vault-deployment") {
            echo "##### Terraform Plan (Check) the Changes ####"
            sh "terraform plan -var-file=vault.tfvars"
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