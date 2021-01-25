#!/bin/sh
SHELL_FOLDER=$(cd "$(dirname "$0")";pwd)
cd $SHELL_FOLDER
sh run.sh stop com.wyyt.databus.app.DatabusApplication