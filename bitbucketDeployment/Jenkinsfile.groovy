#!/usr/bin/env groovy
package com.lib
import groovy.json.JsonSlurper

node('master') {
  properties([parameters([ 
    booleanParam(defaultValue: false, description: 'Apply All Changes', name: 'terraformApply'), 
    booleanParam(defaultValue: false, description: 'Destroy All', name: 'terraformDestroy'), 
    string(defaultValue: 'test', description: 'Please provide namespace for bitbucket-deployment', name: 'namespace', trim: true)
    ]
    )])
    stage('Checkout SCM') {
      git 'https://github.com/Murodbey/Terraform-project.git'
    } 
    stage('Generate Vars') {
        def file = new File("${WORKSPACE}/Bitbucket-deployment/bitbucket.tfvars")
        file.write """
        namespace             =  "${namespace}"
        """
      }
    stage("Terraform init") {
      dir("${workspace}/Bitbucket-deployment/") {
        sh "terraform init"
      }
    }
    stage("Terraform Apply/Plan"){
      if (!params.terraformDestroy) {
        if (params.terraformApply) {
          dir("${workspace}/Bitbucket-deployment/") {
            echo "##### Terraform Applying the Changes ####"
            sh "terraform apply --auto-approve -var-file=bitbucket.tfvars"
        }
      } else {
          dir("${WORKSPACE}/Bitbucket-deployment") {
            echo "##### Terraform Plan (Check) the Changes ####"
            sh "terraform plan -var-file=bitbucket.tfvars"
          }
        }
      } 
    }
    stage('Terraform Destroy') {
      if (!params.terraformApply) {
        if (params.terraformDestroy) {
          dir("${WORKSPACE}/Bitbucket-deployment") {
            echo "##### Terraform Destroying ####"
            sh "terraform destroy --auto-approve -var-file=bitbucket.tfvars"
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
    stage("Sending slack notification") {
      slackSend baseUrl: 'https://fuchicorp.slack.com/services/hooks/jenkins-ci/', 
      channel: 'test-message', 
      color: '#00FF00', 
      message: 'The multi-srv job is build successful', 
      tokenCredentialId: 'slack-token' 
    }
 }