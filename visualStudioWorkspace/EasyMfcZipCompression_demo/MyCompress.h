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

/////////////////////////////////////////////////////////////////////////////
// The compression code is all the work of Gilles Vollant, and of Jean-loup
// Gailly and Mark Adler.  See the other header files for their copyrights.
// All I did was create this thin wrapper around it; I also renamed the files
// to begin with "MyCompress", just to make them easier to keep together.
//
//                   - Larry Leonard, www.DefinitiveSolutions.com, April 2003
/////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////
// How to add this class to your application.
//
// Add MyCompress.cpp and MyCompress.h to your project in the usual folders.
//
// Add MyCompressZip.c to your project, and use the "Project, Settings" menu
// item, "C/C++" tab, "Precompiler Headers" category, to set this one file
// to "Not using precompiled headers".
//
// Add MyUncompressZip.c to your project, and use the "Project, Settings" menu
// item, "C/C++" tab, "Precompiler Headers" category, to set this one file
// to "Not using precompiled headers".
//
// Be sure MyCompressZlib.lib is in your linker's include path, and the files
// MyCompressZlib.h and MyCompressZconf.h are where the compiler can find them.
//
// Add /NODEFAULTLIB:LIBCD to your linker options if necessary.
/////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////
// How to use this class.
//
//		#include "MyCompress.h"
//		   . . . 
//		MyCompress mc;
//		VERIFY(mc.Compress(sUncompPath, sCompPath));
//		VERIFY(mc.Uncompress(sUncompPath, sCompPath));
//
/////////////////////////////////////////////////////////////////////////////

#if !defined(MYCOMPRESSH_098BA7A9_6385_40cb_B7B0_2BDC004547E4_INCLUDED_)
#define MYCOMPRESSH_098BA7A9_6385_40cb_B7B0_2BDC004547E4_INCLUDED_
#pragma once


/////////////////////////////////////////////////////////////////////////////
// MyCompress

class MyCompress
{
// Construction.
public:
	explicit MyCompress();
	virtual ~MyCompress();

// Interface.
public:
	bool	Compress(const CString& sUncompressedPath,
				const CString& sCompressedPath) const;
	
	bool	Uncompress(const CString& sUncompressedPath,
				const CString& sCompressedPath) const;
};

#endif // MYCOMPRESSH_098BA7A9_6385_40cb_B7B0_2BDC004547E4_INCLUDED_
