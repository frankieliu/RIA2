#!/bin/bash

JAVA=/home/users/frankliu/.jdks/adopt-openjdk-14.0.2/bin/java
IDEA=-javaagent:/home/users/frankliu/Apps/idea-IC-201.8538.31/lib/idea_rt.jar=44520:/home/users/frankliu/Apps/idea-IC-201.8538.31/bin
ENCODING=-Dfile.encoding=UTF-8
CLASSPATH=-classpath /home/users/frankliu/group/RIA2/target/classes:/home/users/frankliu/.m2/repository/net/imagej/ij/1.52h/ij-1.52h.jar:/home/users/frankliu/.m2/repository/com/google/code/gson/gson/2.8.5/gson-2.8.5.jar:/home/users/frankliu/.m2/repository/com/github/pcj/google-options/1.0.0/google-options-1.0.0.jar:/home/users/frankliu/.m2/repository/com/google/code/findbugs/jsr305/3.0.1/jsr305-3.0.1.jar:/home/users/frankliu/.m2/repository/com/google/guava/guava/19.0/guava-19.0.jar
CLASS=com.mycompany.imagej.Batch
ARGS=-i src/test/resources/Images/root_1_lg.png -n 10 -d 1,2,3

# JAVA=/usr/lib/jvm/jdk-12.0.1/bin/java
# IDEA="-Didea.launcher.port=42173 -Didea.launcher.bin.path=/home/adam/Apps/idea-IU-182.4505.22/bin"
# ENCODING="-Dfile.encoding=UTF-8"
# INTELLIJ="com.intellij.rt.execution.application.AppMainV2"
# CLASSPATH="-classpath /home/adam/github/RIA2/target/classes:/home/adam/.m2/repository/net/imagej/ij/1.52h/ij-1.52h.jar:/home/adam/.m2/repository/com/google/code/gson/gson/2.8.5/gson-2.8.5.jar:/home/adam/.m2/repository/com/github/pcj/google-options/1.0.0/google-options-1.0.0.jar:/home/adam/.m2/repository/com/google/code/findbugs/jsr305/3.0.1/jsr305-3.0.1.jar:/home/adam/.m2/repository/com/google/guava/guava/19.0/guava-19.0.jar:/home/adam/Apps/idea-IU-182.4505.22/lib/idea_rt.jar"
# CLASS="com.mycompany.imagej.Batch"

# $JAVA $IDEA $ENCODING $CLASSPATH $INTELLLIJ $CLASS $@
$JAVA $ENCODING $CLASSPATH $CLASS $@
