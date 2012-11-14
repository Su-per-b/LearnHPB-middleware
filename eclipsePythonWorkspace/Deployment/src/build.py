#!/usr/bin/env python

import argparse
import json
import os
import os.path
import shutil
import sys
import tempfile
import subprocess

import glob, os, shutil


#VISUAL_STUDIO_BIN_PATH = "E:\\SRI\\straylight_repo\\visualStudioWorkspace\\bin\\Debug"

VISUAL_STUDIO_PROJECT_PATH = r'E:\SRI\straylight_repo\visualStudioWorkspace'
VISUAL_STUDIO_BIN_PATH = os.path.join (VISUAL_STUDIO_PROJECT_PATH, r'bin\Debug')

MSBUILD = r'C:\Windows\Microsoft.NET\Framework\v4.0.30319\MSBuild.exe'

JAVA_CLIENT_PROJECT_PATH = r'E:\SRI\straylight_repo\eclipseWorkspaceGUI\StrayLight'
JAVA_CLIENT_NATIVE_CODE_PATH  = os.path.join (JAVA_CLIENT_PROJECT_PATH, r'Client\native_code')

JAVA_CLIENT_TARGET =  os.path.join (JAVA_CLIENT_PROJECT_PATH, r'Client\target')
JAVA_CLIENT_TARGET_NATIVE_CODE =  os.path.join (JAVA_CLIENT_TARGET, r'native_code')

FMU_FOLDER_SRC =  r'E:\SRI\straylight_repo\assets\FMUs\LearnGB_0v4_02_VAVReheat_ClosedLoop_edit1'

def main(argv=None):

    cleanTarget()
    buildNativeCode()
    buildJavaClient()
    copyNativeCode()
    copyJavaFiles()
    
def cleanTarget():
    
    if ( os.path.isdir(JAVA_CLIENT_TARGET)):
        shutil.rmtree(JAVA_CLIENT_TARGET)
    
def copyJavaFiles():
    src = os.path.join (JAVA_CLIENT_PROJECT_PATH, r'Client\run.bat')
    dest = os.path.join (JAVA_CLIENT_PROJECT_PATH, r'Client\target')
    shutil.copy2(src, dest)
    
 
def buildJavaClient():
    cmd =  ['mvn', 'clean', 'install'] 
    rc  = subprocess.call(cmd, shell=True,  cwd=JAVA_CLIENT_PROJECT_PATH)
    print (rc)
    
    
def buildNativeCode():
    
    cmd =  [MSBUILD, 'straylight.sln', r'/t:clean', r'/p:Configuration=Debug'] 
    cmd =  [MSBUILD, 'straylight.sln', r'/p:Configuration=Debug'] 
    rc  = subprocess.call(cmd, shell=True,  cwd=VISUAL_STUDIO_PROJECT_PATH)
    
    
    
def copyNativeCode():
    
    os.makedirs(JAVA_CLIENT_TARGET_NATIVE_CODE)
    
    dest = os.path.join (JAVA_CLIENT_TARGET_NATIVE_CODE, r'bin')
    os.makedirs(dest)
    
    dest = os.path.join (JAVA_CLIENT_TARGET_NATIVE_CODE, r'fmu')
    shutil.copytree(FMU_FOLDER_SRC, dest)
    
    source = os.path.join(VISUAL_STUDIO_BIN_PATH, "FMUwrapper.dll")
    dest = os.path.join (JAVA_CLIENT_TARGET_NATIVE_CODE, r'bin')
    result = shutil.copy2(source, dest)
    print(result)
    
    source = os.path.join(VISUAL_STUDIO_BIN_PATH, "expat.dll")
    dest = os.path.join (JAVA_CLIENT_TARGET_NATIVE_CODE, r'bin')
    result = shutil.copy2(source, dest)
    print(result)
    

            
def copyAll(source, dest):
    for file in source:
        if os.path.isfile(file):
            shutil.copy2(file, dest)
            
            

def runCommand( cmd ):
    rc  = subprocess.call(cmd, shell=True)
    print (rc)
    

if __name__ == "__main__":
  main()
