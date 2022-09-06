//git凭证ID
def git_auth = "60259b37-7710-4d31-b925-b83c0136bf28"
//git的url地址
def git_url = "https://github.com/xue1587607907/yygh-back-end.git"
//镜像的版本号
def tag = "latest"
//Harbor的url地址
def harbor_url = "192.168.66.102:85"
//镜像库项目名称
def harbor_project = "yygh"
//Harbor的登录凭证ID
def harbor_auth = "906109fc-6885-4b34-a493-db0062fce31d"

node {
    //获取当前选择的项目名称
    def selectedProjectNames = "${project_name}".split(",")
    //获取当前选择的服务器名称
    def selectedServers = "${publish_server}".split(",")

    stage('pull code') {
        checkout([$class: 'GitSCM', branches: [[name: "*/${branch}"]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: "${git_auth}", url: "${git_url}"]]])
        echo "pull code Successful....."
    }


    stage('编译，打包微服务工程，上传镜像') {
        for (int i = 0; selectedProjectNames.length > i; i++) {
            //service-gateway@8210
            def projectInfo = selectedProjectNames[i];
            //当前遍历的项目名称
            def currentProjectName = "${projectInfo}".split("@")[0]
            //当前遍历的项目端口
            def currentProjectPort = "${projectInfo}".split("@")[1]

            // 打包工程
            sh "mvn -f ${currentProjectName} clean package -Dmaven.test.skip=true"

            if ($ { currentProjectName }.equals("service-gateway") || $ { currentProjectName }.equals("hospital-manage")) {
                sh "mvn dockerfile:build"
            } else {
                sh "cd service"
                sh "mvn dockerfile:build"
            }
//            sh "mvn -f ${currentProjectName} clean package dockerfile:build"

            //定义镜像名称
            def imageName = "${currentProjectName}:${tag}"

            //对镜像打上标签
            sh "docker tag ${imageName} ${harbor_url}/${harbor_project}/${imageName}"

            //把镜像推送到Harbor
            withCredentials([usernamePassword(credentialsId: "${harbor_auth}", passwordVariable: 'password', usernameVariable: 'username')]) {

                //登录到Harbor
                sh "docker login -u ${username} -p ${password} ${harbor_url}"

                //镜像上传
                sh "docker push ${harbor_url}/${harbor_project}/${imageName}"

                sh "echo 镜像上传成功"
            }

            //部署应用
            sshPublisher(publishers: [sshPublisherDesc(configName: "${currentServerName}", transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: "/opt/jenkins_shell/deploy.sh $harbor_url $harbor_project $currentProjectName $tag $currentProjectPort ", execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: '')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])


            //遍历所有服务器，分别部署
//            for (int j = 0; j < selectedServers.length; j++) {
//                //获取当前遍历的服务器名称
//                def currentServerName = selectedServers[j]
//
//                //加上的参数格式：--spring.profiles.active=eureka-server1/eureka-server2
//                def activeProfile = "--spring.profiles.active="
//
//                //根据不同的服务名称来读取不同的Eureka配置信息
//                if (currentServerName == "master_server") {
//                    activeProfile = activeProfile + "eureka-server1"
//                } else if (currentServerName == "slave_server") {
//                    activeProfile = activeProfile + "eureka-server2"
//                }
//
//                //部署应用
//                sshPublisher(publishers: [sshPublisherDesc(configName: "${currentServerName}", transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: "/opt/jenkins_shell/deployCluster.sh $harbor_url $harbor_project $currentProjectName $tag $currentProjectPort $activeProfile", execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: '')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
//
//            }

        }
    }


//    stage('代码审查') {
//        for(int i=0;i<selectedProjectNames.length;i++){
//            //tensquare_eureka_server@10086
//            def projectInfo = selectedProjectNames[i];
//            //当前遍历的项目名称
//            def currentProjectName = "${projectInfo}".split("@")[0]
//            //当前遍历的项目端口
//            def currentProjectPort = "${projectInfo}".split("@")[1]
//
//            //定义当前Jenkins的SonarQubeScanner工具
//            def scannerHome = tool 'sonar-scanner'
//            //引用当前JenkinsSonarQube环境
//            withSonarQubeEnv('sonarqube') {
//                sh """
//                         cd ${currentProjectName}
//                         ${scannerHome}/bin/sonar-scanner
//                 """
//            }
//        }
//
//
//    }
}