def git_auth = "60259b37-7710-4d31-b925-b83c0136bf28"
def remote_url = "https://github.com/xue1587607907/yygh-back-end.git"
node {
    stage('pull code') {
//         checkout([$class: 'GitSCM', branches: [[name: '*/${branch}']], extensions: [[$class: 'CloneOption', noTags: false, timeout: 30]], userRemoteConfigs: [[credentialsId: "${git_auth}", url: "${remote_url}"]]])    
        
        checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [[$class: 'CloneOption', noTags: false, timeout: 30]], userRemoteConfigs: [[credentialsId: '60259b37-7710-4d31-b925-b83c0136bf28', url: 'https://github.com/xue1587607907/yygh-back-end.git']]])
        
      
        echo 'pull Successful....'
        
        }
}
