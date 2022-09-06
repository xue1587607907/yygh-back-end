def git_auth = "68f2087f-a034-4d39-a9ff-1f776dd3dfa8"
def remote_uri = "https://github.com/xue1587607907/yygh-back-end.git"
node {
    stage('pull code') {
        checkout([$class: 'GitSCM', branches: [[name: '*/${branch}']], extensions: [], userRemoteConfigs: [[credentialsId: '${git_auth}', url: 'remote_url']]])    }
        echo 'pull Successful....'
}
