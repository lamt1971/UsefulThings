#!/bin/bash

TRUE="Y"
FALSE="N"

JBOSS_HOME="D:/jboss-eap-6.4"
DEV_HOME="D:/NGSP/go-live-fixes"

CLEAN_WAR="clean"
CLEAN_EAR="clean"

SKIP_WAR=$FALSE
SKIP_EAR=$FALSE
SKIP_DEPLOY=$FALSE
SKIP_RESTART=$FALSE
REDEPLOY_ONLY=$FALSE

function about
{
    echo "$0"
    echo ""
    echo "Rebuild the web and deploy to JBOSS"
    echo ""
    echo "Options:"
    echo ""
    echo " -w       : Skip the war build"
    echo " -e       : Skip the ear build"
    echo " -d       : Skip the ear deployment"
    echo " -r       : Skip the JBOSS restart"
    echo " -c <opt> : skip the maven clean"
    echo "            w - skip clean for the war"
    echo "            e - skip clean for the ear"
    echo "            b - skip clean for both the war and ear"
    echo ""
    echo "Example:"
    echo ""
    echo " Rebuild the war then ear but don't deploy it"
    echo ""
    echo "  $0 -r"
    echo ""
    echo " Rebuild the war then ear without clean and deploy to JBOSS"
    echo ""
    echo "  $0 -c b"
    echo ""
    echo " Don't rebuild anything just deploy the pervious build to JBOSS"
    echo ""
    echo "  $0 -we"
    echo ""
    echo "Good luck"
    exit 0
}

while getopts "c:wedrh" opt
do
    case $opt in
        h)
	    about
	    ;;
        c)
	    case $OPTARG in
	        w)
		    echo "Will not clean the war"
		    CLEAN_WAR=""
		    ;;
	        e)
		    echo "Will not clean the ear"
		    CLEAN_EAR=""
		    ;;
		b)
		    echo "Will not clean the war or ear"
		    CLEAN_WAR=""
		    CLEAN_EAR=""
		    ;;
	    esac
	    ;;
        w)
	    echo "Skipping the war build"
	    SKIP_WAR=$TRUE
	    ;;
        e)
	    echo "Skipping the ear build"
	    SKIP_EAR=$TRUE
	    ;;
        d)
	    echo "Skipping the ear deployment"
	    SKIP_DEPLOY=$TRUE
	    ;;
        r)
	    echo "Skipping the JBOSS restart"
	    SKIP_RESTART=$TRUE
	    ;;
        R)
	    REDEPLOY_ONLY=$TRUE
	    ;;
    esac
done

echo "Building Web App"

if [ $SKIP_WAR = $FALSE ]
then
    echo "Building the war"
    cd ${DEV_HOME}/ecommerce-desktop-web-app
    mvn -DskipTests=true ${CLEAN_WAR} install
    if [ $? -ne 0 ]
    then
        echo "Failed to build the WAR"
        exit 1
    fi
    cd ..
fi

if [ $SKIP_EAR = $FALSE ]
then
    echo "Building the ear"
    cd ${DEV_HOME}/ecommerce-desktop-web-app-ear
    mvn -DskipTests=true ${CLEAN_EAR} install
    if [ $? -ne 0 ]
    then
        echo "Failed to build the EAR"
        exit 1
    fi
    cd ..
fi

if [ $SKIP_DEPLOY = $FALSE ]
then
    echo "Undeploying the ear file"
    ${JBOSS_HOME}/bin/jboss-cli.sh -c --command="undeploy ecommerce-desktop-web-app-ear-2.0.11-SNAPSHOT.ear --all-relevant-server-groups"
    echo "Deploying the ear file"
    cd ${DEV_HOME}/ecommerce-desktop-web-app-ear

    ${JBOSS_HOME}/bin/jboss-cli.sh -c --command="deploy target/ecommerce-desktop-web-app-ear-2.0.11-SNAPSHOT.ear --all-server-groups"

    echo "Deploying the static config"

    cd ${JBOSS_HOME}/welcome-content
    rm -rf `ls -1 | grep -v siteImages`
    unzip -q ${DEV_HOME}/ecommerce-desktop-web-app/target/ecommerce-desktop-web-app-2.0.12-SNAPSHOT-static-resources.zip
    #unzip -q ${DEV_HOME}/../temp/siteImages.zip
	
	
    if [ $SKIP_RESTART = $FALSE ]
    then
	# Try just stopping and re-starting the app
	#if [ $REDEPLOY_ONLY = $TRUE ]
	#then
	#    echo "Restarting the app"
        #    ${JBOSS_HOME}/bin/jboss-cli.sh -c --command="undeploy quotations-management-ear.ear --keep-content"
        #    ${JBOSS_HOME}/bin/jboss-cli.sh -c --command="deploy --name=quotations-management-ear.ear"
	#    exit
	#fi

        # Stop and re-start JBOSS
        ${JBOSS_HOME}/bin/jboss-cli.sh -c --command="server-group=rs-server-group:stop-servers"
        ${JBOSS_HOME}/bin/jboss-cli.sh -c --command="server-group=rs-server-group:start-servers"

	echo -e "Waiting for JBOSS to stop: \c"
	cnt=0
	while [ $cnt -eq 0 ]
	do
	    echo -e ".\c"
	    sleep 5
            cnt=`grep "stopping on" ${JBOSS_HOME}/domain/servers/appserver/logserver.log | wc -l`
        done
        echo -e " ${COL_RED}STOPPED${COL_RESET}"

        # Renove the logs
        rm -f ${JBOSS_HOME}/domain/servers/appserver/server.log

        # Restart JBOSS
        nohup ${JBOSS_HOME}/bin/domain.sh > /dev/null 2>&1 &

        # Wait for jboss to start
        cnt=0

        echo -e "Waiting for JBOSS to start: \c"
        while [ $cnt -eq 0 ]
	do
	    echo -e ".\c"
	    sleep 5
            cnt=`grep "JBoss EAP 6.4.0.GA (AS 7.5.0.Final-redhat-1) started in" ${JBOSS_HOME}/standalone/log/server.log | wc -l`
        done
        echo -e " ${COL_GREEN}STARTED${COL_RESET}"
    fi
fi

echo "Completed"
