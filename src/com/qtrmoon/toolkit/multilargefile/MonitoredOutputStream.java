/* Licence:
*   Use this however/wherever you like, just don't blame me if it breaks anything.
*
* Credit:
*   If you're nice, you'll leave this bit:
*
*   Class by Pierre-Alexandre Losson -- http://www.telio.be/blog
*   email : plosson@users.sourceforge.net
*/
package com.qtrmoon.toolkit.multilargefile;

import java.io.OutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Original : plosson on 05-janv.-2006 10:44:18 - Last modified  by $Author: jamao $ on $Date: 2013/08/13 03:46:01 $
 * @version 1.0 - Rev. $Revision: 1.1 $
 */
public class MonitoredOutputStream extends OutputStream
{
    private OutputStream target;
    private OutputStreamListener listener;

    public MonitoredOutputStream(OutputStream target, OutputStreamListener listener)
    {
        this.target = target;
        this.listener = listener;
        this.listener.start();
    }

    public void write(byte b[], int off, int len) throws IOException
    {
        target.write(b,off,len);
        listener.bytesRead(len - off);//将当前的读取字节数传给监听对象，即可计算读取进度。
    }

    public void write(byte b[]) throws IOException
    {
        target.write(b);
        listener.bytesRead(b.length);
    }

    public void write(int b) throws IOException
    {
        target.write(b);
        listener.bytesRead(1);
    }

    public void close() throws IOException
    {
        target.close();
        listener.done();
    }

    public void flush() throws IOException
    {
        target.flush();
    }
}
