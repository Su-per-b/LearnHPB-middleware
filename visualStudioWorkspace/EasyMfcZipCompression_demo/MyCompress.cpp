/***********************************************************************/ 
/* Copyright (C) 2003 Definitive Solutions, Inc.  All Rights Reserved. */ 
/*                                                                     */ 
/* This program is free software; you can redistribute it and/or       */ 
/* modify it under the terms of the GNU General Public License as      */ 
/* published by the Free Software Foundation; either version 2 of the  */ 
/* License, or (at your option) any later version.                     */ 
/*                                                                     */ 
/* This program is distributed in the hope that it will be useful, but */ 
/* WITHOUT ANY WARRANTY; without even the implied warranty of          */ 
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU   */ 
/* General Public License for more details.                            */ 
/*                                                                     */ 
/* You should have received a copy of the GNU General Public License   */ 
/* along with this program; if not, write to the Free Software         */ 
/* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA            */ 
/* 02111-1307  USA                                                     */ 
/***********************************************************************/ 

#include "stdafx.h"

#include "MyCompress.h"
#include "MyCompressZip.h"
#include "MyCompressUnzip.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif


/////////////////////////////////////////////////////////////////////////////
// MyCompress

// Constructor.
/* explicit */ MyCompress::MyCompress()
{
}

// Destructor.
/* virtual */ MyCompress::~MyCompress()
{
}

// Compress file.
bool MyCompress::Compress(const CString& sUncompressedPath,
						  const CString& sCompressedPath) const
{
	_ASSERTE(! sUncompressedPath.IsEmpty());
	_ASSERTE(! sCompressedPath.IsEmpty());

	bool bReturn(true);

	zipFile zf(zipOpen(sCompressedPath, /* bAppend */ false));

	if (zf) {
		zip_fileinfo zfi = { 0 };

		bReturn = (ZIP_OK == zipOpenNewFileInZip(zf,
			/* filename */					sUncompressedPath,
			/* zip_fileinfo* */				&zfi,
			/* extrafield_local */			NULL,
			/* size_extrafield_local */		0,
			/* extrafield_global */			NULL,
			/* size_extrafield_global */	0,
			/* comment */					NULL,
			/* method */					Z_DEFLATED,
			/* level */						Z_DEFAULT_COMPRESSION));

		if (bReturn) {
			CFile filIn(sUncompressedPath, CFile::modeRead);
			const int nBufSize(1024 * 16);
			char szBuf[nBufSize] = { 0 };
			UINT uiRead(0U);
			
			while (bReturn  &&
			  0U < (uiRead = filIn.Read(szBuf, nBufSize * sizeof(char)))) {

				if (ZIP_OK != zipWriteInFileInZip(zf, szBuf, uiRead)) {
					_ASSERTE(! "Error");
					bReturn = false;
				}
			}
			
			if (ZIP_OK != zipCloseFileInZip(zf)) {
				_ASSERTE(! "Error");
				bReturn = false;
			}

			if (ZIP_OK != zipClose(zf, NULL)) {
				_ASSERTE(! "Error");
				bReturn = false;
			}
		}
		else {
			_ASSERTE(! "Error");
		}
	}
	else {
		_ASSERTE(! "Error");
		bReturn = false;
	}

	return bReturn;
}

// Uncompress file.
bool MyCompress::Uncompress(const CString& sUncompressedPath,
							const CString& sCompressedPath) const
{
	_ASSERTE(! sUncompressedPath.IsEmpty());
	_ASSERTE(! sCompressedPath.IsEmpty());
	
	bool bReturn(true);
	
	unzFile uzf(unzOpen(sCompressedPath));
	
	if (uzf) {
		bReturn = (ZIP_OK == unzGoToFirstFile(uzf));

		if (bReturn) {
			bReturn = (ZIP_OK == unzOpenCurrentFile(uzf));

			if (bReturn) {

				CFile filOut(sUncompressedPath, CFile::modeCreate | CFile::modeWrite);
				const int nBufSize(1024 * 16);
				char szBuf[nBufSize] = { 0 };
				UINT uiRead(0U);

				while (0U < (uiRead = unzReadCurrentFile(uzf, szBuf, nBufSize))) {
					filOut.Write(szBuf, uiRead * sizeof(char));
				}

				if (ZIP_OK != unzCloseCurrentFile(uzf)) {
					_ASSERTE(! "Error");
					bReturn = false;
				}

				if (ZIP_OK != unzClose(uzf)) {
					_ASSERTE(! "Error");
					bReturn = false;
				}
			}
			else {
				_ASSERTE(! "Error");
			}
		}
		else {
			_ASSERTE(! "Error");
			bReturn = false;
		}
	}
	else {
		_ASSERTE(! "Error");
		bReturn = false;
	}

	return bReturn;
}
