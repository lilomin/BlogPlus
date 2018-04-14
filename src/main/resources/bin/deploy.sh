#!/bin/sh
deployPath="/root/usr/deploy/repo"
targetPath=$deployPath/target
appName="BlogPlus"

function log() {
        logPath=$deployPath/deploy.`date +%Y-%m-%d`.log
        if [ ! -d $deployPath  ]; then
                mkdir -p $deployPath
        fi
        if [ ! -d $logPath ];then
                touch $logPath
        fi
        echo [`date +%Y-%m-%d\ %H:%M:%S`] $* >> $logPath;
}

#执行git拉取源码
log 'pull src from github'
cd $deployPath
gitLog=`git pull origin 2>&1`
if [ $? -ne 0  ];
then
    log $gitLog
    exit $?
fi
echo "$gitLog" | while read line
do
    log $line
done

#执行maven进行编译
log 'maven is building'
mavenLog=`mvn clean package -Dmaven.test.skip=true -Pprod  2>&1`
if [ $? -ne 0  ];
then
        log $mavenLog
        exit $?
fi
echo "$mavenLog" | while read line
do
        log $line
done

log 'kill old process'
pid=` ps aux | grep BlogPlus | awk -F ' ' '{print $2}'`
if [ -n "${pid}" ]; then
        kill -9 $pid
fi
log "killed process is "${pid}

log 'starting thr process'
cd $targetPath
mv -f $appName.jar $deployPath
cd $deployPath
chmod +x $appName.jar
nohup java -jar $appName.jar >/dev/null 2>&1 &
sleep 5
pid=`ps aux | grep BlogPlus | awk -F ' ' '{print $2}'`
log "process started "${pid}