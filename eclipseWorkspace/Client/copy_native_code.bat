
rmdir published /s /q
rmdir native_code /s /q

mkdir published
mkdir published\native_code

copy ..\..\visualStudioWorkspace\bin\Debug\expat.dll published\native_code\expat.dll
copy ..\..\visualStudioWorkspace\bin\Debug\FMUwrapper.dll published\native_code\FMUwrapper.dll

xcopy /e /v /i ..\..\assets\FMUs\LearnGB_0v4_02_VAVReheat_ClosedLoop_edit2 published\native_code\fmu

xcopy /e /v /i published\native_code native_code