#include "stdafx.h"
#include "utils.h"




void getModuleFolderPath(TCHAR * szDir) {

	//allocate string buffers using character independant data type.
	_TCHAR exePath[MAX_PATH] =  _T("");
	GetModuleFileName(NULL, exePath, MAX_PATH);

	char    ch = '\\';   
	_TCHAR * slashAndFileName = _tcsrchr(exePath, ch);
	size_t theSize = slashAndFileName - exePath;

	// Extract directory
	_tcsnccpy(szDir, exePath, theSize);
	szDir[theSize] = '\0';

}


void getUnzipFolder(TCHAR * szDir) {

	getModuleFolderPath(szDir);
	_tcscat(szDir, _T("\\FMUs\\DoubleInputDoubleOutput.fmu_unzipped"));
	szDir[lstrlen(szDir)] = '\0';

}




void getUnzipFolder2(TCHAR * szDir) {

	getModuleFolderPath(szDir);

	_tcscat(szDir, _T("\\FMUs\\LearnGB_VAVReheat_ClosedLoop"));
	szDir[lstrlen(szDir)] = '\0';

}




char * getResultItemAsString(FMUwrapper * fmuWrapper) {
	Straylight::ResultItem * ri;

	fmuWrapper->doOneStep();
	ri = fmuWrapper->getResultItem();

	return ri->getString();
}



char * wstrdup(_TCHAR * wSrc)
{
    int l_idx=0;
    int l_len = wstrlen(wSrc);
    char * l_nstr = (char*)malloc(l_len);
    if (l_nstr) {
        do {
           l_nstr[l_idx] = (char)wSrc[l_idx];
           l_idx++;
        } while ((char)wSrc[l_idx]!=0);
    }
    l_nstr[l_idx] = 0;
    return l_nstr;
}

// returns number of TCHARs in string
int wstrlen(_TCHAR * wstr)
{
    int l_idx = 0;
    while (((char*)wstr)[l_idx]!=0) l_idx+=2;
    return l_idx;
}