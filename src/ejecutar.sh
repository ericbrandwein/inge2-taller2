#!/usr/bin/env bash

JRE=/usr/lib/jvm/java-8-openjdk/jre/lib/rt.jar

mvn install
javac -target 1.8 -source 1.8 -g examples/A.java
java -jar \
  zero-analysis/target/zero-analysis-1.0-SNAPSHOT-jar-with-dependencies.jar \
  -cp .:./examples/:$JRE -f J A -v -print-tags -p jtp.DivisionByZeroAnalysis on
cat sootOutput/A.jimple

