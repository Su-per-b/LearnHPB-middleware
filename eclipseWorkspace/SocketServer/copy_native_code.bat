rmdir published /s /q
mkdir published

rmdir ..\FMUwrapper\native_code /s /q
mkdir ..\FMUwrapper\native_code

copy /y ..\..\visualStudioWorkspace\bin\Debug\expat.dll ..\FMUwrapper\native_code\expat.dll
copy /y ..\..\visualStudioWorkspace\bin\Debug\FMUwrapper.dll ..\FMUwrapper\native_code\FMUwrapper.dll

xcopy /y /e /v /i ..\..\assets\FMUs\LearnGB_0v4_02_VAVReheat_ClosedLoop_edit2 ..\FMUwrapper\native_code\fmu

xcopy /y /e /v /i ..\FMUwrapper\native_code published\native_code
pause





