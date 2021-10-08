/*
 *     Percussion CMS
 *     Copyright (C) 1999-2020 Percussion Software, Inc.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     Mailing Address:
 *
 *      Percussion Software, Inc.
 *      PO Box 767
 *      Burlington, MA 01803, USA
 *      +01-781-438-9900
 *      support@percussion.com
 *      https://www.percussion.com
 *
 *     You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <https://www.gnu.org/licenses/>
 */

package com.percussion.HTTPClient;

import java.io.IOException;

/**
 * This holds various information about an active response. Used by the
 * StreamDemultiplexor and RespInputStream.
 *
 * @version	0.3-3  06/05/2001
 * @author	Ronald Tschalär
 * @since	V0.2
 */
@Deprecated
final class ResponseHandler
{
    /** the response stream */
    RespInputStream     stream;

    /** the response class */
    Response            resp;

    /** the response class */
    Request             request;

    /** signals that the demux has closed the response stream, and that
	therefore no more data can be read */
    boolean             eof = false;

    /** this is non-null if the stream has an exception pending */
    IOException         exception = null;


    /**
     * Creates a new handler. This also allocates the response input
     * stream.
     *
     * @param resp     the reponse
     * @param request  the request
     * @param demux    our stream demultiplexor.
     */
    ResponseHandler(Response resp, Request request, StreamDemultiplexor demux)
    {
	this.resp     = resp;
	this.request  = request;
	this.stream   = new RespInputStream(demux, this);

	Log.write(Log.DEMUX, "Demux: Opening stream " + this.stream.hashCode() +
			     " for demux (" + demux.hashCode() + ")");
    }


    /** holds the string that marks the end of this stream; used for
	multipart delimited responses. */
    private byte[] endbndry = null;

    /** holds the compilation of the above string */
    private int[]  end_cmp  = null;

    /**
     * return the boundary string for this response. Set's up the
     * InputStream buffer if neccessary.
     *
     * @param  MasterStream the input stream from which the stream demux
     *                      is reading.
     * @return the boundary string.
     */
    byte[] getEndBoundary(BufferedInputStream MasterStream)
		throws IOException, ParseException
    {
	if (endbndry == null)
	    setupBoundary(MasterStream);

	return endbndry;
    }

    /**
     * return the compilation of the boundary string for this response.
     * Set's up the InputStream buffer if neccessary.
     *
     * @param  MasterStream the input stream from which the stream demux
     *                      is reading.
     * @return the compiled boundary string.
     */
    int[] getEndCompiled(BufferedInputStream MasterStream)
		throws IOException, ParseException
    {
	if (end_cmp == null)
	    setupBoundary(MasterStream);

	return end_cmp;
    }

    /**
     * Gets the boundary string, compiles it for searching, and initializes
     * the buffered input stream.
     */
    void setupBoundary(BufferedInputStream MasterStream)
		throws IOException, ParseException
    {
	String endstr = "--" + Util.getParameter("boundary",
			    resp.getHeader("Content-Type")) +
			"--\r\n";
	endbndry = endstr.getBytes("8859_1");
	end_cmp = Util.compile_search(endbndry);
	MasterStream.markForSearch();
    }
}