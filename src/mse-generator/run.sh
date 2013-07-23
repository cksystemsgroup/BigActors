#!/bin/sh

cd $(dirname $0)

#java -jar target/mse-generator-1.0-SNAPSHOT.jar at.uni_salzburg.cs.ros.GeneratorNode "__master:=http://localhost:11311/"
java -jar target/mse-generator-1.0-SNAPSHOT.jar at.uni_salzburg.cs.ros.GeneratorNode


