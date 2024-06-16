# Intro to JLink

This repo provides an introduction to using some of the key functionality of `jlink` to create custom runtime images for running a Java application.

Be sure to check out the YouTube video for additional details: https://youtu.be/mJKlxqQQeyI

The repo is divided into five distinct parts;  

## Step 1 Hello World with JLink
We start by using the most basic functionality jlink, by creating a runtime image for a "Hello World!" program. [Link](step-1-hello-world-with-jlink)

## Step 2 Using JDeps
Step 2 covers how to use JDeps to find the dependent modules for an application, and build a runtime image with those modules. [Link](step-2-using-jdeps)

## Step 3 Adding Non-JDK Modules to an Image
In this step we look at how to add non-JDK modules, in this `org.apache.commons.lang3` to a runtime image. [Link](step-3-adding-non-jdk-modules)

## Step 4 Adding Explicit Modules to an Image
During this step we walk-through the process of creating our own explicit module and adding it to a runtime image. [Link](step-4-adding-explicit-modules) 

## Step 5 Creating a Launcher
We create a launcher for our runtime image, which can be used to streamline the process of running an application. [Link](step-5-creating-a-launcher)

## Step 6 Additional Customization Options
We look at a couple of the customization options; generating a CDS archive, and compressing the runtime image, `jlink` provides. [Link](step-6-additional-customization)

