#!/bin/bash

sbt "~run -Dlogger.application=INFO -Drun.mode=Dev -Dhttp.port=11133 -Dapplication.router=testOnlyDoNotUseInAppConf.Routes $*"