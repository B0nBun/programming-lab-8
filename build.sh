#!/bin/sh
# Can't specify multiple profiles in one command for some bizzare and unknown reason
mvn -P client package && mvn -P server package