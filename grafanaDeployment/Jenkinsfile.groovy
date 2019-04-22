#!/usr/bin/env groovy
package com.lib
import groovy.json.JsonSlurper

node('master') {
  properties([parameters([
    booleanParam(defaultValue: false, description: 'Plan before apply', name: 'terraformPlan'), 
    booleanParam(defaultValue: false, description: 'Apply All Changes', name: 'terraformApply'), 
    booleanParam(defaultValue: false, description: 'Destroy All', name: 'terraformDestroy'), 
    string(defaultValue: 'default_password', description: 'Please provide password for grafana', name: 'password', trim: true),
    string(defaultValue: 'test', description: 'Please provide namespace for grafana-deployment', name: 'namespace', trim: true)
    ]
    )])
    checkout scm
    stage('Generate Vars') {
        def file = new File("${WORKSPACE}/grafanaDeployment/grafana.tfvars")
        file.write """
        password              =  "${password}"
        namespace             =  "${namespace}"
        """
      }
    stage("Terraform init") {
      dir("${workspace}/grafanaDeployment/") {
        sh "terraform init"
      }
    }
    stage("Terraform Plan/Apply/Destroy"){
      if (params.terraformPlan) {
        dir("${workspace}/grafanaDeployment/") {
        echo "##### Terraform Plan (Check) the Changes ####"
        sh "terraform plan -var-file=grafana.tfvars"
        }
      } else if (params.terraformApply) {
               dir("${workspace}/grafanaDeployment/") {
               echo "##### Terraform Applying the Changes ####"
               sh "terraform apply --auto-approve -var-file=grafana.tfvars"
        }
      } else if (params.terraformDestroy) {
               dir("${workspace}/grafanaDeployment/") {
               echo "##### Terraform Destroying grafana deployment ####"
               sh "terraform destroy --auto-approve -var-file=grafana.tfvars"
        }
      } else {
        println("""
              Sorry I don`t understand ${params.terraformPlan}!!!
              Please provide correct option (plan/apply/destroy)
              """)
      }
    }
}