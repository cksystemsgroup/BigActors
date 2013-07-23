#!/bin/sh

cd $(dirname $0)

# java -XX:MaxPermSize=128m -Xmx256m -Xss256k -jar target/mse-viewer-1.0-SNAPSHOT-war-exec.jar $*
java -jar target/mse-viewer-1.0-SNAPSHOT-war-exec.jar $*

# options:  -Dbigraph.archive.cleanup=false -Dbigraph.archive.dir=/home/clem/my/archive/dir/name

