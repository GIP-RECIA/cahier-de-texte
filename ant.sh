#!/bin/sh

export ANT_HOME=./apache-ant-1.7.1

chmod 755 $ANT_HOME/bin/ant
$ANT_HOME/bin/ant $@