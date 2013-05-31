#!/bin/sh

cd $(dirname $0)

java -jar target/mse-viewer-1.0-SNAPSHOT-war-exec.jar $*

# options:  -Dbigraph.archive.cleanup=false -Dbigraph.archive.dir=/home/clem/my/archive/dir/name

